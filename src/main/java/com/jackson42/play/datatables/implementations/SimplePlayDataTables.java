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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jackson42.play.datatables.configs.PlayDataTablesConfig;
import com.jackson42.play.datatables.converters.Converter;
import com.jackson42.play.datatables.entities.Column;
import com.jackson42.play.datatables.entities.Parameters;
import com.jackson42.play.datatables.entities.internal.AjaxResult;
import com.jackson42.play.datatables.entities.internal.DataSource;
import com.jackson42.play.datatables.entities.internal.FieldBehavior;
import com.jackson42.play.datatables.enumerations.OrderEnum;
import com.jackson42.play.datatables.interfaces.Context;
import com.jackson42.play.datatables.interfaces.Payload;
import com.jackson42.play.datatables.interfaces.PlayDataTables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.mvc.Http;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * APlayDataTables.
 *
 * @param <E> the Entity type
 * @param <S> the Source Provider type
 * @param <P> the Payload type
 * @author Pierre Adam
 * @since 21.03.01
 */
public abstract class SimplePlayDataTables<E, S, P extends Payload> extends CustomizableDataTables<E, S, P> implements PlayDataTables<E, S, P> {

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
     * The fields specific behavior.
     * If set for a given field, the supplier, search handler or order handler might be used.
     */
    protected final Map<String, FieldBehavior<E, S, P>> fieldsBehavior;

    /**
     * The instance specific converters.
     */
    private final Map<Class<?>, Converter<?>> converters;

    /**
     * The initial provider supplier allows you to create your own initial provider.
     */
    protected final Supplier<S> providerSupplier;

    /**
     * The global search supplier. If set, the handler will be called when a search not specific to a field is required.
     */
    protected BiConsumer<S, String> globalSearchHandler;

    /**
     * Initialize the provider object if needed. Is called on each forged request.
     */
    protected Consumer<S> initProviderConsumer;

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

