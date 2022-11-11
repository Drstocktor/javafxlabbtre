package com.example.javafxlabbtre.controller;

import com.example.javafxlabbtre.model.*;
import com.example.javafxlabbtre.model.Rectangle;
import com.example.javafxlabbtre.model.Shape;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import javax.swing.*;
import java.awt.*;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static java.lang.Math.sqrt;

public class Controller implements Initializable {
    public Button applyButton;
    public Button saveButton;
    public Button undoButton;
    public Canvas canvas;
    public ChoiceBox<String> sizeSelectBox;
    public Button rectangleButton;
    public Button circleButton;
    public Button selectButton;
    public ColorPicker colorPicker;

    private JFrame saveFrame = new JFrame();
    public ArrayList<Shape> placedShapes = new ArrayList<>();
    public ArrayList<Shape> oldPlacedShapes = new ArrayList<>();
    public Mode currentMode = Mode.CIRCLE;
    public Size currentSize = Size.SMALL;
    private Color originalColor = Color.WHITE;
    private final String[] SIZES = {"Small", "Medium", "Large"};
    private final double SMALL = 15;
    private final double MEDIUM = 30;
    private final double LARGE = 60;
    private final Color SELECTED_COLOR = Color.BLUE;
    private GraphicsContext gfxContext;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupSaveFrame();
        sizeSelectBox.getItems().addAll(SIZES);
        sizeSelectBox.setValue("Small"); //default value i menyn
        sizeSelectBox.setOnAction(event -> setSize());
        gfxContext = canvas.getGraphicsContext2D();
        gfxContext.setFill(Color.WHITE);
        gfxContext.fillRect(0,0, canvas.getWidth(), canvas.getHeight());

        // TODO lägg till animation
    }

    private void setupSaveFrame() {
        saveFrame.setVisible(false);
        saveFrame.setSize(400, 400);
    }


    public void setSize() {
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
            saveBeforeEdit();
            switch (currentMode) {
                case CIRCLE -> createCircle(mouseEvent);
                case RECTANGLE -> createRectangle(mouseEvent);
                case SELECT -> selectObject(mouseEvent);
            }
        }
    }

    private void selectObject(MouseEvent e) {
        //TODO optimise click detection???
        for (Shape s : placedShapes) {
            double distX = e.getX() - s.getPositionX();
            double distY = e.getY() - s.getPositionY();
            double distance = sqrt((distX * distX) + (distY * distY));
            if (s.getSize() == Size.LARGE && distance <= LARGE) {
                originalColor = s.getColor();
                modifyShape(s);
                break;
            } else if (s.getSize() == Size.MEDIUM && distance <= MEDIUM) {
                originalColor = s.getColor();
                modifyShape(s);
                break;
            } else if (s.getSize() == Size.SMALL && distance <= SMALL) {
                originalColor = s.getColor();
                modifyShape(s);
                break;
            }
        }
    }

    private void modifyShape(Shape s) {
        applyButton.setVisible(true);
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

    public void clearSelected() {
        for (Shape s : placedShapes) {
            s.setSelected(false);
            applyButton.setVisible(false);
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
        for (Shape s : placedShapes) {
            if (s.isSelected()) {
                if (!newColor.equals(SELECTED_COLOR)) {
                    s.setColor(newColor);
                    s.setSize(currentSize);
                    setNewSize(s);
                    clearSelected();
                    repaintCanvas();
                } else {
                    s.setColor(originalColor);
                    s.setSize(currentSize);
                    setNewSize(s);
                    clearSelected();
                    repaintCanvas();
                }
            }
        }
    }

    private void repaintCanvas() {
        gfxContext.setFill(Color.WHITE);
        gfxContext.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
        for (Shape s : placedShapes) {
            if (s instanceof Circle) {
                paintCircle((Circle) s);
            } else {
                paintRectangle((Rectangle) s);
            }
        }
    }

    public void undoChange() {
        placedShapes.clear();
        placedShapes.addAll(oldPlacedShapes);
        clearSelected();
        repaintCanvas();
    }

    public void saveBeforeEdit() {
        oldPlacedShapes.clear();
        oldPlacedShapes.addAll(placedShapes);
    }

    public void saveFile(ActionEvent actionEvent) {
        // todo code save file
        String fileName = "";
        String fileDir = "";


        FileDialog fd = new FileDialog(saveFrame, "Save", FileDialog.SAVE);
        fd.setMultipleMode(true);
        fd.setVisible(true);

        if (fd.getFile()!=null) {
            fileName = fd.getFile();
            fileDir = fd.getDirectory();
        }

        try {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileName));
        } catch (Exception e) {

        }

    }
}