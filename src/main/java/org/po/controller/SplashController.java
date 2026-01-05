    package org.po.controller;


    import javafx.animation.FadeTransition;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.layout.StackPane;
    import javafx.stage.Stage;
    import javafx.util.Duration;

    public class SplashController {

        @FXML
        private StackPane root;

        @FXML
        public void initialize() {
            playSplash();
        }

        private void playSplash() {
            //change transition time here
            FadeTransition fade = new FadeTransition(Duration.seconds(0.5), root);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            //change duration delay here
            fade.setDelay(Duration.seconds(0));

            fade.setOnFinished(e -> loadMainApp());
            fade.play();
        }

        private void loadMainApp() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Main.fxml"));
                Parent mainRoot = loader.load(); // injection happens here
                Scene mainScene = new Scene(mainRoot);

                Stage stage = (Stage) root.getScene().getWindow();
                stage.setScene(mainScene);
                stage.centerOnScreen();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
