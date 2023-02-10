package org.vizzoid.utils.exception;

public class RaidServerRuntimeException extends RuntimeException {

    public RaidServerRuntimeException(String s, String violator) {
        // Maybe violator.toString?
        super(s + " Violator: " + violator);
    }

}
