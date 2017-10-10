package Frontend;

import Backend.Database.DataBaseServer;
import Backend.User.User;
import Backend.User.UserUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.sql.*;

public class DerLoginStarter extends Application {

        static String userAbfrage;
        ResultSet resultset;
        Statement stmt = null;
        public String unser;
        String userName;
        String userPassword;

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
        @FXML private TextField passwortField;
        @FXML private TextField textField;

        public void buttonPressed(ActionEvent actionEvent){

            //Button verfärbung
            actionTarget.setStyle("-fx-border-color: blue;");
            /*
            //DB-Verbindung herstellen
            try {
                DataBaseServer connection = new DataBaseServer();
                connection.dbConnect();
                System.out.println("Verbindung zur DB war erfolgreich");
            }
            catch(Exception s) {
                System.out.println(s.toString() + "Verbindung zur DB war nicht erfolgreich");
            }//try*/

            //Login
            try {
                //Username aus TextField wird ausgelesen
               String username = textField.getText();
               //Passwort aus PasswortField wird ausgelesen
                String password = passwortField.getText();

                User user = UserUtils.authenticateUser(username, password);

                /*
                //DB inhalt wird ausgelesen
                userAbfrage = new String ("Select strName, strPassword from Haushaltsbuch");
                           resultset = stmt.executeQuery(userAbfrage);

                while (resultset.next()){
                    userName = resultset.getString("userName");
                    userPassword = resultset.getString("userPassword");
                }

                boolean vergleichUser = userName.equals(username);
                boolean vergleichPassword = userPassword.equals(password);

                if (vergleichUser == true || vergleichPassword == true ){
                    actionTarget.setStyle("-fx-border-color: green;");
                    System.out.println("Login war erfolgreich");
                }
                else{
                    actionTarget.setStyle("-fx-border-color: red;");
                    System.out.println("Login war nicht erfolgreich");
                }
                */

                if(user == null){
                    actionTarget.setStyle("-fx-border-color: red;");
                    System.out.println("Login war nicht erfolgreich");
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
