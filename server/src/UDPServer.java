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

/**
 * {@link UDPServer} is the server that manages all services. It calls all
 * services with a broadcast message, and saves their responses to a map. For
 * each request generates a unique code corresponding to the client. Then waits
 * until the timeout. After that finds the service with the lowest load and
 * returns it's {@link ServerResponse}
 */
public class UDPServer extends Thread {
    private DatagramSocket socket;
    // The map that stores each code and the responses that were sent for that code
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
        System.out.println("started: UDPServer at " + port);
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
     * Generates a code for the client and triggers
     * {@link #sendBroadcastMessage(String, InetAddress)}
     *
     * @param type String type of server that is being requested
     * @return code the code to search asynchronously
     */
    public String broadcast(String type) throws Exception {
        String host = "255.255.255.255"; // broadcast address
        String code = Base64.getEncoder().encodeToString(
                (LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + type).getBytes());
        System.out.println("UDP broadcast code: {" + code + "}4");

        String message = port + " " + code + " " + type;
        InetAddress adds = InetAddress.getByName(host);

        requests.put(code, new ArrayList<>());
        sendBroadcastMessage(
                message, adds);
        return code;
    }

    /**
     * Sends the given message as UDP broadcast
     *
     * @param message     String message content message content order :{port code
     *                    type}
     * @param inetAddress InetAddress of a single peer
     * @throws IOException upon failing to send the packet
     */
    public void sendBroadcastMessage(String message, InetAddress inetAddress) throws IOException {
        socket = new DatagramSocket();
        socket.setBroadcast(true);
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, inetAddress, 4445);
        socket.send(packet);
        socket.close();
        System.out.println("broadcast to " + inetAddress + ":4445, with message: {" + message + "}");
    }

    /**
     * Waits until the timeout and then returns the best available {@link Address}
     *
     * @param code          String return value of
     *                      {@link UDPServer#broadcast(String)}
     * @param timeoutSecond int
     * @return Address IP address and port values of the server
     * @throws NoResponseException When no response is received while waiting
     */
    public Address getResponseAsync(String code, int timeoutSecond) throws NoResponseException, InterruptedException {
        long then = System.currentTimeMillis();
        while (true) {
            long now = System.currentTimeMillis();
            if (now - then > (timeoutSecond * 1000L)) {
                break;
            }
        }
        /*
         * while (then.plusSeconds(timeoutSecond).isBefore(LocalDateTime.now())) {
         * Thread.sleep(1000L);
         * }
         */
        System.out.println("Responses were collected, analyzing");
        if (requests.containsKey(code) && requests.get(code).size() > 0) {
            List<ServerResponse> responses = requests.get(code);
            ServerResponse best = responses.get(0);
            for (ServerResponse i : responses) {
                if (best.load() > i.load()) {
                    best = i;
                }
            }
            System.out.println("best address found:" + best);
            requests.remove(code);
            return best.address();
        }
        System.out.println("No response");
        throw new NoResponseException();
    }
}
