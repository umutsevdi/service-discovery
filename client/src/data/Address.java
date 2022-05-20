package data;

public class Address {
    private final String ip;
    private final int port;

    public Address(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String ip() {
        return ip;
    }

    public int port() {
        return port;
    }

    @Override
    public String toString() {
        return "Address{ip='" + ip + ", port=" + port + '}';
    }
}
