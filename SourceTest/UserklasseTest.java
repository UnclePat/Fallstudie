import Backend.User.User;

import java.util.Calendar;
import java.util.Date;

public class UserklasseTest {
  public static void main(String[] args) {
    User testUser = new User();

    testUser.setAdmin(true);
    testUser.setName("Hans");
    testUser.setPassword("test");
    Calendar temp = Calendar.getInstance();
    temp.set(2017, 10, 18);
    testUser.setDateCreated(temp.getTime());
    testUser.setFkeyUserCreated(1);

    testUser.saveItem();

    testUser.setName("Updated");

    testUser.saveItem();
  }
}

