

import paho.mqtt.client as mqtt
from influxdb import InfluxDBClient

INFLUXDB_ADDRESS = '172.31.184.140'
INFLUXDB_USER = 'admin'
INFLUXDB_PASSWORD = 'admin'
INFLUXDB_DATABASE = 'weather_stations'

MQTT_ADDRESS = '172.31.238.115'
#MQTT_USER = 'cdavid'
#MQTT_PASSWORD = 'cdavid'
MQTT_TOPIC = 'house/dht22/#'
MQTT_CLIENT_ID = 'Client_1'

influxdb_client = InfluxDBClient(INFLUXDB_ADDRESS, 8086, INFLUXDB_USER, INFLUXDB_PASSWORD, None)



def on_connect(client, userdata, flags, rc):
    """ The callback for when the client receives a CONNACK response from the server."""
    print('Connected with result code ' + str(rc))
    client.subscribe(MQTT_TOPIC)



def _send_sensor_data_to_influxdb(topic, data):
    json_body = [
        {
            'measurement': data,
            'tags': {
                'location': topic,
            },
            'fields': {
                'value': data
            }
        }
    ]
    influxdb_client.write_points(json_body)

def on_message(client, userdata, msg):
    """The callback for when a PUBLISH message is received from the server."""
    message_as_string = msg.payload.decode('utf-8')
    print(float(message_as_string))
    _send_sensor_data_to_influxdb(msg.topic,float(message_as_string))
    #if sensor_data is not None:
        #_send_sensor_data_to_influxdb(sensor_data)

def _init_influxdb_database():
    databases = influxdb_client.get_list_database()
    if len(list(filter(lambda x: x['name'] == INFLUXDB_DATABASE, databases))) == 0:
        influxdb_client.create_database(INFLUXDB_DATABASE)
    influxdb_client.switch_database(INFLUXDB_DATABASE)

def main():
    _init_influxdb_database()

    mqtt_client = mqtt.Client(MQTT_CLIENT_ID)
    #mqtt_client.username_pw_set(MQTT_USER, MQTT_PASSWORD)
    mqtt_client.on_connect = on_connect
    mqtt_client.on_message = on_message

    mqtt_client.connect(MQTT_ADDRESS, 1883)
    mqtt_client.loop_forever()


if __name__ == '__main__':
    print('MQTT to InfluxDB bridge')
    main()
