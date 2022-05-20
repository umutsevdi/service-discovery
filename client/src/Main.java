import data.Address;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

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
                System.out.println("Error: Server type was not found");
                return;
            }
            String[] rawData = in.split(" ");
            System.out.println(Arrays.toString(rawData));
            if (rawData[0].equals("OK")) {
                String[] data = rawData[1].split(":");
                System.out.println("Received : { ip:" + data[0] + ", port:" + data[1] + " }");
                address = new Address(data[0], Integer.parseInt(data[1]));
                System.out.println("Converted to address: " + address);
            } else {
                System.out.println("Error: Invalid Format: " + rawData[1]);
                scanner.close();
                return;
            }
        } catch (Exception e) {
            System.out.println("Error: No response from the server,  " + e.getMessage());
            scanner.close();
            return;
        }

        System.out.println("Service type " + serverType + " exists, enter the message to send:");
        String message = scanner.nextLine();
        System.out.println("Sending message...");
        try (
                Socket socket = new Socket(InetAddress.getByName(address.ip()), address.port());
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            System.out.println("Message was sent");
            output.writeUTF(message);
            System.out.println("Waiting for response...");
            System.out.println("Received:\n" + input.readUTF());
        } catch (Exception e) {
            System.out.println("Error: No response from service, " + e.getMessage());
            scanner.close();
            return;
        }
        scanner.close();
    }
}
