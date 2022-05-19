package data;

public class Address {
    private String ip;
    private String port;

    public Address(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "address:{ ip:" + ip + ", port:" + port + "}";
    }

}
