package Backend;

import java.time.LocalDate;

public class Auswertung {
    LocalDate filterDatumVon;
    LocalDate filterDatumBis;

    double filterBetragVon;
    double filterBetragBis;

    String filterBeschreibung;

    public Auswertung(LocalDate filterDatumVon, LocalDate filterDatumBis, double filterBetragVon, double filterBetragBis, String filterBeschreibung){
        this.filterDatumVon = filterDatumVon;
        this.filterDatumBis = filterDatumBis;
        this.filterBetragVon = filterBetragVon;
        this.filterBetragBis = filterBetragBis;
        this.filterBeschreibung = filterBeschreibung;
    }
}
