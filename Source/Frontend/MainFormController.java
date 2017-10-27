package Frontend;

import Backend.Auswertung;
import Backend.BuisnessObjects.AbrechnungsItem;
import Backend.BuisnessObjects.BarChartInputAbrechnungsItemsProMonat;
import Backend.BuisnessObjects.Kategorie;
import Backend.Database.DataBaseServer;
import Backend.User.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MainFormController extends Application {
    @FXML
    private TextField txtBackupPath;
    @FXML
    private DatePicker dateBelegdatum;
    @FXML
    private TextField txtBelegBeschreibung;
    @FXML
    private TextField txtBelegBetrag;
    @FXML
    private Text Status;
    @FXML
    private TextField txtUserNameNew;
    @FXML
    private TextField txtPasswordNew;
    @FXML
    private TextField txtPasswordRetypeNew;
    @FXML
    private TextField txtEditedUserName;
    @FXML
    private TextField txtEditedPasswordOld;
    @FXML
    private TextField txtEditedUserPasswordNew;
    @FXML
    private TextField txtEditedUserPasswordNewRetype;

    @FXML
    private TreeView kategorieTree;

    @FXML
    private TableView tblRecentEntryTabView;
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

    @FXML
    private Button btnNewUser;
    @FXML
    private Button btnSelectUser;

    @FXML
    private PieChart pieStartLastExpenses;
    @FXML
    private StackedBarChart BarChartStart;

    @FXML
    private Text txtEditUserNoUserSelected;
    @FXML
    private Text txtEditUserPasswordsNotEqual;
    @FXML
    private Text txtEditUserNewPasswordEmpty;
    @FXML
    private Text txtEditUserPasswordChangeSuccessful;
    @FXML
    private Text txtEditUserPasswordEmpty;
    @FXML
    private Text txtNewUserInvalidUsername;
    @FXML
    private Text txtNewUserPasswordEmpty;
    @FXML
    private Text txtNewUserPasswordsNotEqual;
    @FXML
    private Text txtNewUserSuccessful;

    @FXML
    private CheckBox checkShowDeletedItems;

    @FXML
    private Button btnDeleteKategorie;
    @FXML
    private Button btnDeleteAbrechnungsItem;

    @FXML
    private Text labelUserName;
    @FXML
    private Text labelUserPassword;
    @FXML
    private Text labelUserPasswordRetype;
    @FXML
    private Text labelNewUser;

    @FXML
    private Line lineNewUser;

    @FXML
    private DatePicker dateAuswertungBis;
    @FXML
    private DatePicker dateAuswertungVon;
    @FXML
    private TextField txtAuswertungBetragVon;
    @FXML
    private TextField txtAuswertungBetragBis;
    @FXML
    private TextField txtAuswertungSearch;
    @FXML
    private ChoiceBox choiceAuswertungKategorie;
    @FXML
    private TableView tblAuswertung;
    @FXML
    private PieChart chartAuswertung;

    @FXML
    private TextField summeAuswertung;

    private static Kategorie currentKategorie = null;
    private static AbrechnungsItem currentAbrechnungsItem = null;
    private static TreeItem<Kategorie> currentItemKategorieTree = null;
    private static User editedUser = Backend.Base.Application.getCurrentUser();

    public static User getEditedUser() {
        return editedUser;
    }
    public static void setEditedUser(User editedUser) {
        MainFormController.editedUser = editedUser;
    }
    public static Auswertung currentAuswertung = null;

    /**
     * Diese Methode wird durch die Application aufgerufen und lädt die Oberfläche MainForm.fxml.
     * @param primaryStage
     * Die Stage auf die das MainForm geladen wird.
     */
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

    /**
     * Initialisiert das MainForm
     */
    @FXML
    protected void initialize(){
        /**
         * Prüft welcher Tab ausgewählt ist und ruft ggf. die refresh Methode auf, welche dann die Ansicht aktualisiert.
         */
        mainTabControl.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            if (newTab == tabHaushaltsbuch){
                refreshHaushaltsbuch();
                editedUser = null;
                return;
            }else {
                currentKategorie = null;
                editedUser = null;
            }

            if (newTab == tabStart){
                refreshStart();
                return;
            }
            /**
             * Bei Auswahl des Einstellungstabs wird direkt der aktuelle USer in die Variable editedUser geladen.
             * Diese Variable wird im Textfeld Benutzer angezeigt, wessen Passwort geändert werden soll.
             */
            if (newTab == tabEinstellungen){
                editedUser = Backend.Base.Application.getCurrentUser();
                resetTabEinstellungenLabels();
                initChangePassword();
                initNewUser();
                return;
            }

            if (newTab == tabAuswertungen){
                initTabAuswertung();
            }
        });
        /**
         * Die Methode ermittelt das aktuelle ausgewählte Item im TreeView
         */
        kategorieTree.getSelectionModel().selectedItemProperty().addListener((observable, oldTreeItem, newTreeItem) -> {
            TreeItem<Kategorie> item = (TreeItem<Kategorie>) newTreeItem;
            if (item == null){
                return;
            }
            /**
             * Durch diese Konsolenausgabe kann während der Laufzeit in der Entwicklungsumgebung geprüft werden,
             * ob das richtige TreeItem ausgewählt ist. Die Variable currentKategorie wird mit dem aktuell ausgewählten
             * Item befüllt.
             */
            System.out.println("Selected Key : " + item.getValue().getKey() + " Selected Item: " + item.getValue().toString());
            refreshAbrechnungsItemView(item.getValue());
            MainFormController.currentKategorie = item.getValue();
            MainFormController.currentItemKategorieTree = item;
            /**
             * Wenn das aktuell ausgewählt TreeItem bereits gelöscht wurde (ein DeletionFlag besitzt), wird der Button
             * als Wiederherstllungsbutton angezeigt. Andernfalls wir der Löschen Button dargestellt.
             */
            if (currentKategorie.getDeletionFlag()){
                btnDeleteKategorie.setText("Wiederherstellen");
            }else{
                btnDeleteKategorie.setText("Löschen");
            }
        });
        /**
         * Färbt die TreeItems je nach Zustand (gelöscht, nicht gelöscht) rot oder schwarz.
         */
        kategorieTree.setCellFactory(tv ->  new TreeCell<Kategorie>() {
            @Override
            public void updateItem(Kategorie item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().removeAll();
                if (empty) {
                    setText("");
                    setStyle("");
                } else {
                    setText(item.toString());
                    if (item.getDeletionFlag()){
                        super.updateSelected(empty);
                        setTextFill(Color.RED);
                    }
                    else{
                        setTextFill(Color.BLACK);
                    }
                }
            }
        });

        /**
         * Initialisiert TableView der Abrechnungsitems
         */
        TableColumn dateColumn = new TableColumn("Datum");
        TableColumn beschreibungColumn = new TableColumn("Beschreibung");
        TableColumn betragColumn = new TableColumn("Betrag");


        dateColumn.setCellValueFactory(new PropertyValueFactory<AbrechnungsItem,LocalDate>("belegDatum"));
        beschreibungColumn.setCellValueFactory(new PropertyValueFactory<AbrechnungsItem,String>("beschreibung"));
        betragColumn.setCellValueFactory(new PropertyValueFactory<AbrechnungsItem,Double>("rechnungsBetrag"));
        dateColumn.prefWidthProperty().bind(tblAbrechnungsItems.widthProperty().multiply(0.15)); // w * 1/3
        beschreibungColumn.prefWidthProperty().bind(tblAbrechnungsItems.widthProperty().multiply(0.65)); // w * 1/3
        betragColumn.prefWidthProperty().bind(tblAbrechnungsItems.widthProperty().multiply(0.2)); // w * 1/3

        tblAbrechnungsItems.getColumns().addAll(dateColumn, beschreibungColumn, betragColumn);

        /**
         * Wenn das aktuell ausgewählt AbrechnungsItem bereits gelöscht wurde (ein DeletionFlag besitzt), wird der Button
         * als Wiederherstllungsbutton angezeigt. Andernfalls wir der Löschen-Button dargestellt.
         */
        tblAbrechnungsItems.getSelectionModel().selectedItemProperty().addListener((observable, oldTableItem, newTableItem) -> {
            AbrechnungsItem item = (AbrechnungsItem) newTableItem;
            if (item == null){
                return;
            }
            System.out.println("Selected Key : " + item.getKey() + " Selected Item: " + item.getBeschreibung());
            MainFormController.currentAbrechnungsItem = item;

            if (currentAbrechnungsItem.getDeletionFlag()){
                btnDeleteAbrechnungsItem.setText("Wiederherstellen");
            }else{
                btnDeleteAbrechnungsItem.setText("Löschen");
            }
        });
        /**
         * Färbt die AbrechnungsItems je nach Zustand (gelöscht, nicht gelöscht) rot oder schwarz.
         */
        tblAbrechnungsItems.setRowFactory(row -> new TableRow<AbrechnungsItem>(){
            @Override
            public void updateItem(AbrechnungsItem item, boolean empty){
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setStyle("");
                } else {
                    if (item.getDeletionFlag()) {
                        for(int i=0; i<getChildren().size();i++){
                            ((Labeled) getChildren().get(i)).setTextFill(Color.RED);
                        }
                    } else {
                        for(int i=0; i<getChildren().size();i++){
                            ((Labeled) getChildren().get(i)).setTextFill(Color.BLACK);;
                        }
                    }
                }
            }
        });
        /**
         * Folgende drei Lambdas stellen sicher, dass in den Textboxen "txtBelegBetrag", "txtAuswertungBetragVon" und
         * "txtAuswertungBetragBis" nur numerische Werte mit maximal 2 Nachkommastellen eingegeben
         * werden können.
         */
        txtBelegBetrag.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,16}([\\.|\\,]\\d{0,2})?")) {
                txtBelegBetrag.setText(oldValue);
            }
        });

        txtAuswertungBetragVon.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,16}([\\.|\\,]\\d{0,2})?")) {
                txtAuswertungBetragVon.setText(oldValue);
            }
        });

        txtAuswertungBetragBis.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,16}([\\.|\\,]\\d{0,2})?")) {
                txtAuswertungBetragBis.setText(oldValue);
            }
        });

        //Set EditUserFields
        txtEditedUserName.setEditable(false);

        /**
         * Initialisiert den TableView, welcher die letzten 10 Abrechnungsitems anzeigt.
         */
        TableColumn RecentBetragColumn = new TableColumn("Betrag");
        TableColumn RecentBeschreibungColumn = new TableColumn("Beschreibung");
        TableColumn RecentDateColumn = new TableColumn ("Datum");

        RecentDateColumn.setCellValueFactory(new PropertyValueFactory<AbrechnungsItem,LocalDate>("belegDatum"));
        RecentBeschreibungColumn.setCellValueFactory(new PropertyValueFactory<AbrechnungsItem,String>("beschreibung"));
        RecentBetragColumn.setCellValueFactory(new PropertyValueFactory<AbrechnungsItem, String>("rechnungsBetrag"));
        RecentDateColumn.prefWidthProperty().bind(tblRecentEntryTabView.widthProperty().multiply(0.15)); // w * 1/3
        RecentBeschreibungColumn.prefWidthProperty().bind(tblRecentEntryTabView.widthProperty().multiply(0.65)); // w * 1/3
        RecentBetragColumn.prefWidthProperty().bind(tblRecentEntryTabView.widthProperty().multiply(0.2)); // w * 1/3

        tblRecentEntryTabView.getColumns().addAll(RecentDateColumn, RecentBeschreibungColumn, RecentBetragColumn);
        refreshStart();

        checkShowDeletedItems.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                refreshKategorieView();
                kategorieTreeReselectLastItem();
            }
        });

        //Init Table Auswertungen
        TableColumn AuswertungBetragColumn = new TableColumn("Betrag");
        TableColumn AuswertungBeschreibungColumn = new TableColumn("Beschreibung");
        TableColumn AuswertungDateColumn = new TableColumn ("Datum");

        AuswertungDateColumn.setCellValueFactory(new PropertyValueFactory<AbrechnungsItem,LocalDate>("belegDatum"));
        AuswertungBeschreibungColumn.setCellValueFactory(new PropertyValueFactory<AbrechnungsItem,String>("beschreibung"));
        AuswertungBetragColumn.setCellValueFactory(new PropertyValueFactory<AbrechnungsItem, String>("rechnungsBetrag"));
        AuswertungDateColumn.prefWidthProperty().bind(tblAuswertung.widthProperty().multiply(0.15)); // w * 1/3
        AuswertungBeschreibungColumn.prefWidthProperty().bind(tblAuswertung.widthProperty().multiply(0.65)); // w * 1/3
        AuswertungBetragColumn.prefWidthProperty().bind(tblAuswertung.widthProperty().multiply(0.2)); // w * 1/3

        tblAuswertung.getColumns().addAll(AuswertungDateColumn, AuswertungBeschreibungColumn, AuswertungBetragColumn);
    }

    private void initChangePassword() {
        txtEditedUserName.setText(editedUser.getName());
        if (Backend.Base.Application.getCurrentUser().getAdmin()){
            btnSelectUser.setVisible(true);
        }else{
            btnSelectUser.setVisible(false);
        }
    }

    private void initNewUser(){
        if (Backend.Base.Application.getCurrentUser().getAdmin()){
            labelNewUser.setVisible(true);
            labelUserName.setVisible(true);
            labelUserPassword.setVisible(true);
            labelUserPasswordRetype.setVisible(true);
            lineNewUser.setVisible(true);
            txtUserNameNew.setVisible(true);
            txtPasswordNew.setVisible(true);
            txtPasswordRetypeNew.setVisible(true);
            btnNewUser.setVisible(true);
        }else{
            labelNewUser.setVisible(false);
            labelUserName.setVisible(false);
            labelUserPassword.setVisible(false);
            labelUserPasswordRetype.setVisible(false);
            lineNewUser.setVisible(false);
            txtUserNameNew.setVisible(false);
            txtPasswordNew.setVisible(false);
            txtPasswordRetypeNew.setVisible(false);
            btnNewUser.setVisible(false);
        }
    }

    private void resetTabEinstellungenLabels(){
        txtEditUserNoUserSelected.setVisible(false);
        txtEditUserPasswordsNotEqual.setVisible(false);
        txtEditUserNewPasswordEmpty.setVisible(false);
        txtEditUserPasswordChangeSuccessful.setVisible(false);
        txtEditUserPasswordEmpty.setVisible(false);

        txtNewUserInvalidUsername.setVisible(false);
        txtNewUserPasswordEmpty.setVisible(false);
        txtNewUserPasswordsNotEqual.setVisible(false);
        txtNewUserSuccessful.setVisible(false);
    }


    public void refreshHaushaltsbuch(){
        System.out.println("Call tab Haushaltsbuch");
        refreshKategorieView();
        clearTblAbrechnungsItems();
        txtBelegBetrag.setText("0.0");
}

    private void clearTblAbrechnungsItems() {
        for ( int i = 0; i < tblAbrechnungsItems.getItems().size(); i++) {
            tblAbrechnungsItems.getItems().clear();
        }
    }

    private void clearTblAuswertung() {
        tblAuswertung.getItems().clear();
    }

    // Is here to build a tree item
    private TreeItem<Kategorie> makeTreeItem(Kategorie item) {
        TreeItem<Kategorie> node = new TreeItem<>(item);
        return node;

    }

    void refreshAbrechnungsItemView(Kategorie kategorie){
        System.out.println("Call refreshAbrechnungsItemView");
        clearTblAbrechnungsItems();

        List<AbrechnungsItem> abrechnungsItems = kategorie.getAbrechnungsItems(checkShowDeletedItems.isSelected());

        tblAbrechnungsItems.setItems(FXCollections.observableArrayList(abrechnungsItems));
        btnDeleteAbrechnungsItem.setText("Löschen");
    }

    @FXML void refreshKategorieView(){
        List<Integer> kategorieKeys;

        btnDeleteAbrechnungsItem.setText("Löschen");

        try {
            kategorieKeys = Kategorie.getKategorieKeysForUser(Backend.Base.Application.getCurrentUser());
            TreeItem<Kategorie> dummyRoot = new TreeItem<>();

            for(Integer key : kategorieKeys) {

                Kategorie kat = new Kategorie();
                kat = kat.loadItem(key);

                // Debug
                System.out.println(key);
                System.out.println("Kategoriename: " + kat.getName());
                System.out.println("Kategorie has deletionFlag: " + kat.getDeletionFlag());

                TreeItem<Kategorie> item = makeTreeItem(kat);

                if (kat.getDeletionFlag()){
                    if (checkShowDeletedItems.isSelected())
                        dummyRoot.getChildren().add(item);
                }else{
                    dummyRoot.getChildren().add(item);
                }
            }

            kategorieTree.setShowRoot(false);
            kategorieTree.setRoot(dummyRoot);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refreshStart() {
        System.out.println("Call tab Start");
        refreshRecentItemView();
        refreshPieChart();
        refreshBarChartStart();
    }

    private void refreshBarChartStart() {
        System.out.println("Call refreshBarChartStart.");
        BarChartStart.getData().clear();
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        List<BarChartInputAbrechnungsItemsProMonat> AI = AbrechnungsItem.getItemsPerMonthAndKategorie();
        List<String> kategorieNamen = Kategorie.getAllKategorieNames();
        System.out.println(AI);
        System.out.println(kategorieNamen);

        for (String kategorieName : kategorieNamen) {
            System.out.println("Current Kategorie " + kategorieName);

            XYChart.Series series = new XYChart.Series();
            series.setName(kategorieName);

            for (BarChartInputAbrechnungsItemsProMonat item : AI) {
                if (item.getBetrag() != 0)
                    series.getData().add(new XYChart.Data(item.getMonatName(), item.getBetrag()));
            }

            BarChartStart.getData().add(series);
        }
    }

    private void refreshPieChart() {
        System.out.println("Call refreshPieChart.");

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        try {
            List<Integer> katKeys = Kategorie.getKategorieKeysForUser(Backend.Base.Application.getCurrentUser());
            for (Integer key : katKeys) {
                Kategorie kat = new Kategorie().loadItem(key);
                if (kat.getSum() != 0 && !kat.getDeletionFlag())
                    pieChartData.add(new PieChart.Data(kat.getName(), kat.getSum()));
            }

            pieStartLastExpenses.setData(pieChartData);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refreshRecentItemView() {
        System.out.println("RefreshRecentItems");
        List<AbrechnungsItem> AbrechnungsItems = AbrechnungsItem.getRecentItems();
        tblRecentEntryTabView.setItems(FXCollections.observableArrayList(AbrechnungsItems));
    }

    public void btnBackupPressed(ActionEvent actionEvent){
        System.out.println("Backup started.");
        Status.setVisible(false);
        String path = txtBackupPath.getText();
        DataBaseServer connection = new DataBaseServer();
        String backupPath = path +"\\Backup" + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format( new Date()) + ".bak";

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
        resetTabEinstellungenLabels();
        UserSelectorController selector = new UserSelectorController();

        try {
            editedUser = selector.showWindow();

            txtEditedUserName.setText(editedUser.getName());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Erzeugt einen neuen KategorieEditor, um den Namen der bestehenden Kategorie zu ändern. Nach Schließen des Editors
     * wird die Kategorie gespeichert und der KategorieTree aktualisiert.
     * @param actionEvent
     */
    public void btnKategorieEditPressed(ActionEvent actionEvent){
        KategorieEditorController editor = new KategorieEditorController();
        KategorieEditorController.setKategorie(currentKategorie);

        Stage kategorieEditor = new Stage();
        editor.start(kategorieEditor);

        Kategorie kat = KategorieEditorController.getKategorie();
        kat.saveItem();
        refreshKategorieView();
        selectKategorie(kat);
    }

    /**
     * Erzeugt einen neuen KategorieEditor, um den Namen der neuen Kategorie zu vergeben. Nach Schließen des Editors
     * wird die Kategorie gespeichert und der KategorieTree aktualisiert.
     * @param actionEvent
     */
    public void btnKategorieNewPressed(ActionEvent actionEvent){
        KategorieEditorController editor = new KategorieEditorController();
        KategorieEditorController.setKategorie(null);

        Stage kategorieEditor = new Stage();
        editor.start(kategorieEditor);

        KategorieEditorController.getKategorie().saveItem();
        refreshKategorieView();
        kategorieTreeReselectLastItem();
    }

    /**
     * Validiert die Angaben die der Benutzer gemacht hat. Sind diese ungültig, so werden entsprechende Meldungen
     * angezeigt. Andernfalls wird das neue AbrechnungsItem an der Datenbank erzeugt.
     * @param actionEvent
     */
    public void btnAbrechnungsItemSavePressed(ActionEvent actionEvent) {
        System.out.println("Call btnAbrechnungsItemSave");
        boolean valid = true;

        if (txtBelegBeschreibung.getText() == null || txtBelegBeschreibung.getText().trim().isEmpty()){
            System.out.println("Could not save. Field txtBelegBeschreibung is empty.");
            //Meldung Produzieren
            valid = false;
        }

        if (txtBelegBetrag.getText() == null || txtBelegBetrag.getText().trim().isEmpty()){
            System.out.println("Could not save. Field txtBelegBetrag is empty.");
            //Meldung Produzieren
            valid = false;
        }

        if (dateBelegdatum.getValue() == null){
            System.out.println("Could not save. Field dateBelegdatum is empty.");
            //Meldung Produzieren
            valid = false;
        }

        double betrag = 0.0;
        String beschreibung = "";
        LocalDate belegdatum = LocalDate.now();

        try{
            String strBetrag = txtBelegBetrag.getText().trim().replace(',', '.');
            betrag = Double.parseDouble(strBetrag);
        }catch(Exception ex){
            System.out.println("Invalid betrag. Could not parse.");
            valid = false;
        }

        try{
            beschreibung = txtBelegBeschreibung.getText().trim();
        }catch(Exception ex){
            System.out.println("Invalid beschreibung. Could not parse.");
            valid = false;
        }

        try{
            belegdatum = dateBelegdatum.getValue();
        }catch(Exception ex){
            System.out.println("Invalid belegDatum. Could not parse.");
            valid = false;
        }

        if(currentKategorie == null){
            System.out.println("No Kategorie selected.");
            valid = false;
        }

        if (!valid){
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
        if(!tblAbrechnungsItems.getItems().add(item)){
            System.out.println("Could not create Item.");
            return;
        }

        txtBelegBeschreibung.setText("");
        txtBelegBetrag.setText("0,0");
        dateBelegdatum.setValue(null);

        refreshKategorieView();
        kategorieTreeReselectLastItem();
    }

    /**
     * Validiert die Angaben die der Benutzer gemacht hat. Sind diese ungültig, so werden entsprechende Meldungen
     * angezeigt. Andernfalls wird der neue Benutzer an der Datenbank erzeugt.
     * @param actionEvent
     */
    public void btnNewUserPressed(ActionEvent actionEvent) {
        System.out.println("Call btnNewUserPressed");
        boolean valid = true;
        resetTabEinstellungenLabels();

        if (txtUserNameNew.getText() == null || txtUserNameNew.getText().trim().isEmpty()){
            System.out.println("Could not save. Field txtUserNameNew is empty.");
            txtNewUserInvalidUsername.setText("Benutzernamefeld darf nicht leer sein!");
            txtNewUserInvalidUsername.setVisible(true);

            //Meldung Produzieren
            valid = false;
        }

        if (txtPasswordNew.getText() == null || txtPasswordNew.getText().trim().isEmpty()){
            System.out.println("Could not save. Field txtPasswordNew is empty.");
            txtNewUserPasswordEmpty.setVisible(true);

            //Meldung Produzieren
            valid = false;
        }

        if (txtPasswordRetypeNew.getText() == null || txtPasswordRetypeNew.getText().trim().isEmpty()){
            System.out.println("Could not save. Field txtPasswordRetypeNew is empty.");
            txtNewUserPasswordsNotEqual.setVisible(true);
            //Meldung Produzieren
            valid = false;
        }

        String userName = "";
        String password = "";

        userName = txtUserNameNew.getText();

        if(!User.checkUsernameUnique(userName)){
            System.out.println("Username non unique.");
            txtNewUserInvalidUsername.setText("Der angegebene Benutzername existiert bereits!");
            txtNewUserInvalidUsername.setVisible(true);
            valid = false;
        }

        if (txtPasswordNew.getText().equals(txtPasswordRetypeNew.getText())){
            password = txtPasswordNew.getText();
        }else{
            System.out.println("Passwords do not match.");
            txtNewUserPasswordsNotEqual.setVisible(true);
            valid = false;
        }

        if (!valid){
            return;
        }

        User user = new User();
        user.setDeletionFlag(false);
        user.setFkeyUserCreated(Backend.Base.Application.getCurrentUser().getKey());
        user.setDateCreated(LocalDate.now());
        user.setAdmin(false);
        user.setPassword(password);
        user.setName(userName);
        user.saveItem();

        txtUserNameNew.setText("");
        txtPasswordNew.setText("");
        txtPasswordRetypeNew.setText("");
        txtNewUserSuccessful.setVisible(true);
    }

    /**
     * Markiert das im KategorieTree markierte Element als gelöscht und aktualisiert den Tree.
     * @param actionEvent
     */
    public void btnKategorieDeletePressed(ActionEvent actionEvent) {
        try {
            System.out.println("Call btnKategorieDeletePressed.");
            if(currentKategorie.getDeletionFlag()){
                currentKategorie.markAsNotDeleted();
            }else {
                currentKategorie.markAsDeleted();
            }

            if (!checkShowDeletedItems.isSelected()){
                clearTblAbrechnungsItems();
            }
            refreshKategorieView();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting Kategorie.");
        }
    }

    /**
     * Markiert das in der AbrechnungsItem Tabelle markierte Element als gelöscht und aktualisiert die Tabelle.
     * @param actionEvent
     */
    public void btnAbrechnungsItemDeletePressed(ActionEvent actionEvent) {
        try {
            System.out.println("Call btnAbrechnungsItemDeletePressed.");
            if(currentAbrechnungsItem.getDeletionFlag()){
                currentAbrechnungsItem.markAsNotDeleted();
            }else {
                currentAbrechnungsItem.markAsDeleted();
            }
            refreshKategorieView();
            kategorieTreeReselectLastItem();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting AbrechnungsItem.");
        }
    }

    /**
     * Speichert den Benutzer der aktuell in der Bearbeitung ist. Validiert die Angaben des Benutzers, falls dies
     * erfolgreich ist, wird der Benutzer gespeichert. Andernfalls werden an entsprechender Stelle Meldungen angezeigt.
     * @param actionEvent
     */
    public void btnEditedUserSavePressed(ActionEvent actionEvent) {
        System.out.println("Call btnEditedUserSavePressed");
        boolean valid = true;
        resetTabEinstellungenLabels();

        if (txtEditedUserName.getText() == null || txtEditedUserName.getText().trim().isEmpty()){
            System.out.println("Could not save. No user selected.");
            txtEditUserNoUserSelected.setVisible(true);

            //Direkt raus, ohne User macht es keinen Sinn weiter zu prüfen
            return;
        }

        if (txtEditedPasswordOld.getText() == null || txtEditedPasswordOld.getText().trim().isEmpty()){
            System.out.println("Could not save. Field txtEditedUserPasswordOld is empty.");
            txtEditUserPasswordEmpty.setText("Passwortfeld darf nicht leer sein!");
            txtEditUserPasswordEmpty.setVisible(true);

            //Meldung Produzieren
            valid = false;
        }

        if (txtEditedUserPasswordNew.getText() == null || txtEditedUserPasswordNew.getText().trim().isEmpty()){
            System.out.println("Could not save. Field txtEditedUserPasswordNew is empty.");
            txtEditUserNewPasswordEmpty.setVisible(true);

            //Meldung Produzieren
            valid = false;
        }

        if (txtEditedUserPasswordNewRetype.getText() == null || txtEditedUserPasswordNewRetype.getText().trim().isEmpty()){
            System.out.println("Could not save. Field txtEditedUserPasswordNewRetype is empty.");
            txtEditUserPasswordsNotEqual.setVisible(true);

            //Meldung Produzieren
            valid = false;
        }

        if (txtEditedPasswordOld.getText().equals(editedUser.getPassword())){
            if (txtEditedUserPasswordNew.getText().equals(txtEditedUserPasswordNewRetype.getText())){
                editedUser.setPassword(txtEditedUserPasswordNew.getText());
            }else{
                System.out.println("New passwords do not match.");
                txtEditUserPasswordsNotEqual.setVisible(true);

                valid = false;
            }
        }else{
            System.out.println("Old password does not match.");
            txtEditUserPasswordEmpty.setText("Das angegebene Passwort ist falsch!");
            txtEditUserPasswordEmpty.setVisible(true);
            valid = false;
        }

        if (!valid){
            return;
        }

        editedUser.saveItem();

        txtEditedPasswordOld.setText("");
        txtEditedUserPasswordNew.setText("");
        txtEditedUserPasswordNewRetype.setText("");

        txtEditUserPasswordChangeSuccessful.setVisible(true);
    }

    private void kategorieTreeReselectLastItem() {
        TreeItem<Kategorie> selectedItem = currentItemKategorieTree;
        int row = kategorieTree.getRow( selectedItem);
        kategorieTree.getSelectionModel().select( row );
    }

    /**
     * Selektiert die angegebene Kategorie im Baum, falls diese gefunden werden kann.
     * @param kategorie
     * Kategorie, die selektiert werden soll.
     */
    private void selectKategorie(Kategorie kategorie){
        TreeItem<Kategorie> nodeToSelect = searchKategorieInTree(kategorieTree.getRoot(), kategorie);

        int row = kategorieTree.getRow(nodeToSelect);
        kategorieTree.getSelectionModel().select( row );
    }

    /**
     * Durchsucht rekursiv den Kategorie Baum nach einer definierten Kategorie.
     * @param item
     * Item in welchem gesucht werden soll.
     * @param searchedKategorie
     * Kategorie, die gefunden werden soll.
     * @return
     * Das TreeItem, falls es gefunden wurde, andernfalls null.
     */
    TreeItem<Kategorie> searchKategorieInTree(TreeItem<Kategorie> item, Kategorie searchedKategorie) {

        if(item.getValue().equals(searchedKategorie)) return item; // hit!

        // continue on the children:
        TreeItem<Kategorie> result = null;
        for(TreeItem<Kategorie> child : item.getChildren()){
            result = searchKategorieInTree(child, searchedKategorie);
            if(result != null) return result; // hit!
        }

        //no hit:
        return null;
    }

    /**
     * Setzt alle Ausgabefelder zurück.
     * Überträgt die angegebenen Filtereinstellungen an ein neues Objekt der Klasse Auwertung. Dieses wertet aus, das
     * Ergebnis dieser Auswertung wird anschließend im Tab dargsetellt.
     * @param actionEvent
     */
    @FXML
    public void btnAuswertungCommitPressed(ActionEvent actionEvent){
        clearTblAuswertung();
        chartAuswertung.getData().clear();

        String strBetragVon = txtAuswertungBetragVon.getText().trim().replace(',', '.');
        Double betragVon = null;
        if (!strBetragVon.equals("")){
            betragVon = Double.parseDouble(strBetragVon);
        }

        String strBetragBis = txtAuswertungBetragBis.getText().trim().replace(',', '.');
        Double betragBis = null;
        if (!strBetragBis.equals("")){
            betragBis = Double.parseDouble(strBetragBis);
        }

        LocalDate datumVon = dateAuswertungVon.getValue();
        LocalDate datumBis = dateAuswertungBis.getValue();

        String beschreibung = txtAuswertungSearch.getText();

        Kategorie selectedKategorie = (Kategorie) choiceAuswertungKategorie.getSelectionModel().getSelectedItem();

        Auswertung auswertung = new Auswertung(datumVon, datumBis, betragVon, betragBis, beschreibung, selectedKategorie);
        List<AbrechnungsItem> resultAuswertung = auswertung.getResult();
        Map<Integer, String> kategorieKeyNameMapping = auswertung.getKategorieKeyNameMapping();

        tblAuswertung.setItems(FXCollections.observableArrayList(resultAuswertung));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        Double summe = 0.0;

        //Wenn Kategorien da sind, PieChart in Kats aufteilen. Wenn nur eine Kategorie, alle AI teilen.
        if (selectedKategorie == null){
            for (Map.Entry<Integer, String> mapping : kategorieKeyNameMapping.entrySet()) {
                double sum = 0;

                for (AbrechnungsItem item : resultAuswertung) {
                    if (item.getParentKategorieFkey() == mapping.getKey())
                        sum += item.getRechnungsBetrag();
                }

                if (sum != 0) {
                    pieChartData.add(new PieChart.Data(mapping.getValue(), sum));
                    summe += sum;
                }
            }

        }else{
            for (AbrechnungsItem item : resultAuswertung) {
                pieChartData.add(new PieChart.Data(item.getBeschreibung(), item.getRechnungsBetrag()));
                summe += item.getRechnungsBetrag();
            }
        }

        chartAuswertung.setData(pieChartData);
        summeAuswertung.setText(new DecimalFormat("#.00").format(summe) + " €");
    }

    @FXML
    public void btnAuswertungResetFilterPressed(ActionEvent actionEvent){
        initTabAuswertung();
    }

    /**
     * Initialisiert das Tab Auswertungen. Alle Textfelder werden geleert und die Kategorien des angemeldeten Benutzers
     * in die Auswahlliste geladen.
     */
    private void initTabAuswertung() {
        try {
            currentAuswertung = null;
            txtAuswertungBetragBis.setText("");
            txtAuswertungBetragVon.setText("");
            txtAuswertungSearch.setText("");
            choiceAuswertungKategorie.setValue(null);
            dateAuswertungVon.setValue(null);
            dateAuswertungBis.setValue(null);


            List<Kategorie> kategorien = new ArrayList<>();

            for (Integer key : Kategorie.getKategorieKeysForUser(Backend.Base.Application.getCurrentUser())) {
                Kategorie kat = new Kategorie().loadItem(key);
                kategorien.add(kat);
            }

            kategorien.add(null);

            choiceAuswertungKategorie.getItems().clear();
            choiceAuswertungKategorie.getItems().addAll(kategorien);
            choiceAuswertungKategorie.setValue(null);

            chartAuswertung.getData().clear();

            clearTblAuswertung();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

