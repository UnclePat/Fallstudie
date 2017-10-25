package Backend.BuisnessObjects;

public class BarChartInputAbrechnungsItemsProMonat {
    private String monatName;
    private double betrag;
    private String kategorieName;

    //Getter und Setter Methoden für die angegebenen Variablen, mit dem Wert der Variablen als Rückgabewert.

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
