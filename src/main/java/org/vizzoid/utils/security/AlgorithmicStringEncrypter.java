package org.vizzoid.utils.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class AlgorithmicStringEncrypter implements StringEncrypter {

    protected final MessageDigest algorithm;

    public AlgorithmicStringEncrypter(String algorithm) throws NoSuchAlgorithmException {
        this(MessageDigest.getInstance(algorithm));
    }

    public AlgorithmicStringEncrypter(MessageDigest algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public byte[] encrypt(String message) {
        return algorithm.digest(message.getBytes(StandardCharsets.UTF_8));
    }

}
