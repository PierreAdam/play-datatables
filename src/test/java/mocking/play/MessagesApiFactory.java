package mocking.play;

import play.api.i18n.DefaultMessagesApi;
import play.i18n.MessagesApi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * FakeMessagesApi.
 *
 * @author Pierre Adam
 * @since 21.03.31
 */
public class MessagesApiFactory {

    private MessagesApiFactory() {
    }

    public static MessagesApi create() {
        final Map<String, Map<String, String>> data = new HashMap<>();

        data.put("en", Collections.singletonMap("hello", "Hello"));
        data.put("fr", Collections.singletonMap("hello", "Bonjour"));
        data.put("es", Collections.singletonMap("hello", "Buenos dias"));

        return new MessagesApi(new DefaultMessagesApi(data));
    }
}
