package Backend.User;

import Backend.BuisnessObjects.DatabaseItem;
import Backend.Database.DataBaseServer;

import java.sql.ResultSet;
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

    private void setUserGroups(List<UserAccessRight> _userGroups){
        this.userGroups = _userGroups;
    }

    @Override
    public boolean saveItem() {
        if (this.getKey() == null){
            this.setKey(createItem());

            if (this.getKey() == null){
                return false;
            }else{
                return true;
            }
        }
        else{
            return updateItem();
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
    protected boolean updateItem() {
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
            if (!this.getDeletionFlag()){
                values.add(null);
                values.add(null);
            }
            else{
                values.add(this.getDeletedByUser().toString());
                values.add(this.getDateDeleted().toString());
            }

            values.add(this.getKey().toString());

            DataBaseServer connection = new DataBaseServer();

            connection.update(query, values);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User loadItem(Integer key) {
        try {
            String query = "SELECT [intKey]" +
                    "      ,[strName]" +
                    "      ,[boolAdmin]" +
                    "      ,[strPassword]" +
                    "      ,[dateCreated]" +
                    "      ,[intFkeyUserCreatedBy]" +
                    "      ,[boolDeletionFlag]" +
                    "      ,[intFkeyUserDeletedBy]" +
                    "      ,[dateDeleted]" +
                    "  FROM [dbo].[User]" +
                    "  WHERE [intKey] = ? ";

            DataBaseServer connection = new DataBaseServer();

            ResultSet result = null;

            List<String> values = new ArrayList<>();
            values.add(key.toString());

            result = connection.select(query, values);

            User user = new User();

            user.setKey(result.getInt("intKey"));
            user.setName(result.getString("strName"));
            user.setAdmin(result.getBoolean("boolAdmin"));
            user.setPassword(result.getString("strPassword"));
            user.setDateCreated(result.getDate("dateCreated").toLocalDate());
            user.setFkeyUserCreated(result.getInt("intFkeyUserCreatedBy"));
            user.setDeletionFlag(result.getBoolean("boolDeletionFlag"));
            user.setDeletedByUser(result.getInt("intUserFkeyUserDeletedBy"));
            user.setDateDeleted(result.getDate("dateDeleted").toLocalDate());

            return user;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static User loadItemLogin(String userName, String password) {
        try {
            String query = "SELECT [intKey]" +
                "      ,[strName]" +
                "      ,[boolAdmin]" +
                "      ,[strPassword]" +
                "      ,[dateCreated]" +
                "      ,[intFkeyUserCreatedBy]" +
                "      ,[boolDeletionFlag]" +
                "      ,[intFkeyUserDeletedBy]" +
                "      ,[dateDeleted]" +
                "  FROM [dbo].[User]" +
                "  WHERE [strName] = ? AND [strPassword] = ?";

            DataBaseServer connection = new DataBaseServer();

            ResultSet result = null;

            List<String> values = new ArrayList<>();
            values.add(userName);
            values.add(password);

            result = connection.select(query, values);
            result.next();
            User user = new User();

            user.setKey(result.getInt("intKey"));
            user.setName(result.getString("strName"));
            user.setAdmin(result.getBoolean("boolAdmin"));
            user.setPassword(result.getString("strPassword"));
            user.setDateCreated(result.getDate("dateCreated").toLocalDate());
            user.setFkeyUserCreated(result.getInt("intFkeyUserCreatedBy"));
            user.setDeletionFlag(result.getBoolean("boolDeletionFlag"));
            user.setDeletedByUser(result.getInt("intFkeyUserDeletedBy"));
            user.setDateDeleted(result.getDate("dateDeleted") == null ? null : result.getDate("dateDeleted").toLocalDate());

            return user;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
