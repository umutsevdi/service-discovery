package com.company;

import com.company.exception.InvalidRequestException;
import com.company.executor.Executor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * {@link GenericExecuteServer} is a Runnable that executes given class on any
 * request
 * Returns {@code OK} followed by the response upon success,
 * Returns {@code ERR} upon receiving {@link InvalidRequestException}
 * <p>
 * {@author umutsevdi, MetinUsta}
 */
public class GenericExecuteServer extends Thread {

    private final int port;
    private final Executor serviceImpl;

    public GenericExecuteServer(int port, Executor serviceImpl) {
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
                    try {
                        String response = serviceImpl.execute(msg);
                        output.writeUTF("OK " + response);
                        // switch
                    } catch (InvalidRequestException e) {
                        output.writeUTF("ERR");
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
