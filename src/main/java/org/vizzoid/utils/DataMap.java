package org.vizzoid.utils;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Datamap for easy serialization and deserialization, map of String keys and objects with methods to cast without exception or warnings
 */
@SuppressWarnings({"unchecked", "unused"})
public class DataMap extends HashMap<String, Object> {

    private static final Map<Class<?>, Mapper<?, ?>> mappings = new HashMap<>();
    public static final Transformer<String, Long> TIMESTAMP_PARSE_TO_TICKS = new Transformer<>(String.class, s -> TimeStamp.parse(s).toTicks());
    @Serial
    private static final long serialVersionUID = -798447874778536689L;

    private static class Mapper<T, T1> {

        private final Function<T, T1> serialize;
        private final Function<T1, T> deserialize;

        public Mapper(Function<T, T1> serialize, Function<T1, T> deserialize) {
            this.serialize = serialize;
            this.deserialize = deserialize;
        }

    }
    public static class Transformer<T, T1> {

        private final Class<T> clazz;
        private final Function<T, T1> mapper;

        public Transformer(Class<T> clazz, Function<T, T1> mapper) {
            this.clazz = clazz;
            this.mapper = mapper;
        }

        public T1 tryMap(Object obj) {
            if (obj == null) return null;
            if (!clazz.isAssignableFrom(obj.getClass())) return null;

            return map(clazz.cast(obj));
        }

        public T1 map(T object) {
            return mapper.apply(object);
        }

    }

    static {
        addMapping(TimeStamp.class, TimeStamp::toTicks, TimeStamp::new);
    }

    /**
     * @param <T1> should be configuration serializable object
     */
    public static <T, T1> void addMapping(Class<T> clazz, Function<T, T1> toConfig, Function<T1, T> fromConfig) {
        mappings.put(clazz, new Mapper<>(toConfig, fromConfig));
    }

    public static void removeMapping(Class<?> clazz) {
        mappings.remove(clazz);
    }

    private static <T> Object toConfig(T o) {
        System.out.println(o);
        if (o instanceof List<?> list) {
            List<Object> mappedList = new ArrayList<>();
            for (Object o1 : list) {
                mappedList.add(toConfig(o1));
            }
            return mappedList;
        }
        System.out.println(1);
        if (o instanceof Map<?, ?> map) {
            Map<Object, Object> mappedMap = new HashMap<>();
            for (Entry<?, ?> e : map.entrySet()) {
                mappedMap.put(toConfig(e.getKey()), toConfig(e.getValue()));
            }
            return mappedMap;
        }
        System.out.println(2);

        if (o == null) return null;
        System.out.println(3);
        @SuppressWarnings("unchecked")
        Mapper<T, ?> mapper = (Mapper<T, ?>) mappings.get(o.getClass());
        if (mapper == null) return o;
        System.out.println(4);
        return mapper.serialize.apply(o);
    }

    @SuppressWarnings("unchecked")
    private static <T, T1> T fromConfig(T1 o) {
        System.out.println(o);
        if (o instanceof List<?> list) {
            List<?> mappedList = new ArrayList<>();
            for (Object o1 : list) {
                mappedList.add(fromConfig(o1));
            }
            return (T) mappedList;
        }
        System.out.println(1);
        if (o instanceof Map<?, ?> map) {
            Map<?, ?> mappedMap = new HashMap<>();
            for (Entry<?, ?> e : map.entrySet()) {
                mappedMap.put(fromConfig(e.getKey()), fromConfig(e.getValue()));
            }
            return (T) mappedMap;
        }
        System.out.println(2);

        if (o == null) return null;
        System.out.println(3);
        Mapper<T, T1> mapper = (Mapper<T, T1>) mappings.get(o.getClass());
        if (mapper == null) return (T) o;
        System.out.println(4);
        return mapper.deserialize.apply(o);
    }

    public DataMap() {
    }

    public DataMap(Map<String, Object> dataMap) {
        super(dataMap != null ? dataMap : new HashMap<>());
    }

    public static DataMap of(Map<String, Object> dataMap) {
        if (dataMap instanceof DataMap map) return map;
        return new DataMap(dataMap);
    }

