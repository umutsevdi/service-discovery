package executor;

import exception.InvalidRequestException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

public class Encryptor implements Executor {

    public Encryptor() {
    }

    @Override
    public String execute(String args) throws InvalidRequestException {
        try {
         return    Base64.getEncoder().encode(args.getBytes()).toString();
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
