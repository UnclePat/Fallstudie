package Backend.BuisnessObjects;

import Backend.Database.DataBaseServer;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AbrechnungsItem extends DatabaseItem{
    private SimpleDoubleProperty rechnungsBetrag = new SimpleDoubleProperty(0);
    private SimpleStringProperty beschreibung = new SimpleStringProperty("");
    private Integer parentKategorieFkey;
    private LocalDate belegDatum;

    public double getRechnungsBetrag() {
        return rechnungsBetrag.get();
    }

    public void setRechnungsBetrag(double rechnungsBetrag) {
        this.rechnungsBetrag.set(rechnungsBetrag);
    }

    public String getBeschreibung() {
        return beschreibung.get();
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung.set(beschreibung);
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
                    "           ,?)";

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
            return null;
        }
    }



    @Override
    protected boolean updateItem() {
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

            if (!this.getDeletionFlag()){
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
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public AbrechnungsItem loadItem(Integer key) {
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
            result.next();

            AbrechnungsItem item = new AbrechnungsItem();

            item.setKey(result.getInt("intKey"));
            item.beschreibung = new SimpleStringProperty(result.getString("strBechreibung"));
            item.rechnungsBetrag = new SimpleDoubleProperty(result.getDouble("decValue"));
            item.setBelegDatum(result.getDate("dateBelegDatum").toLocalDate());
            item.setDateCreated(result.getDate("dateCreated").toLocalDate());
            item.setFkeyUserCreated(result.getInt("intFkeyUserCreatedBy"));
            item.setParentKategorieFkey(result.getInt("intFkeyKategorieParent"));
            item.setDeletionFlag(result.getBoolean("boolDeletionFlag"));
            item.setDeletedByUser(result.getInt("intFkeyUserDeletedBy"));
            item.setDateDeleted(result.getDate("dateDeleted") == null ? null : result.getDate("dateDeleted").toLocalDate());

            return item;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static  List<AbrechnungsItem> getRecentItems() {

        List<AbrechnungsItem> resultList = new ArrayList<>();
        try {
            String query = "SELECT TOP 10 [dateBelegDatum]" +
                    "      ,[strBechreibung]" +
                    "      ,[decValue]" +
                    "  FROM [dbo].[AbrechnungsItem]" +
                    "  WHERE [boolDeletionFlag] = 0 "+
                    "order by [dateBelegDatum] desc";

            DataBaseServer connection = new DataBaseServer();

            ResultSet result = null;

            List<String> values = new ArrayList<>();

            result = connection.select(query, values);


            while (result.next()) {
                AbrechnungsItem item = new AbrechnungsItem();
                item.beschreibung = new SimpleStringProperty(result.getString("strBechreibung"));
                item.rechnungsBetrag = new SimpleDoubleProperty(result.getDouble("decValue"));
                item.setBelegDatum(result.getDate("dateBelegDatum").toLocalDate());
                resultList.add(item);
            }
            return resultList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
