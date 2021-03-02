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

import java.util.List;

/**
 * DataSource.
 *
 * @param <ENTITY> the type parameter
 * @author Pierre Adam
 * @since 21.03.01
 */
public class DataSource<ENTITY> {

    /**
     * The Entities.
     */
    List<ENTITY> entities;

    /**
     * The Records total.
     */
    long recordsTotal;

    /**
     * The Records filtered.
     */
    long recordsFiltered;

    /**
     * Instantiates a new Data source.
     */
    public DataSource() {
    }

    /**
     * Instantiates a new Data source.
     *
     * @param recordsTotal    the records total
     * @param recordsFiltered the records filtered
     * @param entities        the entities
     */
    public DataSource(final long recordsTotal, final long recordsFiltered, final List<ENTITY> entities) {
        this.entities = entities;
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
    }

    /**
     * Gets entities.
     *
     * @return the entities
     */
    public List<ENTITY> getEntities() {
        return this.entities;
    }

    /**
     * Sets entities.
     *
     * @param entities the entities
     */
    public void setEntities(final List<ENTITY> entities) {
        this.entities = entities;
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
}
