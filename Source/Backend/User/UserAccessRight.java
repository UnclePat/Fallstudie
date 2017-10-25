package Backend.User;

import Backend.BuisnessObjects.DatabaseItem;

/* Die Klasse USerAccessRight checkt, welche einzelnen Werte in der Datenbank von welchem User erstellt, gespeichert, geupdatet oder geladen wurden. */

public class UserAccessRight extends DatabaseItem{
    @Override
    public String getTableName() {
        return null;
    }

    @Override
    public boolean saveItem() {
        if (this.getKey() == null){
            this.setKey(createItem());

            if (this.getKey() == null){
                return false;
            }else{
                return true;
            }
        }
        else{
            return updateItem();
        }
    }

    @Override
    protected Integer createItem() {
        return 0;
    }

    @Override
    protected boolean updateItem() {
        return true;
    }

    @Override
    public UserAccessRight loadItem(Integer key) {
        return new UserAccessRight();
    }
}
