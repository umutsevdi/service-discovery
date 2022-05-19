package data;

public record ServerResponse(Address address, int load) {

    @Override
    public String toString() {
        return "Response{ address:" + address + ", load:" + load + "}";
    }
}
