package org.allyssinxd.myifoodjavaedition.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.allyssinxd.myifoodjavaedition.entities.Register;
import org.allyssinxd.myifoodjavaedition.entities.RegisterDAO;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class DatabaseViewController implements Initializable {
    @FXML
    private TableView<Register> tableView;
    @FXML
    private TableColumn<Register, LocalDate> dateColumn;
    @FXML
    private TableColumn<Register, Float> valueColumn;
    @FXML
    private TableColumn<Register, LocalTime> startColumn;
    @FXML
    private TableColumn<Register, LocalTime> endColumn;
    @FXML
    private TableColumn<Register, String> averageColumn;
    @FXML
    private TableColumn<Register, Integer> acceptedColumn;
    @FXML
    private TableColumn<Register, Integer> deniedColumn;
    @FXML
    private SplitMenuButton splitMenu;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateColumn.setCellValueFactory(new PropertyValueFactory<Register, LocalDate>("date"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<Register, Float>("value"));
        startColumn.setCellValueFactory(new PropertyValueFactory<Register, LocalTime>("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<Register, LocalTime>("end"));
        averageColumn.setCellValueFactory(new PropertyValueFactory<Register, String>("average"));
        acceptedColumn.setCellValueFactory(new PropertyValueFactory<Register, Integer>("acceptedRuns"));
        deniedColumn.setCellValueFactory(new PropertyValueFactory<Register, Integer>("deniedRuns"));

        UpdateTable();
    }

    @FXML
    protected void ChangeDropdownMenu(javafx.event.ActionEvent event){
        MenuItem menuItem = (MenuItem) event.getSource();
        splitMenu.setText(menuItem.getText());

        UpdateTable();
    }

    private void UpdateTable(){
        ObservableList<Register> registers = FXCollections.observableArrayList(
                RegisterDAO.GetAllByMonth(splitMenu.getText()));
        tableView.setItems(registers);
    }
}
