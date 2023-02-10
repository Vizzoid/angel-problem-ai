package org.vizzoid.utils.data;

import org.jetbrains.annotations.NotNull;

/**
 * Mentions an object as being able to be serialized by an ObjectToFile serializer.
 * These objects should also include a static method to return the object from Data
 */
public interface DataHolder {

    /**
     * Template for data methods in DataHolders
     * ALL DATAHOLDERS MUST HAVE THIS METHOD!!!!!!!!!!!!!!!!!!!
     *
     * @param data read this map using your toData() method to set your variables of this instance
     * @return instance of this object
     */
    static @NotNull DataHolder fromData(final @NotNull Data data) {
        return new DataHolder() { // replace this anonymous object with this object and setting variables using data
            @Override
            public @NotNull MutableData toData() {
                return new MutableData(data, DataHolder.class);
            }
        };
    }

    @NotNull MutableData toData();

}
