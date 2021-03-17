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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jackson42.play.datatables.entities.Column;
import com.jackson42.play.datatables.entities.Order;
import com.jackson42.play.datatables.entities.Parameters;
import com.jackson42.play.datatables.entities.internal.AjaxResult;
import com.jackson42.play.datatables.entities.internal.DataSource;
import com.jackson42.play.datatables.enumerations.OrderEnum;
import com.jackson42.play.datatables.interfaces.Context;
import com.jackson42.play.datatables.interfaces.Payload;
import com.jackson42.play.datatables.interfaces.PlayDataTables;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.mvc.Http;
import play.twirl.api.Html;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.*;

/**
 * APlayDataTables.
 *
 * @param <E> the Entity type
 * @param <S> the Source Provider type
 * @param <P> the Payload type
 * @author Pierre Adam
 * @since 21.03.01
 */
public abstract class SimplePlayDataTables<E, S, P extends Payload> implements PlayDataTables<E, S, P> {

    /**
     * The potential prefixes of the getter in the target classes.
     */
    protected static final String[] METHOD_PREFIXES = {"get", "is", "has", "can"};

    /**
     * The Logger.
     */
    protected final Logger logger;

    /**
     * The Messages api.
     */
    protected final MessagesApi messagesApi;

    /**
     * The Entity class.
     */
    protected final Class<E> entityClass;

    /**
     * The fields display suppliers. If set for a given field, the supplier will be called when forging the ajax response object.
     * If not set, the answer will try to reach the variable on the given T class.
     */
    private final Map<String, BiFunction<E, Context<P>, String>> fieldsDisplaySupplier;

    /**
     * The fields search handler. If set for a given field, the handler will be called when searching on that field.
     * If not set, the search will have no effect.
     */
    private final Map<String, BiConsumer<S, String>> fieldsSearchHandler;

    /**
     * The fields order handler. If set for a given field, the handler will be called when ordering on that field.
     * If not set, the search will be set to the name of the field followed by "ASC" or "DESC"
     */
    private final Map<String, BiConsumer<S, OrderEnum>> fieldsOrderHandler;

    /**
     * The initial provider supplier allows you to create your own initial provider.
     */
    private final Supplier<S> providerSupplier;

    /**
     * The global search supplier. If set, the handler will be called when a search not specific to a field is required.
     */
    private BiConsumer<S, String> globalSearchHandler;

    /**
     * Initialize the provider object if needed. Is called on each forged request.
     */
    private Consumer<S> initProviderConsumer;

    /**
     * Instantiates a new A play data tables.
     *
     * @param entityClass      the entity class
     * @param messagesApi      the messages api
     * @param providerSupplier the query supplier
     */
    public SimplePlayDataTables(final Class<E> entityClass, final MessagesApi messagesApi, final Supplier<S> providerSupplier) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.entityClass = entityClass;
        this.messagesApi = messagesApi;
        this.providerSupplier = providerSupplier;

