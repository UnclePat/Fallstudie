package Backend.User;

import Backend.BuisnessObjects.DatabaseItem;
import Backend.Database.DataBaseServer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class User extends DatabaseItem{
    private List<UserAccessRight> userGroups;

    public List<UserAccessRight> getUserGroups() {
        return userGroups;
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
            String query = "";
            List<String> values = new ArrayList<>();

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
            String query = "INSERT INTO [dbo].[User]\n" +
                    "           ([strName]\n" +
                    "           ,[boolAdmin]\n" +
                    "           ,[strPassword]\n" +
                    "           ,[dateCreated]\n" +
                    "           ,[intFkeyUserCreatedBy]\n" +
                    "           ,[boolDeletionFlag]\n" +
                    "           ,[intFkeyUserDeletedBy]\n" +
                    "           ,[dateDeleted])\n" +
                    "     VALUES\n" +
                    "           (?\n" +
                    "\t\t\t,?\n" +
                    "\t\t\t,?\n" +
                    "\t\t\t,?\n" +
                    "\t\t\t,?\n" +
                    "\t\t\t,?\n" +
                    "\t\t\t,?)\n" +
                    "GO";

            List<String> values = new ArrayList<>();
            values.add(this.getName());

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
