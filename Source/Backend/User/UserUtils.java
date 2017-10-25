package Backend.User;

/* Gibt die Werte userName und password zurück, damit überprüft werden kann, ob dieser User zugelassen ist oder nicht. */

public class UserUtils {
    public static User authenticateUser(String userName, String password)
    {

        return User.loadItemLogin(userName, password);
    }
}
