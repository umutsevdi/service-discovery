package com.company;

import com.company.exception.InvalidRequestException;
import com.company.executor.Executor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * {@link GenericExecutorRunnable} is a Runnable that executes given class on any
 * request
 * Returns {@code OK} followed by the response upon success,
 * Returns {@code ERR} upon receiving {@link InvalidRequestException}
 *
 * {@author umutsevdi}
 */
public class GenericExecutorRunnable implements Runnable {

    private final int port;
    private final Executor serviceImpl;

    public GenericExecutorRunnable(int port, Executor serviceImpl) {
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
}