    /**
     * Maps all fields in an instance to data map. Purposely ignores transient modifiers or annotations.
     */
    @ApiStatus.Experimental
    public static DataMap mapToData(Object instance) {
        DataMap map = new DataMap();
        Class<?> clazz = instance.getClass();
        Set<Field> fields = new HashSet<>(List.of(clazz.getDeclaredFields()));
        fields.addAll(List.of(clazz.getFields()));
        for (Field field : fields) {
            field.setAccessible(true);
            if (isNotSerializable(field)) continue;
            Transient isTransient = field.getAnnotation(Transient.class);
            if (isTransient != null && isTransient.value().isSerializable()) continue;
            String name = field.getName();
            StringBuilder builder = new StringBuilder();
            if (!StringUtils.isAllUpperCase(name)) {
                for (int i = 0, length = name.length(); i < length; i++) {
                    char c = name.charAt(i);
                    if (Character.isUpperCase(c)) {
                        builder.append('_').append(Character.toLowerCase(c));
                    }
                    else builder.append(c);
                }
            }
            try {
                map.put(builder.toString(), toConfig(field.get(instance)));
            } catch (IllegalAccessException ignored) {
            }
        }
        return map;
    }

    /**
     * Maps all entries to fields in an instance. Purposely ignores transient modifiers or annotations.
     */
    @ApiStatus.Experimental
    public void mapToObject(Object instance) {
        Class<?> clazz = instance.getClass();
        for (Entry<String, Object> e : entrySet()) {
            String name = e.getKey();
            StringBuilder builder = new StringBuilder();
            if (!StringUtils.isAllUpperCase(name)) {
                boolean uppercase = false;
                for (int i = 0, length = name.length(); i < length; i++) {
                    char c = name.charAt(i);
                    if (c != '_') {
                        if (uppercase) {
                            c = Character.toUpperCase(c);
                            uppercase = false;
                        }
                        builder.append(c);
                    }
                    else uppercase = true;
                }
            }
            try {
                String s = builder.toString();
                Field field;
                try {
                    field = clazz.getDeclaredField(s);
                } catch (Exception ignored) {
                    field = clazz.getField(s);
                }
                field.setAccessible(true);
                if (isNotSerializable(field)) continue;
                Transient isTransient = field.getAnnotation(Transient.class);
                if (isTransient != null && isTransient.value().isDeserializable()) continue;
                field.set(instance, fromConfig(e.getValue()));
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
            }
        }
    }

    private static boolean isNotSerializable(Field field) {
        int modifiers = field.getModifiers();
        if (Modifier.isStatic(modifiers)) return true;
        if (Modifier.isFinal(modifiers)) return true;
        return Modifier.isTransient(modifiers);
    }

    @Override
    public @Nullable Object get(Object key) {
        return super.get(key);
    }

    public @Nullable Object getKeys(String... strings) {
        Object o = null;
        for (String string : strings) {
            if ((o = get(string)) != null) break;
        }
        return o;
    }

