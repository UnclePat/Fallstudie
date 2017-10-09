package Backend.BuisnessObjects;

import Backend.User.User;
import Backend.BuisnessObjects.DatabaseItem;
import Backend.Database.DataBaseServer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.List;

public class Kategorie extends DatabaseItem{

    private String name;
    private User user;
    private List<AbrechnungItem> AbrechnungsItems;
    private List<Kategorie> SubKategorien;

    Integer KFkeyUserCreated = null;
    boolean KDeletionFlag = false;
    Integer KFkeyUserDeleted = null;
    String Kname;
    String Kbeschreibung;
    Integer FkeyKategorieParent = null;

    public void setKFkeyUserCreated(Integer KFkeyUserCreated) { this.KFkeyUserCreated = KFkeyUserCreated;}
    public Integer getKFkeyUserCreated() { return KFkeyUserCreated;}
    public void setKDeletionFlag(boolean DeletionFlag) { this.KDeletionFlag = KDeletionFlag;}
    public boolean getKDeletionFlag() { return KDeletionFlag; }
    public void setKFkeyUserDeleted(Integer KFkeyUserDeleted) { this.KFkeyUserDeleted = KFkeyUserDeleted; }
    public Integer getKFkeyUserDeleted() { return KFkeyUserDeleted; }
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
            String query ="INSERT INTO [dbo].[Kategorie]\n" +
                    "           ([dateCreated]\n" +
                    "           ,[intFkeyUserCreatedBy]\n" +
                    "           ,[boolDeletionFlag]\n" +
                    "           ,[intFkeyUserDeletedBy]\n" +
                    "           ,[dateDeleted]\n" +
                    "           ,[strName]\n" +
                    "           ,[strBeschreibung]\n" +
                    "           ,[intFkeyKategorieParent])\n" +
                    "     VALUES\n" +
                    "           ?,\n" +
                    "           ?,\n" +
                    "           ?,\n" +
                    "           ?,\n" +
                    "           ?,\n" +
                    "           ?,\n" +
                    "           ?,\n" +
                    "           ?)\n" +
                    "";

            List<String> values = new ArrayList<>();
            values.add(java.sql.Date.valueOf(this.getDateCreated()).toString());
            values.add(this.getKFkeyUserCreated().toString());
            values.add(this.getKDeletionFlag() ? "1":"0");
            values.add(this.getKFkeyUserDeleted().toString());
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
            String query = "UPDATE [dbo].[Kategorie]\n" +
                    "   SET [dateCreated] = ?\n" +
                    "      ,[intFkeyUserCreatedBy] = ?\n" +
                    "      ,[boolDeletionFlag] = ?\n" +
                    "      ,[intFkeyUserDeletedBy] = ?\n" +
                    "      ,[dateDeleted] = ?\n" +
                    "      ,[strName] = ?\n" +
                    "      ,[strBeschreibung] = ?\n" +
                    "      ,[intFkeyKategorieParent] = ?\n" +
                    " WHERE ?";

            List<String> values = new ArrayList<>();
            values.add(java.sql.Date.valueOf(this.getDateCreated()).toString());
            values.add(this.getKFkeyUserCreated().toString());
            values.add(this.getKDeletionFlag() ? "1" : "0");
            values.add(this.getKFkeyUserDeleted().toString());
            values.add(java.sql.Date.valueOf(this.getDateDeleted()).toString());
            values.add(this.getKname());
            values.add(this.getKbeschreibung());
            values.add(this.getFkeyKategorieParent().toString());

            DataBaseServer connection = new DataBaseServer();

            connection.update(query, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Kategorie loadItem(Integer key) {
        try {
            String query = "SELECT [intKey]\n" +
                    "      ,[dateCreated]\n" +
                    "      ,[intFkeyUserCreatedBy]\n" +
                    "      ,[boolDeletionFlag]\n" +
                    "      ,[intFkeyUserDeletedBy]\n" +
                    "      ,[dateDeleted]\n" +
                    "      ,[strName]\n" +
                    "      ,[strBeschreibung]\n" +
                    "      ,[intFkeyKategorieParent]\n" +
                    "  FROM [dbo].[Kategorie] " +
                    "   WHERE [intKey] = ?"    ;

            DataBaseServer connection = new DataBaseServer();

            ResultSet result = null;

            List<String> values = new ArrayList<>();
            values.add(key.toString());


            result = connection.select(query, values);

            Kategorie kategorie = new Kategorie();

            kategorie.setKey(result.getInt("intKey"));
            kategorie.setDateCreated(result.getDate("dateCreated").toLocalDate());
            kategorie.setKFkeyUserCreated(result.getInt("intFkeyUserCreatedBy"));
            kategorie.setDateDeleted(result.getDate("dateDeleted").toLocalDate());
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
