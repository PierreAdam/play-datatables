package com.jackson42.play.datatables.implementation;

import com.jackson42.play.datatables.interfaces.Payload;
import play.libs.typedmap.TypedEntry;
import play.libs.typedmap.TypedKey;
import play.libs.typedmap.TypedMap;

import java.util.Optional;

/**
 * PlayDataTablesPayloadImpl.
 *
 * @author Pierre Adam
 * @since 21.03.01
 */
public class BasicPayload implements Payload {

    /**
     * The Typed map.
     */
    private TypedMap typedMap;

    /**
     * Instantiates a new Play data tables payload.
     */
    public BasicPayload() {
        this.typedMap = TypedMap.create();
    }

    @Override
    public <T> void put(final TypedKey<T> key, final T value) {
        this.typedMap = this.typedMap.put(key, value);
    }

    @Override
    public <T> void putAll(final TypedEntry<?>... typedEntries) {
        this.typedMap = this.typedMap.putAll(typedEntries);
    }

    @Override
    public <T> T get(final TypedKey<T> key) {
        return this.typedMap.get(key);
    }

    @Override
    public <T> Optional<T> getOptional(final TypedKey<T> key) {
        return this.typedMap.getOptional(key);
    }
}
