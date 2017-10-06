import Backend.User.User;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class UserklasseTest {

  public void test() {
    User testUser = new User();

    testUser.setAdmin(true);
    testUser.setName("Hans");
    testUser.setPassword("test");
    testUser.setDateCreated(LocalDate.now());
    testUser.setFkeyUserCreated(1);


    testUser.saveItem();
  }

}