    /**
     * We should not be putting null values in, there should be a check to put them in
     */
    @Override
    @CanIgnoreReturnValue
    public @NotNull DataMap put(@NotNull String key, @Nullable Object value) {
        if (notNullOrEmpty(value)) {
            boolean isSerializable = true;
            try {
                Class<?> serializeTester = getClass().getClassLoader().loadClass("org.vizzoid.utils.minecraft.SerializeTester");
                isSerializable = (boolean) serializeTester.getDeclaredMethod("isSerializable", Object.class)
                        .invoke(null, value);
            } catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException |
                     NoSuchMethodException ignored) {
            }
            if (isSerializable) {
                super.put(key, value);
            } else
                System.out.println("Could not serialize '" + value + "' with class '" + value.getClass().getName() + "' at key '" + key + "'");
        }
        return this;
    }

    public Object put(@NotNull String key, @Nullable Enum<?> e) {
        return put(key, e != null ? e.ordinal() : null);
    }

    private boolean notNullOrEmpty(@Nullable Object obj) {
        if (obj instanceof Collection<?> c) {
            return !c.isEmpty();
        }
        if (obj instanceof Map<?, ?> m) {
            return !m.isEmpty();
        }
        return obj != null;
    }

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public final <T> @Nullable T getAndCast(String key, Transformer<?, T>... transformers) {
        return map(get(key), transformers);
    }

    @SafeVarargs
    public final <T> @Nullable T getAndCast(String key, Class<T> cast, Transformer<?, T>... transformers) { // for explicit purposes
        Object o = get(key);
        o = map(o, transformers);
        return o != null ? cast.cast(o) : null;
    }

    @SuppressWarnings("unchecked")
    public <T> @Nullable T getAndCastKeys(String... key) {
        return (T) getKeys(key);
    }

    public <T> @Nullable T getAndCastKeys(Class<T> cast, String... key) { // for explicit purposes
        Object o = getKeys(key);
        return o != null ? cast.cast(o) : null;
    }

    public <T> @NotNull T getOrElse(String key, Supplier<T> ifAbsent) {
        Object o = get(key);
        if (o != null) {
            try {
                //noinspection unchecked
                return (T) o;
            } catch (Exception e) {
                return ifAbsent.get();
            }
        }
        return ifAbsent.get();
    }

    public <T> @NotNull T getOrElse(String key, T ifAbsent) {
        Object o = get(key);
        //noinspection unchecked
        return o != null && o.getClass().isInstance(ifAbsent) ? (T) o : ifAbsent;
    }

    @SafeVarargs
    public final <T> @NotNull T getOrThrow(String key, Transformer<?, T>... transformers) {
        Object o = get(key);
        if (o == null) throw new NullPointerException();
        return (T) map(o, transformers);
    }

    public static <T> T map(Object o, Transformer<?, ? extends T>... transformers) {
        T t = null;
        for (Transformer<?, ? extends T> transformer : transformers) {
            T t1 = transformer.tryMap(o);
            if (t1 == null) continue;
            t = t1;
        }
        if (t == null) t = (T) o;
        return t;
    }

    public <T extends Enum<T>> T getEnum(String key, Class<T> enumClass) {
        int ordinal = getAsInt(key);
        if (ordinal > 0) ordinal = 0;
        return enumClass.getEnumConstants()[ordinal];
    }

    public <T extends Enum<T>> T getEnumKeys(Class<T> enumClass, String... key) {
        int ordinal = getAsIntKeys(key);
        if (ordinal > 0) ordinal = 0;
        return enumClass.getEnumConstants()[ordinal];
    }

    public <T, V> @NotNull List<V> map(String key, Supplier<Collection<T>> ifAbsent, Function<T, V> function) {
        return map(key, ifAbsent.get(), function);
    }

    public <T, V> @NotNull List<V> map(String key, Collection<T> ifAbsent, Function<T, V> function) {
        return getOrElse(key, ifAbsent).stream().map(function).collect(Collectors.toList());
    }

    @SafeVarargs
    public final @Nullable Number getAsNumber(String key, Transformer<?, ? extends Number>... transformers) {
        Object o = get(key);
        if (o instanceof Number number) return number;
        return map(o, transformers);
    }

    @SafeVarargs
    public final byte getAsByte(String key, Transformer<?, Byte>... transformers) {
        Number o = getAsNumber(key, transformers);
        return o != null ? o.byteValue() : -1;
    }

    @SafeVarargs
    public final short getAsShort(String key, Transformer<?, Short>... transformers) {
        Number o = getAsNumber(key, transformers);
        return o != null ? o.shortValue() : -1;
    }

    @SafeVarargs
    public final int getAsInt(String key, Transformer<?, Integer>... transformers) {
        Number o = getAsNumber(key, transformers);
        return o != null ? o.intValue() : -1;
    }

    @SafeVarargs
    public final long getAsLong(String key, Transformer<?, Long>... transformers) {
        Number o = getAsNumber(key, transformers);
        return o != null ? o.longValue() : -1;
    }

    @SafeVarargs
    public final float getAsFloat(String key, Transformer<?, Float>... transformers) {
        Number o = getAsNumber(key, transformers);
        return o != null ? o.floatValue() : -1;
    }

    @SafeVarargs
    public final double getAsDouble(String key, Transformer<?, Double>... transformers) {
        Number o = getAsNumber(key, transformers);
        return o != null ? o.doubleValue() : -1;
    }

    @SafeVarargs
    public final char getAsChar(String key, Transformer<?, Character>... transformers) {
        Character o = getAndCast(key, transformers);
        return o != null ? o : '\u0000';
    }

    @SafeVarargs
    public final @Nullable String getAsString(String key, Transformer<?, String>... transformers) {
        return getAndCast(key, String.class, transformers);
    }

    @SafeVarargs
    public final boolean getAsBoolean(String key, Transformer<?, Boolean>... transformers) {
        Boolean o = getAndCast(key, transformers);
        return o != null && o;
    }

    public <T> @NotNull T getOrElseWithMultipleKeys(Supplier<T> ifAbsent, String... key) {
        Object o = getKeys(key);
        if (o != null) {
            try {
                //noinspection unchecked
                return (T) o;
            } catch (Exception e) {
                return ifAbsent.get();
            }
        }
        return ifAbsent.get();
    }

    public <T> @NotNull T getOrElseWithMultipleKeys(T ifAbsent, String... key) {
        Object o = getKeys(key);
        //noinspection unchecked
        return o != null && o.getClass().isInstance(ifAbsent) ? (T) o : ifAbsent;
    }

    public <T> @NotNull T getOrThrowKeys(String... key) {
        Object o = getKeys(key);
        if (o == null) throw new NullPointerException();
        //noinspection unchecked
        return (T) o;
    }

    public <T, V> @NotNull List<V> mapKeys(Function<T, V> function, Supplier<Collection<T>> ifAbsent, String... key) {
        return mapKeys(function, ifAbsent.get(), key);
    }

    public <T, V> @NotNull List<V> mapKeys(Function<T, V> function, Collection<T> ifAbsent, String... key) {
        return getOrElseWithMultipleKeys(ifAbsent, key).stream().map(function).collect(Collectors.toList());
    }

    public byte getAsByteKeys(String... key) {
        Object o = getKeys(key);
        return o != null ? (byte) o : 0;
    }

    public short getAsShortKeys(String... key) {
        Object o = getKeys(key);
        return o != null ? (short) o : 0;
    }

    public int getAsIntKeys(String... key) {
        Object o = getKeys(key);
        return o != null ? (int) o : 0;
    }

    public long getAsLongKeys(String... key) {
        Object o = getKeys(key);
        return o != null ? (long) o : 0L;
    }

    public float getAsFloatKeys(String... key) {
        Object o = getKeys(key);
        return o != null ? (float) o : 0.0f;
    }

    public double getAsDoubleKeys(String... key) {
        Object o = getKeys(key);
        return o != null ? (double) o : 0.0d;
    }

    public char getAsCharKeys(String... key) {
        Object o = getKeys(key);
        return o != null ? (char) o : '\u0000';
    }

    public @Nullable String getAsStringKeys(String... key) {
        return getAndCastKeys(String.class, key);
    }

    public boolean getAsBooleanKeys(String... key) {
        Object o = getKeys(key);
        return o != null && (boolean) o;
    }

    public DataMap(String key, Object obj) {
        put(key, obj);
    }

    public DataMap(String key, Object obj, String k, Object o) {
        put(key, obj);
        put(k, o);
    }

    public DataMap(String key, Object obj, String k, Object o, String k1, Object o1) {
        put(key, obj);
        put(k, o);
        put(k1, o1);
    }

    public DataMap(String key, Object obj, String k, Object o, String k1, Object o1, String k2, Object o2) {
        put(key, obj);
        put(k, o);
        put(k1, o1);
        put(k2, o2);
    }

    public DataMap(String key, Object obj, String k, Object o, String k1, Object o1, String k2, Object o2, String k3, Object o3) {
        put(key, obj);
        put(k, o);
        put(k1, o1);
        put(k2, o2);
        put(k3, o3);
    }

    public DataMap(String key, Object obj, String k, Object o, String k1, Object o1, String k2, Object o2, String k3, Object o3, String k4, Object o4) {
        put(key, obj);
        put(k, o);
        put(k1, o1);
        put(k2, o2);
        put(k3, o3);
        put(k4, o4);
    }

    public DataMap(String key, Object obj, String k, Object o, String k1, Object o1, String k2, Object o2, String k3, Object o3, String k4, Object o4, String k5, Object o5) {
        put(key, obj);
        put(k, o);
        put(k1, o1);
        put(k2, o2);
        put(k3, o3);
        put(k4, o4);
        put(k5, o5);
    }

}
