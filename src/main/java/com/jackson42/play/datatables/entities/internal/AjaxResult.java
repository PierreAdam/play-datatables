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
