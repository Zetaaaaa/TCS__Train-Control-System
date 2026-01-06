package org.po.controller;

import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import org.po.model.Database;
import org.po.model.Neighbor;
import org.po.model.Position;
import org.po.model.Station;

import javafx.scene.shape.Line;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DashboardControler {

    @FXML
    private Pane mapContainer;

    @FXML
    private Button showPanel;

    @FXML
    private HBox panelLayout;

    private Boolean visible = false;

    private Database database;

    private Connection connection;

    private ArrayList<Station> stations = new ArrayList<>();

    private void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setDatabase(Database database) throws SQLException {
        this.database = database;
        initData(); // safe place
    }

    private void initData() throws SQLException {
        System.out.println("dashboard controler init");
        System.out.println(database);
        database.initializeConnection();
        System.out.println("connection");
        System.out.println(database.getConnection());
        setConnection(database.getConnection());
        // Sample query
        ResultSet rs = database.executeQuery(connection, "SELECT * FROM stations");

        while (rs.next()) {
            System.out.println(rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3));
            stations.add(new Station(
                    rs.getString(2),
                    new Position(rs.getInt(3)*10, rs.getInt(4)*10),
                    rs.getString(2),new ArrayList<>()));
        }


        stations.get(0).addConnection(stations.get(1));
        stations.get(1).addConnection(stations.get(0));
        stations.get(1).addConnection(stations.get(2));
        stations.get(2).addConnection(stations.get(1));

        for (Station s : stations) {
            drawStationMarker(s, mapContainer);
            for (Neighbor n : s.getConnections()) {
                drawSingleConnection(s, n, mapContainer);
            }
        }

        showPanel.setOnAction(event -> {
            showPanel.setMouseTransparent(true);
            togglePanel();
            PauseTransition pause = new PauseTransition(Duration.millis(300));
            pause.setOnFinished(e -> showPanel.setMouseTransparent(false));
            pause.play();
        });
    }

    @FXML
    public void initialize() throws SQLException {
//        ArrayList<Station> stations = new ArrayList<>();
//        stations.add(new Station(
//                "Krakow Glowny",
//                new Position(50.0, 0.0),
//                "Krakow",new ArrayList<>()));
//        stations.add(new Station(
//                "Krakow Nowa Huta",
//                new Position(250.0, 20.0),
//                "Krakow",
//                new ArrayList<>()));
//        stations.add(new Station(
//                "Tarnow",
//                new Position(650.0, 100.0),
//                "Tarnow",
//                new ArrayList<>()));

    }

    public void drawStationMarker(Station station, Pane container) {
        Label stationLabel = new Label(station.getName());
        stationLabel.setStyle("-fx-font-size: 10px;");

        VBox stationBox = new VBox(stationLabel);
        stationBox.setStyle(
                "-fx-background-color: #ffffff; " +
                        "-fx-border-color: #34495e; " +
                        "-fx-border-width: 1px; " +
                        "-fx-padding: 2px; " +
                        "-fx-background-radius: 2px; " +
                        "-fx-border-radius: 2px;"
        );
        stationBox.setMinSize(100, 100);
        stationBox.setMaxSize(100, 100);
        stationBox.setLayoutX(station.getPosition().getX());
        stationBox.setLayoutY(station.getPosition().getY());

        container.getChildren().add(stationBox);
    }
    public void drawSingleConnection(Station source, Neighbor connection, Pane container) {
        Station destination = connection.getDestination();

        Line line = new Line();

        line.setStartX(source.getPosition().getX());
        line.setStartY(source.getPosition().getY());

        line.setEndX(destination.getPosition().getX());
        line.setEndY(destination.getPosition().getY());

        line.setStrokeWidth(4.0);
        line.setStroke(Color.web("#34495e"));
        line.setOpacity(0.8);

        // 6. Add to container behind stations
        container.getChildren().add(0, line);
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