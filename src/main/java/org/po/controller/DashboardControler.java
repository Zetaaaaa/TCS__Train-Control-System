package org.po.controller;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class DashboardControler {

    @FXML
    private Button showPanel;

    @FXML
    private HBox panelLayout;

    Boolean visible = false;





    @FXML
    public void initialize() {
        showPanel.setOnAction(event -> {
            System.out.println("toogle");
            showPanel.setMouseTransparent(true);
            togglePanel();
            PauseTransition pause = new PauseTransition(Duration.millis(300));
            pause.setOnFinished(e -> showPanel.setMouseTransparent(false));
            pause.play();
        });

    }

    private void togglePanel() {
        TranslateTransition transition = new TranslateTransition(Duration.millis(200), panelLayout);
        if(!visible) {
            transition.setByX(-200);
            transition.setInterpolator(Interpolator.EASE_IN);
            transition.play();
            visible = true;


        }
        else {
            transition.setByX(+200);
            transition.setInterpolator(Interpolator.LINEAR);
            transition.play();
            visible =  false;
        }

    }
}
