package tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.jackson42.play.datatables.entities.Column;
import com.jackson42.play.datatables.entities.Parameters;
import com.jackson42.play.datatables.entities.Search;
import com.jackson42.play.datatables.entities.internal.DataSource;
import com.jackson42.play.datatables.enumerations.OrderEnum;
import com.jackson42.play.datatables.interfaces.DataTablesHelper;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Application;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Http;
import play.test.Helpers;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * FormEntitiesTest.
 *
 * @author Pierre Adam
 * @since 21.03.31
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EntitiesTest implements DataTablesHelper {

    /**
     * The Logger.
     */
    private final Logger logger;

    /**
     * Instantiates a new Form entities test.
     */
    public EntitiesTest() {
        this.logger = LoggerFactory.getLogger(DataProviderTest.class);
    }

    /**
     * Validate the Parameters logic.
     */
    @Test
    @Order(0)
    public void parametersLogic() {
        final Parameters parameters = new Parameters();

        Assertions.assertFalse(parameters.hasGlobalSearch());
        Assertions.assertNotNull(parameters.getSafeColumns());
        Assertions.assertNotNull(parameters.getSafeOrder());

        parameters.setSearch(new Search());
        Assertions.assertFalse(parameters.hasGlobalSearch());

        parameters.getSearch().setValue("");
        Assertions.assertFalse(parameters.hasGlobalSearch());

        parameters.getSearch().setValue("something");
        Assertions.assertTrue(parameters.hasGlobalSearch());
    }

    /**
     * Validate the Column logic.
     */
    @Test
    @Order(1)
    public void columnLogic() {
        final Column column = new Column();

        Assertions.assertFalse(column.hasSearch());

        column.setSearch(new Search());
        Assertions.assertFalse(column.hasSearch());

        column.getSearch().setValue("");
        Assertions.assertFalse(column.hasSearch());

        column.getSearch().setValue("something");
        Assertions.assertTrue(column.hasSearch());
    }

    /**
     * Validate the Order entity.
     */
    @Test
    @Order(2)
    public void orderLogic() {
        final com.jackson42.play.datatables.entities.Order order = new com.jackson42.play.datatables.entities.Order();

        order.setDir("asc");
        Assertions.assertEquals(OrderEnum.ASC, order.getOrder());
        order.setDir("AsC");
        Assertions.assertEquals(OrderEnum.ASC, order.getOrder());
        order.setDir("desc");
        Assertions.assertEquals(OrderEnum.DESC, order.getOrder());
        order.setDir("DeSc");
        Assertions.assertEquals(OrderEnum.DESC, order.getOrder());
        order.setDir("invalid");
        Assertions.assertEquals(OrderEnum.ASC, order.getOrder());
    }

    /**
     * Validate the Order entity.
     */
    @Test
    @Order(3)
    public void dataSourceLogic() {
        final DataSource<Integer> integerDataSource = new DataSource<>();

        integerDataSource.setEntities(Arrays.asList(1, 2, 3));
        integerDataSource.setRecordsFiltered(50);
        integerDataSource.setRecordsTotal(80);

        Assertions.assertEquals(3, integerDataSource.getEntities().size());
        Assertions.assertEquals(50, integerDataSource.getRecordsFiltered());
        Assertions.assertEquals(80, integerDataSource.getRecordsTotal());
    }

    /**
     * Load simple json.
     */
    @Test
    @Order(10)
    public void loadSimpleJson() {
        final Application application = Helpers.fakeApplication();
        final FormFactory formFactory = application.injector().instanceOf(FormFactory.class);
        Assertions.assertNotNull(formFactory);

        final Http.Request fakeRequest = Helpers.fakeRequest("POST", "https://localhost:9000/datatables")
                .transientLang(Locale.ENGLISH)
                .bodyJson(this.loadBody("simple.json"))
                .build();

        Assertions.assertEquals("Success",
                this.dataTablesAjaxRequest(fakeRequest, formFactory, boundForm -> {
                    this.logger.error("Error on the form {}", boundForm.errors());
                    Assertions.fail("The form wasn't valid !");
                    return "Fail";
                }, form -> {
                    Assertions.assertNotNull(form);
                    final Parameters parameters = form.getParameters();
                    Assertions.assertNotNull(parameters);
                    Assertions.assertNotNull(parameters.getColumns());
                    Assertions.assertNotNull(parameters.getOrder());
                    Assertions.assertEquals(0, parameters.getStart());
                    Assertions.assertEquals(10, parameters.getLength());
                    Assertions.assertEquals(5, parameters.getDraw());
                    Assertions.assertNull(parameters.getSearch());
                    Assertions.assertFalse(parameters.hasGlobalSearch());
                    Assertions.assertFalse(parameters.getColumns().isEmpty());
                    Assertions.assertFalse(parameters.getOrder().isEmpty());
                    parameters.getColumns().forEach(column -> {
                        Assertions.assertNotNull(column.getName());
                        Assertions.assertNull(column.getSearch());
                    });

                    return "Success";
                })
        );
    }

    /**
     * Load complete json.
     */
    @Test
    @Order(11)
    public void loadCompleteJson() {
        final Application application = Helpers.fakeApplication();
        final FormFactory formFactory = application.injector().instanceOf(FormFactory.class);
        Assertions.assertNotNull(formFactory);

        final Http.Request fakeRequest = Helpers.fakeRequest("POST", "https://localhost:9000/datatables")
                .transientLang(Locale.ENGLISH)
                .bodyJson(this.loadBody("complete.json"))
                .build();

        Assertions.assertEquals("Success",
                this.dataTablesAjaxRequest(fakeRequest, formFactory, boundForm -> {
                    this.logger.error("Error on the form {}", boundForm.errors());
                    Assertions.fail("The form wasn't valid !");
                    return "Fail";
                }, form -> {
                    Assertions.assertNotNull(form);
                    final Parameters parameters = form.getParameters();
                    Assertions.assertNotNull(parameters);
                    Assertions.assertNotNull(parameters.getColumns());
                    Assertions.assertNotNull(parameters.getOrder());
                    Assertions.assertEquals(0, parameters.getStart());
                    Assertions.assertEquals(10, parameters.getLength());
                    Assertions.assertEquals(42, parameters.getDraw());
                    Assertions.assertNotNull(parameters.getSearch());
                    Assertions.assertTrue(parameters.hasGlobalSearch());
                    Assertions.assertFalse(parameters.getColumns().isEmpty());
                    Assertions.assertFalse(parameters.getOrder().isEmpty());
                    parameters.getColumns().forEach(column -> {
                        Assertions.assertNotNull(column.getName());
                        if (column.getData() > 0) {
                            Assertions.assertNotNull(column.getSearch());
                            Assertions.assertTrue(column.hasSearch());
                        } else {
                            Assertions.assertFalse(column.hasSearch());
                            Assertions.assertNull(column.getSearch());
                        }
                    });

                    final Map<Integer, Column> indexedColumns = parameters.getIndexedColumns();
                    Assertions.assertEquals(3, indexedColumns.size());
                    Assertions.assertNotNull(indexedColumns.get(0));
                    Assertions.assertNotNull(indexedColumns.get(1));
                    Assertions.assertNotNull(indexedColumns.get(2));

                    final List<Column> orderedColumns = parameters.getOrderedColumns();
                    for (int i = 0; i < orderedColumns.size(); i++) {
                        Assertions.assertEquals(indexedColumns.get(i), orderedColumns.get(i));
                    }

                    return "Success";
                })
        );
    }

    /**
     * Load complete json.
     */
    @Test
    @Order(12)
    public void loadInvalidJson() {
        final Application application = Helpers.fakeApplication();
        final FormFactory formFactory = application.injector().instanceOf(FormFactory.class);
        Assertions.assertNotNull(formFactory);

        final Http.Request fakeRequest = Helpers.fakeRequest("POST", "https://localhost:9000/datatables")
                .transientLang(Locale.ENGLISH)
                .bodyJson(this.loadBody("invalid.json"))
                .build();

        Assertions.assertEquals("Success",
                this.dataTablesAjaxRequest(fakeRequest, formFactory, boundForm -> {
                    return "Success";
                }, form -> {
                    Assertions.fail("The form shouldn't have worked !");
                    return "Fail";
                })
        );
    }

    /**
     * Load complete json.
     */
    @Test
    @Order(13)
    public void loadWeirdJson() {
        final Application application = Helpers.fakeApplication();
        final FormFactory formFactory = application.injector().instanceOf(FormFactory.class);
        Assertions.assertNotNull(formFactory);

        final Http.Request fakeRequest = Helpers.fakeRequest("POST", "https://localhost:9000/datatables")
                .transientLang(Locale.ENGLISH)
                .bodyJson(this.loadBody("weird.json"))
                .build();

        Assertions.assertEquals("Success",
                this.dataTablesAjaxRequest(fakeRequest, formFactory, boundForm -> {
                    this.logger.error("Error on the form {}", boundForm.errors());
                    Assertions.fail("The form wasn't valid !");
                    return "Fail";
                }, form -> {
                    Assertions.assertNotNull(form);
                    final Parameters parameters = form.getParameters();
                    Assertions.assertNotNull(parameters);
                    Assertions.assertNull(parameters.getColumns());
                    Assertions.assertNull(parameters.getOrder());
                    Assertions.assertEquals(0, parameters.getStart());
                    Assertions.assertEquals(10, parameters.getLength());
                    Assertions.assertEquals(1, parameters.getDraw());
                    Assertions.assertNotNull(parameters.getSearch());
                    Assertions.assertFalse(parameters.hasGlobalSearch());
                    parameters.getSearch().setValue("");
                    Assertions.assertFalse(parameters.hasGlobalSearch());
                    Assertions.assertTrue(parameters.getSafeColumns().isEmpty());
                    Assertions.assertTrue(parameters.getSafeOrder().isEmpty());
                    return "Success";
                })
        );
    }

    /**
     * Load complete json.
     */
    @Test
    @Order(14)
    public void loadAsyncJson() {
        final Application application = Helpers.fakeApplication();
        final FormFactory formFactory = application.injector().instanceOf(FormFactory.class);
        Assertions.assertNotNull(formFactory);

        final Http.Request fakeRequest = Helpers.fakeRequest("POST", "https://localhost:9000/datatables")
                .transientLang(Locale.ENGLISH)
                .bodyJson(this.loadBody("simple.json"))
                .build();

        try {
            Assertions.assertEquals("Success", this
                    .dataTablesAjaxRequestAsync(fakeRequest, formFactory, boundForm -> {
                        this.logger.error("Error on the form {}", boundForm.errors());
                        Assertions.fail("The form wasn't valid !");
                        return "Fail";
                    }, form -> {
                        Assertions.assertNotNull(form);
                        final Parameters parameters = form.getParameters();
                        Assertions.assertNotNull(parameters);
                        Assertions.assertNotNull(parameters.getColumns());
                        Assertions.assertNotNull(parameters.getOrder());
                        Assertions.assertEquals(0, parameters.getStart());
                        Assertions.assertEquals(10, parameters.getLength());
                        Assertions.assertEquals(5, parameters.getDraw());
                        Assertions.assertNull(parameters.getSearch());
                        Assertions.assertFalse(parameters.hasGlobalSearch());
                        Assertions.assertFalse(parameters.getColumns().isEmpty());
                        Assertions.assertFalse(parameters.getOrder().isEmpty());
                        parameters.getColumns().forEach(column -> {
                            Assertions.assertNotNull(column.getName());
                            Assertions.assertNull(column.getSearch());
                        });

                        return "Success";
                    })
                    .toCompletableFuture()
                    .get()
            );
        } catch (final InterruptedException | ExecutionException e) {
            this.logger.error("Error while executing the completable future", e);
            Assertions.fail("Something went wrong while executing the completable future");
        }
    }

    /**
     * Load complete json.
     */
    @Test
    @Order(14)
    public void loadInvalidAsyncJson() {
        final Application application = Helpers.fakeApplication();
        final FormFactory formFactory = application.injector().instanceOf(FormFactory.class);
        Assertions.assertNotNull(formFactory);

        final Http.Request fakeRequest = Helpers.fakeRequest("POST", "https://localhost:9000/datatables")
                .transientLang(Locale.ENGLISH)
                .bodyJson(this.loadBody("invalid.json"))
                .build();

        try {
            Assertions.assertEquals("Success", this
                    .dataTablesAjaxRequestAsync(fakeRequest, formFactory, boundForm -> {
                        return "Success";
                    }, form -> {
                        Assertions.fail("The form shouldn't have worked !");
                        return "Fail";
                    })
                    .toCompletableFuture()
                    .get()
            );
        } catch (final InterruptedException | ExecutionException e) {
            this.logger.error("Error while executing the completable future", e);
            Assertions.fail("Something went wrong while executing the completable future");
        }
    }

    /**
     * Load body json node.
     *
     * @param filename the filename
     * @return the json node
     */
    private JsonNode loadBody(final String filename) {
        final String path = "testdata/formentities/" + filename;
        final URL resource = this.getClass().getClassLoader().getResource(path);
        if (resource == null) {
            throw new RuntimeException("The file [" + path + "] was not found ! Aborting test !");
        } else {
            try {
                return Json.mapper().reader().readTree(resource.openStream());
            } catch (final IOException e) {
                throw new RuntimeException("An error occurred while loading the file [" + path + "]. Aborting test !", e);
            }
        }
    }
}
