/*

 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Application Server");
        UDPServer udpThread = new UDPServer(8081);
        udpThread.start();
        Thread applicationThread = new Thread(new ApplicationServer(8080, udpThread));
        applicationThread.start();

        System.out.println("Hello world!");
    }
}