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
