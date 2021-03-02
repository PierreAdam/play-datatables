package com.jackson42.play.datatables.interfaces;

import play.libs.typedmap.TypedEntry;
import play.libs.typedmap.TypedKey;

import java.util.Optional;

/**
 * PlayDataTablesPayload.
 *
 * @author Pierre Adam
 * @since 21.03.01
 */
public interface Payload {

    /**
     * Put the typed key with it's value in the payload.
     *
     * @param <T>   the type parameter
     * @param key   the key
     * @param value the value
     */
    <T> void put(final TypedKey<T> key, final T value);

    /**
     * Put the typed entries in the payload.
     *
     * @param <T>          the type parameter
     * @param typedEntries the typed entries
     */
    <T> void putAll(final TypedEntry<?>... typedEntries);

    /**
     * Get data of T from the payload.
     *
     * @param <T> the type parameter
     * @param key the key
     * @return the t
     */
    <T> T get(TypedKey<T> key);

    /**
     * Get and optional data of T from the payload.
     *
     * @param <T> the type parameter
     * @param key the key
     * @return the t
     */
    <T> Optional<T> getOptional(TypedKey<T> key);
}