        this.fieldsDisplaySupplier = new HashMap<>();
        this.fieldsSearchHandler = new HashMap<>();
        this.fieldsOrderHandler = new HashMap<>();
        this.globalSearchHandler = null;
    }

    @Override
    public void setInitProviderConsumer(final Consumer<S> initQueryConsumer) {
        this.initProviderConsumer = initQueryConsumer;
    }

    @Override
    public void setFieldDisplaySupplier(final String field, final Function<E, String> fieldSupplier) {
        this.fieldsDisplaySupplier.put(field, (entity, request) -> fieldSupplier.apply(entity));
    }

    @Override
    public void setFieldDisplaySupplier(final String field, final BiFunction<E, Context<P>, String> fieldSupplier) {
        this.fieldsDisplaySupplier.put(field, fieldSupplier);
    }

    @Override
    public void setFieldDisplayHtmlSupplier(final String field, final Function<E, Html> fieldSupplier) {
        this.fieldsDisplaySupplier.put(field, (t, request) -> fieldSupplier.apply(t).body());
    }

    @Override
    public void setFieldDisplayHtmlSupplier(final String field, final BiFunction<E, Context<P>, Html> fieldSupplier) {
        this.fieldsDisplaySupplier.put(field, (t, request) -> fieldSupplier.apply(t, request).body());
    }

    @Override
    public void setSearchHandler(final String field, final BiConsumer<S, String> searchHandler) {
        this.fieldsSearchHandler.put(field, searchHandler);
    }

    @Override
    public void setOrderHandler(final String field, final BiConsumer<S, OrderEnum> orderHandler) {
        this.fieldsOrderHandler.put(field, orderHandler);
    }

    @Override
    public void setGlobalSearchHandler(final BiConsumer<S, String> globalSearchHandler) {
        this.globalSearchHandler = globalSearchHandler;
    }

    @Override
    public JsonNode getAjaxResult(final Http.Request request, final Parameters parameters) {
        return this.getAjaxResult(request, parameters, null);
    }

    @Override
    public JsonNode getAjaxResult(final Http.Request request, final Parameters parameters, final P suppliedPayload) {
        // Prepare data from the parameters and prepare the answer.
        final Map<Integer, Column> indexedColumns = parameters.getIndexedColumns();
        final AjaxResult result = new AjaxResult(parameters.getDraw());
        final P payload = suppliedPayload == null ? this.getDefaultPayload() : suppliedPayload;

        final S provider = this.internalForgeInitialProvider(this.providerSupplier);

        if (this.initProviderConsumer != null) {
            this.initProviderConsumer.accept(provider);
        }

        //final DataSource<ENTITY> source = this.getFromSource(PROVIDER, this.globalSearchHandler, this.fieldsSearchHandler, this.fieldsOrderHandler, parameters, payload);
        final DataSource<E> source = this.processProvider(provider, payload, parameters);

        for (final E entity : source.getEntities()) {
            result.getData().add(this.objectToArrayNode(request, entity, indexedColumns, payload));
        }

        result.setRecordsTotal(source.getRecordsTotal());
        result.setRecordsFiltered(source.getRecordsFiltered());

        return Json.toJson(result);
    }

    /**
     * Process provider data source.
     *
     * @param provider   the provider
     * @param payload    the payload
     * @param parameters the parameters
     * @return the data source
     */
    protected DataSource<E> processProvider(final S provider, final P payload, final Parameters parameters) {
        // Set the pagination on the provider.
        this.setPagination(provider, parameters.getStart(), parameters.getLength());

        this.preSearchHook(provider, payload, parameters);

        // Process global search.
        if (parameters.hasGlobalSearch()) {
            if (this.globalSearchHandler != null) {
                this.globalSearchHandler.accept(provider, parameters.getSearch().getValue());
            } else {
                this.logger.warn("A global search has been asked for but the global search handler is null. setGlobalSearchHandler needs to be called.");
            }
        }

        // Process Column search.
        final Map<Integer, Column> indexedColumns = parameters.getIndexedColumns();
        indexedColumns.forEach((idx, column) -> {
            if (column == null || !column.hasSearch()) {
                return;
            }
            final String columnName = column.getName();
            if (this.fieldsSearchHandler.containsKey(columnName)) {
                this.fieldsSearchHandler.get(column.getName()).accept(provider, column.getSearch().getValue());
            } else {
                this.fallbackSearchHandler(provider, columnName, column.getSearch().getValue());
            }
        });

        this.postSearchHook(provider, payload, parameters);

        this.preOrderHook(provider, payload, parameters);

        if (parameters.getOrder() != null) {
            for (final Order order : parameters.getOrder()) {
                final String columnName = indexedColumns.get(order.getColumn()).getName();
                if (this.fieldsOrderHandler.containsKey(columnName)) {
                    this.fieldsOrderHandler.get(columnName).accept(provider, order.getOrder());
                } else {
                    this.fallbackOrderHandler(provider, columnName, order.getOrder());
                }
            }
        }

        this.postOrderHook(provider, payload, parameters);

        return this.dataSourceFromProvider(provider, payload);
    }

    /**
     * Sets pagination.
     *
     * @param provider        the provider
     * @param startElement    the start element
     * @param numberOfElement the number of element
     */
    protected abstract void setPagination(final S provider, int startElement, int numberOfElement);

    /**
     * Fallback order handler.
     *
     * @param provider   the provider
     * @param columnName the column name
     * @param order      the order
     */
    protected abstract void fallbackOrderHandler(final S provider, String columnName, OrderEnum order);

    /**
     * Fallback search handler.
     *
     * @param provider   the provider
     * @param columnName the column name
     * @param value      the value
     */
    protected abstract void fallbackSearchHandler(S provider, String columnName, String value);

    /**
     * Data source from provider data source.
     *
     * @param provider the provider
     * @param payload  the payload
     * @return the data source
     */
    protected abstract DataSource<E> dataSourceFromProvider(final S provider, final P payload);

    /**
     * Pre search hook.
     *
     * @param provider   the provider
     * @param payload    the payload
     * @param parameters the parameters
     */
    protected abstract void preSearchHook(final S provider, final P payload, final Parameters parameters);

    /**
     * Post search hook.
     *
     * @param provider   the provider
     * @param payload    the payload
     * @param parameters the parameters
     */
    protected abstract void postSearchHook(final S provider, final P payload, final Parameters parameters);

    /**
     * Pre order hook.
     *
     * @param provider   the provider
     * @param payload    the payload
     * @param parameters the parameters
     */
    protected abstract void preOrderHook(final S provider, final P payload, final Parameters parameters);

    /**
     * Post order hook.
     *
     * @param provider   the provider
     * @param payload    the payload
     * @param parameters the parameters
     */
    protected abstract void postOrderHook(final S provider, final P payload, final Parameters parameters);

    /**
     * Internal forge initial query query.
     *
     * @param initialProviderSupplier the initial query supplier
     * @return the query
     */
    protected S internalForgeInitialProvider(final Supplier<S> initialProviderSupplier) {
        return initialProviderSupplier.get();
    }

    /**
     * Convert an object to an Array node using the indexed columns.
     *
     * @param request        the request
     * @param entity         the object
     * @param indexedColumns the indexed column
     * @param payload        the payload
     * @return the array node
     */
    protected JsonNode objectToArrayNode(final Http.Request request, final E entity, final Map<Integer, Column> indexedColumns, final P payload) {
        final ArrayNode data = Json.newArray();

        for (int i = 0; i < indexedColumns.size(); i++) {
            final Column column = indexedColumns.get(i);
            if (column == null) {
                data.addNull();
                continue;
            }

            if (this.fieldsDisplaySupplier.containsKey(column.getName())) {
                data.add(this.fieldsDisplaySupplier.get(column.getName()).apply(entity, new BasicContext<>(request, this.messagesApi, payload)));
            } else {
                final Method method = this.methodForColumn(column);
                if (method == null) {
                    data.addNull();
                    continue;
                }

                try {
                    SimplePlayDataTables.addToArray(data, method.invoke(entity));
                } catch (final IllegalAccessException | InvocationTargetException e) {
                    data.addNull();
                }
            }
        }

        return data;
    }

    /**
     * Try to solve a getter for a given column.
     *
     * @param column the column
     * @return the method or null
     */
    protected Method methodForColumn(final Column column) {
        for (final String methodPrefix : SimplePlayDataTables.METHOD_PREFIXES) {
            try {
                return this.entityClass.getMethod(methodPrefix + StringUtils.capitalize(column.getName()));
            } catch (final NoSuchMethodException ignore) {
            }
        }

        try {
            return this.entityClass.getMethod(column.getName());
        } catch (final NoSuchMethodException ignore) {
        }

        return null;
    }

    /**
     * Gets default payload.
     *
     * @return the default payload
     */
    protected abstract P getDefaultPayload();

    /**
     * Add an object to a Json ArrayNode trying to solve the type.
     * If the object can't be solved, null is put on the array.
     *
     * @param data   the array
     * @param object the object
     */
    private static void addToArray(final ArrayNode data, final Object object) {
        if (object == null) {
            data.addNull();
        } else if (object instanceof String) {
            data.add((String) object);
        } else if (object instanceof Integer) {
            data.add((Integer) object);
        } else if (object instanceof Long) {
            data.add((Long) object);
        } else if (object instanceof Double) {
            data.add((Double) object);
        } else if (object instanceof UUID) {
            data.add(object.toString());
        } else if (object instanceof Boolean) {
            data.add((Boolean) object);
        } else if (object instanceof JsonNode) {
            data.add((JsonNode) object);
        } else if (object instanceof Enum) {
            final String tmp = object.toString();
            try {
                data.add(Integer.valueOf(tmp));
            } catch (final NumberFormatException ignore) {
                data.add(tmp);
            }
        } else if (object instanceof DateTime) {
            data.add(((DateTime) object).toString("dd/MM/yyyy"));
        } else {
            data.addNull();
        }
    }
}
