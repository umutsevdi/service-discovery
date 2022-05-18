package com.company;

import java.util.Map;

public class Main {

    public static enum ServiceType {
        CALCULATOR("calc"),
        TO_JSON("json"),
        DATE_TIME("date");

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
                        case TO_JSON: {
                            System.out.println("Starting ToJson Service");
                            break;
                        }
                        case DATE_TIME: {
                            System.out.println("Starting DateTime Service");

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
