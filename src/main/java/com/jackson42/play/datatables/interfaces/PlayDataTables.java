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

import com.fasterxml.jackson.databind.JsonNode;
import com.jackson42.play.datatables.entities.Parameters;
import com.jackson42.play.datatables.enumerations.OrderEnum;
import play.mvc.Http;
import play.twirl.api.Html;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * PlayDataTables.
 *
 * @param <E> the Entity type
 * @param <S> the Source Provider type
 * @param <P> the Payload type
 * @author Pierre Adam
 * @since 21.03.01
 */
public interface PlayDataTables<E, S, P extends Payload> {

    /**
     * The initial where condition. Is called on each forged request and should not contains orders or weird things.
     *
     * @param initialQuery the consumer that allows to set the initial query.
     */
    void setInitProviderConsumer(final Consumer<S> initialQuery);

    /**
     * The fields display suppliers. If set for a given field, the supplier will be called when forging the ajax response object.
     * If not set, the answer will try to reach the variable on the given T class.
     *
     * @param field         the field name
     * @param fieldSupplier the field display supplier
     */
    void setFieldDisplaySupplier(final String field, final Function<E, String> fieldSupplier);

    /**
     * The fields display suppliers. If set for a given field, the supplier will be called when forging the ajax response object.
     * If not set, the answer will try to reach the variable on the given T class.
     *
     * @param field         the field name
     * @param fieldSupplier the field display supplier
     */
    void setFieldDisplaySupplier(final String field, final BiFunction<E, Context<P>, String> fieldSupplier);

    /**
     * The fields display suppliers. If set for a given field, the supplier will be called when forging the ajax response object.
     * If not set, the answer will try to reach the variable on the given T class.
     *
     * @param field         the field name
     * @param fieldSupplier the field display supplier
     */
    void setFieldDisplayHtmlSupplier(final String field, final Function<E, Html> fieldSupplier);

    /**
     * The fields display suppliers. If set for a given field, the supplier will be called when forging the ajax response object.
     * If not set, the answer will try to reach the variable on the given T class.
     *
     * @param field         the field name
     * @param fieldSupplier the field display supplier
     */
    void setFieldDisplayHtmlSupplier(final String field, final BiFunction<E, Context<P>, Html> fieldSupplier);

    /**
     * The fields search handler. If set for a given field, the handler will be called when searching on that field.
     * If not set, the search will have no effect.
     *
     * @param field         the field name
     * @param searchHandler the field search handler
     */
    void setSearchHandler(final String field, final BiConsumer<S, String> searchHandler);

    /**
     * The fields order handler. If set for a given field, the handler will be called when ordering on that field.
     * If not set, the search will be set to the name of the field followed by "ASC" or "DESC"
     *
     * @param field        the field name
     * @param orderHandler the field order handler
     */
    void setOrderHandler(final String field, final BiConsumer<S, OrderEnum> orderHandler);

    /**
     * The global search supplier. If set, the handler will be called when a search not specific to a field is required.
     *
     * @param globalSearchHandler the global search handler
     */
    void setGlobalSearchHandler(final BiConsumer<S, String> globalSearchHandler);

    /**
     * Build the Ajax result in the form of a Json ObjectNode. Parameters SHOULD come from a form.
     *
     * @param request    the request
     * @param parameters the parameters
     * @param payload    the payload
     * @return the Json ObjectNode
     * @see Parameters
     */
    JsonNode getAjaxResult(final Http.Request request, final Parameters parameters, final P payload);

    /**
     * Build the Ajax result in the form of a Json ObjectNode. Parameters SHOULD come from a form.
     *
     * @param request    the request
     * @param parameters the parameters
     * @return the Json ObjectNode
     * @see Parameters
     */
    JsonNode getAjaxResult(final Http.Request request, final Parameters parameters);
}
