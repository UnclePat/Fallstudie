package Backend.User;

import Backend.BuisnessObjects.DatabaseItem;
import Backend.Database.DataBaseServer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repräsentiert einen Benutzer der Applikation.
 */
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

    /**
     * Speichert das Objekt. Ist bereits ein Schlüßel vorhanden, so wird davon ausgegangen, dass das Objekt noch nie
     * gespeichert wurde, es wird CreateItem aufgerufen. Ist ein Schlüßel vorhanden, wird UpdateItem aufgerufen.
     * War der Vorgang erfolgreich, wird true zurück gegeben andernfalls false.
     * @return
     * True falls erfolgreich, andernfalls false.
     */
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

    /**
     * Erstellt das Objekt mittels insert-Statement auf der Datenbank und gibt den generierten Key zurück.
     * @return
     * Der generierte Key.
     */
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

    /**
     * Aktualisiert mittels Update-Statement das Objekt auf der Datenbank.
     * @return
     * True falls erfolgreich, andernfalls false.
     */
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

    /**
     * Lädt das Objekt, das durch den übergebenen Key identifiziert werden kann von der Datenbank.
     * @param key
     * Der Key des zu ladenenden Objekts.
     * @return
     * Das geladene Objekt.
     */
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

    /**
     * Ermittelt alle Keys aller angelegten Benutzer.
     * @return
     * Eine Liste, die alle ermittelten Schlüßel enthält.
     */
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

    /**
     * Abwandlung der loadItem Methode. Hierbei wird der zu ladende Benutzer nicht anhand des Schlüßels ermittelt,
     * sondern mittels einer Kombination aus Benutzername und Passwort. Diese Methode wird nur während des Logins
     * verwendet. Objekt, falls die Kombination existiert, andernfalls null.
     * @param userName
     * Der Name des zu prüfenden Benutzers.
     * @param password
     * Das Passwort des zu prüfenden Benutzers.
     * @return
     * Objekt, falls die Kombination existiert, andernfalls null.
     */
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

    /**
     * Prüft ob ein übergebener Benutzername bereits in der Datenbank existiert.
     * @param username
     * Der zu prüfende Benutzername
     * @return
     * True, falls der Wert einzigartig wäre, false, falls der Wert bereits existiert.
     */
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
