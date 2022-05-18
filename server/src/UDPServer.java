import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import exception.TimeoutException;
public class UDPServer extends Thread {
    private DatagramSocket socket;
    private Map<String,List<String> > request;

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
        DatagramPacket sendPacket = new DatagramPacket(type.getBytes(), type.length(),
                InetAddress.getByName("255.255.255.255"), 8088);
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();

            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                continue; // Don't want to broadcast to the loopback interface
            }

            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                InetAddress broadcast = interfaceAddress.getBroadcast();
                if (broadcast == null) {
                    continue;
                }

                // Send the broadcast package!
                socket.send(sendPacket);

                System.out.println(getClass().getName() + ">>> Request packet sent to: " + broadcast.getHostAddress()
                        + "; Interface: " + networkInterface.getDisplayName());
            }
        }
    }
}
