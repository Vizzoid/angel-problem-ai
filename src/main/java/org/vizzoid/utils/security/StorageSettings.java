package org.vizzoid.utils.security;

public class StorageSettings {

    public boolean encryptKey = false;
    public boolean encryptValue = true;
    public Hashing hashing = Hashing.MD5;

    public StorageSettings encryptKey(boolean encryptKey) {
        this.encryptKey = encryptKey;
        return this;
    }

    public StorageSettings encryptValue(boolean encryptValue) {
        this.encryptValue = encryptValue;
        return this;
    }

    public StorageSettings hashing(Hashing hashing) {
        this.hashing = hashing;
        return this;
    }
}
