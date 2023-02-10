package org.vizzoid.utils.data;

import org.vizzoid.utils.MultiMap;

import java.util.ArrayList;
import java.util.List;

/**
 * How the serializer works is that it takes in MutableData, which is the data distributed by serializable objects
 */
public class Serializer {

    private final MultiMap<Class<?>, Data> serialized = new MultiMap<>();

    public void add(MutableData dataObject) {
        serialized.add(dataObject.getIdentifier(), new Data(dataObject));
    }

    public void remove(Class<?> identifier) {
        serialized.remove(identifier);
    }

    public void clear() {
        serialized.clear();
    }

    public List<Data> getSerialized(Class<?> identifier) {
        return new ArrayList<>(serialized.get(identifier));
    }

    @Override
    public String toString() {
        return "Serializer{" +
            "serialized=" + serialized +
            '}';
    }
}
