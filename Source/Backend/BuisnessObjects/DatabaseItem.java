package Backend.BuisnessObjects;

import Backend.Database.DataBaseServer;
import Backend.User.User;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public abstract class DatabaseItem {
    Integer key = null;
    List<String> columnNames;
    String tableName;

    private LocalDate dateDeleted;
    private Integer fkeyUserDeleted = null;
    private Integer fkeyUserCreated = null;

    private LocalDate dateCreated;
    private User createdBy;
    private boolean deletionFlag;

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean getDeletionFlag(){
        return deletionFlag;
    }

    public void setDeletionFlag(boolean deletionFlag) {
        this.deletionFlag = deletionFlag;
    }

    public LocalDate getDateDeleted() {
        return dateDeleted;
    }

    public void setDateDeleted(LocalDate dateDeleted) {
        this.dateDeleted = dateDeleted;
    }

    public void setDeletedByUser(Integer FKeyUserDeleted){
        this.fkeyUserDeleted = FKeyUserDeleted;
    }

    public Integer getDeletedByUser() {
        return fkeyUserDeleted;
    }

    public void setFkeyUserCreated(Integer FkeyUserCreated){
        this.fkeyUserCreated = FkeyUserCreated;
    }

    public Integer getFkeyUserCreated() {
        return fkeyUserCreated;
    }

    public void DatabaseItem(List<String> _columnNames, String _tableName, int _key){

        this.key = _key;
        this.columnNames = _columnNames;
        this.tableName = _tableName;
    }

    public Integer getKey() {
        return key;
    }

    protected void setKey(Integer key) {
        this.key = key;
    }

    public abstract void saveItem();

    protected abstract Integer createItem();
    protected abstract void updateItem();

    public abstract DatabaseItem loadItem(Integer key);

    public void markAsDeleted() throws SQLException {
        DataBaseServer db = new DataBaseServer();
        db.markAsDeleted(this.tableName, this.key.intValue());
    }

    public void markAsNotDeleted() throws SQLException {
        DataBaseServer db = new DataBaseServer();
        db.markAsNotDeleted(this.tableName, this.key.intValue());
    }
}
