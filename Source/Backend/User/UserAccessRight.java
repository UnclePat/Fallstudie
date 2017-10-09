package Backend.User;

import Backend.BuisnessObjects.DatabaseItem;

public class UserAccessRight extends DatabaseItem{
    @Override
    public void saveItem() {

    }

    @Override
    protected Integer createItem() {
        return 0;
    }

    @Override
    protected void updateItem() {

    }

    @Override
    public UserAccessRight loadItem(Integer key) {
        return new UserAccessRight();
    }
}
