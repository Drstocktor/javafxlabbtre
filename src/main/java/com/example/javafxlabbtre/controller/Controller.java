package com.example.javafxlabbtre.controller;

import com.example.javafxlabbtre.Mode;
import com.example.javafxlabbtre.Size;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public ChoiceBox<String> sizeSelectBox;
    @FXML
    private Canvas canvas;
    @FXML
    private Button rectangleButton;
    @FXML
    private Button circleButton;
    @FXML
    private Button polygonButton;
    @FXML
    private Button selectButton;
    @FXML
    private ColorPicker colorPicker;

    ArrayList<Shape> placedShapes = new ArrayList<>();
    GraphicsContext paintCanvas;
    private Mode currentMode = Mode.CIRCLE;
    private Size currentSize = Size.MEDIUM;
    private final String[] SIZES = {"Small", "Medium", "Large"};
    private final double[] RECTSIZE = {5, 10, 15};
    private double positionX;
    private double positionY;

    public void getSize(ActionEvent event) {
        String tempCurrentSize = sizeSelectBox.getValue();
        switch (tempCurrentSize) {
                case "Small" -> currentSize = Size.SMALL;
                case "Medium" -> currentSize = Size.MEDIUM;
                case "Large" -> currentSize = Size.LARGE;
                default -> {
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sizeSelectBox.getItems().addAll(SIZES);
        sizeSelectBox.setOnAction(this::getSize);
        paintCanvas = canvas.getGraphicsContext2D();
        canvas.setOnMouseClicked(this::placeShape);
    }

    public void rectangleMode(MouseEvent mouseEvent) {
        currentMode = Mode.RECTANGLE;
    }

    public void polygonMode(MouseEvent mouseEvent) {
        currentMode = Mode.POLYGON;
    }

    public void circleMode(MouseEvent mouseEvent) {
       currentMode = Mode.CIRCLE;
    }

    public void selectMode(MouseEvent mouseEvent) {
        currentMode = Mode.SELECT;
    }

    public Color changeColor(ActionEvent actionEvent) {
        return colorPicker.getValue();
    }

    public Color getColor() {
        return colorPicker.getValue();
    }

    public void placeShape(MouseEvent mouseEvent) {
        switch (currentMode) {
            case CIRCLE -> createCircle(mouseEvent);
            case POLYGON -> createPolygon();
            case RECTANGLE -> createRectangle();
            case SELECT -> selectObject();
        }
    }

    private void selectObject() {

    }


    private void createRectangle() {
        Rectangle rectangle = new Rectangle();
    }

    private void createPolygon() {
    }

    private void createCircle(MouseEvent e) {
        Circle circle = new Circle(e.getSceneX(), e.getSceneY(), RECTSIZE[2], getColor());
    }

}