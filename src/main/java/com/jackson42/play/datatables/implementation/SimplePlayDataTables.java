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
 * @param <PROVIDER> the type parameter
 * @param <ENTITY>   the type parameter
 * @param <PAYLOAD>  the type parameter
 * @author Pierre Adam
 * @since 21.03.01
 */
public abstract class SimplePlayDataTables<ENTITY, PROVIDER, PAYLOAD extends Payload> implements PlayDataTables<ENTITY, PROVIDER, PAYLOAD> {

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
    protected final Class<ENTITY> entityClass;

    /**
     * The fields display suppliers. If set for a given field, the supplier will be called when forging the ajax response object.
     * If not set, the answer will try to reach the variable on the given T class.
     */
    private final Map<String, BiFunction<ENTITY, Context<PAYLOAD>, String>> fieldsDisplaySupplier;

    /**
     * The fields search handler. If set for a given field, the handler will be called when searching on that field.
     * If not set, the search will have no effect.
     */
    private final Map<String, BiConsumer<PROVIDER, String>> fieldsSearchHandler;

    /**
     * The fields order handler. If set for a given field, the handler will be called when ordering on that field.
     * If not set, the search will be set to the name of the field followed by "ASC" or "DESC"
     */
    private final Map<String, BiConsumer<PROVIDER, OrderEnum>> fieldsOrderHandler;

    /**
     * The initial provider supplier allows you to create your own initial provider.
     */
    private final Supplier<PROVIDER> providerSupplier;

    /**
     * The global search supplier. If set, the handler will be called when a search not specific to a field is required.
     */
    private BiConsumer<PROVIDER, String> globalSearchHandler;

    /**
     * Initialize the provider object if needed. Is called on each forged request.
     */
    private Consumer<PROVIDER> initProviderConsumer;

    /**
     * Instantiates a new A play data tables.
     *
     * @param entityClass      the entity class
     * @param messagesApi      the messages api
     * @param providerSupplier the query supplier
     */
    public SimplePlayDataTables(final Class<ENTITY> entityClass, final MessagesApi messagesApi, final Supplier<PROVIDER> providerSupplier) {
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
    public void setInitProviderConsumer(final Consumer<PROVIDER> initQueryConsumer) {
        this.initProviderConsumer = initQueryConsumer;
    }

    @Override
    public void setFieldDisplaySupplier(final String field, final Function<ENTITY, String> fieldSupplier) {
        this.fieldsDisplaySupplier.put(field, (entity, request) -> fieldSupplier.apply(entity));
    }

    @Override
    public void setFieldDisplaySupplier(final String field, final BiFunction<ENTITY, Context<PAYLOAD>, String> fieldSupplier) {
        this.fieldsDisplaySupplier.put(field, fieldSupplier);
    }

    @Override
    public void setFieldDisplayHtmlSupplier(final String field, final Function<ENTITY, Html> fieldSupplier) {
        this.fieldsDisplaySupplier.put(field, (t, request) -> fieldSupplier.apply(t).body());
    }

    @Override
    public void setFieldDisplayHtmlSupplier(final String field, final BiFunction<ENTITY, Context<PAYLOAD>, Html> fieldSupplier) {
        this.fieldsDisplaySupplier.put(field, (t, request) -> fieldSupplier.apply(t, request).body());
    }

    @Override
    public void setSearchHandler(final String field, final BiConsumer<PROVIDER, String> searchHandler) {
        this.fieldsSearchHandler.put(field, searchHandler);
    }

    @Override
    public void setOrderHandler(final String field, final BiConsumer<PROVIDER, OrderEnum> orderHandler) {
        this.fieldsOrderHandler.put(field, orderHandler);
    }

    @Override
    public void setGlobalSearchHandler(final BiConsumer<PROVIDER, String> globalSearchHandler) {
        this.globalSearchHandler = globalSearchHandler;
    }

    @Override
    public JsonNode getAjaxResult(final Http.Request request, final Parameters parameters) {
        return this.getAjaxResult(request, parameters, null);
    }

    @Override
    public JsonNode getAjaxResult(final Http.Request request, final Parameters parameters, final PAYLOAD suppliedPayload) {
        // Prepare data from the parameters and prepare the answer.
        final Map<Integer, Column> indexedColumns = parameters.getIndexedColumns();
        final AjaxResult result = new AjaxResult(parameters.getDraw());
        final PAYLOAD payload = suppliedPayload == null ? this.getDefaultPayload() : suppliedPayload;

        final PROVIDER provider = this.internalForgeInitialProvider(this.providerSupplier);

        if (this.initProviderConsumer != null) {
            this.initProviderConsumer.accept(provider);
        }

        //final DataSource<ENTITY> source = this.getFromSource(PROVIDER, this.globalSearchHandler, this.fieldsSearchHandler, this.fieldsOrderHandler, parameters, payload);
        final DataSource<ENTITY> source = this.processProvider(provider, payload, parameters);

        for (final ENTITY entity : source.getEntities()) {
            result.getData().add(this.objectToArrayNode(request, entity, indexedColumns, payload));
        }

        result.setRecordsTotal(source.getRecordsTotal());
        result.setRecordsFiltered(source.getRecordsFiltered());

        return Json.toJson(result);
    }

    protected DataSource<ENTITY> processProvider(final PROVIDER provider, final PAYLOAD payload, final Parameters parameters) {
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

    protected abstract void setPagination(final PROVIDER provider, int startElement, int numberOfElement);

    protected abstract void fallbackOrderHandler(final PROVIDER provider, String columnName, OrderEnum order);

    protected abstract void fallbackSearchHandler(PROVIDER provider, String columnName, String value);

    protected abstract DataSource<ENTITY> dataSourceFromProvider(final PROVIDER provider, final PAYLOAD payload);

    protected abstract void preSearchHook(final PROVIDER provider, final PAYLOAD payload, final Parameters parameters);

    protected abstract void postSearchHook(final PROVIDER provider, final PAYLOAD payload, final Parameters parameters);

    protected abstract void preOrderHook(final PROVIDER provider, final PAYLOAD payload, final Parameters parameters);

    protected abstract void postOrderHook(final PROVIDER provider, final PAYLOAD payload, final Parameters parameters);

    /**
     * Internal forge initial query query.
     *
     * @param initialProviderSupplier the initial query supplier
     * @return the query
     */
    protected PROVIDER internalForgeInitialProvider(final Supplier<PROVIDER> initialProviderSupplier) {
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
    protected JsonNode objectToArrayNode(final Http.Request request, final ENTITY entity, final Map<Integer, Column> indexedColumns, final PAYLOAD payload) {
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
    protected abstract PAYLOAD getDefaultPayload();

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
