package Backend.BuisnessObjects;

import Backend.Base.Application;
import Backend.User.User;
import Backend.Database.DataBaseServer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repräsentiert eine Kategorie mit etwaigen Sub-Kategorien und ihren AbrechnungsItems.
 */
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

    /**
     * Gibt die Liste der an das Objekt angehängten AbrechnungsItems zurück. Der Parameter showDeleted gibt an, ob
     * hierbei gelöschte AbrechnungsItems ebenfalls berücksichtigt werden sollen.
     * @param showDeleted
     * Gibt an, ob gelöschte AbrechnungsItems in der Ergebnisliste berücksichtigt werden sollen. Falls true werden
     * alle Items der Kategorie zurückgegeben, andernfalls wird die Ergebnisliste um als gelöscht markierte Items
     * bereinigt.
     * @return
     * Die Liste der, anhand des Parameters showDeleted, relevanten AbrechnungsItems.
     */
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

    /**
     * Aktualisiert mittels Update-Statement das Objekt auf der Datenbank.
     * @return
     * True falls erfolgreich, andernfalls false.
     */
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

    /**
     * Ermittelt alle Keys jener Kategorien, die einem übergebenen Benutzer zugeordnet werden können.
     * @param user
     * Der Benutzer, für welchen Kategorie Keys ermittelt werden sollen.
     * @return
     * Eine Liste von Kategorie Keys, die dem übergebenen Benutzer zugeordnet werden konnten.
     * @throws SQLException
     */
    public static List<Integer> getKategorieKeysForUser(User user) throws SQLException {
        List<Integer> returnList = new ArrayList<Integer>();

        String query = "SELECT [intKey]" +
                "  FROM [dbo].[Kategorie]" +
                "  WHERE [intFkeyUser] = ?" +
                "  ORDER BY [strName] ASC";

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

    /**
     * Lädt das Objekt, das durch den übergebenen Key identifiziert werden kann von der Datenbank.
     * @param key
     * Der Key des zu ladenenden Objekts.
     * @return
     * Das geladene Objekt.
     */
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

    /**
     * Ermittelt die Summe einer Kategorie. Hierbei werden alle AbrechnungsItems und Sub-Kategorien berücksichtigt, die
     * nicht als gelöscht markiert sind.
     * @return
     * Die ermittelte Summe.
     */
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

    /**
     * Lädt alle Kategorien, die einer Kategorie zugeordnet sind in die hierfür vorgesehene Liste. Hierbei wird die
     * übergebene Kategorie verwendet.
     * @param kategorie
     * Kategorie, deren Sub-Kategorien geladen werden sollen.
     */
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

    /**
     * Lädt alle AbrechnungsItems einer übergebenen Kategorie in ihr vorgesehenes Feld.
     * @param kategorie
     * Kategorie, für welche die Items geladen werden sollen.
     */
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

    /**
     * Läst die Namen aller Kategorien eines übergebenen Benutzers in eine Liste von Strings.
     * @return
     * Liste der ermittelten Namen.
     */
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
