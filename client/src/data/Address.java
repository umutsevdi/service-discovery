package data;


public class Address {
    private String ip;
    private int port;

    public Address(String ip, int port ) {
        this.ip = ip;
        this.port = port;
    }

    public String ip() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int port() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "Address{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}