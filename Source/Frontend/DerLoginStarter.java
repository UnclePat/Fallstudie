package Frontend;

import Backend.User.UserUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.application.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;



public class DerLoginStarter extends Application {
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


            }
        }



        @FXML private Button actionTarget;
        @FXML private Text fehler;

        public void handleSubmitButtonaction(ActionEvent actionEvent) {
            actionTarget.setStyle("-fx-border-color: blue;");

            fehler.setVisible(true);

        }
    }
