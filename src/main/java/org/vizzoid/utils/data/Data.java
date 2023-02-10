package org.vizzoid.utils.data;

import java.util.List;
import java.util.Map;

/**
 * Immutable version of MutableData, distributed, in all places marked returning this will not return instance of mutable version: deserialization will create immutable data
 */
public class Data extends IData {

    public Data(Map<? extends String, ?> dataMap) {
        super(dataMap);
    }

    public Data(IData iData) {
        super(iData);
    }

    // Use this the most
    // unchecked operations can choke on it
    public <T> T as(String s) {
        return (T) get(s);
    }

    public Object get(String s) {
        return dataMap.get(s);
    }

    public int asInt(String s) {
        return as(s);
    }

    public double asDouble(String s) {
        return as(s);
    }

    public <E> List<E> asList(String s) {
        return as(s);
    }

    public List<? extends Data> asDataList(String s) {
        return as(s);
    }

    public <K, V> Map<K, V> asMap(String s) {
        return as(s);
    }

    public <V> Map<? extends Data, V> asDataKeyMap(String s) {
        return as(s);
    }

    public <K> Map<K, ? extends Data> asDataValueMap(String s) {
        return as(s);
    }

    public Map<? extends Data, ? extends Data> asDataMap(String s) {
        return as(s);
    }

    public String asString(String s) {
        return as(s);
    }

}
