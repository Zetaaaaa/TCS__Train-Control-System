package org.po.controller;

import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import org.po.model.*;

import javafx.scene.shape.Line;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class DashboardControler implements Listener {


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

    private HashMap<Train, Circle> trains = new HashMap<>();


    private void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setDatabase(Database database) throws SQLException {
        this.database = database;
        initData(); // safe place
    }

    private void initData() throws SQLException {

        database.initializeConnection();

        setConnection(database.getConnection());
        // Sample query
        ResultSet rs = database.executeQuery(connection, "SELECT * FROM stations order by station_id");

        while (rs.next()) {
//            System.out.println(rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3));
            stations.add(new Station(
                    rs.getString(2),
                    new Position(100+rs.getInt(3)*8, 100+rs.getInt(4)*8),
                    rs.getString(2),new ArrayList<>()));
        }

        ResultSet rs2 = database.executeQuery(connection, "SELECT station_id, destination_id  FROM neighbors order by station_id");

        while (rs2.next()) {
//            System.out.println(rs2.getString(1)+" "+rs2.getString(2));
            stations.get(rs2.getInt(1)-1).addConnection(stations.get(rs2.getInt(2)-1));
        }



        //Train trainPassenger = TrainFactory.getTrain("passeNGER","EIP123","IC",190,450,true,false);
        for (Station s : stations) {
            drawStationMarker(s, mapContainer);
        }
        for (Station s : stations) {
            for (Neighbor n : s.getConnections()) {
                drawSingleConnection(s, n, mapContainer);
            }
        }

        ResultSet rs3 = database.executeQuery(connection, "SELECT `name`, `pos_x`, `pos_y`, `number`, `operator`, `speed`, `running`, `current_neighbor_id`, `neighbor_progress`, `current_station_id` FROM `trains` order by train_id");

        while (rs3.next()) {
            Train trainPassenger = TrainFactory.getTrain("passeNGER", rs3.getString(1), rs3.getString(5), rs3.getDouble(6), 450, true, false);
            trainPassenger.initialize(rs3.getBoolean(7), stations.get(rs3.getInt(10)-1), new Neighbor(stations.get(rs3.getInt(10)-1), stations.get(rs3.getInt(8)-1)), rs3.getDouble(9));
            Circle circle = drawTrainMarker(trainPassenger, mapContainer);

            trainPassenger.addListener(this);
            trainPassenger.start();
            trains.put(trainPassenger,circle);

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

    public Circle drawTrainMarker(Train train, Pane container) {
        // 1. Calculate Position
        // If the train is between stations, we calculate the interpolation point
        double startX = train.getCurrentStation().getPosition().getX();
        double startY = train.getCurrentStation().getPosition().getY();
        double endX = train.getNextNeighbor().getDestination().getPosition().getX();
        double endY = train.getNextNeighbor().getDestination().getPosition().getY();

        double progress = train.getNeighborProgress(); // Assuming 0.0 to 1.0

        double currentX = startX + (endX - startX) * progress;
        double currentY = startY + (endY - startY) * progress;

        // 2. Create the Circle (Smaller than station markers)
        Circle trainCircle = new Circle(8, Color.web("#e74c3c")); // Red color for trains
        trainCircle.setStroke(Color.WHITE);
        trainCircle.setStrokeWidth(2);
        trainCircle.setScaleZ(-10);

        System.out.println(currentX+" "+currentY);
        // 3. Position the Circle
        trainCircle.setCenterX(currentX);
        trainCircle.setCenterY(currentY);

        // 4. Add Interactivity (Optional: shows train name on hover)
        Tooltip tooltip = new Tooltip("Train: " + train.getTrainName() + "\nOperator: " + train.getOperator());
        Tooltip.install(trainCircle, tooltip);

        trainCircle.setOnMouseEntered(e -> trainCircle.setRadius(10));
        trainCircle.setOnMouseExited(e -> trainCircle.setRadius(8));

        //train click
//        trainCircle.setPickOnBounds(false);
//        trainCircle.setOnMouseClicked(e -> {
//            System.out.println("Dot clicked");
//            e.consume(); // remove this if blocks should ALSO receive click
//        });

        // 5. Add to container (on top of lines, but usually on top of stations)

        container.getChildren().add(trainCircle);

        return trainCircle;
    }
    public void drawStationMarker(Station station, Pane container) {
        //!!!STYLING SHOULD BE REPLACED BY CSS CLASS AND BY STYLED IN CSS FILE!!! - ok
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

            // Create a PopupControl
            PopupControl popup = new PopupControl();

// Create content for the popup
            VBox content = new VBox(10);
            content.getStyleClass().add("station-popup"); // Assign class
            content.setPadding(new Insets(15));

// Station name label
            Label nameLabel = new Label(station.getName());
            nameLabel.getStyleClass().add("station-popup-title"); // Assign class

            content.getChildren().addAll(nameLabel);
            popup.getScene().setRoot(content);

            // Set the content to the popup
            popup.getScene().setRoot(content);

            // Position popup near the mouse click
            popup.show(stationBox, event.getScreenX() + 10, event.getScreenY() + 10);

            // Hide popup when clicking anywhere else
            content.setOnMouseClicked(e -> popup.hide());
            boolean prev_select = select_mult;
            select_mult = event.isShiftDown();
            if (prev_select != select_mult) {selected_stations.clear();}
            if(!select_mult){
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
            transition.setByX(-300);
            transition.setInterpolator(Interpolator.EASE_IN);
            transition.play();
            visible = true;
        }
        else {
            transition.setByX(+300);
            transition.setInterpolator(Interpolator.LINEAR);
            transition.play();
            visible =  false;
        }
    }

    @Override
    public void update() {
        Platform.runLater(() -> {
            moveTrainmarker();
        });
    }

    private void moveTrainmarker(){
        System.out.println("moveMarker");
    }
}