package Backend;

import Backend.BuisnessObjects.AbrechnungsItem;
import Backend.BuisnessObjects.Kategorie;
import Backend.Database.DataBaseServer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Auswertung {
    private LocalDate filterDatumVon;
    private LocalDate filterDatumBis;

    private Double filterBetragVon;
    private Double filterBetragBis;

    private String filterBeschreibung;

    private Kategorie filterKategorie;

    public Auswertung(LocalDate filterDatumVon, LocalDate filterDatumBis, Double filterBetragVon, Double filterBetragBis, String filterBeschreibung, Kategorie kategorie){
        this.filterDatumVon = filterDatumVon;
        this.filterDatumBis = filterDatumBis;
        this.filterBetragVon = filterBetragVon;
        this.filterBetragBis = filterBetragBis;
        this.filterBeschreibung = filterBeschreibung;
        this.filterKategorie = kategorie;
    }

    public List<AbrechnungsItem> getResult(){
        try {
            List<AbrechnungsItem> resultList = new ArrayList<>();

            DataBaseServer connection = new DataBaseServer();

            String query = "SELECT [intKey]" +
                    "  FROM [Haushaltsbuch].[dbo].[AbrechnungsItem]" +
                    "  WHERE 1=1";

            List<String> values = new ArrayList<>();

            if (this.filterBetragBis != null){
                query = query.concat("  AND [decValue] <= ?");
                values.add(filterBetragBis.toString());
            }

            if (this.filterBetragVon != null){
                query = query.concat("  AND [decValue] >= ?");
                values.add(filterBetragVon.toString());
            }

            if (this.filterDatumBis != null){
                query = query.concat("  AND [dateBelegDatum] <= ?");
                values.add(filterDatumBis.toString());
            }

            if (this.filterDatumVon != null){
                query = query.concat(" AND [dateBelegDatum] >= ?");
                values.add(filterDatumVon.toString());
            }

            if (this.filterKategorie != null){
                query = query.concat(" AND [intFkeyKategorieParent] = ?");
                values.add(filterKategorie.getKey().toString());
            }

            if (this.filterBeschreibung != null){
                query = query.concat(" AND [strBechreibung] LIKE '%" + this.filterBeschreibung + "%'");
            }

            ResultSet result = connection.select(query, values);

            while(result.next()){
                AbrechnungsItem item = new AbrechnungsItem().loadItem(result.getInt("intKey"));
                resultList.add(item);
            }

            return resultList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<Integer, String> getKategorieKeyNameMapping(){
        Map<Integer, String> resultMap = new HashMap<>();

        try {
            String query = "SELECT [intKey], [strName]" +
                    "  FROM [Haushaltsbuch].[dbo].[Kategorie]" +
                    "  WHERE [boolDeletionFlag] = 0";

            List<String> values = new ArrayList<String>();

            DataBaseServer connection = new DataBaseServer();

            ResultSet result = connection.select(query, values);

            while (result.next()) {
                resultMap.put(result.getInt("intKey"), result.getString("strName"));
            }

            return resultMap;
        } catch (SQLException e) {
            e.printStackTrace();
            return resultMap;
        }
    }
}
