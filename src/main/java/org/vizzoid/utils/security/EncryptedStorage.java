package org.vizzoid.utils.security;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EncryptedStorage {

    private final boolean encryptKey;
    private final boolean encryptValue;
    private final StringEncrypter encrypter;
    private final Map<Object, Object> storage = new HashMap<>();

    public EncryptedStorage() {
        this(new StorageSettings());
    }

    public EncryptedStorage(StorageSettings settings) {
        this.encryptKey = settings.encryptKey;
        this.encryptValue = settings.encryptValue;
        this.encrypter = StringEncrypter.create(settings.hashing);
    }

    private Object tryEncrypt(boolean doEncrypt, String obj) {
        return doEncrypt ? encrypter.encrypt(obj) : obj;
    }

    public void place(String key, String value) {
        storage.put(tryEncrypt(encryptKey, key), tryEncrypt(encryptValue, value));
    }

    public boolean matches(String key, String attempt) {
        Object previousValue = storage.get(tryEncrypt(encryptKey, key));
        if (previousValue instanceof byte[] arr) {
            return Arrays.equals(arr, encrypter.encrypt(attempt));
        } else {
            return Objects.equals(attempt, previousValue);
        }
    }

}
