package org.vizzoid.utils.data;

public class SerializedUtils {/*

    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    private static final File storage;
    private static final Serializer serializer = new Serializer();
    private static final Serializer deserialized;

    static {
        Serializer deserialized1;
        URL url = SerializedUtils.class.getClassLoader().getResource("data.yml");
        if (url != null) {
            storage = new File(url.getFile());
            try {
                deserialized1 = mapper.readValue(storage, Serializer.class);
            } catch (IOException e) {
                e.printStackTrace();
                deserialized1 = new Serializer();
            }
            Shutdown.add(() -> mapper.writeValue(storage, serializer), SerializedUtils.class, Priority.LOWEST);
        } else {
            storage = null;
            deserialized1 = new Serializer();
        }
        deserialized = deserialized1;
    }

    public static @NotNull Optional<Data> deserialize(Class<?> identifier, Predicate<Data> predicate) {
        return deserialize(identifier).stream().filter(predicate).findFirst();
    }

    public static @NotNull List<Data> deserialize(Class<?> identifier) {
        return deserialized.getSerialized(identifier);
    }

    public static IData convert(DataHolder holder) {
        return holder.toData();
    }

    public static Collection<? extends IData> convert(Collection<? extends DataHolder> holder) {
        return holder.stream().map(SerializedUtils::convert).toList();
    }

    public static Collection<? extends IData> convert(Map<?, ? extends DataHolder> holder) {
        return convert(holder.values());
    }

    public static <T> T revert(Data data, Class<T> returnType) {
        try {
            return (T) returnType.getMethod("fromData", Data.class).invoke(null, data);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> Collection<? extends T> revert(Collection<? extends Data> holder, final Class<T> returnType) {
        return holder.stream().map(d -> revert(d, returnType)).toList();
    }

    public static <T> Collection<? extends T> revert(Map<?, ? extends Data> holder, Class<T> returnType) {
        return revert(holder.values(), returnType);
    }
*/

    public static void add(MutableData object) {
        // serializer.add(object);
    }

}
