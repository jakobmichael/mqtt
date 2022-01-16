import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.awt.*;

public class MqttSubscriber implements MqttCallback {

    private String brokerIp;
    private String clientId;
    private String topic;
    private MemoryPersistence persistence;
    private MqttClient client;
    private MqttConnectOptions opts;

    public MqttSubscriber(String brokerIp, String clientId, String topic) {
        this.brokerIp = brokerIp;
        this.clientId = clientId;
        this.topic = topic;
        this.persistence = new MemoryPersistence();
    }

    public void generateMqttClient() {
        try {
            this.client = new MqttClient(this.brokerIp, this.clientId, this.persistence);
            this.opts = new MqttConnectOptions();
            this.opts.setCleanSession(true);
        } catch (Exception e) {
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

    public void subscribe() {

        try {

            this.client.setCallback(this);
            this.client.subscribe(this.topic);

            System.out.println("[+] Subscribed topic: " + this.topic);
            System.out.println("[+] Client is Listening to broker on: " + this.brokerIp);

        } catch (MqttException me) {
            System.out.println(me);
        }
    }


    public void connectionLost(Throwable arg0) {
        System.out.println("[-] Lost connection to broker");
    }


    public void deliveryComplete(IMqttDeliveryToken arg0) {

    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {

        System.out.println("| Topic:" + topic);
        System.out.println("| Message: " + message.toString());
        System.out.println("-------------------------------------------------");

        if(message.toString().equalsIgnoreCase("beep")) {
            Toolkit.getDefaultToolkit().beep();
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

}