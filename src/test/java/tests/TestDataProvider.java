package tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.jackson42.play.datatables.entities.Column;
import com.jackson42.play.datatables.entities.Parameters;
import com.jackson42.play.datatables.entities.Search;
import com.jackson42.play.datatables.enumerations.OrderEnum;
import dataprovider.DummyProvider;
import dataprovider.MyDataTable;
import dataprovider.PersonEntity;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.ParametersHelper;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * tests.TestDataProvider.
 *
 * @author Pierre Adam
 * @since 21.03.02
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestDataProvider {

    private final Logger logger;

    private final MyDataTable myDataTable;

    /**
     * Instantiates a new Test data provider.
     */
    TestDataProvider() {
        this.logger = LoggerFactory.getLogger(TestDataProvider.class);
        this.myDataTable = new MyDataTable(null);
        this.myDataTable.setGlobalSearchHandler((dummyProvider, search) ->
                dummyProvider.alterData(list -> list.stream().filter(entity ->
                        String.format("%s %s", entity.getFirstName(), entity.getLastName()).contains(search)).collect(Collectors.toList()))
        );

        this.myDataTable.setSearchHandler("firstName", (dummyProvider, search) ->
                dummyProvider.alterData(list -> list.stream().filter(entity -> entity.getFirstName().contains(search)).collect(Collectors.toList()))
        );

        this.myDataTable.setSearchHandler("bloodGroup", (dummyProvider, search) ->
                dummyProvider.alterData(list -> {
                            final List<PersonEntity> newList = list
                                    .stream()
                                    .filter(entity -> entity.getBloodGroup().contains(search))
                                    .peek(entity -> this.logger.debug("KEEP : {}", entity.toString()))
                                    .collect(Collectors.toList());

                            return newList;
                        }
                )
        );

        final Function<Function<PersonEntity, String>, BiConsumer<DummyProvider, OrderEnum>> orderHandler = method ->
                (dummyProvider, orderEnum) ->
                        dummyProvider.alterData(list -> {
                            list.sort(Comparator.comparing(method));
                            return list;
                        });

        this.myDataTable.setOrderHandler("firstName", orderHandler.apply(PersonEntity::getFirstName));
        this.myDataTable.setOrderHandler("lastName", orderHandler.apply(PersonEntity::getLastName));
        this.myDataTable.setOrderHandler("title", orderHandler.apply(PersonEntity::getTitle));
        this.myDataTable.setOrderHandler("bloodGroup", orderHandler.apply(PersonEntity::getBloodGroup));

        this.myDataTable.setFieldDisplaySupplier("firstName", (entity, payloadContext) -> entity.getFirstName());
        this.myDataTable.setFieldDisplaySupplier("lastName", (entity, payloadContext) -> entity.getLastName());
        this.myDataTable.setFieldDisplaySupplier("title", (entity, payloadContext) -> entity.getTitle());
        this.myDataTable.setFieldDisplaySupplier("bloodGroup", (entity, payloadContext) -> entity.getBloodGroup());
    }

    /**
     * Check dummy query.
     */
    @Test
    @Order(1)
    public void checkDummyQuery() {
        final DummyProvider querySource = new DummyProvider();
        final List<PersonEntity> data = querySource.getData();

        for (final PersonEntity entity : data) {
            this.logger.trace("Entry : {}", entity.toString());
        }

        // Check if the data has been correctly generated.
        Assertions.assertEquals(DummyProvider.SAMPLE_SIZE, data.size());
    }

    /**
     * Pagination.
     */
    @Test
    @Order(2)
    public void pagination() {
        final Parameters parameters = ParametersHelper.createForNameEntity();

        final JsonNode ajaxResult = this.myDataTable.getAjaxResult(null, parameters);

        this.logger.trace("{}", ajaxResult.toPrettyString());
        Assertions.assertTrue(ajaxResult.has("data"));

        final JsonNode data = ajaxResult.get("data");
        Assertions.assertTrue(data.isArray());
        Assertions.assertEquals(parameters.getLength(), data.size());
    }

    /**
     * Search.
     */
    @Test
    @Order(3)
    public void search() {
        final Parameters parameters = ParametersHelper.createForNameEntity();

        final Column bloodTypeColumn = parameters.getColumns().get(3);
        final Search search = new Search();
        search.setValue("A+");
        bloodTypeColumn.setSearch(search);

        final JsonNode ajaxResult = this.myDataTable.getAjaxResult(null, parameters);

        this.logger.trace("{}", ajaxResult.toPrettyString());

        final JsonNode data = ajaxResult.get("data");
        Assertions.assertTrue(data.isArray());
        for (final JsonNode node : data) {
            Assertions.assertEquals("A+", node.get(3).asText());
        }
    }
}
