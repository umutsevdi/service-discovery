package data;

public class Address {
    private String ip;
    private int port;

    public Address(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String ip() {
        return this.ip;
    }

    public int port() {
        return this.port;
    }

    @Override
    public String toString() {
        return "address:{ ip:" + ip + ", port:" + port + "}";
    }

}
