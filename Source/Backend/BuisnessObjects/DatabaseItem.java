package Backend.BuisnessObjects;

import java.util.List;

public abstract class DatabaseItem {
    List<String> columnNames;
    String tableName;

    public void DatabaseItem(List<String> _columnNames, String _tableName){

        this.columnNames = _columnNames;
        this.tableName = _tableName;
    }
}
