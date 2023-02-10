package org.vizzoid.utils;

import com.google.errorprone.annotations.CheckReturnValue;

/**
 * @param <V> consumer for build
 */
public interface IConsumerBuilder<K, V> extends BasicBuilder {

    /**
     * Build for public use
     */
    @CheckReturnValue
    default K build(V consumer) {
        check();
        return build0(consumer);
    }

    /**
     * Overriden method that returns instance name K
     */
    K build0(V consumer);

}
