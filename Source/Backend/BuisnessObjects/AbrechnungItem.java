package Backend.BuisnessObjects;

public class AbrechnungItem extends DatabaseItem{
    private double rechnungsBetrag;

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
