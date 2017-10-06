import Backend.User.User;

import java.util.Date;

public class UserklasseTest {

  public void test() {
    User testUser = new User();

    testUser.setAdmin(true);
    testUser.setName("Hans");
    testUser.setPassword("pwd4sa");
    testUser.setDateCreated(new Date("2017-10-01"));
    testUser.setFkeyUserCreated(2);
    testUser.setdeletionFlag(false);
    testUser.setDeletedByUser(testUser);
    testUser.setDateDeleted(new Date("2017-10-01"));


    testUser.saveItem();
  }

}