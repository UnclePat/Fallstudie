package Frontend;

import Backend.BuisnessObjects.AbrechnungItem;
import Backend.BuisnessObjects.Kategorie;
import Backend.Database.DataBaseServer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import sun.reflect.generics.scope.DummyScope;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainFormController extends Application {
    private boolean tabSelection = false;
    @FXML
    private TextField txtBackupPath;
    @FXML
    private Text Status;
    @FXML
    private TreeView kategorieTree;

    @FXML
    private TableView RecentEntryTabView;

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
        //refreshAbrechnungsItemView();
    }

    private boolean checkTab() {
        tabSelection = !tabSelection;
        return tabSelection;
    }

    // Is here to build a tree item
    private TreeItem<String> makeTreeItem(String item) {
        TreeItem<String> dummyRoot = new TreeItem<String>(item);
        return dummyRoot;

    }


    @FXML void refreshKategorieView(){
        if(checkTab()) {
            // if tab selected do something

            // Debug
            System.out.println("Call tab haushaltsbuch");


            Kategorie kat = new Kategorie();
            List<Integer> test = null;

            try {
                test = kat.getKategorieKeysForUser(Backend.Base.Application.getCurrentUser());
                TreeItem<String> dummyRoot = new TreeItem<>();

                for(Integer item : test) {


                    String item_name = kat.loadItem(item).getKname();

                    // Debug
                    System.out.println(item);
                    System.out.println("Kategoriename: " + item_name);

                    dummyRoot.getChildren().add(makeTreeItem(item_name));

                }

                kategorieTree.setShowRoot(false);
                kategorieTree.setRoot(dummyRoot);
            } catch (SQLException e) {
                e.printStackTrace();
            }



        }
    }

    public void refreshAbrechnungsItemView(){
      /*  if(checkTab()) {
            // if tab selected do something

            // Debug
            System.out.println("Call tab Haushaltsbuch");
            AbrechnungItem AI = new AbrechnungItem();
            Double Test = null;

            try {
                Test = AI.getRechnungsBetrag();
                TableView<Double> Dummy = new TableView<>();

                Dummy.getColumns().add(makeTreeItem(item_name));
                ObservableList E = new ObservableList() {
                }


                kategorieTree.setShowRoot(false);
                Dummy.setEditable(false);
                Dummy.setItems(E);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        /*if(checkTab()) {
            // if tab clicked do something
            System.out.println("Call tab Start");
            {Datum, Rechnung, Kategorie, Beschreibung},
            {Datum, Rechnung, Kategorie, Beschreibung},
            ArrayList<String,Date, String, String> test = null;

        }
*/
    }

    public void btnBackupPressed(ActionEvent actionEvent){
        System.out.println("Backup started.");
        Status.setVisible(false);
        String path = txtBackupPath.getText();
        DataBaseServer connection = new DataBaseServer();
        String backupPath = path +"\\Backup.bak";

        try {
            connection.executeBackup(backupPath);
            System.out.println("Backup successful.");
            Status.setFill(Paint.valueOf("Green"));
            Status.setText("Backup erfolgreich");
            Status.setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Backup failed.");
            Status.setText("Backup fehlerhaft");
            Status.setFill(Paint.valueOf("Red"));
            Status.setVisible(true);
        }

    }
}

