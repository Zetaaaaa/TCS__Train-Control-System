package org.po.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;
import org.po.model.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.MapValueFactory;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseControler {


    @FXML
    private BorderPane borderPane;

    @FXML
    private ComboBox<String> tableAction;

    @FXML
    private ComboBox<String> tableSelect;;

    private String setOperation = null;

    @FXML
    private Button exectuteQuery;

    private ArrayList<String> tableListNames = new ArrayList<>(List.of("stations","trains","rides","neighbors","routes"));

    private ArrayList<String> tableSqlOperations = new ArrayList<>(List.of("SELECT","DELETE","INSERT"));

    Database database;

    @FXML
    private void initialize() throws SQLException {
        database = new Database();
        database.initializeConnection();
        Connection connection = database.getConnection();

        if(borderPane.getCenter() == null){
            Label textLabel = new Label("No table selected...");
                    textLabel.getStyleClass().add("test");
            borderPane.setCenter(textLabel);
        }

        tableSelect.getItems().addAll(tableSqlOperations);

        tableSelect.setOnAction(event -> {
            ResultSet res = null;
            try {
                res = database.executeQuery(connection, "select * from "+tableSelect.getSelectionModel().getSelectedItem());
                TableView<Map<String, Object>> builtTable = buildTable(res);
                borderPane.setCenter(builtTable);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        tableAction.getItems().addAll(tableSelect.getItems());
        tableAction.setOnAction(event -> {
            setOperation = tableAction.getSelectionModel().getSelectedItem();
        });

    }

    public static TableView<Map<String, Object>> buildTable(ResultSet rs) throws SQLException {

        //TableView<S>
//        S - represents type of each row
//        So TableView<Map<String, Object>>
//        says that each row is a map of key-value pairs.
//        Key = column name (String)
//        Value = the actual value to display (Object)

        TableView<Map<String, Object>> tableView = new TableView<>();
        ObservableList<Map<String, Object>> data = FXCollections.observableArrayList();

        //get metaddata about query
        ResultSetMetaData meta = rs.getMetaData();

        int columnCount = meta.getColumnCount();

        // Create columns dynamically
        for (int i = 1; i <= columnCount; i++) {
            String columnName = meta.getColumnLabel(i);

            //TableColumn<Map<String, Object>, Object>
            // <S,T>
            // S - type of data in column (the same as tableview)
            // T - type of data in that column (cell type)
            // so each row is a map of column names → values

            TableColumn<Map<String, Object>, Object> column = new TableColumn<>(columnName);

            // Every TableColumn needs a cell value factory,
            // which tells it how to extract T from S
            column.setCellValueFactory(cellData ->
                    new ReadOnlyObjectWrapper<>(cellData.getValue().get(columnName))
            );

            tableView.getColumns().add(column);
        }

        // Populate rows
        //for each row create map that has key(String) and value (object)
        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(meta.getColumnLabel(i), rs.getObject(i));
            }
            data.add(row);
        }

        tableView.setItems(data);
        return tableView;
    }

}

