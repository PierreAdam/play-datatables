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

package com.jackson42.play.datatables.entities;

import javax.validation.Valid;

/**
 * ColumnEntity.
 *
 * @author Pierre Adam
 * @since 20.07.02
 */
public class Column {

    /**
     * The number of the column.
     */
    private int data;

    /**
     * The name of the column.
     */
    private String name;

    /**
     * Is the column searcheable.
     */
    private boolean searcheable;

    /**
     * Is the column orderable.
     */
    private boolean orderable;

    /**
     * The search object.
     */
    @Valid
    private Search search;

    /**
     * Get the data.
     *
     * @return the data
     */
    public int getData() {
        return this.data;
    }

    /**
     * Set the data
     *
     * @param data the data
     */
    public void setData(final int data) {
        this.data = data;
    }

    /**
     * Get the name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the name
     *
     * @param name the name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Is searcheable ?
     *
     * @return true if is searcheable
     */
    public boolean isSearcheable() {
        return this.searcheable;
    }

    /**
     * Set searcheable.
     *
     * @param searcheable true if searcheable
     */
    public void setSearcheable(final boolean searcheable) {
        this.searcheable = searcheable;
    }

    /**
     * Is orderable ?
     *
     * @return true if is orderable
     */
    public boolean isOrderable() {
        return this.orderable;
    }

    /**
     * Set orderable.
     *
     * @param orderable true if orderable
     */
    public void setOrderable(final boolean orderable) {
        this.orderable = orderable;
    }

    /**
     * Get search query.
     *
     * @return search query
     */
    public Search getSearch() {
        return this.search;
    }

    /**
     * Set search query.
     *
     * @param search search query
     */
    public void setSearch(final Search search) {
        this.search = search;
    }

    /**
     * Has search boolean.
     *
     * @return the boolean
     */
    public boolean hasSearch() {
        return this.getSearch() != null && this.getSearch().getValue() != null && !this.getSearch().getValue().isEmpty();
    }
}
