package org.vizzoid.utils.security;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * User for encrypting messages into byte format
 */
public interface StringEncrypter {

    static StringEncrypter create(Hashing hashing) {
        try {
            switch (hashing) {
                case MD5 -> {
                    return new MD5StringEncrypter();
                }
                case SHA256 -> {
                    return new SHA256StringEncrypter();
                }
            }
        } catch (NoSuchAlgorithmException ignored) {}
        throw new UnsupportedOperationException();
    }

    byte[] encrypt(String message);

    default boolean matches(byte[] toMatch, String message) {
        return Arrays.equals(toMatch, encrypt(message));
    }
}
