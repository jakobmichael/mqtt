import java.util.Scanner;

public class MqttMain {

    private final static String LOCALHOST_BROKER = "tcp://localhost:1883";


    public static void main(String[] args) {
        String broker = new String(LOCALHOST_BROKER);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Use localhost as broker?");
        if(scanner.next().equalsIgnoreCase("no")){
            System.out.println("Specify brokerIp:port");
            broker = scanner.next();
        }
        System.out.println("Specify opic to subscribe:");
        String topic = scanner.next();
        MqttSubscriber subscriber = new MqttSubscriber(broker,"client_2",topic);
        subscriber.generateMqttClient();
        subscriber.connectClientToBroker();
        subscriber.subscribe();
    }
}
