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

package tests;

import com.jackson42.play.datatables.converters.standards.DateTimeConverter;
import com.jackson42.play.datatables.exceptions.InitializationException;
import com.jackson42.play.datatables.implementations.BasicContext;
import com.jackson42.play.datatables.implementations.BasicPayload;
import mocking.play.MessagesApiFactory;
import org.junit.jupiter.api.*;
import play.i18n.MessagesApi;
import play.mvc.Http;
import play.test.Helpers;

import java.util.Locale;

/**
 * ContextTest.
 *
 * @author Pierre Adam
 * @since 21.03.31
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContextTest {

    /**
     * Validate the behavior when message api is missing.
     */
    @Test
    @Order(1)
    public void withoutMessageApi() {
        final Http.Request fakeRequest = Helpers.fakeRequest("POST", "https://localhost:9000/datatables")
                .transientLang(Locale.ENGLISH)
                .build();

        final BasicContext<BasicPayload> context = new BasicContext<>(fakeRequest, null, new BasicPayload());

        Assertions.assertThrows(InitializationException.class, context::getMessages);
    }

    /**
     * Validate the context.
     */
    @Test
    @Order(1)
    public void contextLogic() {
        final Http.Request fakeRequest = Helpers.fakeRequest("POST", "https://localhost:9000/datatables")
                .transientLang(Locale.ENGLISH)
                .build();
        final MessagesApi messagesApi = MessagesApiFactory.create();

        final BasicPayload basicPayload = new BasicPayload();
        basicPayload.put(DateTimeConverter.DATETIME_FORMAT, "yyyy-MM-dd");

        final BasicContext<BasicPayload> context = new BasicContext<>(fakeRequest, messagesApi, basicPayload);

        Assertions.assertEquals(basicPayload.get(DateTimeConverter.DATETIME_FORMAT), context.getPayload().get(DateTimeConverter.DATETIME_FORMAT));
        Assertions.assertEquals(fakeRequest, context.getRequest());
        Assertions.assertNotNull(context.getMessages());
    }
}
