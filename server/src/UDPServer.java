import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import data.Address;
import data.ServerResponse;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import exception.NoResponseException;
import exception.TimeoutException;

public class UDPServer extends Thread {
    private DatagramSocket socket;
    private Map<String, List<ServerResponse>> requests = new HashMap<String, List<ServerResponse>>();

    public UDPServer() {
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(port);) {
            while (true) {
                try (Socket socket = server.accept(); // a socket to interact with a new client
                        DataInputStream input = new DataInputStream(socket.getInputStream());
                        DataOutputStream output = new DataOutputStream(socket.getOutputStream());) {
                    String serverType = input.readUTF().split(" ")[1]; // reading a message
                    try {
                        String response = udpServer.broadcast(serverType);
                        output.writeUTF(response); // resend it to the client

                    } catch (IllegalArgumentException e) {
                        output.writeUTF("InvalidRequest");
                    } catch (TimeoutException e) {
                        output.writeUTF("Timeout");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String broadcast(String type) throws Exception {
        String code = String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)) + type;
        InetAddress inetAddress = InetAddress.getByName("255.255.255.255");
        socket = new DatagramSocket();
        socket.setBroadcast(true);
        String message = code + ":" + type;
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, inetAddress, 4445);
        socket.send(packet);
        socket.close();
        System.out.println("broadcast to " + inetAddress.getAddress() + ":4445, with message:" + message);
        return code;
    }

    public Address getResponseAsync(String code, int timeoutSecond) throws NoResponseException {
        LocalDateTime then = LocalDateTime.now();
        while (then.plusSeconds(timeoutSecond).isBefore(LocalDateTime.now())) {

        }
        if (requests.containsKey(code) && requests.get(code).size() > 0) {
            List<ServerResponse> responses = requests.get(code);
            ServerResponse best = responses.get(0);
            for (ServerResponse i : responses) {
                if (best.getLoad() > i.getLoad()) {
                    best = i;
                }
            }
            requests.remove(code);
            return best.getAddress();
        }
        throw new NoResponseException();
    }
}
