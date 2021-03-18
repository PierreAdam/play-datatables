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

package com.jackson42.play.datatables.implementation;

import com.jackson42.play.datatables.entities.Parameters;
import com.jackson42.play.datatables.entities.internal.DataSource;
import com.jackson42.play.datatables.enumerations.OrderEnum;
import com.jackson42.play.datatables.interfaces.Payload;

/**
 * DataTablesHooks.
 *
 * @param <E> the Entity type
 * @param <S> the Source Provider type
 * @param <P> the Payload type
 * @author Pierre Adam
 * @since 21.03.18
 */
public abstract class CustomizableDataTables<E, S, P extends Payload> {

    /**
     * Gets default payload.
     *
     * @return the default payload
     */
    protected abstract P getDefaultPayload();

    /**
     * Sets pagination.
     *
     * @param provider        the provider
     * @param startElement    the start element
     * @param numberOfElement the number of element
     */
    protected abstract void setPagination(final S provider, int startElement, int numberOfElement);

    /**
     * Data source from provider data source.
     *
     * @param provider the provider
     * @param payload  the payload
     * @return the data source
     */
    protected abstract DataSource<E> dataSourceFromProvider(final S provider, final P payload);

    /**
     * Fallback order handler.
     *
     * @param provider   the provider
     * @param columnName the column name
     * @param order      the order
     */
    protected void fallbackOrderHandler(final S provider, final String columnName, final OrderEnum order) {
        // Default behavior does nothing
    }

    /**
     * Fallback search handler.
     *
     * @param provider   the provider
     * @param columnName the column name
     * @param value      the value
     */
    protected void fallbackSearchHandler(final S provider, final String columnName, final String value) {
        // Default behavior does nothing
    }

    /**
     * Pre search hook.
     *
     * @param provider   the provider
     * @param payload    the payload
     * @param parameters the parameters
     */
    protected void preSearchHook(final S provider, final P payload, final Parameters parameters) {
        // Default behavior does nothing
    }

    /**
     * Post search hook.
     *
     * @param provider   the provider
     * @param payload    the payload
     * @param parameters the parameters
     */
    protected void postSearchHook(final S provider, final P payload, final Parameters parameters) {
        // Default behavior does nothing
    }

    /**
     * Pre order hook.
     *
     * @param provider   the provider
     * @param payload    the payload
     * @param parameters the parameters
     */
    protected void preOrderHook(final S provider, final P payload, final Parameters parameters) {
        // Default behavior does nothing
    }

    /**
     * Post order hook.
     *
     * @param provider   the provider
     * @param payload    the payload
     * @param parameters the parameters
     */
    protected void postOrderHook(final S provider, final P payload, final Parameters parameters) {
        // Default behavior does nothing
    }
}
