package Backend.Base;

import Backend.User.User;

public class Application {
    private static User currentUser;

    private void setCurrentUser(User _currentUser){
        Application.currentUser = _currentUser;
    }

    public static User getCurrentUser(){
        return Application.currentUser;
    }

    public static void Main(String[] args){
        // Hier das LoginForm in einer Schleife aufrufen und den User setzen, anschließend die Kontrolle an das MainForm übergeben.
    }
}
