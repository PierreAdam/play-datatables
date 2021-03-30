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

import javax.validation.Valid;
import java.util.*;

/**
 * AjaxQueryForm.
 *
 * @author Pierre Adam
 * @since 20.07.02
 */
public class Parameters {

    /**
     * The draw number. It is increased by one each time the page request the server and must be given back in the answer.
     */
    private int draw;

    /**
     * The list of columns.
     */
    @Valid
    private List<Column> columns;

    /**
     * The order.
     */
    @Valid
    private List<Order> order;

    /**
     * The start offset.
     */
    private int start;

    /**
     * The length of the data to send.
     */
    private int length;

    /**
     * The search.
     */
    @Valid
    private Search search;

    /**
     * Get the draw number.
     *
     * @return the draw number
     */
    public int getDraw() {
        return this.draw;
    }

    /**
     * Set the draw number.
     *
     * @param draw the draw number
     */
    public void setDraw(final int draw) {
        this.draw = draw;
    }

    /**
     * Get the list of columns.
     *
     * @return the list of columns
     */
    public List<Column> getColumns() {
        return this.columns;
    }

    /**
     * Get the list of columns or an empty list if columns is null.
     *
     * @return the list of columns
     */
    public List<Column> getSafeColumns() {
        return this.columns != null ? this.columns : new ArrayList<>();
    }

    /**
     * Set the list of columns.
     *
     * @param columns the list of columns
     */
    public void setColumns(final List<Column> columns) {
        this.columns = columns;
    }

    /**
     * Get the list for ordering.
     *
     * @return the list of ordering
     */
    public List<Order> getOrder() {
        return this.order != null ? this.order : new ArrayList<>();
    }

    /**
     * Get the list for ordering or an empty list if order is null.
     *
     * @return the list of ordering
     */
    public List<Order> getSafeOrder() {
        return this.order != null ? this.order : new ArrayList<>();
    }

    /**
     * Set the list for ordering.
     *
     * @param order the list for ordering
     */
    public void setOrder(final List<Order> order) {
        this.order = order;
    }

    /**
     * Get the start index.
     *
     * @return the start index
     */
    public int getStart() {
        return this.start;
    }

    /**
     * Set the start index.
     *
     * @param start the start index
     */
    public void setStart(final int start) {
        this.start = start;
    }

    /**
     * Get the length of the result.
     *
     * @return the length of the result
     */
    public int getLength() {
        return this.length;
    }

    /**
     * Set the length of the result.
     *
     * @param length the length of the result
     */
    public void setLength(final int length) {
        this.length = length;
    }

    /**
     * Get the search
     *
     * @return the search
     */
    public Search getSearch() {
        return this.search;
    }

    /**
     * Set the search.
     *
     * @param search the search
     */
    public void setSearch(final Search search) {
        this.search = search;
    }

    /**
     * Does the parameters have a search request ?
     *
     * @return true if there is a search
     */
    public boolean hasGlobalSearch() {
        return this.getSearch() != null && this.getSearch().getValue() != null && !this.getSearch().getValue().isEmpty();
    }

    /**
     * Get the indexed columns.
     *
     * @return the indexed columns
     */
    public Map<Integer, Column> getIndexedColumns() {
        return new HashMap<Integer, Column>() {{
            for (final Column column : Parameters.this.getSafeColumns()) {
                this.put(column.getData(), column);
            }
        }};
    }

    /**
     * Gets ordered columns.
     *
     * @return the ordered columns
     */
    public List<Column> getOrderedColumns() {
        final TreeMap<Integer, Column> treeMap = new TreeMap<>(this.getIndexedColumns());
        return new ArrayList<>(treeMap.values());
    }
}
