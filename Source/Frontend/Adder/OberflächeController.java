package Frontend.Adder;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import sun.plugin.javascript.navig.Anchor;

import java.net.URL;
import java.util.ResourceBundle;

public class OberflächeController extends Application {

    @FXML
    private AnchorPane HaushaltsbuchContentpane;

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Oberfläche.fxml"));
            Scene scene = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setResizable(false);

            AnchorPane pane = (AnchorPane) FXMLLoader.load(getClass().getResource("/Frontend/Adder/UserControlHaushaltsbuchController.fxml"));
            HaushaltsbuchContentpane.getChildren().clear();
            HaushaltsbuchContentpane.getChildren().add(pane);



        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();

        }
    }
}

