package com.codepoetics.octarine.lenses;

import com.codepoetics.octarine.com.codepoetics.octarine.tuples.Pair;
import com.codepoetics.octarine.com.codepoetics.octarine.tuples.Pairable;

import java.util.function.Function;
import java.util.function.Supplier;

public interface BoundLens<T, V> extends Supplier<V>, Function<V, T>, Pairable<V, Function<V, T>> {

    public static <T, V> BoundLens<T, V> fromPair(Pair<V, Function<V, T>> pair) {
        return binding(pair.first(), pair.second());
    }

    public static <T, V> BoundLens<T, V> binding(V value, Function<V, T> setter) {
        return new SimpleBoundLens<T, V>(value, setter);
    }

    static class SimpleBoundLens<T, V> implements BoundLens<T, V> {
        private final V value;
        private final Function<V, T> setter;

        private SimpleBoundLens(V value, Function<V, T> setter) {
            this.value = value;
            this.setter = setter;
        }

        @Override
        public V get() { return value; }

        @Override
        public T apply(V value) { return setter.apply(value); }

        @Override public Pair<V, Function<V, T>> toPair() { return Pair.of(value, setter); }
    }

    default T update(Function<V, V> updater) {
        return apply(updater.apply(get()));
    }
}
