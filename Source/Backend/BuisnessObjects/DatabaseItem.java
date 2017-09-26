package Backend.BuisnessObjects;

import java.util.List;

public abstract class DatabaseItem {
    int key;
    List<String> columnNames;
    String tableName;

    public void DatabaseItem(List<String> _columnNames, String _tableName, int _key){

        this.key = _key;
        this.columnNames = _columnNames;
        this.tableName = _tableName;
    }

    public abstract void saveItem();

    protected abstract int createItem();
    protected abstract void updateItem();

    public abstract void loadItem();
}
