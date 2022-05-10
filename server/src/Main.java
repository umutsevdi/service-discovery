import java.net.DatagramPacket;

/*

 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Application Server");
        Thread applicationThread = new Thread(new ApplicationServer(34522));
        applicationThread.start();

        System.out.println("Hello world!");
    }
}