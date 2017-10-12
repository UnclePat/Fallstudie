package Backend.BuisnessObjects;

import Backend.User.User;
import Backend.Database.DataBaseServer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Kategorie extends DatabaseItem{

    private List<AbrechnungItem> abrechnungsItems = new ArrayList<AbrechnungItem>();
    private List<Kategorie> subKategorien = new ArrayList<Kategorie>();

    private String name;
    private Integer FkeyKategorieParent = null;
    private Integer FkeyUser = null;

    public void setName(String Kname) {
        this.name = Kname;
    }
    public String getName() {
        return name;
    }
    public void setFkeyKategorieParent(Integer KFkeyKategorieParent) { this.FkeyKategorieParent = FkeyKategorieParent;}
    public Integer getFkeyKategorieParent(){ return FkeyKategorieParent; }
    public Integer getFkeyUser() {
        return FkeyUser;
    }
    public void setFkeyUser(Integer fkeyUser) {
        FkeyUser = fkeyUser;
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
            String query ="INSERT INTO [dbo].[Kategorie]" +
                    "           ([dateCreated]" +
                    "           ,[intFkeyUserCreatedBy]" +
                    "           ,[boolDeletionFlag]" +
                    "           ,[intFkeyUserDeletedBy]" +
                    "           ,[dateDeleted]" +
                    "           ,[strName]" +
                    "           ,[intFkeyKategorieParent]" +
                    "           ,[intFkeyUser])" +
                    "     VALUES" +
                    "           ?," +
                    "           ?," +
                    "           ?," +
                    "           ?," +
                    "           ?," +
                    "           ?," +
                    "           ?," +
                    "           ?)" +
                    "";

            List<String> values = new ArrayList<>();
            values.add(java.sql.Date.valueOf(this.getDateCreated()).toString());
            values.add(this.getFkeyUserCreated().toString());
            values.add(this.getDeletionFlag() ? "1":"0");
            values.add(this.getDeletedByUser().toString());
            values.add(java.sql.Date.valueOf(this.getDateDeleted()).toString());
            values.add(this.getName());
            values.add(this.getFkeyKategorieParent().toString());
            values.add(this.getFkeyUser().toString());

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
            String query = "UPDATE [dbo].[Kategorie]" +
                    "   SET [dateCreated] = ?" +
                    "      ,[intFkeyUserCreatedBy] = ?" +
                    "      ,[boolDeletionFlag] = ?" +
                    "      ,[intFkeyUserDeletedBy] = ?" +
                    "      ,[dateDeleted] = ?" +
                    "      ,[strName] = ?" +
                    "      ,[intFkeyKategorieParent] = ?" +
                    "      ,[intFkeyUser] = ?" +
                    " WHERE ?";

            List<String> values = new ArrayList<>();
            values.add(java.sql.Date.valueOf(this.getDateCreated()).toString());
            values.add(this.getFkeyUserCreated().toString());
            values.add(this.getDeletionFlag() ? "1" : "0");
            values.add(this.getDeletedByUser().toString());
            values.add(java.sql.Date.valueOf(this.getDateDeleted()).toString());
            values.add(this.getName());
            values.add(this.getFkeyKategorieParent().toString());
            values.add(this.getFkeyUser().toString());

            values.add(this.getKey().toString());

            DataBaseServer connection = new DataBaseServer();

            connection.update(query, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Integer> getKategorieKeysForUser(User user) throws SQLException {
        List<Integer> returnList = new ArrayList<Integer>();

        String query = "SELECT [intKey]" +
                "  FROM [dbo].[Kategorie]" +
                "  WHERE [intFkeyUser] = ?";

        DataBaseServer connection = new DataBaseServer();

        List<String> values = new ArrayList<String>();
        values.add(user.getKey().toString());

        ResultSet result = connection.select(query, values);

        while (result.next()) {

            int key= result.getInt("intKey");
            returnList.add(key);
        }

        return returnList;
    }

    @Override
    public Kategorie loadItem(Integer key) {
        try {
            String query = "SELECT [intKey]" +
                    "      ,[dateCreated]" +
                    "      ,[intFkeyUserCreatedBy]" +
                    "      ,[boolDeletionFlag]" +
                    "      ,[intFkeyUserDeletedBy]" +
                    "      ,[dateDeleted]" +
                    "      ,[strName]" +
                    "      ,[intFkeyKategorieParent]" +
                    "      ,[intFkeyUser]" +
                    "  FROM [dbo].[Kategorie] " +
                    "   WHERE [intKey] = ?";

            DataBaseServer connection = new DataBaseServer();

            ResultSet result = null;

            List<String> values = new ArrayList<>();
            values.add(key.toString());


            result = connection.select(query, values);
            result.next();

            Kategorie kategorie = new Kategorie();

            kategorie.setKey(result.getInt("intKey"));
            kategorie.setDateCreated(result.getDate("dateCreated").toLocalDate());
            kategorie.setFkeyUserCreated(result.getInt("intFkeyUserCreatedBy"));
            kategorie.setDateDeleted(result.getDate("dateDeleted") == null ? null : result.getDate("dateDeleted").toLocalDate());
            kategorie.setDeletedByUser(result.getInt("intFkeyUserDeletedBy"));
            kategorie.setDeletionFlag(result.getBoolean("boolDeletionFlag"));
            kategorie.setName(result.getString("strName"));
            kategorie.setFkeyKategorieParent(result.getInt("intFkeyKategorieParent"));
            kategorie.setFkeyUser(result.getInt("intFkeyUser"));

            loadAbrechnungsItems(kategorie);
            loadSubKategorien(kategorie);

            return kategorie;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public double getSum(){
        double sum = 0;

        for (Kategorie subKategorie : subKategorien) {
            sum += subKategorie.getSum();
        }

        for (AbrechnungItem item : abrechnungsItems) {
            sum += item.getRechnungsBetrag();
        }

        return sum;
    }

    private void loadSubKategorien(Kategorie kategorie){
        try {
            String query = "SELECT [intKey]" +
                "  FROM [dbo].[Kategorie]" +
                "  WHERE [intFkeyKategorieParent] = ?";

            List<String> values = new ArrayList<String>();
            values.add(kategorie.key.toString());

            DataBaseServer connection = new DataBaseServer();

            ResultSet result = connection.select(query, values);

            while (result.next()) {

                int key= result.getInt("intKey");
                Kategorie kat = new Kategorie().loadItem(key);
                subKategorien.add(kat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAbrechnungsItems(Kategorie kategorie){
        try {
            String query = "SELECT [intKey]" +
                    "  FROM [dbo].[AbrechnungsItem]" +
                    "  WHERE [intFkeyKategorieParent] = ?";

            List<String> values = new ArrayList<String>();
            values.add(kategorie.key.toString());

            DataBaseServer connection = new DataBaseServer();

            ResultSet result = connection.select(query, values);

            while (result.next()) {

                int key = result.getInt("intKey");
                AbrechnungItem abrechnungsItem = new AbrechnungItem().loadItem(key);
                abrechnungsItems.add(abrechnungsItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return this.getName();
    }
}
