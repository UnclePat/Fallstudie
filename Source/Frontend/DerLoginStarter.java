package Frontend;

import Backend.User.User;
import Backend.User.UserUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class DerLoginStarter extends Application {
        /*
        static String userAbfrage;
        ResultSet resultset;
        Statement stmt = null;
        public String unser;
        String userName;
        String userPassword;
        */
        @Override
        public void start(Stage primaryStage) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("LoginSCB.fxml"));
                Scene scene = new Scene(root, 785, 475);
                primaryStage.setScene(scene);
                primaryStage.show();
                primaryStage.setResizable(false);
            } catch (Exception e) {
                e.printStackTrace();

            }//try
        }//start

        @FXML private Button actionTarget;
        @FXML private TextField passwordField;
        @FXML private TextField textField;
        @FXML private Text Fehler;




    public void buttonPressed(ActionEvent actionEvent){

            //Button verfärbung
            actionTarget.setStyle("-fx-border-color: blue;");

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
                    System.out.println("Login war nicht erfolgreich");
                    Fehler.setVisible(true);
                }
                else{
                    System.out.println("Login war erfolgreich");
                }
            }
            catch(Exception s){
                actionTarget.setStyle("-fx-border-color: red;");
                System.out.println(s.toString() + "Exception: Login war nicht erfolgreich");
               

            }//try



        }
    }
