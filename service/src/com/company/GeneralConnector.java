package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GeneralConnector implements Runnable {

    private final int port;
    private final Executor serviceImpl;


    public GeneralConnector(int port, Executor serviceImpl) {
        this.port = port;
        this.serviceImpl = serviceImpl;
    }

    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(port)) {
            while (true) {
                try (Socket socket = server.accept(); // a socket to interact with a new client
                        DataInputStream input = new DataInputStream(socket.getInputStream());
                        DataOutputStream output = new DataOutputStream(socket.getOutputStream())
                ) {
                    String msg = input.readUTF(); // reading a message
                    try {
                        String response = serviceImpl.execute(msg);
                        output.writeUTF(response);
                        //                        switch
                    } catch (IllegalArgumentException e) {
                        output.writeUTF("InvalidRequest");
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
