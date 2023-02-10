package org.vizzoid.utils.data;

import java.util.Map;

/**
 * Mutable version of MutableData, only retrieval from toData() methods
 * When serialized, Data is created and the identifier is added to the map for identifying and getting all data under that class
 * This is so we do not have to store the class in the data object while easily getting all data for an object
 */
public class MutableData extends IData {
    public final Class<?> identifier;

    public MutableData(Class<?> identifier) {
        super();
        this.identifier = identifier;
    }

    public MutableData(Map<? extends String, ?> dataMap, Class<?> identifier) {
        super(dataMap);
        this.identifier = identifier;
    }

    public MutableData(IData iData, Class<?> identifier) {
        super(iData);
        this.identifier = identifier;
    }

    public Class<?> getIdentifier() {
        return identifier;
    }

    public MutableData put(String s, Object obj) {
        dataMap.put(s, obj);
        return this;
    }

    public void serialize() {
        SerializedUtils.add(this);
    }

}
