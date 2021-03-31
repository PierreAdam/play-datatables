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

package tools;

import com.jackson42.play.datatables.entities.Column;
import com.jackson42.play.datatables.entities.Parameters;

import java.util.ArrayList;
import java.util.List;

/**
 * ParametersHelper.
 *
 * @author Pierre Adam
 * @since 21.03.02
 */
public class ParametersHelper {

    /**
     * Create for name entity parameters.
     *
     * @return the parameters
     */
    public static Parameters createForNameEntity() {
        final Parameters parameters = new Parameters();
        final ColumnFactory columnFactory = new ColumnFactory();
        parameters.setDraw(1);
        parameters.setStart(0);
        parameters.setLength(10);
        parameters.setColumns(columnFactory.getColumns());

        columnFactory
                .addColumn("createdAt")
                .addColumn("uid")
                .addColumn("firstName")
                .addColumn("lastName")
                .addColumn("fullName") // Computed column that does not exists in the original entity
                .addColumn("title")
                .addColumn("bloodGroup")
                .addColumn("active")
                .addColumn("longNumber")
                .addColumn("integerNumber")
                .addColumn("doubleNumber")
                .addColumn("floatNumber")
                .addColumn("bigIntegerNumber")
                .addColumn("bigDecimalNumber")
                .addColumn("jsonNode")
                .addColumn("simpleEnum")
                .addColumn("numberEnum")
                .addColumn("nullData")
                .addColumn("actions");

        return parameters;
    }

    /**
     * The type Column factory.
     */
    private static class ColumnFactory {

        /**
         * The Id.
         */
        private int id;

        /**
         * The Columns.
         */
        private final List<Column> columns;

        /**
         * Instantiates a new Column factory.
         */
        public ColumnFactory() {
            this.id = 0;
            this.columns = new ArrayList<>();
        }

        /**
         * Add column column factory.
         *
         * @param name the name
         * @return the column factory
         */
        public ColumnFactory addColumn(final String name) {
            return this.addColumn(name, true, true);
        }

        /**
         * Add column column factory.
         *
         * @param name        the name
         * @param searcheable the searcheable
         * @param orderable   the orderable
         * @return the column factory
         */
        public ColumnFactory addColumn(final String name, final boolean searcheable, final boolean orderable) {
            this.columns.add(new Column() {{
                this.setData(ColumnFactory.this.id);
                this.setName(name);
                this.setSearcheable(searcheable);
                this.setOrderable(orderable);
            }});
            this.id++;
            return this;
        }

        /**
         * Gets columns.
         *
         * @return the columns
         */
        public List<Column> getColumns() {
            return this.columns;
        }
    }
}
