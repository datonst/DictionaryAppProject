module com.app.dictionaryproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens com.app.dictionaryproject to javafx.fxml;
    exports com.app.dictionaryproject;
}