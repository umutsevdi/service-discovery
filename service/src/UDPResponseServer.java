import data.Address;
import data.UDPRequest;
import exception.InvalidRequestException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Optional;

/**
 * Receives UDP calls and responds them with the {@link Address} of it's
 * {@link GenericExecutionServer}
 */
public class UDPResponseServer extends Thread {
    private final DatagramSocket socket;
    private final byte[] buf = new byte[256];
    private final GenericExecutionServer appServer;

    public UDPResponseServer(GenericExecutionServer appServer) throws SocketException, UnknownHostException {
        this.socket = new DatagramSocket(4445, InetAddress.getByName("0.0.0.0"));
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
                InetAddress tcpResponseAddress = packet.getAddress();

                // InetAddress tcpResponseAddress = InetAddress.getByName("192.168.137.85");
                System.out.println("Packet received : " + packet.toString());
                System.out.println(
                        InetAddress.getLocalHost().getHostAddress() + " " + InetAddress
                                .getLoopbackAddress() + " "
                                + Inet4Address
                                        .getLocalHost()
                                + " " + Inet4Address.getLoopbackAddress());
                InetAddress address = InetAddress.getLocalHost();
                // InetAddress address = InetAddress.getByName("192.168.137.100");
                String received = new String(packet.getData(), 0, packet.getLength());

                UDPRequest request = resolveMessage(received);
                System.out.println("udpRequest resolved:: " + request);

                Address tcpAddress = new Address(tcpResponseAddress, request.port());
                Address localAddress = new Address(address, appServer.getPort());
                System.out.println("from: " + localAddress + " to: " + tcpAddress + " preparing message");

                Optional<String> response = generateTCPResponse(localAddress, request);
                System.out.println("Response is generated: " + response.get());
                if (response.isPresent()) {
                    respondWithTCP(tcpAddress, response.get());
                    System.out.println("TCP Response was sent");
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (Exception e) {
                System.out.println("Received request was invalid, ignored");
            }
        }
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
     * Generates a TCP message that contains port number and load
     * message format : "code ip:port load"
     *
     * @param request {@link UDPRequest}
     * @return message if server type matches, null if otherwise
     */
    private Optional<String> generateTCPResponse(Address address, UDPRequest request) {
        if (appServer.getServiceImpl().getServiceType().getName().equals(request.type())) {
            return Optional.of(request.code() + " " + address.ip().getHostAddress() + ":" + appServer
                    .getPort() + " " + appServer.getLoad());
        }
        return Optional.empty();
    }

    /**
     * Responds incoming UDP request with given message
     *
     * @param address Address values
     * @param message Response message
     * @throws Exception Socket related exceptions
     */
    public void respondWithTCP(Address address, String message) throws Exception {
        System.out.println("responding with tcp :" + address + " with message : " + message);
        Socket socket = new Socket(address.ip(), address.port());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        output.writeUTF(message);
    }

}
