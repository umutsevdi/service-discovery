package data;

import java.net.InetAddress;

public class Address {
    private InetAddress ip;
    private int port;

    public Address(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public InetAddress ip() {
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
