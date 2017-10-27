package Backend.BuisnessObjects;

import Backend.Database.DataBaseServer;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Basis Klasse für alle weiteren Klassen, die eine Anbindung an die Datenbank benötigen. Diese Klasse definiert alle
 * nötigen Methoden (CRUD) und Felder, die eine Klasse benötigt um ihre Objekte an der Datenbank zu manipulieren.
 */
public abstract class DatabaseItem {
    Integer key = null;

    public abstract String getTableName();

    private LocalDate dateDeleted;
    private Integer fkeyUserDeleted = null;
    private Integer fkeyUserCreated = null;

    private LocalDate dateCreated;
    private boolean deletionFlag;

    //Getter und Setter Methoden für die bereits angegebenen Variablen, mit dem Wert der Variablen als Rückgabewert

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

    /**
     * Markiert das Objekt in der Datenbank als gelöscht.
     * @throws SQLException
     */
    public void markAsDeleted() throws SQLException {
        DataBaseServer db = new DataBaseServer();
        db.markAsDeleted(this.getTableName(), this.key.intValue());
    }

    /**
     * Entfernt die Markierung als gelöscht eines Objektes von der Datenbank.
     * @throws SQLException
     */
    public void markAsNotDeleted() throws SQLException {
        DataBaseServer db = new DataBaseServer();
        db.markAsNotDeleted(this.getTableName(), this.key.intValue());
    }
}
