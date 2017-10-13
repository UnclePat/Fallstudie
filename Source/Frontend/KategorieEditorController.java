package Frontend;

import Backend.BuisnessObjects.Kategorie;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class KategorieEditorController extends Application {
    @FXML
    private Button btnKategorieSave;
    @FXML
    private Button btnCancel;

    private static Kategorie kategorie;

    public static Kategorie getKategorie() {
        return kategorie;
    }
    public static void setKategorie(Kategorie kategorie) {
        KategorieEditorController.kategorie = kategorie;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("KategorieEditor.fxml"));
            Scene scene = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.showAndWait();


        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void btnKategorieSavePressed(ActionEvent actionEvent){
    }

    public void btnCancelPressed(ActionEvent actionEvent){
    }
}

