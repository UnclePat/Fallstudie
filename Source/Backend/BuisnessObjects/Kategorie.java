package Backend.BuisnessObjects;

import Backend.User.User;

import java.util.List;

public class Kategorie extends DatabaseItem{

    private String name;
    private User user;
    private List<AbrechnungItem> AbrechnungsItems;
    private List<Kategorie> SubKategorien;

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
        return 0;
    }

    @Override
    protected void updateItem() {


    }

    @Override
    public Kategorie loadItem(Integer key) {
        return  new Kategorie();
    }

    public double getSum(){
        double sum = 0;

        for (Kategorie subKategorie : SubKategorien) {
            sum += subKategorie.getSum();
        }

        for (AbrechnungItem item : AbrechnungsItems) {
            sum += item.getRechnungsBetrag();
        }

        return sum;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
