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
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

public class KategorieEditorController extends Application {

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

    /**
     * Initialisiert das KategorieEditor Fenster. Wurde das Fenster mit einer Kategorie aufgerufen, so wird diese
     * im Fenster dargestellt und bearbeitet. Wurde keine angegeben wird eine neue Kategorie erzeugt und bearbeitet.
     * Die Labels in der Kopfzeile werden entsprechend gesetzt.
     */
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

    /**
     * Setzt die Werte der editierten Kategorie und beendet den Dialog. Sollte der angegebene Name der Kategorie leer
     * sein, so wird die Methode abgebrochen, das Fenster nicht geschlossen.
     * @param actionEvent
     */
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

    /**
     * Der Abbrechen-Button schließt das Fenster ohne Änderungen an der Kategorie vorzunehmen. Es wird ein Flag gesetzt,
     * welches diesen Zustand anzeigt.
     * @param actionEvent
     */
    public void btnCancelPressed(ActionEvent actionEvent){
        canceled = true;
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}

