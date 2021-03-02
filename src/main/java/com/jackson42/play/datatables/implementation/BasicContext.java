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

import com.jackson42.play.datatables.interfaces.Context;
import com.jackson42.play.datatables.interfaces.Payload;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.Http;

/**
 * Context.
 *
 * @param <PAYLOAD> the type of the payload
 * @author Pierre Adam
 * @since 21.03.01
 */
public class BasicContext<PAYLOAD extends Payload> implements Context<PAYLOAD> {

    /**
     * The current request.
     */
    private final Http.Request request;

    /**
     * The instance of the preferred messages from the request.
     */
    private final Messages messages;

    /**
     * The current payload.
     */
    private final PAYLOAD payload;

    /**
     * Instantiates a new Basic context.
     *
     * @param request     the request
     * @param messagesApi the messages api
     * @param payload     the payload
     */
    public BasicContext(final Http.Request request, final MessagesApi messagesApi, final PAYLOAD payload) {
        this.request = request;
        this.messages = messagesApi == null ? null : messagesApi.preferred(request);
        this.payload = payload;
    }

    @Override
    public Http.Request getRequest() {
        return this.request;
    }

    @Override
    public Messages getMessages() {
        if (this.messages == null) {
            throw new RuntimeException("Unable to resolve the Messages. MessagesApi is missing !");
        }
        return this.messages;
    }

    @Override
    public PAYLOAD getPayload() {
        return this.payload;
    }
}
