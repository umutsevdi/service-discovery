import executor.*;

import java.net.SocketException;
import java.net.UnknownHostException;

public class Main {


    public static void main(String[] args) {
        int readArgs = 0;
        int port = 0;
        Executor executor = null;
        for (String i : args) {
            if (readArgs == 0) {
                if (i.equals("--service")) {
                    readArgs = 1;
                } else if (i.equals("--port")) {
                    readArgs = 2;
                }
            } else {
                if (readArgs == 2) {
                    port = Integer.parseInt(i);
                } else {
                    switch (Executor.ServiceType.valueOf(i)) {
                        case CALCULATOR -> {
                            System.out.println("Starting Calculator Service");
                            executor = new EvaluateExpression();
                        }
                        case DNS_LOOKUP -> {
                            System.out.println("Starting DNS Lookup Service");
                            executor = new DNSLookup();
                        }
                        case DATE_TIME -> {
                            System.out.println("Starting DateTime Service");
                            executor = new DateTime();
                        }
                        case ENCRYPTOR -> {
                            System.out.println("Starting Encryption Service");
                            executor = new Encryptor();
                        }
                        default -> {
                            System.out.println("Invalid Argument");
                            return;
                        }
                    }
                }
                readArgs = 0;
            }
        }
        if (port == 0 || executor == null) {
            return;
        }

        try {
            GenericExecutionServer appServer = new GenericExecutionServer(port, executor);
            appServer.start();
            UDPResponseServer udpResponseServer = new UDPResponseServer(appServer);
            udpResponseServer.start();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
