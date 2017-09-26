package Backend.User;

import Backend.BuisnessObjects.DatabaseItem;

import java.util.List;

public class User extends DatabaseItem{
    private List<UserGroup> userGroups;

    public List<UserGroup> getUserGroups() {
        return userGroups;
    }

    private void setUserGroups(List<UserGroup> _userGroups){
        this.userGroups = _userGroups;
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
