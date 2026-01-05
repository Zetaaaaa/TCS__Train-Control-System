module org.po.tcs {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires java.sql;
    requires java.dotenv;
    requires mysql.connector.java;
    requires annotations;

    opens org.po.controller to javafx.fxml;
    exports org.po;
    opens org.po to javafx.fxml;
}