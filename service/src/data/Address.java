package data;

import java.net.InetAddress;
import java.util.Objects;

public final class Address {
    private final InetAddress ip;
    private final int port;

   public Address(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public InetAddress ip() {
        return ip;
    }

    public int port() {
        return port;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        var that = (Address) obj;
        return Objects.equals(this.ip, that.ip) &&
                this.port == that.port;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }


    @Override
    public String toString() {
        return "address:{ ip:" + ip + ", port:" + port + "}";
    }

}

