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

    private String Kname;
    private String Kbeschreibung;
    private Integer FkeyKategorieParent = null;
    private Integer FkeyUser = null;

    public void setKname(String Kname) {
        this.Kname = Kname;
    }
    public String getKname() {
        return Kname;
    }
    public void setKbeschreibung(String Kbeschreibung) { this.Kbeschreibung = Kbeschreibung; }
    public String getKbeschreibung() { return Kbeschreibung; }
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
                    "           ,[strBeschreibung]" +
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
                    "           ?," +
                    "           ?)" +
                    "";

            List<String> values = new ArrayList<>();
            values.add(java.sql.Date.valueOf(this.getDateCreated()).toString());
            values.add(this.getFkeyUserCreated().toString());
            values.add(this.getDeletionFlag() ? "1":"0");
            values.add(this.getDeletedByUser().toString());
            values.add(java.sql.Date.valueOf(this.getDateDeleted()).toString());
            values.add(this.getKname());
            values.add(this.getKbeschreibung());
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
                    "      ,[strBeschreibung] = ?" +
                    "      ,[intFkeyKategorieParent] = ?" +
                    "      ,[intFkeyUser] = ?" +
                    " WHERE ?";

            List<String> values = new ArrayList<>();
            values.add(java.sql.Date.valueOf(this.getDateCreated()).toString());
            values.add(this.getFkeyUserCreated().toString());
            values.add(this.getDeletionFlag() ? "1" : "0");
            values.add(this.getDeletedByUser().toString());
            values.add(java.sql.Date.valueOf(this.getDateDeleted()).toString());
            values.add(this.getKname());
            values.add(this.getKbeschreibung());
            values.add(this.getFkeyKategorieParent().toString());
            values.add(this.getFkeyUser().toString());

            values.add(this.getKey().toString());

            DataBaseServer connection = new DataBaseServer();

            connection.update(query, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                    "      ,[strBeschreibung]" +
                    "      ,[intFkeyKategorieParent]" +
                    "      ,[intFkeyUser]" +
                    "  FROM [dbo].[Kategorie] " +
                    "   WHERE [intKey] = ?";

            DataBaseServer connection = new DataBaseServer();

            ResultSet result = null;

            List<String> values = new ArrayList<>();
            values.add(key.toString());


            result = connection.select(query, values);

            Kategorie kategorie = new Kategorie();

            kategorie.setKey(result.getInt("intKey"));
            kategorie.setDateCreated(result.getDate("dateCreated").toLocalDate());
            kategorie.setFkeyUserCreated(result.getInt("intFkeyUserCreatedBy"));
            kategorie.setDateDeleted(result.getDate("dateDeleted").toLocalDate());
            kategorie.setDeletedByUser(result.getInt("intFkeyUserDeletedBy"));
            kategorie.setDeletionFlag(result.getBoolean("boolDeletionFlag"));
            kategorie.setKname(result.getString("strName"));
            kategorie.setKbeschreibung(result.getString("strBeschreibung"));
            kategorie.setFkeyKategorieParent(result.getInt("intFkeyKategorieParent"));
            kategorie.setFkeyUser(result.getInt("intFkeyUser"));

            loadAbrechnungsItems();
            loadSubKategorien();

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

    private void loadSubKategorien(){
        try {
            String query = "SELECT [intKey]" +
                "  FROM [dbo].[Kategorie]" +
                "  WHERE [intFkeyKategorieParent] = ?";

            List<String> values = new ArrayList<String>();
            values.add(this.key.toString());

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

    private void loadAbrechnungsItems(){
        try {
            String query = "SELECT [intKey]" +
                    "  FROM [dbo].[AbrechnungsItem]" +
                    "  WHERE [intFkeyKategorieParent] = ?";

            List<String> values = new ArrayList<String>();
            values.add(this.key.toString());

            DataBaseServer connection = new DataBaseServer();

            ResultSet result = connection.select(query, values);

            while (result.next()) {

                int key= result.getInt("intKey");
                AbrechnungItem abrechnungsItem = new AbrechnungItem().loadItem(key);
                abrechnungsItems.add(abrechnungsItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
