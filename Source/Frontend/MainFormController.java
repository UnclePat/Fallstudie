package Frontend;

import Backend.Database.DataBaseServer;
import Backend.User.User;
import Backend.User.UserUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.sql.SQLException;

public class MainFormController extends Application {

    @FXML
    private AnchorPane HaushaltsbuchContentpane;

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
            Scene scene = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setResizable(false);



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

