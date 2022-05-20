import exception.InvalidRequestException;
import executor.Executor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * {@link GenericExecutionServer} is a Runnable that executes given class on any
 * request
 * Returns {@code OK} followed by the response upon success,
 * Returns {@code ERR} upon receiving {@link InvalidRequestException}
 * <p>
 * {@author umutsevdi, MetinUsta}
 */
public class GenericExecutionServer extends Thread {

    private final int port;
    private final Executor serviceImpl;

    public GenericExecutionServer(int port, Executor serviceImpl) {
        this.port = port;
        this.serviceImpl = serviceImpl;
    }


    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(port)) {
            while (true) {
                try (Socket socket = server.accept(); // a socket to interact with a new client
                     DataInputStream input = new DataInputStream(socket.getInputStream());
                     DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
                    String msg = input.readUTF(); // reading a message
                    System.out.println("Message from the client is received, message:"+ msg);
                    try {
                        String response = serviceImpl.execute(msg);
                        output.writeUTF("OK " + response);
                        System.out.println("Responded successfully, "+ response);
                        // switch
                    } catch (InvalidRequestException e) {
                        output.writeUTF("ERR");
                        System.out.println("Request format error, responded with ERR");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return port;
    }

    public Executor getServiceImpl() {
        return serviceImpl;
    }

    public int getLoad() {
        return (int) (Math.random() * 100);
    }
}
