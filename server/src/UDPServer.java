import data.Address;
import data.ServerResponse;
import exception.NoResponseException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class UDPServer extends Thread {
    private DatagramSocket socket;
    private final Map<String, List<ServerResponse>> requests = new HashMap<>();
    private final int port;

    public UDPServer(int port) {
        this.port = port;
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Catches incoming answers to the UDP message and saves them to the server
     */
    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(port)) {
            while (true) {
                try (Socket socket = server.accept(); // a socket to interact with a new client
                     DataInputStream input = new DataInputStream(socket.getInputStream());
                     DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
                    String[] message = input.readUTF().split(" "); // reading a message
                    if (message.length == 3) {
                        try {
                            String code = message[0];
                            String[] addressInfo = message[1].split(":");
                            ServerResponse response = new ServerResponse(
                                    new Address(
                                            addressInfo[0], // ip address
                                            Integer.parseInt(addressInfo[1]) // port number
                                    ), // address
                                    Integer.parseInt(message[2]) // load
                            );
                            if (requests.containsKey(code)) {
                                requests.get(code).add(response);
                            } else {
                                System.out.println("InvalidResponse: No such code demanded, probably late response:" +
                                        "\n{" + Arrays.toString(message) + "}");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("InvalidResponse:{" + Arrays.toString(message) + "}, ignored");
                        }


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * broadcast: prepares a broadcast message and sends it as a UDP broadcast
     *
     * @param type: String type of server that is being requested
     * @return code the code to search asynchronously
     */
    public String broadcast(String type) throws Exception {
        String host = "255.255.255.255"; //  broadcast address
        String code =
                Arrays.toString(Base64.getEncoder().encode((LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + type)
                        .getBytes()));
        System.out.println("code for the UDP Broadcast: " + code);

        String message = port + " " + code + " " + type;
        InetAddress adds = InetAddress.getByName(host);

        sendBroadcastMessage(
                message, adds
        );
        return code;
    }

    /**
     * sendBroadcastMessage: sends a broadcast message to a single peer
     *
     * @param message     : String message content message content order :{port code type}
     * @param inetAddress : InetAddress of a single peer
     * @throws IOException : upon failing to send the packet
     */
    public void sendBroadcastMessage(String message, InetAddress inetAddress) throws IOException {
        socket = new DatagramSocket();
        socket.setBroadcast(true);
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, inetAddress, 4445);
        socket.send(packet);
        socket.close();
        System.out.println("broadcast to " + inetAddress + ":4445, with message:" + message);
    }

    /**
     * getResponseAsync: waits until the timeout and finds the best server fitting
     *
     * @param code          : String return value of {@link UDPServer#broadcast(String)}
     * @param timeoutSecond : int
     * @return Address: IP address and port values of the server
     * @throws NoResponseException: When no response is received while waiting
     */
    public Address getResponseAsync(String code, int timeoutSecond) throws NoResponseException {
        LocalDateTime then = LocalDateTime.now();
        while (then.plusSeconds(timeoutSecond).isBefore(LocalDateTime.now())) ;
        if (requests.containsKey(code) && requests.get(code).size() > 0) {
            List<ServerResponse> responses = requests.get(code);
            ServerResponse best = responses.get(0);
            for (ServerResponse i : responses) {
                if (best.load() > i.load()) {
                    best = i;
                }
            }
            requests.remove(code);
            return best.address();
        }
        throw new NoResponseException();
    }
}
