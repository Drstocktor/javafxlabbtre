module com.example.javafxlabbtre {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.javafxlabbtre to javafx.fxml;
    exports com.example.javafxlabbtre;
    exports com.example.javafxlabbtre.controller;
    opens com.example.javafxlabbtre.controller to javafx.fxml;
    exports com.example.javafxlabbtre.model;
    opens com.example.javafxlabbtre.model to javafx.fxml;
}