module org.example.myifoodjavaedition {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens org.example.myifoodjavaedition to javafx.fxml;
    exports org.example.myifoodjavaedition;
}