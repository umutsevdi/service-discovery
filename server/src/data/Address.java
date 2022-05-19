package data;

public record Address(String ip, int port) {

    @Override
    public String toString() {
        return "address:{ ip:" + ip + ", port:" + port + "}";
    }

}
