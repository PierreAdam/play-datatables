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

package com.jackson42.play.datatables.entities.internal;

import com.jackson42.play.datatables.enumerations.OrderEnum;
import com.jackson42.play.datatables.interfaces.Context;
import com.jackson42.play.datatables.interfaces.Payload;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * FieldBehavior.
 *
 * @param <E> the Entity type
 * @param <S> the Source Provider type
 * @param <P> the Payload type
 * @author Pierre Adam
 * @since 21.03.18
 */
public class FieldBehavior<E, S, P extends Payload> {

    /**
     * If set, the supplier will be called when forging the ajax response object.
     * If not set, the value will try to be resolved from the type of the data.
     */
    private BiFunction<E, Context<P>, String> displaySupplier;

    /**
     * If set, the search handler will be called when searching.
     * If not set, the search will fallback to the default behavior
     */
    private BiConsumer<S, String> searchHandler;

    /**
     * The Order handler.
     */
    private BiConsumer<S, OrderEnum> orderHandler;

    /**
     * Gets display supplier.
     *
     * @return the display supplier
     */
    public Optional<BiFunction<E, Context<P>, String>> getDisplaySupplier() {
        return Optional.of(this.displaySupplier);
    }

    /**
     * Sets display supplier.
     *
     * @param displaySupplier the display supplier
     * @return the display supplier
     */
    public FieldBehavior<E, S, P> setDisplaySupplier(final BiFunction<E, Context<P>, String> displaySupplier) {
        this.displaySupplier = displaySupplier;
        return this;
    }

    /**
     * Gets search handler.
     *
     * @return the search handler
     */
    public Optional<BiConsumer<S, String>> getSearchHandler() {
        return Optional.of(this.searchHandler);
    }

    /**
     * Sets search handler.
     *
     * @param searchHandler the search handler
     * @return the search handler
     */
    public FieldBehavior<E, S, P> setSearchHandler(final BiConsumer<S, String> searchHandler) {
        this.searchHandler = searchHandler;
        return this;
    }

    /**
     * Gets order handler.
     *
     * @return the order handler
     */
    public Optional<BiConsumer<S, OrderEnum>> getOrderHandler() {
        return Optional.of(this.orderHandler);
    }

    /**
     * Sets order handler.
     *
     * @param orderHandler the order handler
     * @return the order handler
     */
    public FieldBehavior<E, S, P> setOrderHandler(final BiConsumer<S, OrderEnum> orderHandler) {
        this.orderHandler = orderHandler;
        return this;
    }
}
