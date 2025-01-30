module org.example.myifoodjavaedition {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.sql;


    opens org.allyssinxd.myifoodjavaedition to javafx.fxml;
    exports org.allyssinxd.myifoodjavaedition;
}