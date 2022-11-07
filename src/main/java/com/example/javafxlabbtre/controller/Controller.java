package com.example.javafxlabbtre.controller;

import com.example.javafxlabbtre.Mode;
import com.example.javafxlabbtre.Size;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private ChoiceBox<String> sizeSelectBox;
    @FXML
    private Pane paneCanvas;
    @FXML
    private Button rectangleButton;
    @FXML
    private Button circleButton;
    @FXML
    private Button ellipseButton;
    @FXML
    private Button selectButton;
    @FXML
    private ColorPicker colorPicker;

    ArrayList<Shape> placedShapes = new ArrayList<>();
    GraphicsContext paintCanvas;
    private Mode currentMode = Mode.CIRCLE;
    private Size currentSize = Size.MEDIUM;
    private final String[] SIZES = {"Small", "Medium", "Large"};
    private final double SMALL = 15;
    private final double MEDIUM = 30;
    private final double LARGE = 60;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sizeSelectBox.getItems().addAll(SIZES);
        sizeSelectBox.setOnAction(this::getSize);
        paneCanvas.setOnMouseClicked(this::placeShape);
    }

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

    public void rectangleMode(MouseEvent mouseEvent) {
        currentMode = Mode.RECTANGLE;
    }

    public void ellipseMode(MouseEvent mouseEvent) {
        currentMode = Mode.ELLIPSE;
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
            case ELLIPSE -> createEllipse(mouseEvent);
            case RECTANGLE -> createRectangle(mouseEvent);
            case SELECT -> selectObject(mouseEvent);
        }
    }

    private void selectObject(MouseEvent e) {

    }


    private void createRectangle(MouseEvent e) {
        switch (currentSize) {
            case SMALL -> {
                Rectangle rectangle = new Rectangle(e.getX() - SMALL, e.getY() - SMALL, SMALL, SMALL);
                rectangle.setFill(getColor());
                paneCanvas.getChildren().add(rectangle);
            }
            case MEDIUM -> {
                Rectangle rectangle = new Rectangle(e.getX() - MEDIUM, e.getY() - MEDIUM, MEDIUM, MEDIUM);
                rectangle.setFill(getColor());
                paneCanvas.getChildren().add(rectangle);
            }
            case LARGE -> {
                Rectangle rectangle = new Rectangle(e.getX() - LARGE, e.getY() - LARGE, LARGE, LARGE);
                rectangle.setFill(getColor());
                paneCanvas.getChildren().add(rectangle);
            }
        }
    }

    private void createEllipse(MouseEvent e) {
        switch (currentSize) {
            case SMALL -> {
                Polygon polygon = new Polygon();
                polygon.setFill(getColor());
                paneCanvas.getChildren().add(polygon);
            }
            case MEDIUM -> {
                Ellipse ellipse = new Ellipse(e.getX(), e.getY(), MEDIUM, MEDIUM);
                ellipse.setFill(getColor());
                paneCanvas.getChildren().add(ellipse);
            }
            case LARGE -> {
                Ellipse ellipse = new Ellipse(e.getX(), e.getY(), LARGE, LARGE);
                ellipse.setFill(getColor());
                paneCanvas.getChildren().add(ellipse);
            }
        }
    }

    private void createCircle(MouseEvent e) {
        switch (currentSize) {
            case SMALL -> {
                Circle circle = new Circle(e.getX(), e.getY(), SMALL, getColor());
                paneCanvas.getChildren().add(circle);
            }
            case MEDIUM -> {
                Circle circle = new Circle(e.getX(), e.getY(), MEDIUM, getColor());
                paneCanvas.getChildren().add(circle);
            }
            case LARGE -> {
                Circle circle = new Circle(e.getX(), e.getY(), LARGE, getColor());
                paneCanvas.getChildren().add(circle);
            }
        }
    }

}