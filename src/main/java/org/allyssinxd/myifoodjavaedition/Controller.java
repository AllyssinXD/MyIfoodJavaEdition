package org.allyssinxd.myifoodjavaedition;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;
import javafx.util.converter.LocalTimeStringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
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
    private Button addBtn;

    ObservableList<Register> registersLoadedList = FXCollections.observableArrayList();

    @FXML
    protected void OnAddBtnClicked(javafx.event.ActionEvent event){
        LocalTimeStringConverter timeConverter = new LocalTimeStringConverter();
        LocalDateStringConverter dateConverter = new LocalDateStringConverter();

        //Values from textBoxes
        float value = Float.parseFloat(valueTextBox.getText());
        LocalTime start = timeConverter.fromString(startTextBox.getText());
        LocalTime end = timeConverter.fromString(endTextBox.getText());

        String uncompatibleDate = datePicker.getValue().toString();
        String[] splitedDate = uncompatibleDate.split("-");
        LocalDate date = dateConverter.fromString(splitedDate[2]+"/"+splitedDate[1]+"/"+splitedDate[0]);

        int accepted = Integer.parseInt(acceptedRunsTextbox.getText());
        int denied = Integer.parseInt(deniedRunsTextbox.getText());

        //Calc Average
        Duration duration = Duration.between(start, end);
        if(duration.isNegative()){
            duration = duration.plusHours(24);
        }
        int hours = duration.toHoursPart();
        int minutes = duration.toMinutesPart();

        String average =  LocalTime.of(hours, minutes).toString();

        RegisterDAO newRegister = new RegisterDAO(date, value, start, end, average, accepted, denied);
        newRegister.Create();

        UpdateAll();
    }

    @FXML
    protected void ChangeDropdownMenu(javafx.event.ActionEvent event){
        MenuItem menuItem = (MenuItem) event.getSource();
        dropdownMenu.setText(menuItem.getText());

        UpdateAll();
    }

    @FXML
    protected void OpenDatabaseView(){
        SceneManager.openWindowByFXML("database-view.fxml");
    }

    public void UpdateAll(){
        ArrayList<Register> registers = RegisterDAO.GetAllByMonth(dropdownMenu.getText());

        float totalValue = 0.00f;

        int acceptedRuns = 0;
        int deniedRuns = 0;

        for(Register register : registers){
            totalValue += register.getValue();
            acceptedRuns += register.getAcceptedRuns();
            deniedRuns += register.getDeniedRuns();
        }

        int totalRuns = acceptedRuns + deniedRuns;
        float porcent;
        if(totalRuns != 0) porcent = (float) acceptedRuns / totalRuns * 100;
        else porcent = 0;
        acceptanceRateTextbox.setText("%"+String.format("%.0f", porcent));

        totalValueTextbox.setText(String.format("R$%.2f", totalValue));
        totalHoursTextbox.setText(calculateTotalTime(registers));
    }

    public String calculateTotalTime(ArrayList<Register> registers){
        LocalTimeStringConverter timeConverter = new LocalTimeStringConverter();


        int totalMinutes = 0;
        for(Register register : registers){
            LocalTime convertedTime = timeConverter.fromString(register.getAverage());
            Duration duration = Duration.parse("PT"+convertedTime.getHour()+"H"+convertedTime.getMinute()+"M");
            totalMinutes += (int) duration.toMinutes();
        }

        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;

        return String.format("%02d:%02d", hours, minutes);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Setup Table...


        //Banco de dados
        Connection con = ConnectionFactory.CreateConnection();

        // Some more configs...
        datePicker.setValue(LocalDate.now());

        UpdateAll();
    }
}