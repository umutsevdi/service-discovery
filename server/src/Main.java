public class Main {
    public static void main(String[] args) {
        UDPServer udpThread = new UDPServer(8081);
        udpThread.start();
        Thread applicationThread = new Thread(new ApplicationServer(8080, udpThread));
        applicationThread.start();

        System.out.println("All threads were initialized successfully");
    }
}
