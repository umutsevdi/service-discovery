import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import data.Address;

public class Main {

    public static void main(String[] args) {

        String ADDRESS_NAME = "127.0.0.1";
        int PORT = 8080;
        Address address = new Address(ADDRESS_NAME, PORT);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the server type:");
        String serverType = scanner.nextLine();

        try (
                Socket socket = new Socket(InetAddress.getByName(address.ip()), address.port());
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            output.writeUTF("GET " + serverType);
            System.out.println("Requesting available addresses");
            String in = input.readUTF();
            if (in.equals("NOT_FOUND")) {
                System.out.println("Server type not found");
                return;
            }
            String[] data = in.split(":");
            System.out.println("Received : { ip:" + data[0] + ", port:" + data[1] + " }");
            address = new Address(data[0], Integer.parseInt(data[1]));
        } catch (Exception e) {
            System.out.println("No response: "+e.getMessage());
            scanner.close();
            return;
        }

        System.out.println("Server type " + serverType + " was found, enter the message:");
        String message = scanner.nextLine();
        try (
                Socket socket = new Socket(InetAddress.getByName(address.ip()), address.port());
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            output.writeUTF(message);
            System.out.println("Waiting for response...");
            System.out.println("Received:\n" + input.readUTF());
        } catch (Exception e) {
            System.out.println("No response");
            scanner.close();
            return;
        }
        scanner.close();
    }
}
