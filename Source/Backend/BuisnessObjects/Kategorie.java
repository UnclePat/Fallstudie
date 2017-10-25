package Backend.BuisnessObjects;

import Backend.Base.Application;
import Backend.User.User;
import Backend.Database.DataBaseServer;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

    //Die Klasse Kategorie ruft alle weitere Klassen auf und definiert die Variablen inklusive getter und setter Methoden.

public class Kategorie extends DatabaseItem{
    private List<AbrechnungsItem> abrechnungsItems = new ArrayList<AbrechnungsItem>();
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

    /*  Die Klasse getAbrechnungsitem zeigt die AbrechnungsItems in einer Liste an, sofern sie nicht gelöscht wurden*/

    public List<AbrechnungsItem> getAbrechnungsItems(boolean showDeleted) {
        if(showDeleted) {
            return abrechnungsItems;
        }else{
            List<AbrechnungsItem> clearedList = new ArrayList<>();

            for (AbrechnungsItem item : abrechnungsItems) {
                if (!item.getDeletionFlag()){
                    clearedList.add(item);
                }
            }
            return clearedList;
        }
    }


    public List<Kategorie> getSubKategorien() {
        return subKategorien;
    }

    @Override
    public String getTableName() {
        return "Kategorie";
    }

    /*  In der Klasse saveItem wird dem Item ein Flag verpasst, sollte es noch keine besitzen um anzugeben, ob es gespeichert wurde oder nicht. */

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

    /*  Die Klasse createItem setzt einen Insert Into (SQL) Befehl ab um die Datenbank mit den Werten zu befüllen,
        die bereits vom Anwender angegeben wurden und in den Übergabe Variablen gespeichert sind. */

    @Override
    protected Integer createItem() {
        try {
            String query ="INSERT INTO [dbo].[Kategorie]" +
                    "           ([dateCreated]" +
                    "           ,[intFkeyUserCreatedBy]" +
                    "           ,[boolDeletionFlag]" +
                    "           ,[strName]" +
                    "           ,[intFkeyKategorieParent]" +
                    "           ,[intFkeyUser])" +
                    "     VALUES" +
                    "          (?," +
                    "           ?," +
                    "           ?," +
                    "           ?," +
                    "           ?," +
                    "           ?)";

            List<String> values = new ArrayList<>();
            values.add(java.sql.Date.valueOf(this.getDateCreated()).toString());
            values.add(this.getFkeyUserCreated().toString());
            values.add(this.getDeletionFlag() ? "1":"0");
            values.add(this.getName());
            values.add(this.getFkeyKategorieParent() == null ? null : this.getFkeyKategorieParent().toString());
            values.add(this.getFkeyUser().toString());

            DataBaseServer connection = new DataBaseServer();

            return connection.insert(query, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*  Bei updateItem wird ein update (SQL) Befehl abgesetzt um bereits vorhandene Werte in der Datenbank zu ändern.
        Dabei wird es zuerst gelöscht und mit einer DeletionFlag versehen, damit das Programm sicher sein kann, dass der Wert wirklich gelöscht wurde. */

    @Override
    protected boolean updateItem() {
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
                    " WHERE [intKey] = ?";

            List<String> values = new ArrayList<>();
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

            values.add(this.getName());
            values.add(this.getFkeyKategorieParent() == null ? null : this.getFkeyKategorieParent().toString());
            values.add(this.getFkeyUser().toString());

            values.add(this.getKey().toString());

            DataBaseServer connection = new DataBaseServer();

            connection.update(query, values);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* Mit der Klasse getKeyforKategorieUser werden dem user aus der Datenbank ForeignKeys versehen. */

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

    /*  Die Klasse loadItem überträgt die Werte aus der Datenbank in die bereits angegebenen und vorhandenen Variablen, um sie anzeigen zu können. */

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

    /* Die getSum Klasse summiert die Werte aus einer Kategorie auf. */

    public double getSum(){
        double sum = 0;

        for (Kategorie subKategorie : subKategorien) {
            if (!subKategorie.getDeletionFlag())
                sum += subKategorie.getSum();
        }

        for (AbrechnungsItem item : abrechnungsItems) {
            if (!item.getDeletionFlag())
                sum += item.getRechnungsBetrag();
        }

        return sum;
    }

    /* Die Klasse loadSubKategorien zeigt die einzelnen Unterkategorien von einer Kategorie an. */

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

    /* Mit der Klasse loadAbrechnungsItems werden die AbrechnungsItems (Werte) von einer bestimmten Kategorie geladen und angezeigt. */

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
                AbrechnungsItem abrechnungsItem = new AbrechnungsItem().loadItem(key);
                kategorie.abrechnungsItems.add(abrechnungsItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Die Klasse getAllkategorieName zeigt alle gedankliche kategorien der Datenbank an. */

    public static List<String> getAllKategorieNames(){
        try {
            List<String> kategorieNamen = new ArrayList<>();

            String query = "SELECT [strName]" +
                    "  FROM [Haushaltsbuch].[dbo].[Kategorie] WHERE [intFkeyUserCreatedBy] = ?";

            List<String> values = new ArrayList<String>();
            values.add(Application.getCurrentUser().getKey().toString());

            DataBaseServer connection = new DataBaseServer();

            ResultSet result = connection.select(query, values);

            while (result.next()) {
                kategorieNamen.add(result.getString("strName"));
            }

            return kategorieNamen;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString(){
        return this.getName();
    }
}
