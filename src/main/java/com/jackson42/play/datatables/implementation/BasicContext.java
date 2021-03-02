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
