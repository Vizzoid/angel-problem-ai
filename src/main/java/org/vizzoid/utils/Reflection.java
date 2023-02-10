package org.vizzoid.utils;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.function.Function;

/**
 * Static reflection utility class that returns reusable reflection objects. These objects should be stored in a static final field. Can also easily set such objects
 * TODO: DOCS
 */
public class Reflection {

    @CanIgnoreReturnValue
    public static <T> @Nullable Object method(Class<? super T> clazz, T instance, String name, List<Class<?>> params, List<?> inputs) {
        return method(Object.class, clazz, instance, name, params, inputs);
    }

    @CanIgnoreReturnValue
    public static <T, K> @Nullable K method(Class<K> result, Class<? super T> clazz, T instance, String name, List<Class<?>> params, List<?> inputs) {
        return method(result, clazz, name, params).invoke(instance, inputs);
    }

    public static <T> @NotNull Method<T, Object> method(Class<? super T> clazz, String name, List<Class<?>> params) {
        return method(Object.class, clazz, name, params);
    }

    public static <T, K> @NotNull Method<T, K> method(Class<K> ignoredResult, Class<? super T> clazz, String name, List<Class<?>> params) {
        try {
            return new Method<>(clazz.getDeclaredMethod(name, params.toArray(new Class<?>[]{})));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void field(Class<? super T> clazz, T instance, String name, Object newValue) {
        field(clazz, name).set(instance, newValue);
    }

    public static <T, V> @NotNull Field<T, V> field(Class<? super T> clazz, String name) {
        try {
            return new Field<>(clazz.getDeclaredField(name));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> @NotNull BooleanField<T> booleanField(Class<? super T> clazz, String name) {
        try {
            return new BooleanField<>(clazz.getDeclaredField(name));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> @NotNull T init(Class<T> clazz, Object... params) {
        return init(clazz, 0, params);
    }

    public static <T> @NotNull T init(Class<T> clazz, int constructor, Object... params) {
        //noinspection unchecked
        return init((Constructor<T>) clazz.getDeclaredConstructors()[constructor], params);
    }

    public static <T> @NotNull T initWithClasses(Class<T> clazz, Class<?>[] paramClasses, Object... params) {
        try {
            return init(clazz.getDeclaredConstructor(paramClasses), params);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> @NotNull T init(Constructor<T> constructor, Object... params) {
        try {
            return constructor.newInstance(params);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> @NotNull T interfaces(Class<T> interfaces, Function<Object[], ?> argsHandler) {
        //noinspection unchecked
        return (T) Proxy.newProxyInstance(interfaces.getClassLoader(), new Class[]{interfaces}, (p, m, a) -> {
            if (m.isDefault()) {
                return MethodHandles.lookup()
                    .unreflectSpecial(m, m.getDeclaringClass())
                    .bindTo(p).invokeWithArguments(a);
            }
            return argsHandler.apply(a);
        });
    }

}
