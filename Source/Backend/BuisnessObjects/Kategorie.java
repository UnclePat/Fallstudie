package Backend.BuisnessObjects;

import java.util.List;

public class Kategorie extends DatabaseItem{

    private List<AbrechnungItem> AbrechnungsItems;

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

    public double getSum(){
        double sum = 0;

        for (AbrechnungItem item : AbrechnungsItems) {
            sum += item.getRechnungsBetrag();
        }

        return sum;
    }
}
