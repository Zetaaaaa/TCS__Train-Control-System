module org.po.controller {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.po.model;

    opens org.po.controller to javafx.fxml;
    exports org.po.controller;
}