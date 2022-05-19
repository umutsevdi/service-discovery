package com.company.data;

import java.net.InetAddress;

public record Address(InetAddress ip, int port) {

    @Override
    public String toString() {
        return "address:{ ip:" + ip + ", port:" + port + "}";
    }

}

