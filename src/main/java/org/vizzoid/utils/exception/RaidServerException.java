package org.vizzoid.utils.exception;

public class RaidServerException extends Exception {

    public RaidServerException(String s, Object violator) {
        // Maybe violator.toString?
        super(s + " Violator: " + violator.getClass().getSimpleName());
    }

}
