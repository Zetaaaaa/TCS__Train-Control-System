package org.po;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/Splash.fxml")
        );
        Scene splashScene = new Scene(loader.load());

        stage.setScene(splashScene);
        stage.setTitle("TCS");
        stage.show();
    }

}
