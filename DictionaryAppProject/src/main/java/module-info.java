module com.app.dictionaryproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;
    requires mysql.connector.j;
    requires playwright;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires org.json;
    requires java.desktop;
    requires org.apache.httpcomponents.httpmime;

    opens com.app.dictionaryproject to javafx.fxml;
    exports com.app.dictionaryproject;
    exports com.app.dictionaryproject.Controller;
}

