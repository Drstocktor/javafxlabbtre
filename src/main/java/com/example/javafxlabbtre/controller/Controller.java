package com.example.javafxlabbtre.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public ChoiceBox<String> sizeSelectBox;

    @FXML
    private Button rectangleButton;
    @FXML
    private Button circleButton;
    @FXML
    private Button triangleButton;
    @FXML
    private Button selectButton;

    @FXML
    private ColorPicker colorPicker;

    private final String[] SIZES = {"Small", "Medium", "Large"};

    public void getSize(ActionEvent event) {
        // TODO: 2022-11-05 ändra storlek enligt val
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sizeSelectBox.getItems().addAll(SIZES);
        sizeSelectBox.setOnAction(this::getSize);
    }

    public void placeRectangle(MouseEvent mouseEvent) {
        // TODO: 2022-11-05 placera rektangel 
    }

    public void placeTriangle(MouseEvent mouseEvent) {
        // TODO: 2022-11-05 placera triangel 
    }

    public void placeCircle(MouseEvent mouseEvent) {
        // TODO: 2022-11-05 placera cirkel 
    }
    
    public void selectObject(MouseEvent mouseEvent) {
        // TODO: 2022-11-05 koda select object 
    }

    public Color changeColor(ActionEvent actionEvent) {
        return colorPicker.getValue();
    }
}