package Backend.Base;

import Backend.User.User;
import Frontend.LoginController;
import Frontend.MainFormController;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
    private static User currentUser;

    public static void setCurrentUser(User _currentUser){
        Application.currentUser = _currentUser;
    }

    public static User getCurrentUser(){
        return Application.currentUser;
    }

    //Startet unsere Applikation und ruft dazu als erstes, den Login Controller mit dem aktuellen Benutzer auf
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        LoginController controller = new LoginController();
        Stage login = new Stage();
        controller.start(login);


        if (Application.currentUser != null){
            MainFormController mainController = new MainFormController();
            mainController.start(primaryStage);
        }
    }
}


