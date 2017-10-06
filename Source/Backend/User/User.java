package Backend.User;

import Backend.BuisnessObjects.DatabaseItem;
import Backend.Database.DataBaseServer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User extends DatabaseItem{
    private List<UserAccessRight> userGroups;

    public List<UserAccessRight> getUserGroups() {
        return userGroups;
    }

    String name;
    boolean isAdmin;
    String password;
    Integer FkeyUserCreated = null;
    boolean DeletionFlag = false;
    User DeletedByUser = null;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean getAdmin() {
        return isAdmin;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setFkeyUserCreated(Integer FkeyUserCreated){
        this.FkeyUserCreated = FkeyUserCreated;
    }

    public Integer getFkeyUserCreated() {
        return FkeyUserCreated;
    }

    public void setdeletionFlag(boolean DeletionFlag){
        this.DeletionFlag = DeletionFlag;
    }

    public boolean getDeletionFlag(){
        return DeletionFlag;
    }

    public void setDeletedByUser(User FKeyUserDeleted){
        this.DeletedByUser = FKeyUserDeleted;
    }

    public User getDeletedByUser() {
        return DeletedByUser;
    }

    private void setUserGroups(List<UserAccessRight> _userGroups){
        this.userGroups = _userGroups;
    }

    @Override
    public void saveItem() {
        if (this.getKey() == null){
            this.setKey(createItem());
        }
        else{
            updateItem();
        }
    }

    @Override
    protected Integer createItem() {
        try {
            String query = "INSERT INTO [dbo].[User]" +
                    "           (strName" +
                    "           ,boolAdmin" +
                    "           ,strPassword" +
                    "           ,dateCreated" +
                    "           ,intFkeyUserCreatedBy" +
                    "           ,boolDeletionFlag" +
                    "           )" +
                    "     VALUES" +
                    "           (?" +
                    " ,?" +
                    " ,?" +
                    " ,?" +
                    " ,?" +
                    " ,?)";

            List<String> values = new ArrayList<>();
            values.add(this.getName());
            values.add(this.getAdmin() ? "1":"0");
            values.add(this.getPassword());
            values.add(java.sql.Date.valueOf(this.getDateCreated()).toString());
            values.add(this.getFkeyUserCreated().toString());
            values.add(this.getDeletionFlag() ? "1":"0");

            DataBaseServer connection = new DataBaseServer();

            return connection.insert(query, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }



    @Override
    protected void updateItem() {
        try {
            String query = "UPDATE [dbo].[User]" +
                    "   SET strName = ?" +
                    "      ,boolAdmin = ?" +
                    "      ,strPassword = ?" +
                    "      ,dateCreated = ?" +
                    "      ,intFkeyUserCreatedBy = ?" +
                    "      ,boolDeletionFlag = ?" +
                    "      ,intFkeyUserDeletedBy = ?" +
                    "      ,dateDeleted = ?" +
                    " WHERE intKey = ?";

            List<String> values = new ArrayList<>();
            values.add(this.getName());
            values.add(this.getAdmin() ? "1":"0");
            values.add(this.getPassword());
            values.add(java.sql.Date.valueOf(this.getDateCreated()).toString());
            values.add(this.getFkeyUserCreated().toString());
            values.add(this.getDeletionFlag() ? "1":"0");
            if (this.getDeletedByUser() == null){
                values.add(null);
                values.add(null);
            }
            else{
                values.add(this.getDeletedByUser().getKey().toString());
                values.add(this.getDateDeleted().toString());
            }

            values.add(this.getKey().toString());

            DataBaseServer connection = new DataBaseServer();

            connection.update(query, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadItem() {

    }
}
