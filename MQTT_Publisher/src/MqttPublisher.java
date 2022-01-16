import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttPublisher {

    private String brokerIp;
    private String clientId;
    private final MemoryPersistence persistence;
    private MqttClient client;
    private MqttConnectOptions opts;

    public MqttPublisher(String brokerIp, String clientId) {
        this.brokerIp = brokerIp;
        this.clientId = clientId;
        this.persistence = new MemoryPersistence();

    }

    public void generateMqttClient() {
        try {


            this.client = new MqttClient(this.brokerIp, this.clientId, this.persistence);
            this.opts = new MqttConnectOptions();
            this.opts.setCleanSession(true);
        }catch(Exception e) {
            System.out.println("[-] Error: could not create client");
        }
    }

    public void connectClientToBroker() {
        System.out.println("[+] Connecting to broker: " + this.brokerIp);
        try {
            this.client.connect(this.opts);
            System.out.println("[+] Connected to broker");
        } catch (MqttException e) {
            System.out.println("[-] reason " + e.getReasonCode());
            System.out.println("[-] msg " + e.getMessage());
            System.out.println("[-] loc " + e.getLocalizedMessage());
            System.out.println("[-] cause " + e.getCause());
            System.out.println("[-] excep " + e);
            e.printStackTrace();
        }
    }

    public void publishMessage(String topic, String message, int qos) {
        System.out.println("[+] Publishing message:" + message);

        try {
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(qos);
            this.client.publish(topic, mqttMessage);
            System.out.println("[+] Message published");
        } catch (Exception e) {
            System.out.println("[-] Error: Publish failed");
        }
    }

    public void disconnectBroker() {
        try {
            this.client.disconnect();
            this.client.close();
            System.exit(0);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }


    public String getBrokerIp() {
        return brokerIp;
    }

    public void setBrokerIp(String brokerIp) {
        this.brokerIp = brokerIp;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }


}