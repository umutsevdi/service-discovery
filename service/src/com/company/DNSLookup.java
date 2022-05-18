package com.company;

import java.net.InetAddress;

public class DNSLookup implements Executor{

    public DNSLookup() {
    }

    @Override
    public String execute(String args) {
        try {
            InetAddress inetAddress = InetAddress.getByName(args);
            return ("IP address for " + args + " = " + inetAddress.getHostAddress());
        }catch (Exception e){
            return e.getMessage();
        }
    }
}
