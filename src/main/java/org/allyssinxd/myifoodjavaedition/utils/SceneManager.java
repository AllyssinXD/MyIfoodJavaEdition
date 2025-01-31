package org.allyssinxd.myifoodjavaedition.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.allyssinxd.myifoodjavaedition.MainApplication;

import java.io.IOException;

public class SceneManager {

    public static void loadSceneByFXML(String file, Window window){
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(file));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = (Stage) window;
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void openWindowByFXML(String file){
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(file));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

}
