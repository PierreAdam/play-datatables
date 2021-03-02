package com.jackson42.play.datatables.interfaces;

import play.i18n.Messages;
import play.mvc.Http;

/**
 * PdtContext.
 *
 * @param <PAYLOAD> the type parameter
 * @author Pierre Adam
 * @since 21.03.01
 */
public interface Context<PAYLOAD extends Payload> {

    /**
     * Gets request.
     *
     * @return the request
     */
    Http.Request getRequest();

    /**
     * Gets an instance of messages.
     *
     * @return an instance of messages
     */
    Messages getMessages();

    /**
     * Gets payload.
     *
     * @return the payload
     */
    PAYLOAD getPayload();
}
