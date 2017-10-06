package Backend.User;

public class UserUtils {
    public static User authenticateUser(String userName, String password)
    {
        return User.loadItemLogin(userName, password);
    }
}
