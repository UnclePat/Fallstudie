package Backend.Base;

import Backend.User.User;
import Frontend.LoginController;
import Frontend.MainFormController;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
    private static User currentUser;

    private void setCurrentUser(User _currentUser){
        Application.currentUser = _currentUser;
    }

    public static User getCurrentUser(){
        return Application.currentUser;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        LoginController controller = new LoginController();
        Stage login = new Stage();
        controller.start(login);


        this.setCurrentUser(controller.getCurrentUser());
        MainFormController mainController = new MainFormController();
        mainController.start(primaryStage);
    }
}


