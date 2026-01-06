package org.po.controller;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.po.model.Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class MainController {

    @FXML
    private Button Main;
    @FXML
    private Button Settings;
    @FXML
    private Button About;

    @FXML
    private Button Database;

    @FXML
    private StackPane contentPane;

    @FXML
    private Button themeChange;

    Database database;
    Connection connection;

    private boolean darkTheme = false;

    public void setDatabase(Database database) {
        this.database = database;
    }

    public Database getDatabase() {
        return database;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    @FXML
    private void route(String path) throws IOException, SQLException {
        System.out.println( "/view/"+path + ".fxml");
        FXMLLoader loader  = new FXMLLoader((Objects.requireNonNull(getClass().getResource( "/view/"+path + ".fxml"))));
        Pane pane = loader.load();

        Object controller = loader.getController();

        if (controller instanceof DashboardControler d) {
            d.setDatabase(database);
        }

        if(Objects.equals(path, "Database")){
            DatabaseControler dbController = loader.getController();
            dbController.setDatabase(getDatabase());
        }
//        else if(Objects.equals(path, "Dashboard")){
//            DashboardControler dashboardControler = loader.getController();
//            dashboardControler.setDatabase(getDatabase());
//        }

        contentPane.getChildren().setAll(pane); // replace current center content
    }

    @FXML
    private void initialize() throws SQLException {

        // ✅ Safe to create Database here
        database = new Database();
        setDatabase(database);

        try{
            route("Dashboard");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        refreshFields();

    }

    private void refreshFields(){
        System.out.println("DATABASE");
        System.out.println(database);
            // Load icons
            Image sunIcon = new Image(getClass().getResource("/icons/sun.png").toExternalForm());
            Image moonIcon = new Image(getClass().getResource("/icons/moon.png").toExternalForm());

            ImageView iconView = new ImageView(sunIcon);
            iconView.setFitWidth(16);
            iconView.setFitHeight(16);
            themeChange.setGraphic(iconView);

            Platform.runLater(() -> {
                Scene scene = themeChange.getScene();
                if (scene != null) {
                    scene.getStylesheets().add(getClass().getResource("/css/newLight.css").toExternalForm());
                }
            });

            // Theme toggle action
            themeChange.setOnAction(event -> {
                // Inside your themeChange.setOnAction
                Scene scene = themeChange.getScene();
                if (scene == null) return;

                Pane root = (Pane) scene.getRoot();  // or StackPane, BorderPane, whatever your root is

                // Fade out
                FadeTransition fadeOut = new FadeTransition(Duration.millis(300), root);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.setOnFinished(e -> {
                    // Switch theme after fade-out
                    scene.getStylesheets().clear();

                    if (darkTheme) {
                        scene.getStylesheets().add(getClass().getResource("/css/newLight.css").toExternalForm());
                        darkTheme = false;
                        iconView.setImage(sunIcon);
                    } else {
                        scene.getStylesheets().add(getClass().getResource("/css/newDark.css").toExternalForm());
                        darkTheme = true;
                        iconView.setImage(moonIcon);
                    }

                    // Fade in
                    FadeTransition fadeIn = new FadeTransition(Duration.millis(300), root);
                    fadeIn.setFromValue(0.0);
                    fadeIn.setToValue(1.0);
                    fadeIn.play();
                });

                fadeOut.play();
            });




            Main.setOnAction(event -> {
                try {
                    route("Dashboard");
                    Main.setMouseTransparent(true);
                    Database.setMouseTransparent(false);
                    Settings.setMouseTransparent(false);
                    About.setMouseTransparent(false);
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            Database.setOnAction(event -> {
                try  {
                    route("Database");
                    Main.setMouseTransparent(false);
                    Database.setMouseTransparent(true);
                    Settings.setMouseTransparent(false);
                    About.setMouseTransparent(false);
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            Settings.setOnAction(event -> {
                try {
                    route("Settings");

                    Main.setMouseTransparent(false);
                    Database.setMouseTransparent(false);
                    Settings.setMouseTransparent(true);
                    About.setMouseTransparent(false);
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            About.setOnAction(event -> {
                try {
                    route("About");
                    Main.setMouseTransparent(false);
                    Database.setMouseTransparent(false);
                    Settings.setMouseTransparent(false);
                    About.setMouseTransparent(true);
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

