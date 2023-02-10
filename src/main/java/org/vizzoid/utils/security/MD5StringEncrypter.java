package org.vizzoid.utils.security;

import java.security.NoSuchAlgorithmException;

public class MD5StringEncrypter extends AlgorithmicStringEncrypter {

    public MD5StringEncrypter() throws NoSuchAlgorithmException {
        super("MD5");
    }

    @Override
    public byte[] encrypt(String message) {
        algorithm.update(message.getBytes());
        return algorithm.digest();
    }

}
