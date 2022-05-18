package data;

import sun.util.resources.cldr.ext.LocaleNames_dz;

public class Address {
    private final String ip;
    private final int  port;

    public Address(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }
}
