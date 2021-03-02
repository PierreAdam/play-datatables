/*
 * MIT License
 *
 * Copyright (c) 2021 Pierre Adam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
