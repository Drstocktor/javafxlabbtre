package com.example.javafxlabbtre.controller;

import com.example.javafxlabbtre.model.*;
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

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static java.lang.Math.sqrt;

public class Controller implements Initializable {
    @FXML
    private Canvas canvas;
    @FXML
    private ChoiceBox<String> sizeSelectBox;
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
    private Mode currentMode = Mode.CIRCLE;
    private Size currentSize = Size.SMALL;
    private Color currentColor = getColor();
    private final String[] SIZES = {"Small", "Medium", "Large"};
    private final double SMALL = 15;
    private final double MEDIUM = 30;
    private final double LARGE = 60;
    private final Color SELECTED_COLOR = Color.BLUE;
    private GraphicsContext gfxContext;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sizeSelectBox.getItems().addAll(SIZES);
        sizeSelectBox.setValue("Small"); //default value i menyn
        sizeSelectBox.setOnAction(this::getSize);
        gfxContext = canvas.getGraphicsContext2D();
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

    public void rectangleMode() {
        currentMode = Mode.RECTANGLE;
    }

    public void circleMode() {
        currentMode = Mode.CIRCLE;
    }

    public void selectMode() {
        currentMode = Mode.SELECT;
    }

    public Color getColor() {
        return colorPicker.getValue();
    }

    public void placeShape(MouseEvent mouseEvent) {
        boolean proximity = checkProximity(mouseEvent);
        if (proximity) {
            if (currentMode == Mode.SELECT) {
                if (hasSelected()) {
                    modifySelected();
                } else {
                    selectObject(mouseEvent);
                }
            }
        } else {
            clearSelected();
            switch (currentMode) {
                case CIRCLE -> createCircle(mouseEvent);
                case RECTANGLE -> createRectangle(mouseEvent);
                case SELECT -> selectObject(mouseEvent);
            }
        }
    }

    private void selectObject(MouseEvent e) {
        for (Shape s : placedShapes) {
            double distX = e.getX() - s.getPositionX();
            double distY = e.getY() - s.getPositionY();
            double distance = sqrt((distX * distX) + (distY * distY));
            if (s.getSize() == Size.LARGE && distance <= LARGE) {
                modifyShape(s);
                break;
            } else if (s.getSize() == Size.MEDIUM && distance <= MEDIUM) {
                modifyShape(s);
                break;
            } else if (s.getSize() == Size.SMALL && distance <= SMALL) {
                modifyShape(s);
                break;
            }
        }
    }

    private void modifyShape(Shape s) {
        if (s instanceof Circle) {
            s.setSelected(true);
            gfxContext.setFill(SELECTED_COLOR);
            gfxContext.fillOval(s.getPositionX() - (((Circle)s).getRadius() / 2), s.getPositionY() - (((Circle)s).getRadius() / 2), ((Circle)s).getRadius(), ((Circle)s).getRadius());
        } else if (s instanceof Rectangle) {
            s.setSelected(true);
            gfxContext.setFill(SELECTED_COLOR);
            gfxContext.fillRect(s.getPositionX() - (((Rectangle)s).getWidth() / 2), s.getPositionY() - (((Rectangle)s).getWidth() / 2), ((Rectangle)s).getWidth(), ((Rectangle)s).getHeight());
        }
    }

    private boolean checkProximity(MouseEvent e) {
        for (Shape s : placedShapes) {
            double distX = e.getX() - s.getPositionX();
            double distY = e.getY() - s.getPositionY();
            double distance = sqrt((distX * distX) + (distY * distY));
            if (s.getSize() == Size.LARGE && distance <= LARGE) {
                return true;
            } else if (s.getSize() == Size.MEDIUM && distance <= MEDIUM) {
                return true;
            } else if (s.getSize() == Size.SMALL && distance <= SMALL) {
                return true;
            }
        }
        return false;
    }

    private void clearSelected() {
        for (Shape s : placedShapes) {
            s.setSelected(false);
        }
    }

    public boolean hasSelected() {
        for (Shape s : placedShapes) {
            if (s.isSelected()) {
                return true;
            }
        }
        return false;
    }
    private void modifySelected() {
        for (Shape s : placedShapes) {
            if (s.isSelected()) {
                modifyShape(s);
                break;
            }
        }
    }

    private void createRectangle(MouseEvent e) {
        // Constructor: double positionX, double positionY, Color color, Size size, double base, double height
        switch (currentSize) {
            case SMALL -> {
                Rectangle rectangle = new Rectangle(e.getX(), e.getY(), getColor(), currentSize, SMALL, SMALL);
                paintRectangle(rectangle);
                placedShapes.add(rectangle);
            }
            case MEDIUM -> {
                Rectangle rectangle = new Rectangle(e.getX(), e.getY(), getColor(), currentSize, MEDIUM, MEDIUM);
                paintRectangle(rectangle);
                placedShapes.add(rectangle);
            }
            case LARGE -> {
                Rectangle rectangle = new Rectangle(e.getX(), e.getY(), getColor(), currentSize, LARGE, LARGE);
                paintRectangle(rectangle);
                placedShapes.add(rectangle);
            }
        }
    }

    private void createCircle(MouseEvent e) {
        // Constructor: double positionX, double positionY, Color color, Size size, double radius
        switch (currentSize) {
            case SMALL -> {
                Circle circle = new Circle(e.getX(), e.getY(), getColor(), currentSize, SMALL);
                paintCircle(circle);
                placedShapes.add(circle);
            }
            case MEDIUM -> {
                Circle circle = new Circle(e.getX(), e.getY(), getColor(), currentSize, MEDIUM);
                paintCircle(circle);
                placedShapes.add(circle);
            }
            case LARGE -> {
                Circle circle = new Circle(e.getX(), e.getY(), getColor(), currentSize, LARGE);
                paintCircle(circle);
                placedShapes.add(circle);
            }
        }
    }

    private void paintCircle(Circle circle) {
        if (hasSelected()) {
            if (circle.getColor().equals(SELECTED_COLOR)) {

            }
        } else {
            gfxContext.setFill(circle.getColor());
            gfxContext.fillOval(circle.getPositionX() - (circle.getRadius() / 2), circle.getPositionY() - (circle.getRadius() / 2), circle.getRadius(), circle.getRadius());
        }
    }

    private void paintRectangle(Rectangle rectangle) {
        if (hasSelected()) {

        } else {
            gfxContext.setFill(rectangle.getColor());
            gfxContext.fillRect(rectangle.getPositionX() - (rectangle.getWidth() / 2), rectangle.getPositionY() - (rectangle.getWidth() / 2), rectangle.getWidth(), rectangle.getHeight());
        }
    }


}