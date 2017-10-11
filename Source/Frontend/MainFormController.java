package Frontend;

import Backend.BuisnessObjects.Kategorie;
import Backend.Database.DataBaseServer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainFormController extends Application {
    @FXML
    private TreeView kategorieTree;

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
            Scene scene = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setResizable(false);

            refresh();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();

        }
    }

    public void refresh(){
        refreshKategorieView();
        refreshAbrechnungsItemView();
    }

    public void refreshKategorieView(){
        try {
            List<Kategorie> kategorien = new ArrayList<Kategorie>();
            List<Integer> kategorieKeys = Kategorie.getKategorieKeysForUser(Backend.Base.Application.getCurrentUser());

            TreeItem<String> root = new TreeItem<>();
            root.setValue(Backend.Base.Application.getCurrentUser().getName());

            for (Integer key : kategorieKeys) {
                Kategorie kategorie = new Kategorie().loadItem(key);
                kategorien.add(kategorie);

                root.getChildren().add(new TreeItem<String>(kategorie.getKname()));
            }

            TreeItem<String> dummyRoot = new TreeItem<>();
            dummyRoot.getChildren().addAll(root);
            kategorieTree.setShowRoot(false);
            kategorieTree.setRoot(dummyRoot);
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }

    }

    public void refreshAbrechnungsItemView(){

    }

    public void btnBackupPressed(ActionEvent actionEvent){
        System.out.println("Backup started.");

        DataBaseServer connection = new DataBaseServer();
        String backupPath = "C:\\Users\\patri\\Documents\\Lool";

        try {
            connection.executeBackup(backupPath);
            System.out.println("Backup successful.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Backup failed.");
        }

    }
}

