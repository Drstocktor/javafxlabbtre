module com.example.javafxlabbtre {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.javafxlabbtre to javafx.fxml;
    exports com.example.javafxlabbtre;
}