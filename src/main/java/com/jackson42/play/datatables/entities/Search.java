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

package com.jackson42.play.datatables.entities;

/**
 * ColumnSearchForm.
 *
 * @author Pierre Adam
 * @since 20.07.02
 */
public class Search {

    /**
     * The name to search.
     */
    private String value;

    /**
     * Is the search a regex.
     */
    private boolean regex;

    /**
     * Get the name.
     *
     * @return the name
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Set the name
     *
     * @param value the name
     */
    public void setValue(final String value) {
        this.value = value;
    }

    /**
     * Is the query a regex
     *
     * @return true if regex
     */
    public boolean isRegex() {
        return this.regex;
    }

    /**
     * Set is a regex.
     *
     * @param regex true if is a regex
     */
    public void setRegex(final boolean regex) {
        this.regex = regex;
    }
}
