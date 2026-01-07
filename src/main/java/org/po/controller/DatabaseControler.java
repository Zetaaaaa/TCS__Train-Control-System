package org.po.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import org.jetbrains.annotations.NotNull;
import org.po.model.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.sql.ResultSetMetaData;
import java.util.*;
import java.util.stream.Collectors;

public class DatabaseControler {

    @FXML
    private BorderPane borderPane;


    //Container for form fields
    @FXML
    public VBox formSpace;


    //Select table operation (DELETE,INSERT)
    @FXML
    private ComboBox<String> tableAction;


    //select which table to show
    @FXML
    private ComboBox<String> tableSelect;;

    @FXML
    private Label dbStatus;


    //decoration
    @FXML
    private TitledPane dataPane;

    @FXML
    private Label tableSelectLabel;


    //database object
    private Database database;

    private String tableName;

    //main controller reference
    MainController mainController;


    private String setOperation = null;

    @FXML
    private Button exectuteQuery;

    private final ArrayList<String> tableListNames = new ArrayList<>(List.of("stations","trains","rides","neighbors","routes","route_stops"));

    private final ArrayList<String> tableSqlOperations = new ArrayList<>(List.of("INSERT","DELETE","SELECT"));

    //table fields stored in a Hashmap <Column name, TextField Object reference>
    private final Map<String, TextField> fields = new HashMap<>();



    //to prevent reseting database class is passed down from main controller
    public void setDatabase(Database database) throws SQLException {
        this.database = database;
        // initialize stuff that requires DB connection
        initializeDatabase();
    }


    private void initializeDatabase() throws SQLException {

        if(database.initializeConnection()){
                dbStatus.textProperty().set(dbStatus.getText()+"Connected");
                Connection connection = database.getConnection();
                tableSelect.setOnAction(event -> {
                    //hide element for decoration
                    tableAction.mouseTransparentProperty().setValue(false);

                    //for sql query below
                    tableName = tableSelect.getValue();

                    ResultSet res = null;
                    try {
                        res = database.executeQuery(connection, "select * from "+ tableName);

                        //create table dynamically
                        TableView<Map<String, Object>> builtTable = buildTable(res);
                        borderPane.setCenter(builtTable);

                        //build user interaction area
                        buildFormFields(res);

                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                //if there's no table active
                if(borderPane.getCenter() == null){
                    Label textLabel = new Label("No table selected...");
                    textLabel.getStyleClass().add("test");
                    borderPane.setCenter(textLabel);
                }

                //combobox options
                tableSelect.getItems().addAll(tableListNames);
                tableAction.getItems().addAll(tableSqlOperations);

                //select sample action for decoration
                tableAction.getSelectionModel().select(0);


                //handle table change
                tableAction.setOnAction(event -> {
                    ResultSet res = null;
                    try {
                        res = database.executeQuery(connection, "select * from "+tableSelect.getSelectionModel().getSelectedItem());
                        buildFormFields(res);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            else{
                //Handle connection failure
                dbStatus.getStyleClass().add("error");
                dbStatus.textProperty().set(dbStatus.getText()+" connection failed");

                dataPane.visibleProperty().setValue(false);
                tableSelect.visibleProperty().setValue(false);
                tableSelectLabel.visibleProperty().setValue(false);
            }
    }

    @FXML
    private void initialize()  {
        //prevent operation clicks when there are no fields available
        tableAction.mouseTransparentProperty().setValue(true);
    }

//##FUNCTION OVERLOADING

    //update gui after changes
    private void refresh() throws SQLException {
       ResultSet res= database.executeQuery(database.getConnection(), "select * from "+ tableName);
        TableView<Map<String, Object>> builtTable = buildTable(res);
        borderPane.setCenter(builtTable);
    }

    //select
    private void refresh(ResultSet rs) throws SQLException {
        TableView<Map<String, Object>> builtTable = buildTable(rs);
        borderPane.setCenter(builtTable);
    }


    public static TableView<Map<String, Object>> buildTable(ResultSet rs) throws SQLException {

//        TableView<S>
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

    public void buildFormFields(ResultSet res) throws SQLException {
        ResultSetMetaData meta = res.getMetaData();
        fields.clear();
        formSpace.getChildren().clear();

        switch(tableAction.getSelectionModel().getSelectedItem()){
            case "INSERT":
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    if(!meta.isAutoIncrement(i)){
                        HBox hBox = new HBox();
                        hBox.spacingProperty().set(20);

                        String columnName = meta.getColumnName(i);
                        Label label = new Label(meta.getColumnName(i));
                        TextField textField = new TextField();

                        hBox.getChildren().add(label);
                        hBox.getChildren().add(textField);
                        formSpace.getChildren().add(hBox);
                        formSpace.getChildren().add(new Separator());

                        // store reference column name - text field
                        fields.put(columnName, textField);
                    }
                }

            break;
            case "DELETE", "SELECT":
                for (int i = 1; i <= meta.getColumnCount(); i++) {

                    if (meta.isAutoIncrement(i)) {
                        String columnName = meta.getColumnName(i);
                        Label label = new Label(columnName);
                        TextField textField = new TextField();

                        HBox hBox = new HBox(20);
                        hBox.getChildren().addAll(label, textField);

                        formSpace.getChildren().add(hBox);
                        //  store reference
                        fields.put(columnName, textField);
                    }
                }
                break;
        }
        Button executeButton = getButton();

        formSpace.getChildren().add(executeButton);
    }


    //execute function button logic
    @NotNull
    private Button getButton() {

        Button executeButton = new Button("Execute query");
        executeButton.setOnAction(event -> {
            String query;
            Connection connection = database.getConnection();
            switch(tableAction.getSelectionModel().getSelectedItem()){
                case "INSERT": {
                    query = buildInsertQuery();
                   String res;
                    try {
                        res = database.executeUpdate(connection, query);
                        refresh();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Response" +res);

                    break;
                }

                case "DELETE": {
                    query = buildDeleteQuery();
                    String res;
                    try {
                        res = database.executeUpdate(connection, query);
                        refresh();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Response" +res);
                    break;
                }
                case "SELECT":
                    query = buildSelectQuery();
                    try {
                        ResultSet rs = database.executeQuery(connection,query);
                        System.out.println(rs);
                        refresh(rs);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    break;
            }
        });
        return executeButton;
    }

    private Map<String, String> getFilledFields() {
        return fields.entrySet().stream()
                .filter(e -> !e.getValue().getText().isBlank())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().getText()
                ));
    }
//###############################    query builders    ###############################

    private String buildInsertQuery() {
        Map<String, String> values = getFilledFields();

        String columns = String.join(", ", values.keySet());

        // Add quotes around string values for SQL
        String vals = values.values().stream()
                .map(v -> "'" + v.replace("'", "''") + "'") // escape single quotes
                .collect(Collectors.joining(", "));

        String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + vals + ")";
        return sql;
    }

    private String buildDeleteQuery() {
        // Get the first entry (only one field)
        Map<String, String> values = getFilledFields(); // column -> value

        Map.Entry<String, String> entry = values.entrySet().iterator().next();

        String column = entry.getKey();
        String value = entry.getValue();

        String sql = "DELETE FROM " + tableName + " WHERE " + column + " = "+value;

        return sql;
    }

    private String buildSelectQuery() {
        Map<String, String> values = getFilledFields(); // column -> value

        Map.Entry<String, String> entry = values.entrySet().iterator().next();
        String column = entry.getKey();
        String value = entry.getValue();

        String sql = "SELECT * FROM " + tableName + " WHERE " + column + " = "+value;
        return sql;
    }



}

