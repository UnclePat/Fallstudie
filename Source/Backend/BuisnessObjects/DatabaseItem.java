package Backend.BuisnessObjects;

import Backend.Database.DataBaseServer;
import Backend.User.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public abstract class DatabaseItem {
    Integer key = null;

    public abstract String getTableName();

    private LocalDate dateDeleted;
    private Integer fkeyUserDeleted = null;
    private Integer fkeyUserCreated = null;

    private LocalDate dateCreated;
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

    public Integer getKey() {
        return key;
    }

    protected void setKey(Integer key) {
        this.key = key;
    }

    public abstract boolean saveItem();

    protected abstract Integer createItem();
    protected abstract boolean updateItem();

    public abstract DatabaseItem loadItem(Integer key);

    public void markAsDeleted() throws SQLException {
        DataBaseServer db = new DataBaseServer();
        db.markAsDeleted(this.getTableName(), this.key.intValue());
    }

    public void markAsNotDeleted() throws SQLException {
        DataBaseServer db = new DataBaseServer();
        db.markAsNotDeleted(this.getTableName(), this.key.intValue());
    }
}
