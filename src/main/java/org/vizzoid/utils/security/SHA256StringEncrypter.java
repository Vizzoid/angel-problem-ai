package org.vizzoid.utils.security;

import java.security.NoSuchAlgorithmException;

public class SHA256StringEncrypter extends AlgorithmicStringEncrypter {
    public SHA256StringEncrypter() throws NoSuchAlgorithmException {
        super("SHA-256");
    }
}
