package com.company.data;

public class UDPRequest {
    private int port;
    private String code;
    private String type;

    public UDPRequest(int port, String code, String type) {
        this.port = port;
        this.code = code;
        this.type = type;
    }

    public int getPort() {
        return port;
    }

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}
