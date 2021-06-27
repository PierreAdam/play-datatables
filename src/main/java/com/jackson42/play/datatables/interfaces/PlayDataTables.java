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
import com.jackson42.play.datatables.entities.internal.FieldBehavior;
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
public interface PlayDataTables<E, S, P extends Payload> extends Configurable<PlayDataTables<E, S, P>> {

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
     * @param fieldName     the field name
     * @param fieldSupplier the field display supplier
     */
    default void setFieldDisplaySupplier(final String fieldName, final Function<E, String> fieldSupplier) {
        this.field(fieldName).setDisplaySupplier((entity, request) -> fieldSupplier.apply(entity));
    }

    /**
     * The fields display suppliers. If set for a given field, the supplier will be called when forging the ajax response object.
     * If not set, the answer will try to reach the variable on the given T class.
     *
     * @param fieldName     the field name
     * @param fieldSupplier the field display supplier
     */
    default void setFieldDisplaySupplier(final String fieldName, final BiFunction<E, Context<P>, String> fieldSupplier) {
        this.field(fieldName).setDisplaySupplier(fieldSupplier);
    }

    /**
     * The fields display suppliers. If set for a given field, the supplier will be called when forging the ajax response object.
     * If not set, the answer will try to reach the variable on the given T class.
     *
     * @param fieldName     the field name
     * @param fieldSupplier the field display supplier
     */
    default void setFieldDisplayHtmlSupplier(final String fieldName, final Function<E, Html> fieldSupplier) {
        this.field(fieldName).setDisplaySupplier((entity, request) -> fieldSupplier.apply(entity).body());
    }

    /**
     * The fields display suppliers. If set for a given field, the supplier will be called when forging the ajax response object.
     * If not set, the answer will try to reach the variable on the given T class.
     *
     * @param fieldName     the field name
     * @param fieldSupplier the field display supplier
     */
    default void setFieldDisplayHtmlSupplier(final String fieldName, final BiFunction<E, Context<P>, Html> fieldSupplier) {
        this.field(fieldName).setDisplaySupplier((entity, request) -> fieldSupplier.apply(entity, request).body());
    }

    /**
     * The fields search handler. If set for a given field, the handler will be called when searching on that field.
     * If not set, the search will have no effect.
     *
     * @param fieldName     the field name
     * @param searchHandler the field search handler
     */
    default void setSearchHandler(final String fieldName, final BiConsumer<S, String> searchHandler) {
        this.field(fieldName).setSearchHandler(searchHandler);
    }

    /**
     * The fields order handler. If set for a given field, the handler will be called when ordering on that field.
     * If not set, the search will be set to the name of the field followed by "ASC" or "DESC"
     *
     * @param fieldName    the field name
     * @param orderHandler the field order handler
     */
    default void setOrderHandler(final String fieldName, final BiConsumer<S, OrderEnum> orderHandler) {
        this.field(fieldName).setOrderHandler(orderHandler);
    }

    /**
     * The global search supplier. If set, the handler will be called when a search not specific to a field is required.
     *
     * @param globalSearchHandler the global search handler
     */
    void setGlobalSearchHandler(final BiConsumer<S, String> globalSearchHandler);

    /**
     * Builds the Ajax result in the form of a Json ObjectNode. Parameters SHOULD come from a form.
     *
     * @param request    the request
     * @param parameters the parameters
     * @param payload    the payload
     * @return the Json ObjectNode
     * @see Parameters
     */
    JsonNode getAjaxResult(final Http.Request request, final Parameters parameters, final P payload);

    /**
     * Builds the Ajax result in the form of a Json ObjectNode. Parameters SHOULD come from a form.
     *
     * @param request    the request
     * @param parameters the parameters
     * @return the Json ObjectNode
     * @see Parameters
     */
    default JsonNode getAjaxResult(final Http.Request request, final Parameters parameters) {
        return this.getAjaxResult(request, parameters, null);
    }

    /**
     * Gets the field behavior.
     *
     * @param fieldName the field name
     * @return the field behavior
     */
    FieldBehavior<E, S, P> field(final String fieldName);

    /**
     * Sets the field behavior.
     *
     * @param fieldName     the field name
     * @param fieldBehavior the field behavior
     */
    void setField(final String fieldName, final FieldBehavior<E, S, P> fieldBehavior);
}
