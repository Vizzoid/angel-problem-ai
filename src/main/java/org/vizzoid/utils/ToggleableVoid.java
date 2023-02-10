package org.vizzoid.utils;

/**
 * Toggleable interface that returns pure void (Toggleable<Void> still requires a return of null, however pure void accepts a blank return
 */
public interface ToggleableVoid extends CloseableVoid, Openable {

    default void setOpen(boolean open) {
        if (open) open();
        else close();
    }

}
