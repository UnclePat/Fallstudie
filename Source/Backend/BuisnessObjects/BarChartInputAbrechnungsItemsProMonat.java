package Backend.BuisnessObjects;

/**
 * Ergebnis Klasse für die Kommunikation zwischen MainFormController und AbrechnungsItem.
 * Objekte dieser Klasse enthalten nur die Informationen, die für die Befüllung der BarCharts verwendet werden.
 */
public class BarChartInputAbrechnungsItemsProMonat {
    private String monatName;
    private double betrag;
    private String kategorieName;



    public String getMonatName() {
        return monatName;
    }

    public void setMonatName(String monatName) {
        this.monatName = monatName;
    }

    public double getBetrag() {
        return betrag;
    }

    public void setBetrag(double betrag) {
        this.betrag = betrag;
    }

    public String getKategorieName() {
        return kategorieName;
    }

    public void setKategorieName(String kategorieName) {
        this.kategorieName = kategorieName;
    }
}
