package data;

public class UDPRequest {
    private int port;
    private String code;
    private String type;

    public UDPRequest(int port, String code, String type) {
        this.port = port;
        this.code = code;
        this.type = type;
    }

    public int port() {
        return port;
    }

    public String code() {
        return code;
    }

    public String type() {
        return type;
    }

    @Override
    public String toString() {
        return "UDPRequest{port: " + port + ", code: " + code + ", type: " + type + "}";
    }
}
