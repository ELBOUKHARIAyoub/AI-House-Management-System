module org.example.householdledger {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires itextpdf;
    requires java.net.http;
    requires org.json;


    opens org.example.householdledger to javafx.fxml;
    opens org.example.householdledger.Controller to javafx.fxml;

    exports org.example.householdledger;

}