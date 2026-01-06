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
import java.util.HashMap;
import java.util.Hashtable;
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

    private boolean select_mult = false;

    private HashMap<Integer,Station> selected_stations = new HashMap<>();

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
        ResultSet rs = database.executeQuery(connection, "SELECT * FROM stations order by station_id");

        while (rs.next()) {
            System.out.println(rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3));
            stations.add(new Station(
                    rs.getString(2),
                    new Position(rs.getInt(3)*8, rs.getInt(4)*8),
                    rs.getString(2),new ArrayList<>()));
        }

        ResultSet rs2 = database.executeQuery(connection, "SELECT station_id, destination_id  FROM neighbors order by station_id");

        while (rs2.next()) {
            System.out.println(rs2.getString(1)+" "+rs2.getString(2));
            stations.get(rs2.getInt(1)-1).addConnection(stations.get(rs2.getInt(2)-1));
        }


        for (Station s : stations) {
            drawStationMarker(s, mapContainer);
        }
        for (Station s : stations) {
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

    }

    public void drawStationMarker(Station station, Pane container) {
        //!!!STYLING SHOULD BE REPLACED BY CSS CLASS AND BY STYLED IN CSS FILE!!!
        Label stationLabel = new Label(station.getName());
        stationLabel.setStyle("-fx-font-size: 10px;"+"-fx-font-weight: bold;"+"-fx-text-fill: white;");
        stationLabel.getStyleClass().add("labelText");
        VBox stationBox = new VBox(stationLabel);
        stationBox.setStyle(
                "-fx-background-color: #3b3f42; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-padding: 5px 15px; " +
                        "-fx-alignment: CENTER;"
        );
        stationBox.setMinSize(100, 100);
        stationBox.setMaxSize(100, 100);
        stationBox.setLayoutX(station.getPosition().getX()-50);
        stationBox.setLayoutY(station.getPosition().getY()-50);

        stationLabel.setMouseTransparent(true);
        stationBox.setPickOnBounds(true);
        stationBox.setMouseTransparent(false);
        stationBox.setOnMouseClicked(event -> {
            System.out.println("Station clicked: " + station.getName());

            boolean prev_select = select_mult;
            select_mult = event.isShiftDown();
            if (prev_select != select_mult) {selected_stations.clear();}
            if(select_mult==false){
                if(selected_stations!=null){
                selected_stations.clear();
                selected_stations.put(stations.indexOf(station),station);
                }
                System.out.println(selected_stations.values().stream().findFirst().orElse(null));
            }
            else{
                selected_stations.put(stations.indexOf(station),station);
                System.out.println(selected_stations.values());
            }


            //handleStationClick(station);
        });
        stationBox.setOnMouseEntered(e -> stationBox.setStyle(stationBox.getStyle() + "-fx-background-color: #9e9e9e;"));
        stationBox.setOnMouseExited(e -> stationBox.setStyle(stationBox.getStyle() + "-fx-background-color: #3b3f42;"));
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