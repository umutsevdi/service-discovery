package data;

public class ServerResponse {
    private Address address;
    private int load;

    public ServerResponse(Address address, int load) {
        this.address = address;
        this.load = load;
    }

    public Address address() {
        return this.address;
    }

    public int load() {
        return this.load;
    }

    @Override
    public String toString() {
        return "Response{ address:" + address + ", load:" + load + "}";
    }
}
