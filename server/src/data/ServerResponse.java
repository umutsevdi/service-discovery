package data;

public class ServerResponse {

    private Address address;
    private int load;

    public ServerResponse(Address address, int load) {
        this.address = address;
        this.load = load;
    }

    public Address getAddress() {
        return address;
    }

    public int getLoad() {
        return load;
    }

    @Override
    public String toString() {
        return "Response:{ address:" + address + ", load:" + load + "}";
    }
}
