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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
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
    public List<Shape> placedShapes = new LinkedList<>();
    public List<Shape> oldPlacedShapes = new LinkedList<>();
    public Mode currentMode = Mode.CIRCLE;
    public Size currentSize = Size.SMALL;
    private Color originalColor = Color.WHITE;
    private final String[] SIZES = {"Small", "Medium", "Large"};
    private final double SMALL = 15;
    private final double MEDIUM = 30;
    private final double LARGE = 60;
    private final Color SELECTED_COLOR = Color.BLUE;
    private GraphicsContext gfxContext;

    public Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sizeSelectBox.getItems().addAll(SIZES);
        sizeSelectBox.setValue("Small"); //default value i menyn
        sizeSelectBox.setOnAction(event -> setSize());
        gfxContext = canvas.getGraphicsContext2D();
        gfxContext.setFill(Color.WHITE);
        gfxContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
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
        repaintCanvas();
    }

    public void circleMode() {
        currentMode = Mode.CIRCLE;
        clearSelected();
        repaintCanvas();
    }

    public void selectMode() {
        currentMode = Mode.SELECT;
        clearSelected();
        repaintCanvas();
    }

    public Color getColor() {
        return colorPicker.getValue();
    }

    public void placeShape(MouseEvent mouseEvent) {
        boolean proximity = checkProximity(mouseEvent);
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();
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
            repaintCanvas();
            switch (currentMode) {
                case CIRCLE -> createCircle(x, y);
                case RECTANGLE -> createRectangle(x, y);
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

    public void createRectangle(double x, double y) {
        // Constructor: double positionX, double positionY, Color color, Size size, double base, double height
        Rectangle rectangle = new Rectangle(x, y, getColor(), currentSize, currentSize.getSize(), currentSize.getSize());
        paintRectangle(rectangle);
        saveBeforeEdit();
        placedShapes.add(rectangle);
    }

    public void createCircle(double x, double y) {
        // Constructor: double positionX, double positionY, Color color, Size size, double radius
        Circle circle = new Circle(x, y, getColor(), currentSize, currentSize.getSize());
        paintCircle(circle);
        saveBeforeEdit();
        placedShapes.add(circle);
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
        saveBeforeEdit();
        for (Shape s : placedShapes) {
            if (s.isSelected()) {
                double x = s.getPositionX();
                double y = s.getPositionY();
                setSize();
                if (s instanceof Circle) {
                    createCircle(x, y);
                } else {
                    createRectangle(x, y);
                }
                placedShapes.remove(s);
                repaintCanvas();
                break;
            }
        }
    }

    private void repaintCanvas() {
        gfxContext.setFill(Color.WHITE);
        gfxContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
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

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SVG", "*.svg"));

        File filePath = fileChooser.showSaveDialog(stage);

        if (filePath != null) {
            convertToSVG(filePath.toPath());
        }

    }

    private void convertToSVG(Path file) {
        StringBuffer outPut = new StringBuffer();

        outPut.append("<svg width=\"600\" height=\"500\" xmlns=\"http://www.w3.org/2000/svg\">");

        for (var s : placedShapes) {

            if (s instanceof Circle) {
                outPut.append("<circle cx=\"" + s.getPositionX() +
                        "\" cy=\"" + s.getPositionY() +
                        "\" r=\"" + s.convertToInt() +
                        "\" fill=\"#" +
                        s.getColor().toString().substring(2, 8) +
                        "\" />");

            }
            if (s instanceof Rectangle) {
                outPut.append("<rect x=\"" + (s.getPositionX() - s.convertToInt()) +
                        "\" y=\"" + (s.getPositionY() - s.convertToInt()) +
                        "\" width=\"" + (s.convertToInt() * 2) +
                        "\" height=\"" + (s.convertToInt() * 2) +
                        "\" fill=\"#" +
                        s.getColor().toString().substring(2, 8) +
                        "\" />");
            }
        }
        outPut.append("</svg>");

        try {
            Files.writeString(file, outPut.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}