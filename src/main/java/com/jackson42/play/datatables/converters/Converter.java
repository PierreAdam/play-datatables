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

package com.jackson42.play.datatables.converters;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jackson42.play.datatables.interfaces.Context;
import com.jackson42.play.datatables.interfaces.Payload;

/**
 * The converters may extends this class to have a standardized behavior.
 *
 * @param <T> the type parameter
 * @author Pierre Adam
 * @since 21.03.28
 */
public abstract class Converter<T> {

    /**
     * The class of T.
     */
    protected final Class<T> tClass;

    /**
     * Instantiates a new converter.
     *
     * @param tClass the class of T
     */
    public Converter(final Class<T> tClass) {
        this.tClass = tClass;
    }

    /**
     * Gets backed type.
     *
     * @return the backed type
     */
    public final Class<T> getBackedType() {
        return this.tClass;
    }

    /**
     * Add the object as a value in the ArrayNode.
     *
     * @param array   the array
     * @param obj     the object
     * @param context the context
     */
    public final void addToArray(final ArrayNode array, final Object obj, final Context<Payload> context) {
        this.internalAddToArray(array, this.getBackedType().cast(obj), context);
    }

    /**
     * Internal logic of the method addToArray.
     *
     * @param array   the array
     * @param obj     the object
     * @param context the context
     */
    protected abstract void internalAddToArray(final ArrayNode array, final T obj, final Context<Payload> context);
}
