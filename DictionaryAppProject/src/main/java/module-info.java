module com.app.dictionaryproject {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.app.dictionaryproject to javafx.fxml;
    exports com.app.dictionaryproject;
}