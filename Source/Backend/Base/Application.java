package Backend.Base;

import Backend.User.User;
import Frontend.LoginController;
import Frontend.OberflächeController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

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
        OberflächeController mainController = new OberflächeController();
        mainController.start(primaryStage);
    }
}


