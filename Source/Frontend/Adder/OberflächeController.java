package Frontend.Adder;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class OberflächeController extends Application {

    @FXML
    private AnchorPane HaushaltsbuchContentpane;

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Oberfläche.fxml"));
            Scene scene = new Scene(root, 785, 475);
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setResizable(false);

            Node node;
            node = (Node)FXMLLoader.load(getClass().getResource("/Frontend/Adder/UserControlHaushaltsbuch.fxml"));
            HaushaltsbuchContentpane.getChildren().setAll(node);


        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}

