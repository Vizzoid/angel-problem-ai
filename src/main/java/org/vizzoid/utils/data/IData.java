package org.vizzoid.utils.data;

import java.util.HashMap;
import java.util.Map;

public abstract class IData {

    protected final Map<String, Object> dataMap;

    public IData() {
        this.dataMap = new HashMap<>();
    }

    public IData(Map<? extends String, ?> dataMap) {
        this.dataMap = new HashMap<>(dataMap);
    }

    public IData(IData iData) {
        this(iData.dataMap);
    }

}