        this.fieldsBehavior = new HashMap<>();
        this.converters = new HashMap<>();
        this.globalSearchHandler = null;
    }

    @Override
    public void setInitProviderConsumer(final Consumer<S> initQueryConsumer) {
        this.initProviderConsumer = initQueryConsumer;
    }

    @Override
    public void setGlobalSearchHandler(final BiConsumer<S, String> globalSearchHandler) {
        this.globalSearchHandler = globalSearchHandler;
    }

    @Override
    public JsonNode getAjaxResult(final Http.Request request, final Parameters parameters, final P suppliedPayload) {
        // Prepare data from the parameters and prepare the answer.
        final AjaxResult result = new AjaxResult(parameters.getDraw());
        final P payload = suppliedPayload == null ? this.getDefaultPayload() : suppliedPayload;

        final S provider = this.internalForgeInitialProvider(this.providerSupplier);

        if (this.initProviderConsumer != null) {
            this.initProviderConsumer.accept(provider);
        }

        final DataSource<E> source = this.processProvider(provider, payload, parameters);

        for (final E entity : source.getEntities()) {
            result.getData().add(this.objectToArrayNode(request, entity, parameters, payload));
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
        this.applySearch(provider, parameters);
        this.postSearchHook(provider, payload, parameters);

        this.preOrderHook(provider, payload, parameters);
        this.applyOrder(provider, parameters);
        this.postOrderHook(provider, payload, parameters);

        return this.dataSourceFromProvider(provider, payload);
    }

    /**
     * Apply search.
     *
     * @param provider   the provider
     * @param parameters the parameters
     */
    private void applySearch(final S provider, final Parameters parameters) {
        // Process global search.
        if (parameters.hasGlobalSearch()) {
            if (this.globalSearchHandler != null) {
                this.globalSearchHandler.accept(provider, parameters.getSearch().getValue());
            } else {
                this.logger.warn("A global search has been asked for but the global search handler is null. setGlobalSearchHandler needs to be called.");
            }
        }

        // Process Column search.
        parameters.getColumns()
                .stream()
                .filter(column -> column != null && column.hasSearch())
                .forEach(column -> {
                    final String columnName = column.getName();
                    final Optional<BiConsumer<S, String>> optionalSearchHandler = this.field(columnName).getSearchHandler();

                    if (optionalSearchHandler.isPresent()) {
                        optionalSearchHandler.get().accept(provider, column.getSearch().getValue());
                    } else {
                        this.fallbackSearchHandler(provider, columnName, column.getSearch().getValue());
                    }
                });
    }

    /**
     * Apply order.
     *
     * @param provider   the provider
     * @param parameters the parameters
     */
    private void applyOrder(final S provider, final Parameters parameters) {
        final Map<Integer, Column> indexedColumns = parameters.getIndexedColumns();

        parameters.getOrder().forEach(order -> {
            final String columnName = indexedColumns.get(order.getColumn()).getName();
            final Optional<BiConsumer<S, OrderEnum>> optionalOrderHandler = this.field(columnName).getOrderHandler();

            if (optionalOrderHandler.isPresent()) {
                optionalOrderHandler.get().accept(provider, order.getOrder());
            } else {
                this.fallbackOrderHandler(provider, columnName, order.getOrder());
            }
        });
    }

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
     * @param request    the request
     * @param entity     the object
     * @param parameters the parameters
     * @param payload    the payload
     * @return the array node
     */
    protected JsonNode objectToArrayNode(final Http.Request request, final E entity, final Parameters parameters, final P payload) {
        final ArrayNode data = Json.newArray();

        parameters.getOrderedColumns().forEach(column -> {
            if (column == null) {
                data.addNull();
            } else {
                final Optional<BiFunction<E, Context<P>, String>> optionalDisplaySupplier = this.field(column.getName()).getDisplaySupplier();

                final BasicContext<P> context = new BasicContext<>(request, this.messagesApi, payload);
                if (optionalDisplaySupplier.isPresent()) {
                    data.add(optionalDisplaySupplier.get().apply(entity, context));
                } else {
                    this.resolveColumn(column, data, entity, context);
                }
            }
        });

        return data;
    }

    /**
     * Resolve column.
     *
     * @param column  the column
     * @param data    the data
     * @param entity  the entity
     * @param context the context
     */
    protected void resolveColumn(final Column column, final ArrayNode data, final E entity, final Context<P> context) {
        final Method method = this.methodForColumn(column);

        if (method == null) {
            data.addNull();
            return;
        }

        try {
            final Object obj = method.invoke(entity);
            Converter<?> converter = null;

            // Try getting a converter with the instance converters.
            converter = this.getConverter(obj, this.converters);

            if (converter == null) {
                // Try getting a converter from the global converters.
                converter = this.getConverter(obj, PlayDataTablesConfig.getInstance().getConverters());
            }

            if (converter != null) {
                converter.addToArray(data, obj, context.asGeneric());
            } else {
                data.addNull();
            }
        } catch (final IllegalAccessException | InvocationTargetException e) {
            data.addNull();
        }
    }

    /**
     * Gets converter.
     *
     * @param obj        the obj
     * @param converters the converters
     * @return the converter
     */
    protected Converter<?> getConverter(final Object obj, final Map<Class<?>, Converter<?>> converters) {
        final Class<?> objClass = obj.getClass();

        if (converters.containsKey(objClass)) {
            // If the class is explicitly added with a converter.
            return converters.get(objClass);
        }

        // If a converter could match with a sub-type.
        for (final Map.Entry<Class<?>, Converter<?>> entry : converters.entrySet()) {
            if (entry.getKey().isAssignableFrom(objClass)) {
                return entry.getValue();
            }
        }

        return null;
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

    @Override
    public FieldBehavior<E, S, P> field(final String fieldName) {
        if (!this.fieldsBehavior.containsKey(fieldName)) {
            this.fieldsBehavior.put(fieldName, new FieldBehavior<>());
        }
        return this.fieldsBehavior.get(fieldName);
    }

    @Override
    public void setField(final String fieldName, final FieldBehavior<E, S, P> fieldBehavior) {
        this.fieldsBehavior.put(fieldName, fieldBehavior);
    }

    @Override
    public <T> void addConverter(final Converter<T> converter) {
        this.converters.put(converter.getBackedType(), converter);
    }
}
