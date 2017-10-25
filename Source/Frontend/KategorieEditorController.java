package Frontend;

import Backend.BuisnessObjects.Kategorie;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

//Die Klasse KategorieEditorController ist die "Logik" hinter der gleichnamigen FXML Datei. Sie gibt an, was welcher Button, Textfeld, etc. macht

public class KategorieEditorController extends Application {
    @FXML
    private Button btnKategorieSave;
    @FXML
    private Button btnCancel;

    @FXML
    private TextField txtKategorieName;

    @FXML
    private Label txtHeader;

    private static Kategorie kategorie;
    private static boolean canceled;

    public static Kategorie getKategorie() {
        return kategorie;
    }
    public static void setKategorie(Kategorie kategorie) {
        KategorieEditorController.kategorie = kategorie;
    }

    public static boolean isCanceled() {
        return canceled;
    }

    /* start Klasse ruft die KategorieEditor.fxml Datei auf um die Kategorien anzeigen zu lassen. */

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("KategorieEditor.fxml"));
            Scene scene = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.showAndWait();


        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /* Die Klasse initialize legt eine neue Kategorie an. */

    @FXML
    protected void initialize() {
        if (KategorieEditorController.getKategorie() != null){
            txtHeader.setText("Kategorie " + getKategorie().getName());
            txtKategorieName.setText(getKategorie().getName());
        }else{
            txtHeader.setText("Neue Kategorie");
            txtKategorieName.setText("");
        }
    }

    /*  Die Klasse btnKategorieSavePressed checkt, ob der Button zum speichern gedrückt wurde und gibt den Variablen der Klasse kategorie Schlüssel,
        damit diese als gespeichert dargestellt werden. */

    public void btnKategorieSavePressed(ActionEvent actionEvent){
        if (txtKategorieName.getText().trim().isEmpty()){
            return;
        }

        canceled = false;
        if (getKategorie() == null){
            kategorie = new Kategorie();
            kategorie.setName(txtKategorieName.getText().trim());
            kategorie.setFkeyUser(Backend.Base.Application.getCurrentUser().getKey());
            kategorie.setFkeyUserCreated(Backend.Base.Application.getCurrentUser().getKey());
            kategorie.setDateCreated(LocalDate.now());
            kategorie.setDeletionFlag(false);
        }else{
            kategorie.setName(txtKategorieName.getText().trim());
        }

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /* btnCancelPressed bricht alles ab, sofern der Button Abbrechen gedrückt wurde. */

    public void btnCancelPressed(ActionEvent actionEvent){
        canceled = true;
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}

