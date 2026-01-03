    package org.po.controller;


    import javafx.animation.FadeTransition;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
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
            FadeTransition fade = new FadeTransition(Duration.seconds(2), root);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setDelay(Duration.seconds(2));

            fade.setOnFinished(e -> loadMainApp());
            fade.play();
        }

        private void loadMainApp() {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/org/po/tcs/Main.fxml")
                );

                Scene mainScene = new Scene(loader.load());

                Stage stage = (Stage) root.getScene().getWindow();
                stage.setScene(mainScene);
                stage.centerOnScreen();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
