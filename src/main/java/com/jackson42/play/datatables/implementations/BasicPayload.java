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

package com.jackson42.play.datatables.implementations;

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
        this.putAll(key.bindValue(value));
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
