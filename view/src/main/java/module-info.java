module view {
    requires javafx.controls;
    requires javafx.fxml;


    requires org.po.controller;

    opens org.po.view to javafx.fxml;
    exports org.po.view;
}