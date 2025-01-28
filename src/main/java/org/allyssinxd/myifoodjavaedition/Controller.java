package org.allyssinxd.myifoodjavaedition;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.LocalTimeStringConverter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Controller implements Initializable {

    @FXML
    private SplitMenuButton dropdownMenu;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField startTextBox;
    @FXML
    private TextField endTextBox;
    @FXML
    private TextField valueTextBox;
    @FXML
    private TextField totalValueTextbox;
    @FXML
    private TextField totalHoursTextbox;
    @FXML
    private TextField acceptedRunsTextbox;
    @FXML
    private TextField deniedRunsTextbox;
    @FXML
    private TextField acceptanceRateTextbox;
    @FXML
    private TableView<Register> tableView;
    @FXML
    private TableColumn<Register, String> columnDate;
    @FXML
    private TableColumn<Register, String> columnStart;
    @FXML
    private TableColumn<Register, String> columnEnd;
    @FXML
    private TableColumn<Register, String> columnAverenge;
    @FXML
    private TableColumn<Register, Integer> columnAccepted;
    @FXML
    private TableColumn<Register, Integer> columnDenied;
    @FXML
    private TableColumn<Register, Float> columnValue;

    ObservableList<Register> registersLoadedList = FXCollections.observableArrayList();

    @FXML
    protected void OnAddBtnClicked(){
        LocalTimeStringConverter converter = new LocalTimeStringConverter();

        Float value = Float.parseFloat(valueTextBox.getText());
        LocalTime start = converter.fromString(startTextBox.getText());
        LocalTime end = converter.fromString(endTextBox.getText());
        String date = datePicker.getValue().toString();

        Duration duration = Duration.between(start, end);

        if(duration.isNegative()){
            duration = duration.plusHours(24);
        }

        int hours = duration.toHoursPart();
        int minutes = duration.toMinutesPart();

        LocalTime time = LocalTime.of(hours, minutes);

        String averenge = time.toString();

        registersLoadedList.add(new Register( date, value, start.toString(), end.toString(), averenge,
                Integer.parseInt(acceptedRunsTextbox.getText()), Integer.parseInt(deniedRunsTextbox.getText())));

        UpdateRegistersTable();
        UpdateTotalValue();
        UpdateTotalHours();
        UpdateAcceptanceRate();
    }

    private void UpdateRegistersTable() {
        tableView.setItems(registersLoadedList);
    }

    private void UpdateAcceptanceRate() {
        int accepted = 0;
        int denied = 0;
        for(Register register : tableView.getItems()){
            accepted += register.getAcceptedRuns();
            denied += register.getDeniedRuns();
        }
        int total = accepted + denied;

        if(total == 0) acceptanceRateTextbox.setText("N/A");
        else acceptanceRateTextbox.setText((100 * accepted) / total + "%");
    }

    void UpdateTotalValue(){
        float total = 0.00f;
        for(Register register : tableView.getItems()){
            total += register.getValue();
        }
        totalValueTextbox.setText(String.format("R$%.2f", total));
    }

    void UpdateTotalHours(){
        Duration accumulator = Duration.ZERO;

        for(Register register : tableView.getItems()){
            String[] splited = register.getAverenge().split(":");

            int hours = Integer.parseInt(splited[0]);
            int minutes = Integer.parseInt(splited[1]);

            accumulator = accumulator.plusMinutes(hours * 60L + minutes);
        }

        long hours = accumulator.toMinutes() / 60;
        long minutes = accumulator.toMinutes() % 60;

        totalHoursTextbox.setText(String.format("%02d:%02d", hours, minutes));
    }

    @FXML
    protected void SaveAsJson(){
        ObjectMapper mapper = new ObjectMapper();

        ArrayList<Register> registers = new ArrayList<>(tableView.getItems());

        try {
            String value = mapper.writeValueAsString(registers);
            File file = new File(System.getProperty("user.dir") + "/saves/" + dropdownMenu.getText() + ".json");

            if(!file.exists()) {
                boolean a = file.createNewFile();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/saves/"
                    + dropdownMenu.getText() + ".json"));
            writer.write(value);
            writer.close();
            System.out.println("Arquivo Salvo");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void LoadJson(){
        ObjectMapper mapper = new ObjectMapper();

        try {
            File file = new File(System.getProperty("user.dir") + "/saves/" + dropdownMenu.getText() + ".json");

            if(!file.exists()) {boolean a = file.createNewFile();}

            String json = "";
            Scanner scanner = new Scanner(file);
            if(scanner.hasNextLine()) json += scanner.nextLine();
            ArrayList<Register> registers;
            if(!json.isEmpty()) registers = mapper.readValue(json, new TypeReference<ArrayList<Register>>(){});
            else registers = new ArrayList<>(){};
            tableView.setItems(FXCollections.observableList(registers));
            registersLoadedList = FXCollections.observableList(registers);

            UpdateRegistersTable();
            UpdateTotalHours();
            UpdateTotalValue();
            UpdateAcceptanceRate();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void ChangeDropdownMenu(javafx.event.ActionEvent event){
        MenuItem menuItem = (MenuItem) event.getSource();
        dropdownMenu.setText(menuItem.getText());
        LoadJson();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Setup Table...
        columnDate.setCellValueFactory(new PropertyValueFactory<Register, String>("date"));
        columnStart.setCellValueFactory(new PropertyValueFactory<Register, String>("start"));
        columnEnd.setCellValueFactory(new PropertyValueFactory<Register, String>("end"));
        columnAverenge.setCellValueFactory(new PropertyValueFactory<Register, String>("averenge"));
        columnValue.setCellValueFactory(new PropertyValueFactory<Register, Float>("value"));
        columnAccepted.setCellValueFactory(new PropertyValueFactory<Register, Integer>("acceptedRuns"));
        columnDenied.setCellValueFactory(new PropertyValueFactory<Register, Integer>("deniedRuns"));
        UpdateRegistersTable();

        // Table first load...
        LoadJson();

        // Some more configs...
        datePicker.setValue(LocalDate.now());
    }
}