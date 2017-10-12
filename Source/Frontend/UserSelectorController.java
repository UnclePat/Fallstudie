package Frontend;

import Backend.BuisnessObjects.Kategorie;
import Backend.Database.DataBaseServer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserSelectorController extends Application {
    @FXML
    private TableView tableUserSelector;
    @FXML
    private Button btnSelectUser;

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("UserSelector.fxml"));
            Scene scene = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setResizable(false);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void btnSelectUserPressed(ActionEvent actionEvent){
    }
}

