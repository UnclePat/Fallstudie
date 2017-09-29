package Backend.BuisnessObjects;

import Backend.User.User;

import javax.print.DocFlavor;
import java.sql.Date;

public class AbrechnungItem extends DatabaseItem{
    private double rechnungsBetrag;
    private String name;
    private String beschreibung;

    public double getRechnungsBetrag() {
        return rechnungsBetrag = 0;
    }

    public void setRechnungsBetrag(double rechnungsBetrag) {
        this.rechnungsBetrag = rechnungsBetrag;
    }

    @Override
    public void saveItem() {

    }

    @Override
    protected int createItem() {
        return 0;
    }

    @Override
    protected void updateItem() {

    }

    @Override
    public void loadItem() {

    }
}
