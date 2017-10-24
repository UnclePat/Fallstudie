package Backend;

import Backend.BuisnessObjects.AbrechnungsItem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public List<AbrechnungsItem> getResult(){
        List<AbrechnungsItem> resultList = new ArrayList<>();

        return resultList;
    }
}
