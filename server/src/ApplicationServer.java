import exception.NoResponseException;
import exception.TimeoutException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import data.Address;

public class ApplicationServer implements Runnable {
    private final int port;
    private final int timeoutSecond;
    private final UDPServer udpServer;

    public ApplicationServer(int port, UDPServer udpServer) {
        this.port = port;
        this.udpServer = udpServer;
        timeoutSecond = 30;
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
                        String code = udpServer.broadcast(serverType); // receive unique code corresponding to the
                                                                       // request
                        Address address = udpServer.getResponseAsync(code, timeoutSecond); // check the code value after
                                                                                           // 30 seconds
                        output.writeUTF("OK " + address.getIp() + ":" + address.getPort()); // resend it to the client

                    } catch (IllegalArgumentException e) {
                        output.writeUTF("ERR InvalidRequest");
                    } catch (TimeoutException e) {
                        output.writeUTF("ERR Timeout");
                    } catch (NoResponseException e) {
                        output.writeUTF("ERR NoResponse");
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
