module org.example.myifoodjavaedition {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.sql;


    opens org.allyssinxd.myifoodjavaedition to javafx.fxml;
    exports org.allyssinxd.myifoodjavaedition;
    exports org.allyssinxd.myifoodjavaedition.utils;
    opens org.allyssinxd.myifoodjavaedition.utils to javafx.fxml;
    exports org.allyssinxd.myifoodjavaedition.entities;
    opens org.allyssinxd.myifoodjavaedition.entities to javafx.fxml;
    exports org.allyssinxd.myifoodjavaedition.controllers;
    opens org.allyssinxd.myifoodjavaedition.controllers to javafx.fxml;
}