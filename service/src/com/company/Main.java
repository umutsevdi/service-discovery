package com.company;

import com.company.executor.*;

import java.net.SocketException;

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
                        case CALCULATOR:
                            System.out.println("Starting Calculator Service");
                            executor = new EvaluateExpression();
                            break;
                        case DNS_LOOKUP:
                            System.out.println("Starting DNS Lookup Service");
                            executor = new DNSLookup();
                            break;

                        case DATE_TIME:
                            System.out.println("Starting DateTime Service");
                            executor = new DateTime();
                            break;
                        case ENCRYPTOR:
                            System.out.println("Starting Encryption Service");
                            executor = new Encryptor();
                            break;
                        default:
                            System.out.println("Invalid Argument");
                            return;
                    }
                }
                readArgs = 0;
            }
        }
        if (port == 0 || executor == null) {
            return;
        }
        GenericExecuteServer appServer = new GenericExecuteServer(port, executor);
        appServer.start();
        try {
            new Thread(new UDPResponseServer(appServer)).start();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }
}
