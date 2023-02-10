package org.vizzoid.utils;

import java.util.function.Function;

@SuppressWarnings("unchecked")
public abstract class Preset<T extends Preset<T, T1>, T1 extends PresetOriginated> extends Keyable {

    private Function<T, T1> fromPreset;

    public Preset(Function<T, T1> fromPreset) {
        this.fromPreset = fromPreset;
    }

    public T fromPreset(Function<T, T1> fromPreset) {
        Check.notNull(fromPreset);
        this.fromPreset = fromPreset;
        return (T) this;
    }

    public T1 fromPreset() {
        return fromPreset.apply((T) this);
    }

}
