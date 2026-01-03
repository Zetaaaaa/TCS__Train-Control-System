module org.po.tcs {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;


    opens org.po.tcs to javafx.fxml;
    opens org.po.controller to javafx.fxml;
    exports org.po;
    opens org.po to javafx.fxml;
}