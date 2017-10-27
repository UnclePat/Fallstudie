package Frontend;

import Backend.BuisnessObjects.Kategorie;
import Backend.Database.DataBaseServer;
import Backend.User.User;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserSelectorController extends Dialog {
    @FXML
    private TableView tableUserSelector;


    private static User user = null;

    public User showWindow() throws IOException {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("UserSelector.fxml"));
        final Parent root = loader.load();
        final Scene scene = new Scene(root, 600, 400);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait();

        return user;
    }

    /**
     * Initialisiert die Tabelle UserSelector. Setzt den Namen der Spalte und befüllt die Zeilen mit den Namen
     * der Benutzer
     */
    public void initialize() {
        //Init tableUserSelector
        TableColumn nameColumn = new TableColumn("Benutzername");

        nameColumn.setCellValueFactory(new PropertyValueFactory<User,String>("name"));
        nameColumn.prefWidthProperty().bind(tableUserSelector.widthProperty()); // w * 1/3

        tableUserSelector.getColumns().addAll(nameColumn);

        List<User> userList = new ArrayList<>();

        for (Integer key : User.getAllKeys()){
            User user = new User().loadItem(key);
            userList.add(user);
        }

        tableUserSelector.setItems(FXCollections.observableArrayList(userList));
    }

    /**
     * Diese Methode beschreibt die Aktion, was passiert wenn der Benutzer Auswahl Button geklickt wurde.
     * Es wird der in der Tabelle ausgewählte Benutzer in die Variable Benutzer geschrieben, damit diese
     * weiterverarbeitet werden kann. Anschließend wir das Fenster geschlossen.
      * @param actionEvent
     */
    public void btnSelectUserPressed(ActionEvent actionEvent){
        User selectedUser = (User) tableUserSelector.getSelectionModel().getSelectedItem();

        if (selectedUser != null){
            user = selectedUser;

            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }
    }
}

