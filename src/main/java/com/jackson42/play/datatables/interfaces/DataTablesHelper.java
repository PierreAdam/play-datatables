/*
 * MIT License
 *
 * Copyright (c) 2020 Pierre Adam
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

package com.jackson42.play.datatables.interfaces;

import com.jackson42.play.datatables.entities.AjaxQueryForm;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Http;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * PlayDatatable.
 *
 * @author Pierre Adam
 * @since 20.07.02
 */
public interface DataTablesHelper {

    /**
     * Forge a form from the request and call the errorCallback or the successCallback according to the form.
     *
     * @param <RESULT>        the type of the return
     * @param request         the request
     * @param formFactory     the form factory
     * @param errorCallback   the on error
     * @param successCallback the callback
     * @return the r
     */
    default <RESULT> RESULT dataTablesAjaxRequest(final Http.Request request,
                                                  final FormFactory formFactory,
                                                  final Function<Form<AjaxQueryForm>, RESULT> errorCallback,
                                                  final Function<AjaxQueryForm, RESULT> successCallback) {
        final Form<AjaxQueryForm> boundForm = formFactory.form(AjaxQueryForm.class).bindFromRequest(request);

        if (boundForm.hasErrors()) {
            return errorCallback.apply(boundForm);
        }

        return successCallback.apply(boundForm.get());
    }

    /**
     * Forge a form from the request and call the errorCallback or the successCallback according to the form.
     * Return a CompletionStage that process the form and callbacks asynchronously.
     *
     * @param <RESULT>        the type of the return
     * @param request         the request
     * @param formFactory     the form factory
     * @param errorCallback   the on error
     * @param successCallback the callback
     * @return the r
     */
    default <RESULT> CompletionStage<RESULT> dataTablesAjaxRequestAsync(final Http.Request request,
                                                                        final FormFactory formFactory,
                                                                        final Function<Form<AjaxQueryForm>, RESULT> errorCallback,
                                                                        final Function<AjaxQueryForm, RESULT> successCallback) {
        final Form<AjaxQueryForm> ajaxQueryForm = formFactory.form(AjaxQueryForm.class).bindFromRequest(request);

        return CompletableFuture.supplyAsync(() -> {
            if (ajaxQueryForm.hasErrors()) {
                return errorCallback.apply(ajaxQueryForm);
            }
            return successCallback.apply(ajaxQueryForm.get());
        });
    }
}
