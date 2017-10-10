package Backend.BuisnessObjects;

import Backend.User.User;
import Backend.Database.DataBaseServer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Kategorie extends DatabaseItem{

    private User user;
    private List<AbrechnungItem> AbrechnungsItems;
    private List<Kategorie> SubKategorien;

    private String Kname;
    private String Kbeschreibung;
    private Integer FkeyKategorieParent = null;

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
                    "           ,[intFkeyKategorieParent])" +
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
            values.add(this.getKname());
            values.add(this.getKbeschreibung());
            values.add(this.getFkeyKategorieParent().toString());

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

            return kategorie;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public double getSum(){
        double sum = 0;

        for (Kategorie subKategorie : SubKategorien) {
            sum += subKategorie.getSum();
        }

        for (AbrechnungItem item : AbrechnungsItems) {
            sum += item.getRechnungsBetrag();
        }

        return sum;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
