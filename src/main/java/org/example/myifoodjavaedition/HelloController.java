package org.example.myifoodjavaedition;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.LocalDateStringConverter;
import javafx.util.converter.LocalTimeStringConverter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.*;

public class HelloController implements Initializable {

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

    ObservableList<Register> list = FXCollections.observableArrayList();

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

        String calcAverenge = time.toString();

        list.add(new Register( date, value, start.toString(), end.toString(),calcAverenge,
                Integer.parseInt(acceptedRunsTextbox.getText()), Integer.parseInt(deniedRunsTextbox.getText())));

        tableView.setItems(list);
        updateTotalValue();
        updateTotalHours();
        updateAcceptanceRate();
    }

    private void updateAcceptanceRate() {
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

    void updateTotalValue(){
        float total = 0.00f;
        for(Register register : tableView.getItems()){
            total += register.getValue();
        }
        totalValueTextbox.setText(total + "");
    }

    void updateTotalHours(){
        Duration accumulator = Duration.ZERO;

        for(Register register : tableView.getItems()){
            LocalTime time = LocalTime.parse(register.getAverenge());
            Duration converted = Duration.parse("PT"+time.getHour()+"H"+time.getMinute()+"M");
            accumulator = accumulator.plusMinutes(converted.toMinutes());
        }

        //124

        LocalTime sobra = new LocalTimeStringConverter().fromString("0:"+accumulator.toMinutes() % 60);
        totalHoursTextbox.setText(accumulator.toMinutes() / 60 + ":" + sobra.toString().split(":")[1]);
    }

    @FXML
    protected void saveAsJson(){
        System.out.println("Lol");
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
    protected void loadJson(){
        System.out.println("Lol2");
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
            list = FXCollections.observableList(registers);

            updateTotalHours();
            updateTotalValue();
            updateAcceptanceRate();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void ChangeDropdownMenu(javafx.event.ActionEvent event){
        MenuItem menuItem = (MenuItem) event.getSource();
        dropdownMenu.setText(menuItem.getText());
        loadJson();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        columnDate.setCellValueFactory(new PropertyValueFactory<Register, String>("date"));
        columnStart.setCellValueFactory(new PropertyValueFactory<Register, String>("start"));
        columnEnd.setCellValueFactory(new PropertyValueFactory<Register, String>("end"));
        columnAverenge.setCellValueFactory(new PropertyValueFactory<Register, String>("averenge"));
        columnValue.setCellValueFactory(new PropertyValueFactory<Register, Float>("value"));
        columnAccepted.setCellValueFactory(new PropertyValueFactory<Register, Integer>("acceptedRuns"));
        columnDenied.setCellValueFactory(new PropertyValueFactory<Register, Integer>("deniedRuns"));

        datePicker.setValue(LocalDate.now());

        tableView.setItems(list);

        loadJson();
    }
}