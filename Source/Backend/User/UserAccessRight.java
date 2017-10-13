package Backend.User;

import Backend.BuisnessObjects.DatabaseItem;

public class UserAccessRight extends DatabaseItem{
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
