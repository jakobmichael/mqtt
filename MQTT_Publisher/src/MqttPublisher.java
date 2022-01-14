import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Scanner;

public class MqttPublisher {

    public static void main(String[] args) {

        String topic = "test";
        String content = "";
        int qos = 2;
        String broker = "tcp://localhost:1885";
        String clientId = "mqtt_test";
        String nextPublish = "yes";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            Scanner scanner = new Scanner(System.in);
            MqttClient mqttClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            mqttClient.connect(connOpts);
            System.out.println("Connected to broker");

            while(nextPublish.equals("yes")) {
                System.out.println("Specify MQTT Message: ");
                content = scanner.next();
                System.out.println("Publishing message:" + content);
                MqttMessage message = new MqttMessage(content.getBytes());
                message.setQos(qos);
                mqttClient.publish(topic, message);
                System.out.println("Message published");
                System.out.println("Publish another Message? (yes/no)");
                nextPublish = scanner.next();
            }

            mqttClient.disconnect();
            mqttClient.close();
            System.exit(0);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
}