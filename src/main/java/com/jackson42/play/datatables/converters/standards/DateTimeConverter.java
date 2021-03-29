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

package com.jackson42.play.datatables.converters.standards;

import com.jackson42.play.datatables.converters.ConverterToString;
import com.jackson42.play.datatables.interfaces.Context;
import com.jackson42.play.datatables.interfaces.Payload;
import org.joda.time.DateTime;
import play.libs.typedmap.TypedKey;

import java.util.Optional;

/**
 * DateTimeConverter.
 *
 * @author Pierre Adam
 * @since 21.03.29
 */
public class DateTimeConverter extends ConverterToString<DateTime> {

    /**
     * Instantiates a new converter.
     */
    public DateTimeConverter() {
        super(DateTime.class);
    }

    @Override
    public String convert(final DateTime obj, final Context<Payload> context) {
        final Optional<String> optionalFormat = context.getPayload().getOptional(Config.DATETIME_FORMAT);

        if (optionalFormat.isPresent()) {
            return obj.toString(optionalFormat.get());
        } else {
            return obj.toString("dd/MM/yyyy");
        }
    }

    /**
     * The configuration class.
     */
    public static class Config {

        /**
         * The constant DATETIME_FORMAT.
         */
        public static final TypedKey<String> DATETIME_FORMAT = TypedKey.create();
    }
}
