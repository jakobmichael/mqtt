public class MqttMain {

    private final static String LOCALHOST_BROKER = "tcp://localhost:1883";


    public static void main(String[] args) {
        MqttSubscriber subscriber = new MqttSubscriber(LOCALHOST_BROKER,"client_2","test");
        subscriber.generateMqttClient();
        subscriber.connectClientToBroker();
        subscriber.subscribe();
    }
}
