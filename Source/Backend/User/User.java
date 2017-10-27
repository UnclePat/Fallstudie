package Backend.User;

import Backend.BuisnessObjects.DatabaseItem;
import Backend.Database.DataBaseServer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//Hauptmethode der User Funktionen-Klassen

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
    public String getTableName() {
        return "User";
    }

    /* In der saveItem Klasse wird überprüft, ob der Wert schon gespeichert wurde und sofern benötigt, mit einem Key versehen. */

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

    /* Mit createItem wird ein SQL befehl abgesetzt, der neue Werte in die Datenbank Tabelle einfügt. */

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

    /* Mit der Klasse updateItem werden die gewünschten Werte aus der SQL Tabelle, mit Hilfe eines SQL Befehls, mit den neuen Werten überschrieben. */

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

    /* Die Klasse loadItem setzt einen SQL Befehl ab um die benötigten Werte aus der Datenbank anzeigen zu können. */

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

    /*  Mit der Klasse getAllKeys werden die Keys der Kategorien gesammelt. Dieses Bündel wird so in eine Liste eingetragen und übergeben. */

    public static List<Integer> getAllKeys(){
        try {
            List<Integer> returnList = new ArrayList<Integer>();

            String query = "SELECT [intKey]" +
                    "  FROM [dbo].[User]";
            DataBaseServer connection = new DataBaseServer();

            List<String> values = new ArrayList<>();

            ResultSet result = connection.select(query, values);

            while (result.next()) {
                int key = result.getInt("intKey");
                returnList.add(key);
            }

            return returnList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*  Mit loadItemLogin werden die Werte aus der Datenbank angezeigt, die der bei Login angegebene User angelegt hat. */

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

    /*  Mit checkUsernameUnique wird sichergestellt, dass es jeden User nur einmal so gibt um Komplikationen in der Datenbank zu verhindern. */

    public static boolean checkUsernameUnique(String username){
        boolean result = true;

        try {
            DataBaseServer connection = new DataBaseServer();
            String query = "SELECT COUNT(strName) as ocurrences" +
                    "  FROM [Haushaltsbuch].[dbo].[User]" +
                    "  WHERE strName = ?";

            List<String> values = new ArrayList<>();
            values.add(username);

            ResultSet resultSet = connection.select(query, values);
            resultSet.next();
            if(resultSet.getInt("ocurrences") > 0){
                result = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        }

        return result;
    }
}
