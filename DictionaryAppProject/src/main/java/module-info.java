module com.app.dictionaryproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires playwright;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires org.json;

    opens com.app.dictionaryproject to javafx.fxml;
    exports com.app.dictionaryproject;
    exports com.app.dictionaryproject.Controller;
}

