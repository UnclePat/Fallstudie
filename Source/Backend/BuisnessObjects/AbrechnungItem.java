package Backend.BuisnessObjects;

import Backend.Database.DataBaseServer;
import Backend.User.User;

import javax.print.DocFlavor;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AbrechnungItem extends DatabaseItem{
    private double rechnungsBetrag;
    private String beschreibung;
    private Integer parentKategorieFkey;
    private LocalDate belegDatum;

    public double getRechnungsBetrag() {
        return rechnungsBetrag = 0;
    }

    public void setRechnungsBetrag(double rechnungsBetrag) {
        this.rechnungsBetrag = rechnungsBetrag;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public Integer getParentKategorieFkey() {
        return parentKategorieFkey;
    }

    public void setParentKategorieFkey(Integer parentKategorieFkey) {
        this.parentKategorieFkey = parentKategorieFkey;
    }

    public LocalDate getBelegDatum() {
        return belegDatum;
    }

    public void setBelegDatum(LocalDate belegDatum) {
        this.belegDatum = belegDatum;
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
            String query = "INSERT INTO [dbo].[AbrechnungsItem]" +
                    "           ([dateCreated]" +
                    "           ,[intFkeyUserCreatedBy]" +
                    "           ,[strBechreibung]" +
                    "           ,[decValue]" +
                    "           ,[dateBelegDatum]" +
                    "           ,[intFkeyKategorieParent])" +
                    "     VALUES" +
                    "           (?" +
                    "           ,?" +
                    "           ,?" +
                    "           ,?" +
                    "           ,?" +
                    "           ,?))";

            List<String> values = new ArrayList<>();
            values.add(java.sql.Date.valueOf(this.getDateCreated()).toString());
            values.add(this.getFkeyUserCreated().toString());
            values.add(this.getBeschreibung());
            values.add(String.valueOf(this.getRechnungsBetrag()));
            values.add(java.sql.Date.valueOf(this.getBelegDatum()).toString());
            values.add(this.getParentKategorieFkey().toString());


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
            String query = "UPDATE [dbo].[AbrechnungsItem]" +
                    "   SET [dateCreated] = ?" +
                    "      ,[intFkeyUserCreatedBy] = ?" +
                    "      ,[boolDeletionFlag] = ?" +
                    "      ,[intFkeyUserDeletedBy] = ?" +
                    "      ,[dateDeleted] = ?" +
                    "      ,[strBechreibung] = ?" +
                    "      ,[decValue] = ?" +
                    "      ,[dateBelegDatum] = ?" +
                    "      ,[intFkeyKategorieParent] = ?" +
                    " WHERE [intKey] = ?";

            List<String> values = new ArrayList<>();
            values.add(java.sql.Date.valueOf(this.getDateCreated()).toString());
            values.add(this.getFkeyUserCreated().toString());
            values.add(this.getDeletionFlag() ? "1":"0");

            if (this.getDeletedByUser() == null){
                values.add(null);
                values.add(null);
            }
            else{
                values.add(this.getDeletedByUser().toString());
                values.add(this.getDateDeleted().toString());
            }

            values.add(this.getBeschreibung());
            values.add(String.valueOf(this.getRechnungsBetrag()));
            values.add(this.getBelegDatum().toString());
            values.add(this.getParentKategorieFkey().toString());

            values.add(this.getKey().toString());

            DataBaseServer connection = new DataBaseServer();

            connection.update(query, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AbrechnungItem loadItem(Integer key) {
        try {
            String query = "SELECT [intKey]" +
                    "      ,[dateCreated]" +
                    "      ,[intFkeyUserCreatedBy]" +
                    "      ,[boolDeletionFlag]" +
                    "      ,[intFkeyUserDeletedBy]" +
                    "      ,[dateDeleted]" +
                    "      ,[strBechreibung]" +
                    "      ,[decValue]" +
                    "      ,[dateBelegDatum]" +
                    "      ,[intFkeyKategorieParent]" +
                    "  FROM [dbo].[AbrechnungsItem]" +
                    "  WHERE [intKey] = ? ";

            DataBaseServer connection = new DataBaseServer();

            ResultSet result = null;

            List<String> values = new ArrayList<>();
            values.add(key.toString());

            result = connection.select(query, values);

            AbrechnungItem item = new AbrechnungItem();

            item.setKey(result.getInt("intKey"));
            item.setBeschreibung(result.getString("strBechreibung"));
            item.setRechnungsBetrag(result.getDouble("decValue"));
            item.setBelegDatum(result.getDate("dateBelegDatum").toLocalDate());
            item.setDateCreated(result.getDate("dateCreated").toLocalDate());
            item.setParentKategorieFkey(result.getInt("intFkeyKategorieParent"));
            item.setDeletionFlag(result.getBoolean("boolDeletionFlag"));
            item.setDeletedByUser(result.getInt("intUserFkeyUserDeletedBy"));
            item.setDateDeleted(result.getDate("dateDeleted").toLocalDate());

            return item;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
