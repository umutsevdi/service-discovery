package com.company.executor;

import com.company.exception.InvalidRequestException;

import java.net.InetAddress;

public class DNSLookup implements Executor {

    public DNSLookup() {
    }

    @Override
    public String execute(String args) throws InvalidRequestException {
        try {
            InetAddress inetAddress = InetAddress.getByName(args);
            return ("IP address for " + args + " = " + inetAddress.getHostAddress());
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidRequestException();
        }
    }

    @Override
    public ServiceType getServiceType() {
        return ServiceType.DNS_LOOKUP;
    }
}
