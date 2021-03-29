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

package com.jackson42.play.datatables.configs;

import com.jackson42.play.datatables.converters.Converter;
import com.jackson42.play.datatables.converters.standards.*;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * PlayDataTablesConfig.
 *
 * @author Pierre Adam
 * @since 21.03.29
 */
@Singleton
public class PlayDataTablesConfig {

    /**
     * The global instance of PlayDataTablesConfig.
     */
    private static PlayDataTablesConfig instance;

    /**
     * The Converters.
     */
    private final Map<Class<?>, Converter<?>> converters;

    /**
     * Instantiates a new PlayDataTablesConfig.
     */
    private PlayDataTablesConfig() {
        this.converters = new HashMap<>();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public synchronized static PlayDataTablesConfig getInstance() {
        if (PlayDataTablesConfig.instance == null) {
            PlayDataTablesConfig.instance = new PlayDataTablesConfig();
            PlayDataTablesConfig.instance
                    .addConverter(new StringConverter())
                    .addConverter(new IntegerConverter())
                    .addConverter(new LongConverter())
                    .addConverter(new DoubleConverter())
                    .addConverter(new BooleanConverter())
                    .addConverter(new EnumConverter())
                    .addConverter(new UUIDConverter())
                    .addConverter(new JsonNodeConverter())
                    .addConverter(new DateTimeConverter());
        }
        return PlayDataTablesConfig.instance;
    }

    /**
     * Add converter play data tables config.
     *
     * @param <T>       the type parameter
     * @param converter the converter
     * @return the play data tables config
     */
    public <T> PlayDataTablesConfig addConverter(final Converter<T> converter) {
        this.converters.put(converter.getBackedType(), converter);
        return this;
    }

    /**
     * Gets the converters.
     *
     * @return the converters
     */
    public Map<Class<?>, Converter<?>> getConverters() {
        return this.converters;
    }
}
