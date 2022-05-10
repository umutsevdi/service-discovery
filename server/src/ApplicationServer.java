import enums.ServiceTypes;
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
                     DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                ) {
                    String msg = input.readUTF(); // reading a message
                    try {
                        ServiceTypes type = ServiceTypes.valueOf(msg);
                        udpServer.broadCast(type);
                    } catch (IllegalArgumentException e) {
                        output.writeUTF("InvalidRequest");
                    } catch (TimeoutException e) {
                        output.writeUTF("Timeout");
                    }
                    output.writeUTF(msg); // resend it to the client
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
