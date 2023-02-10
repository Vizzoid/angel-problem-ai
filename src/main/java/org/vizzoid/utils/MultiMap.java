package org.vizzoid.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

/**
 * Is a map of a key and a list of values, provided with many list methods to easily modify and work with the list of values
 */
public class MultiMap<K, V> extends HashMap<K, List<V>> {

    private ThinMap thin = null;
    private ReverseMap reverse = null;

    public MultiMap() {
    }

    public MultiMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public MultiMap(int initialCapacity) {
        super(initialCapacity);
    }

    public MultiMap(Map<? extends K, ? extends List<V>> map) {
        super(map);
    }

    public static <K, V> MultiMap<K, V> fromReverse(Map<? extends V, ? extends K> reverseMap) {
        MultiMap<K, V> map = new MultiMap<>();
        reverseMap.forEach((v, k) -> map.add(k, v));
        return map;
    }

    public Collection<? extends V> valuesFlat() {
        return values().stream().flatMap(List::stream).toList();
    }

    public boolean add(K k, V v) {
        return get(k).add(v);
    }

    public void add(K k, int index, V v) {
        get(k).add(index, v);
    }

    public boolean subtract(K k, V v) {
        return get(k).remove(v);
    }

    public V subtract(K k, int index) {
        return get(k).remove(index);
    }

    public boolean contains(K k, V o) {
        return get(k).contains(o);
    }

    public boolean containsAll(K k, @NotNull Collection<? extends V> c) {
        return new HashSet<>(get(k)).containsAll(c);
    }

    public boolean addAll(K k, @NotNull Collection<? extends V> c) {
        return get(k).addAll(c);
    }

    public boolean removeAll(K k, @NotNull Collection<? extends V> c) {
        return get(k).removeAll(c);
    }

    public V get(K k, int index) {
        return get(k).get(index);
    }

    /*

    @Override
    public List<V> get(Object key) {
        return new ArrayList<>(super.get(key));
    }

    @Override
    public List<V> getOrDefault(Object key, List<V> defaultValue) {
        return new ArrayList<>(super.getOrDefault(key, defaultValue));
    }

    */

    @Override
    public List<V> get(Object key) {
        //noinspection unchecked
        return computeIfAbsent((K) key, k -> new ArrayList<>());
    }

    // Returns map with K and V reversed and list of V to just V
    public Map<V, K> reverse() {
        if (reverse != null) return reverse;
        return reverse = new ReverseMap();
    }

    // Returns map with list of V with V. NOT RECOMMENDED AS REMOVES GREAT AMOUNTS OF ENTRIES
    public Map<K, V> thin() {
        if (thin != null) return thin;
        return thin = new ThinMap();
    }

    @Override
    public Set<Entry<K, List<V>>> entrySet() {
        return super.entrySet();
    }

    @SuppressWarnings("unchecked")
    class ReverseMap implements Map<V, K> {
        @Override
        public int size() {
            return MultiMap.this.size();
        }

        @Override
        public boolean isEmpty() {
            return MultiMap.this.isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            return MultiMap.this.containsValue(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return MultiMap.this.containsKey(value);
        }

        @Override
        public K get(Object key) {
            for (K k : MultiMap.this.keySet()) {
                if (MultiMap.this.contains(k, (V) key)) return k;
            }
            return null;
        }

        @Nullable
        @Override
        public K put(V key, K value) {
            MultiMap.this.add(value, key);
            return null;
        }

        @SuppressWarnings("SuspiciousMethodCalls")
        @Override
        public K remove(Object key) {
            for (K k : MultiMap.this.keySet()) {
                if (MultiMap.this.get(k).remove(key)) return k;
            }
            return null;
        }

        @Override
        public void putAll(@NotNull Map<? extends V, ? extends K> m) {
            m.forEach((v, k) -> MultiMap.this.add(k, v));
        }

        @Override
        public void clear() {
            MultiMap.this.clear();
        }

        @NotNull
        @Override
        public Set<V> keySet() {
            Set<V> set = new HashSet<>();
            MultiMap.this.forEach((k, vl) -> {
                set.addAll(vl);
            });
            return set;
        }

        @NotNull
        @Override
        public Collection<K> values() {
            return MultiMap.this.keySet();
        }

        @NotNull
        @Override
        public Set<Entry<V, K>> entrySet() {
            Set<Entry<K, List<V>>> entries = MultiMap.this.entrySet();
            Set<Entry<V, K>> entrySet = new HashSet<>();
            for (Entry<K, List<V>> entry : entries) {
                for (V v : entry.getValue()) {
                    entrySet.add(new SimpleEntry<>(v, entry.getKey()));
                }
            }
            return entrySet;
        }
    }

    class ThinMap implements Map<K, V> {
        @Override
        public int size() {
            return MultiMap.this.size();
        }

        @Override
        public boolean isEmpty() {
            return MultiMap.this.isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            return MultiMap.this.containsKey(key);
        }

        @Override
        public boolean containsValue(Object value) {
            for (K k : keySet()) {
                //noinspection unchecked
                if (MultiMap.this.contains(k, (V) value)) return true;
            }
            return false;
        }

        @Override
        public V get(Object key) {
            List<V> vs = MultiMap.this.get(key);
            if (vs == null || vs.isEmpty()) return null;
            return vs.get(0);
        }

        @Nullable
        @Override
        public V put(K key, V value) {
            return MultiMap.this.get(key).set(0, value);
        }

        @Override
        public V remove(Object key) {
            return MultiMap.this.get(key).remove(0);
        }

        @Override
        public void putAll(@NotNull Map<? extends K, ? extends V> m) {
            m.forEach(MultiMap.this::add);
        }

        @Override
        public void clear() {
            MultiMap.this.clear();
        }

        @NotNull
        @Override
        public Set<K> keySet() {
            return MultiMap.this.keySet();
        }

        @NotNull
        @Override
        public Collection<V> values() {
            Collection<List<V>> values = MultiMap.this.values();
            List<V> list = new ArrayList<>();
            for (List<V> value : values) {
                list.addAll(value);
            }
            return list;
        }

        @NotNull
        @Override
        public Set<Entry<K, V>> entrySet() {
            Set<Entry<K, List<V>>> entries = MultiMap.this.entrySet();
            Set<Entry<K, V>> entrySet = new HashSet<>();
            for (Entry<K, List<V>> entry : entries) {
                for (V v : entry.getValue()) {
                    entrySet.add(new SimpleEntry<>(entry.getKey(), v));
                }
            }
            return entrySet;
        }
    }

    public V random(K k) {
        return random(k, ThreadLocalRandom.current());
    }

    public V random(K k, Random random) {
        if (containsKey(k)) {
            List<V> list = get(k);
            if (!list.isEmpty()) {
                return list.get(random.nextInt(list.size()));
            }
        }
        return null;
    }

    public static <K, V> MultiMap<K, V> by(List<V> list, Function<V, K> mapper) {
        MultiMap<K, V> map = new MultiMap<>();
        for (V v : list) {
            map.add(mapper.apply(v), v);
        }
        return map;
    }

}
