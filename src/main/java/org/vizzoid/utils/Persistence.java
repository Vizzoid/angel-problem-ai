package org.vizzoid.utils;

public enum Persistence {
    ALWAYS,
    SERIALIZATION,
    DESERIALIZATION,
    NONE;

    public boolean isSerializable() {
        return this == ALWAYS || this == SERIALIZATION;
    }

    public boolean isDeserializable() {
        return this == ALWAYS || this == DESERIALIZATION;
    }

}
