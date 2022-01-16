import java.util.Scanner;

public class MqttMain {

    private final static String LOCALHOST_BROKER = "tcp://localhost:1883";
    private static String continuePublish = "Yes";

    public static void main(String[] args) {
        MqttPublisher publisher = new MqttPublisher(LOCALHOST_BROKER, "client_1");
        publisherLoop(publisher);
    }

    public static void publisherLoop(MqttPublisher pub) {
        Scanner scanner = new Scanner(System.in);
        pub.generateMqttClient();
        pub.connectClientToBroker();

        while (continuePublish.equalsIgnoreCase("Yes")) {
            System.out.println("Specify: topic,message,qos");
            String input = scanner.nextLine();
            System.out.println(input);
            String[] params = input.split(",");
            pub.publishMessage(params[0], params[1], Integer.valueOf(params[2]));
            System.out.println("Continue publishing? yes/no");
            continuePublish = scanner.nextLine();


        }

        pub.disconnectBroker();

    }
}
