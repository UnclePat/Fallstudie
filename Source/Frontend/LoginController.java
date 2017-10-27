package Frontend;

import Backend.User.User;
import Backend.User.UserUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;


public class LoginController extends Application {

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

    /**
     * Überprüft die Angaben des Benutzers, und versucht den angegebenen Benutzer von der Datenbank zu laden.
     * Ist der Benutzer gültig, wird das Objekt in die Application Klasse übertragen und das Formular geschlossen.
     * Andernfalls werden dem Benutzer die entsprechenden Fehler angezeigt.
     * @param actionEvent
     */
    public void buttonPressed(ActionEvent actionEvent){

            try {
                String username = textField.getText();
                String password = passwordField.getText();

                User user = UserUtils.authenticateUser(username, password);

                if(user == null){
                    actionTarget.setStyle("-fx-border-radius: 0 0 0 0;-fx-background-radius: 00 00 0 0;-fx-background-color:white;-fx-border-color:darkgrey; -fx-cursor: hand; ");
                    textField.setStyle("-fx-border-color: red;");
                    passwordField.setStyle("-fx-border-color: red;");
                    Fehler.setText("Benutzername oder Passwort sind ungültig");
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

                StringWriter errors = new StringWriter();
                s.printStackTrace(new PrintWriter(errors));
                JOptionPane.showMessageDialog(null,errors.toString() , "InfoBox: Error", JOptionPane.INFORMATION_MESSAGE);

                actionTarget.setStyle("-fx-border-color: red;");
                textField.setStyle("-fx-border-color: red;");
                passwordField.setStyle("-fx-border-color: red;");
                System.out.println("Login war nicht erfolgreich");
                Fehler.setText("Es konnte keine Verbindung zur Datenbank aufgebaut werden.");
                Fehler.setVisible(true);
            }//try



        }
}
