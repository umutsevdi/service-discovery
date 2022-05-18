import exception.TimeoutException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ApplicationServer implements Runnable {
    private final int port;
    private final UDPServer udpServer;

    public ApplicationServer(int port, UDPServer udpServer) {
        this.port = port;
        this.udpServer = udpServer;
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
}
