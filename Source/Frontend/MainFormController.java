package Frontend;

import Backend.BuisnessObjects.AbrechnungsItem;
import Backend.BuisnessObjects.Kategorie;
import Backend.Database.DataBaseServer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Collections;

public class MainFormController extends Application {
    @FXML
    private TextField txtBackupPath;
    @FXML
    private Text Status;

    @FXML
    private TreeView kategorieTree;

    @FXML
    private TableView RecentEntryTabView;
    @FXML
    private TableView tblAbrechnungsItems;

    @FXML
    private TabPane mainTabControl;

    @FXML
    private Tab tabStart;
    @FXML
    private Tab tabAuswertungen;
    @FXML
    private Tab tabHaushaltsbuch;
    @FXML
    private Tab tabEinstellungen;

    private static Kategorie currentKategorie = null;

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
            Scene scene = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());
            primaryStage.setScene(scene);
            DataBaseServer.dbConnect();
            primaryStage.show();
            primaryStage.setResizable(false);


        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();

        }
    }

    @FXML
    protected void initialize(){
        mainTabControl.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            if (newTab == tabHaushaltsbuch){
                refreshHaushaltsbuch();
                return;
            }

            if (newTab == tabStart){
                refreshStart();
                return;
            }
        });
        kategorieTree.getSelectionModel().selectedItemProperty().addListener((observable, oldTreeItem, newTreeItem) -> {
            TreeItem<Kategorie> item = (TreeItem<Kategorie>) newTreeItem;
            System.out.println("Selected Key : " + item.getValue().getKey() + " Selected Item: " + item.getValue().toString());
            refreshAbrechnungsItemView(item.getValue());
            MainFormController.currentKategorie = item.getValue();
        });

        //Init tblAbrechnungsItems
        TableColumn dateColumn = new TableColumn("Datum");
        TableColumn beschreibungColumn = new TableColumn("Beschreibung");
        TableColumn betragColumn = new TableColumn("Betrag");
        TableColumn objectColumn = new TableColumn("Object");
        objectColumn.setVisible(false);

        tblAbrechnungsItems.getColumns().addAll(dateColumn, beschreibungColumn, betragColumn, objectColumn);
        beschreibungColumn.setCellValueFactory(new PropertyValueFactory<AbrechnungsItem,String>("firstName"));

    }


    public void refreshHaushaltsbuch(){
        System.out.println("Call tab Haushaltsbuch");
        refreshKategorieView();
    }

    // Is here to build a tree item
    private TreeItem<Kategorie> makeTreeItem(Kategorie item) {
        TreeItem<Kategorie> node = new TreeItem<Kategorie>(item);
        return node;

    }

    void refreshAbrechnungsItemView(Kategorie kategorie){
        System.out.println("Call refreshAbrechnungsItemView");
        List<AbrechnungsItem> abrechnungsItems = kategorie.getAbrechnungsItems();

            tblAbrechnungsItems.setItems(abrechnungsItems);

    }

    @FXML void refreshKategorieView(){
        List<Integer> test = null;

        try {
            test = Kategorie.getKategorieKeysForUser(Backend.Base.Application.getCurrentUser());
            TreeItem<Kategorie> dummyRoot = new TreeItem<>();

            for(Integer item : test) {

                Kategorie kat = new Kategorie();
                kat = kat.loadItem(item);

                // Debug
                System.out.println(item);
                System.out.println("Kategoriename: " + kat.getName());

                dummyRoot.getChildren().add(makeTreeItem(kat));

            }

            kategorieTree.setShowRoot(false);
            kategorieTree.setRoot(dummyRoot);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refreshStart() {
        System.out.println("Call tab Start");
    }

    public void refreshAbrechnungsItemView(){
        // if tab clicked do something
        System.out.println("Call tab Haushaltsbuch");
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

    public void btnSelectUserPressed(ActionEvent actionEvent){

    }

    public void btnKategorieEditPressed(ActionEvent actionEvent){
        KategorieEditorController editor = new KategorieEditorController();
        KategorieEditorController.setKategorie(currentKategorie);

        Stage kategorieEditor = new Stage();
        editor.start(kategorieEditor);
    }

    public void btnKategorieNewPressed(ActionEvent actionEvent){
        KategorieEditorController editor = new KategorieEditorController();
        KategorieEditorController.setKategorie(null);

        Stage kategorieEditor = new Stage();
        editor.start(kategorieEditor);
    }
}

