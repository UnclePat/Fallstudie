package Frontend;

import Backend.User.User;
import Backend.User.UserUtils;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.util.Duration;

import javax.swing.*;


public class LoginController extends Application {

        private User currentUser = null;

        @Override
        public void start(Stage loginStage) {
            try {

                Parent root = FXMLLoader.load(getClass().getResource("LoginSCB.fxml"));
                Scene scene = new Scene(root, 785, 475);
                loginStage.setScene(scene);
                loginStage.setResizable(false);
                loginStage.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();

            }//try
        }//start

        @FXML private Button actionTarget;
        @FXML private TextField passwordField;
        @FXML private TextField textField;
        @FXML private Text Fehler;
        @FXML private Pane pane;




    public void buttonPressed(ActionEvent actionEvent){

            //Login
            try {
                //Username aus TextField wird ausgelesen
                String username = textField.getText();
                //Passwort aus PasswortField wird ausgelesen
                String password = passwordField.getText();
                //Parameter werden Übergeben
                User user = UserUtils.authenticateUser(username, password);

                if(user == null){
                    actionTarget.setStyle("-fx-border-color: red;");
                    textField.setStyle("-fx-border-color: red;");
                    passwordField.setStyle("-fx-border-color: red;");
                    Fehler.setText("Benutzername und Passwort sind ungültig.");
                    System.out.println("Login war nicht erfolgreich");
                    Fehler.setVisible(true);
                }
                else{
                    System.out.println("Login war erfolgreich");
                    Backend.Base.Application.setCurrentUser(user);

                    Node source = (Node) actionEvent.getSource();
                    Stage stage = (Stage) source.getScene().getWindow();
                    stage.close();
                }
            }
            catch(Exception s){
                actionTarget.setStyle("-fx-border-color: red;");
                System.out.println(s.toString() + "Exception: Login war nicht erfolgreich");
                s.printStackTrace();

                JOptionPane.showMessageDialog(null, s.getStackTrace(), "InfoBox: Error", JOptionPane.INFORMATION_MESSAGE);

                actionTarget.setStyle("-fx-border-color: red;");
                textField.setStyle("-fx-border-color: red;");
                passwordField.setStyle("-fx-border-color: red;");
                System.out.println("Login war nicht erfolgreich");
                Fehler.setText("Es konnte keine Verbindung zur Datenbank aufgebaut werden.");
                Fehler.setVisible(true);
            }//try



        }

    public User getCurrentUser() {
        return currentUser;
    }

    private void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
