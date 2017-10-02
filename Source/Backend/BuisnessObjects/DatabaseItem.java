package Backend.BuisnessObjects;

import Backend.Database.DataBaseServer;
import Backend.User.User;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public abstract class DatabaseItem {
    Integer key;
    List<String> columnNames;
    String tableName;

    private Date dateCreated;
    private User createdBy;
    private boolean deletionFlag;
    private Date dateDeleted;
    private User deletedBy;

    public void DatabaseItem(List<String> _columnNames, String _tableName, int _key){

        this.key = _key;
        this.columnNames = _columnNames;
        this.tableName = _tableName;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public abstract void saveItem();

    protected abstract Integer createItem();
    protected abstract void updateItem();

    public abstract void loadItem();

    public void markAsDeleted() throws SQLException {
        DataBaseServer db = new DataBaseServer();
        db.markAsDeleted(this.tableName, this.key.intValue());
    }

    public void markAsNotDeleted() throws SQLException {
        DataBaseServer db = new DataBaseServer();
        db.markAsNotDeleted(this.tableName, this.key.intValue());
    }
}
