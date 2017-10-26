package Backend.Base;

import Backend.User.User;
import Frontend.LoginController;
import Frontend.MainFormController;
import javafx.stage.Stage;

/**
 * Hauptklasse der Anwendung. Dient als Haupteinsprungpunkt.
 */
public class Application extends javafx.application.Application {
    private static User currentUser;

    public static void setCurrentUser(User _currentUser){
        Application.currentUser = _currentUser;
    }

    public static User getCurrentUser(){
        return Application.currentUser;
    }

    /**
     * Startet den Hauptthread der Anwendung.
     * @param args
     * Argumente für den Start der Applikation
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Lädt den Login Bildschrim und setzt anhand dessen den angemeldeten Benutzer der Applikation. Anschließend wird
     * das MainForm gestartet.
     * @param primaryStage
     * Primäre Stage, die automatisch per FXML generiert wird.
     */
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


