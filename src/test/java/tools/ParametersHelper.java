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

    public static Parameters createForNameEntity() {
        final Parameters parameters = new Parameters();
        final List<Column> columns = new ArrayList<>();
        parameters.setDraw(1);
        parameters.setStart(0);
        parameters.setLength(10);
        parameters.setColumns(columns);

        columns.add(new Column() {{
            this.setData(0);
            this.setName("firstName");
            this.setSearcheable(true);
            this.setOrderable(true);
        }});

        columns.add(new Column() {{
            this.setData(1);
            this.setName("lastName");
            this.setSearcheable(true);
            this.setOrderable(true);
        }});

        columns.add(new Column() {{
            this.setData(2);
            this.setName("title");
            this.setSearcheable(true);
            this.setOrderable(true);
        }});

        columns.add(new Column() {{
            this.setData(3);
            this.setName("bloodGroup");
            this.setSearcheable(true);
            this.setOrderable(true);
        }});
        
        return parameters;
    }
}
