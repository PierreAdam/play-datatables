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

package com.jackson42.play.datatables.interfaces;

import java.util.Map;

/**
 * Configurable.
 *
 * @param <T> the type parameter
 * @author Pierre Adam
 * @since 21.04.09
 */
public interface Configurable<T extends Configurable<T>> {

    /**
     * Add converter play data tables config.
     *
     * @param <U>       the type parameter
     * @param converter the converter
     * @return the play data tables config
     */
    <U> T addConverter(final Converter<U> converter);

    /**
     * Gets the converters.
     *
     * @return the converters
     */
    Map<Class<?>, Converter<?>> getConverters();
}
