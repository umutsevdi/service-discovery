package com.company;

import com.company.data.Address;
import com.company.data.UDPRequest;
import com.company.exception.InvalidRequestException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Optional;

/**
 * UDP Response Server receives UDP calls and responds them with the port of it's TCP Service
 */
public class UDPResponseServer implements Runnable {
    private final DatagramSocket socket;
    private final byte[] buf = new byte[256];
    private final GenericExecuteServer appServer;

    public UDPResponseServer(GenericExecuteServer appServer) throws SocketException {
        this.socket = new DatagramSocket(4445);
        this.appServer = appServer;
    }

    /**
     * UDP Socket listener that responds with a TCP
     */
    @Override
    public void run() {
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                InetAddress address = packet.getAddress();
                String received = new String(packet.getData(), 0, packet.getLength());
                UDPRequest request = resolveMessage(received);
                Address tcpAddress = new Address(address, request.getPort());

                Optional<String> response = generateTCPResponse(request);
                if (response.isPresent()) {
                    respondWithTCP(tcpAddress, generateTCPResponse(request).get());
                }

            } catch (IOException e) {
                e.printStackTrace(); // Exceptions related to IO
            } catch (InvalidRequestException e) {
                e.printStackTrace(); // ResolveMessageExceptions
            } catch (Exception e) {
                e.printStackTrace(); // TCP Sending Exceptions
            }
        }
    }

    /**
     * Generates a TCP message that contains port number and load
     *
     * @param request {@link UDPRequest}
     * @return message if server type matches, null if otherwise
     */
    private Optional<String> generateTCPResponse(UDPRequest request) {
        if (appServer.getServiceImpl().getServiceType().getName().equals(request.getType())) {
            return Optional.of(appServer.getPort() + " " + appServer.getLoad());
        }
        return Optional.empty();
    }

    /**
     * Formats incoming UDP Request
     *
     * @param message Message format
     * @return Formatted UDP Request
     * @throws InvalidRequestException upon receiving invalid request
     */
    public UDPRequest resolveMessage(String message) throws InvalidRequestException {
        String[] request = message.split(" ");
        if (request.length != 3) {
            throw new InvalidRequestException();
        }
        try {
            return new UDPRequest(Integer.parseInt(request[0]), request[1], request[2]);
        } catch (Exception e) {
            throw new InvalidRequestException();
        }
    }

    /**
     * Responds incoming UDP request with given message
     *
     * @param address Address values
     * @param message Response message
     * @throws Exception Socket related exceptions
     */
    public void respondWithTCP(Address address, String message) throws Exception {
        Socket socket = new Socket(address.getIp(), address.getPort());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        output.writeUTF(message);
    }

}