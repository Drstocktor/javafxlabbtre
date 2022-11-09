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
    public Button applyButton;
    public Button saveButton;
    public Button undoButton;
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
    private Color originalColor = Color.WHITE;
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
        gfxContext.setFill(Color.WHITE);
        gfxContext.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
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
        clearSelected();
    }

    public void circleMode() {
        currentMode = Mode.CIRCLE;
        clearSelected();
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
        originalColor = s.getColor();
        if (s instanceof Circle) {
            s.setSelected(true);
            gfxContext.setFill(SELECTED_COLOR);
            gfxContext.fillOval(s.getPositionX() - (((Circle) s).getRadius() / 2), s.getPositionY() - (((Circle) s).getRadius() / 2), ((Circle) s).getRadius(), ((Circle) s).getRadius());
        } else if (s instanceof Rectangle) {
            s.setSelected(true);
            gfxContext.setFill(SELECTED_COLOR);
            gfxContext.fillRect(s.getPositionX() - (((Rectangle) s).getWidth() / 2), s.getPositionY() - (((Rectangle) s).getWidth() / 2), ((Rectangle) s).getWidth(), ((Rectangle) s).getHeight());
        }
    }

    private boolean checkProximity(MouseEvent e) {
        //TODO collision detection för rektangel?

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

    private void setNewSize(Shape s) {
        if (s instanceof Circle) {
            switch (currentSize) {
                case SMALL -> ((Circle) s).setRadius(SMALL);
                case MEDIUM -> ((Circle) s).setRadius(MEDIUM);
                case LARGE -> ((Circle) s).setRadius(LARGE);
            }
        } else {
            switch (currentSize) {
                case SMALL ->{
                    ((Rectangle) s).setWidth(SMALL);
                    ((Rectangle) s).setHeight(SMALL);
                }
                case MEDIUM -> {
                    ((Rectangle) s).setWidth(MEDIUM);
                    ((Rectangle) s).setHeight(MEDIUM);
                }
                case LARGE -> {
                    ((Rectangle) s).setWidth(LARGE);
                    ((Rectangle) s).setHeight(LARGE);
                }
            }
        }
    }

    private void paintCircle(Circle circle) {
        gfxContext.setFill(circle.getColor());
        gfxContext.fillOval(circle.getPositionX() - (circle.getRadius() / 2), circle.getPositionY() - (circle.getRadius() / 2), circle.getRadius(), circle.getRadius());
    }

    private void paintRectangle(Rectangle rectangle) {
        gfxContext.setFill(rectangle.getColor());
        gfxContext.fillRect(rectangle.getPositionX() - (rectangle.getWidth() / 2), rectangle.getPositionY() - (rectangle.getWidth() / 2), rectangle.getWidth(), rectangle.getHeight());
    }


    public void applyChange(ActionEvent actionEvent) {
        Color newColor = getColor();
        Size originalSize;

        for (Shape s : placedShapes) {
            if (s.isSelected()) {
                if (!newColor.equals(SELECTED_COLOR)) {
                    s.setColor(newColor);
                    originalSize = s.getSize();
                    s.setSize(currentSize);
                    setNewSize(s);
                    if (isSmaller(originalSize, s)) {
                        eraseShape(originalSize, s);
                    }
                    clearSelected();
                    if (s instanceof Circle) {
                        paintCircle((Circle) s);
                    } else {
                        paintRectangle((Rectangle) s);
                    }
                } else {
                    s.setColor(originalColor);
                    s.setSize(currentSize);
                    originalSize = s.getSize();
                    setNewSize(s);
                    if (isSmaller(originalSize, s)) {
                        eraseShape(originalSize, s);
                    }
                    clearSelected();
                    if (s instanceof Circle) {
                        paintCircle((Circle) s);
                    } else {
                        paintRectangle((Rectangle) s);
                    }
                }
            }
        }
    }

    private void eraseShape(Size originalSize, Shape s) {
        // TODO varför blir inte cirklarna färgade ordentligt??
        switch (originalSize) {
            case SMALL -> {}
            case MEDIUM -> {
                if (s instanceof Circle) {
                    gfxContext.setFill(Color.WHITE);
                    gfxContext.fillOval(s.getPositionX() - (MEDIUM / 2), s.getPositionY() - (MEDIUM / 2), MEDIUM, MEDIUM);
                } else {
                    gfxContext.setFill(Color.WHITE);
                    gfxContext.fillRect(s.getPositionX() - (MEDIUM / 2), s.getPositionY() - (MEDIUM / 2), MEDIUM, MEDIUM);
                }
            }
            case LARGE -> {
                if (s instanceof Circle) {
                    gfxContext.setFill(Color.WHITE);
                    gfxContext.fillOval(s.getPositionX() - (LARGE / 2), s.getPositionY() - (LARGE / 2), LARGE, LARGE);
                } else {
                    gfxContext.setFill(Color.WHITE);
                    gfxContext.fillRect(s.getPositionX() - (LARGE / 2), s.getPositionY() - (LARGE / 2), LARGE, LARGE);
                }
            }
        }
    }

    private boolean isSmaller(Size originalSize, Shape s) {
        switch (originalSize) {
            case SMALL -> {}
            case MEDIUM -> {
                if (s.getSize().equals(Size.SMALL)) {
                    return true;
                }
            }
            case LARGE -> {
                if (s.getSize().equals(Size.SMALL) || s.getSize().equals(Size.MEDIUM)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void undoChange(ActionEvent actionEvent) {
        // TODO code undo. double arraylists, or canvas snapshot??
    }

    public void saveFile(ActionEvent actionEvent) {
        // todo code save file
    }
}