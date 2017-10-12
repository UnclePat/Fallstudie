package Frontend.Adder;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleFloatProperty;


public class Table {


    private final SimpleIntegerProperty ID;
    private final SimpleStringProperty Date;
    private final SimpleStringProperty Description;
    private final SimpleFloatProperty Amount;

    public Table(int ID, String Date, String Description, Integer Amount) {

        this.ID = new SimpleIntegerProperty(ID);
        this.Date = new SimpleStringProperty(Date);
        this.Description = new SimpleStringProperty(Description);
        this.Amount = new SimpleFloatProperty(Amount);
    }

    public Integer getID() {
        return ID.get();
    }

    public void setID(Integer v) {
        ID.set(v);
    }

    public String dataProperty() {
        return Date.get();
    }

    public void setDate(String v) {
        Date.set(v);
    }

    public String getDescription() {
        return Description.get();
    }

    public void setDescription(String v) {
        Description.set(v);
    }

    public Float Amount() {
        return Amount.get();
    }

    public void setAmount(Float v) {
        Amount.set(v);

    }
}