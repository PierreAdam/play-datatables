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

package com.jackson42.play.datatables.entities.internal;

import com.fasterxml.jackson.databind.node.ArrayNode;
import play.libs.Json;

/**
 * AjaxResult.
 *
 * @author Pierre Adam
 * @since 21.03.01
 */
public class AjaxResult {

    /**
     * The Draw.
     */
    final int draw;

    /**
     * The Records total.
     */
    long recordsTotal;

    /**
     * The Records filtered.
     */
    long recordsFiltered;

    /**
     * The Data.
     */
    ArrayNode data;

    /**
     * Instantiates a new Ajax result.
     *
     * @param draw the draw
     */
    public AjaxResult(final int draw) {
        this.draw = draw;
        this.data = Json.newArray();
    }

    /**
     * Gets draw.
     *
     * @return the draw
     */
    public int getDraw() {
        return this.draw;
    }

    /**
     * Gets records total.
     *
     * @return the records total
     */
    public long getRecordsTotal() {
        return this.recordsTotal;
    }

    /**
     * Sets records total.
     *
     * @param recordsTotal the records total
     */
    public void setRecordsTotal(final long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    /**
     * Gets records filtered.
     *
     * @return the records filtered
     */
    public long getRecordsFiltered() {
        return this.recordsFiltered;
    }

    /**
     * Sets records filtered.
     *
     * @param recordsFiltered the records filtered
     */
    public void setRecordsFiltered(final long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public ArrayNode getData() {
        return this.data;
    }
}
