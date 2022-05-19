package com.company.executor;

import com.company.exception.InvalidRequestException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;

public class Encryptor implements Executor {

    public Encryptor() {
    }

    @Override
    public String execute(String args) throws InvalidRequestException {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(args.toCharArray(), args.getBytes(), 65536, 256);
            SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
                    .getEncoded(), "AES");
            return secret.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidRequestException();
        }
    }

    @Override
    public ServiceType getServiceType() {
        return ServiceType.ENCRYPTOR;
    }
}
