package Frontend;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;




public class UserControlHaushaltsbuch implements Initializable {


    @FXML
    TableView<Table> tableID;

    @FXML
    TableColumn<Table, Integer> ID;
    @FXML
    TableColumn<Table, String> Date;
    @FXML
    TableColumn<Table, String> Description;
    @FXML
    TableColumn<Table, Float> Amount;

    @FXML
    TextField dateInput;

    @FXML
    TextField descriptionInput;

    @FXML
    TextField amountInput;

    @FXML
    Button add;


    final ObservableList<Table> data = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        Date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        Description.setCellValueFactory(new PropertyValueFactory<>("Description"));
        Amount.setCellValueFactory(new PropertyValueFactory<>("Amount"));

        tableID.setItems(data);

    }

    private int number = 1;


    public void onAddItem(ActionEvent actionEvent){

        Table entry = new Table(number, dateInput.getText(), descriptionInput.getText(), Integer.parseInt(amountInput.getText()));
        number++;

        data.add(entry);

        clearForm();
    }


    private void clearForm() {

        dateInput.clear();
        descriptionInput.clear();
        amountInput.clear();
    }


}
