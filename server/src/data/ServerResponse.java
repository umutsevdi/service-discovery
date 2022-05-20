package data;

public class ServerResponse {
    private  Address address;
    private  int load;

    public ServerResponse(Address address, int load) {
        this.address = address;
        this.load = load;
    }

    public Address address() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int load() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    @Override
    public String toString() {
        return "Response{ address:" + address + ", load:" + load + "}";
    }
}
