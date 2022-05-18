package com.company;

import java.util.Map;

public class Main {

    public static enum ServiceType {
        CALCULATOR("calc"),
        DNS_LOOKUP("dns_lookup"),
        DATE_TIME("date"),
        ENCRYPTOR("encryption");

        private String type;

        ServiceType(String type) {
            this.type = type;
        }
    }

    public static void main(String[] args) {
        int readArgs = 0;
        int port = 0;
        ServiceType type;
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
                    switch (ServiceType.valueOf(i)) {
                        case CALCULATOR: {
                            System.out.println("Starting Calculator Service");
                            executor = new EvaluateExpression();
                            break;
                        }
                        case DNS_LOOKUP: {
                            System.out.println("Starting DNS Lookup Service");
                            executor = new DNSLookup();

                            break;
                        }
                        case DATE_TIME: {
                            System.out.println("Starting DateTime Service");
                            executor = new DateTime();

                            break;
                        }
                        case ENCRYPTOR: {
                            System.out.println("Starting Encryption Service");
                            executor = new Encryptor();

                            break;
                        }
                        default: {
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
        new Thread(
                new GeneralConnector(
                        port, executor
                )
        ).start();
    }
}
