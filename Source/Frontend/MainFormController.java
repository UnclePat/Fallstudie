package Frontend;

import Backend.BuisnessObjects.AbrechnungsItem;
import Backend.BuisnessObjects.Kategorie;
import Backend.Database.DataBaseServer;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private TextField txtBelegdatum;
    @FXML
    private TextField txtBelegBeschreibung;
    @FXML
    private TextField txtBelegBetrag;
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
            }else {
                currentKategorie = null;
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
        //TableColumn objectColumn = new TableColumn("Object");
        //objectColumn.setVisible(false);

        dateColumn.setCellValueFactory(new PropertyValueFactory<AbrechnungsItem,LocalDate>("belegDatum"));
        beschreibungColumn.setCellValueFactory(new PropertyValueFactory<AbrechnungsItem,String>("beschreibung"));
        betragColumn.setCellValueFactory(new PropertyValueFactory<AbrechnungsItem,Double>("rechnungsBetrag"));
        dateColumn.prefWidthProperty().bind(tblAbrechnungsItems.widthProperty().multiply(0.15)); // w * 1/3
        beschreibungColumn.prefWidthProperty().bind(tblAbrechnungsItems.widthProperty().multiply(0.65)); // w * 1/3
        betragColumn.prefWidthProperty().bind(tblAbrechnungsItems.widthProperty().multiply(0.2)); // w * 1/3

        //objectColumn.setCellValueFactory(new PropertyValueFactory<AbrechnungsItem,String>("beschreibung"));

        tblAbrechnungsItems.getColumns().addAll(dateColumn, beschreibungColumn, betragColumn);
    }


    public void refreshHaushaltsbuch(){
        System.out.println("Call tab Haushaltsbuch");
        refreshKategorieView();
        clearTblAbrechnungsItems();
}

    private void clearTblAbrechnungsItems() {
        for ( int i = 0; i < tblAbrechnungsItems.getItems().size(); i++) {
            tblAbrechnungsItems.getItems().clear();
        }
    }

    // Is here to build a tree item
    private TreeItem<Kategorie> makeTreeItem(Kategorie item) {
        TreeItem<Kategorie> node = new TreeItem<Kategorie>(item);
        return node;

    }

    void refreshAbrechnungsItemView(Kategorie kategorie){
        System.out.println("Call refreshAbrechnungsItemView");
        clearTblAbrechnungsItems();

        List<AbrechnungsItem> abrechnungsItems = kategorie.getAbrechnungsItems();

        tblAbrechnungsItems.setItems(FXCollections.observableArrayList(abrechnungsItems));

    }

    @FXML void refreshKategorieView(){
        List<Integer> kategorieKeys;

        try {
            kategorieKeys = Kategorie.getKategorieKeysForUser(Backend.Base.Application.getCurrentUser());
            TreeItem<Kategorie> dummyRoot = new TreeItem<>();

            for(Integer key : kategorieKeys) {

                Kategorie kat = new Kategorie();
                kat = kat.loadItem(key);

                // Debug
                System.out.println(key);
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

    public void btnAbrechnungsItemSave(ActionEvent actionEvent) {
        System.out.println("Call btnAbrechnungsItemSave");
        if (txtBelegBeschreibung.getText() == "" || txtBelegBetrag.getText() == "" || txtBelegdatum.getText() == ""){
            System.out.println("Could not save. Fields are empty.");
            //Meldung Produzieren
            return;
        }

        double betrag = 0.0;
        String beschreibung = "";
        LocalDate belegdatum = LocalDate.now();

        try{
            betrag = Double.parseDouble(txtBelegBetrag.getText());
        }catch(Exception ex){
            System.out.println("Invalid betrag. Could not parse.");
        }

        try{
            beschreibung = txtBelegBetrag.getText();
        }catch(Exception ex){
            System.out.println("Invalid beschreibung. Could not parse.");
        }

        try{
            belegdatum = LocalDate.parse(txtBelegBetrag.getText());
        }catch(Exception ex){
            System.out.println("Invalid belegDatum. Could not parse.");
        }

        if(currentKategorie == null){
            System.out.println("No Kategorie selected.");
            return;
        }

        AbrechnungsItem item = new AbrechnungsItem();
        item.setBelegDatum(belegdatum);
        item.setParentKategorieFkey(currentKategorie.getKey());
        item.setBeschreibung(beschreibung);
        item.setRechnungsBetrag(betrag);
        item.setDateCreated(LocalDate.now());
        item.setDeletionFlag(false);
        item.setFkeyUserCreated(Backend.Base.Application.getCurrentUser().getKey());
        item.saveItem();
        tblAbrechnungsItems.getItems().add(item);
    }

    @FXML
    public void onTabChanged(Event event) {

        /*tabStart.setStyle("-fx-background-color:whitesmoke");*/
    }
}

