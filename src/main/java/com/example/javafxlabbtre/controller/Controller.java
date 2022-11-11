package com.example.javafxlabbtre.controller;

import com.example.javafxlabbtre.model.*;
import com.example.javafxlabbtre.model.Rectangle;
import com.example.javafxlabbtre.model.Shape;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
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

    private JFrame saveFrame = new JFrame();
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
        setupSaveFrame();
        sizeSelectBox.getItems().addAll(SIZES);
        sizeSelectBox.setValue("Small"); //default value i menyn
        sizeSelectBox.setOnAction(event -> setSize());
        gfxContext = canvas.getGraphicsContext2D();
        gfxContext.setFill(Color.WHITE);
        gfxContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

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
                saveBeforeEdit();
                placedShapes.add(rectangle);
            }
            case MEDIUM -> {
                Rectangle rectangle = new Rectangle(e.getX(), e.getY(), getColor(), currentSize, MEDIUM, MEDIUM);
                paintRectangle(rectangle);
                saveBeforeEdit();
                placedShapes.add(rectangle);
            }
            case LARGE -> {
                Rectangle rectangle = new Rectangle(e.getX(), e.getY(), getColor(), currentSize, LARGE, LARGE);
                paintRectangle(rectangle);
                saveBeforeEdit();
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
                saveBeforeEdit();
                placedShapes.add(circle);
            }
            case MEDIUM -> {
                Circle circle = new Circle(e.getX(), e.getY(), getColor(), currentSize, MEDIUM);
                paintCircle(circle);
                saveBeforeEdit();
                placedShapes.add(circle);
            }
            case LARGE -> {
                Circle circle = new Circle(e.getX(), e.getY(), getColor(), currentSize, LARGE);
                paintCircle(circle);
                saveBeforeEdit();
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
                case SMALL -> {
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
        saveBeforeEdit();
        for (Shape s : placedShapes) {
            if (s.isSelected()) {
                double x = s.getPositionX();
                double y = s.getPositionY();

                // todo create new circle
                setSize();
                if (s instanceof Circle) {
                    editCircle(x, y);
                } else {
                    editRectangle(x, y);
                }
                placedShapes.remove(s);
                repaintCanvas();
                break;
            }
        }
    }

    private void editCircle(double x, double y) {
        // Constructor: double positionX, double positionY, Color color, Size size, double radius
        switch (currentSize) {
            case SMALL -> {
                Circle circle = new Circle(x, y, getColor(), currentSize, SMALL);
                paintCircle(circle);
                placedShapes.add(circle);
            }
            case MEDIUM -> {
                Circle circle = new Circle(x, y, getColor(), currentSize, MEDIUM);
                paintCircle(circle);
                placedShapes.add(circle);
            }
            case LARGE -> {
                Circle circle = new Circle(x, y, getColor(), currentSize, LARGE);
                paintCircle(circle);
                placedShapes.add(circle);
            }
        }
    }

    private void editRectangle(double x, double y) {
        // Constructor: double positionX, double positionY, Color color, Size size, double base, double height
        switch (currentSize) {
            case SMALL -> {
                Rectangle rectangle = new Rectangle(x, y, getColor(), currentSize, SMALL, SMALL);
                placedShapes.add(rectangle);
            }
            case MEDIUM -> {
                Rectangle rectangle = new Rectangle(x, y, getColor(), currentSize, MEDIUM, MEDIUM);
                placedShapes.add(rectangle);
            }
            case LARGE -> {
                Rectangle rectangle = new Rectangle(x, y, getColor(), currentSize, LARGE, LARGE);
                placedShapes.add(rectangle);
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
        WritableImage image = canvas.snapshot(new SnapshotParameters(), null);
        Image i = canvas.snapshot(new SnapshotParameters(), null);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
//        fileChooser.getExtensionFilters().clear();
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));

        File file = fileChooser.showSaveDialog(stage);

//        if (file != null) {
//            // todo FML
//            ImageIO.write(i,"png", new File("file.png"));
////            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
//        }




//        // todo code save file
//        String fileName = "";
//        String fileDir = "";
//        WritableImage image = canvas.snapshot(new SnapshotParameters(), null);
//
//        FileDialog fd = new FileDialog(saveFrame, "Save", FileDialog.SAVE);
//        fd.setMultipleMode(true);
//        fd.setVisible(true);
//
//        if (fd.getFile() != null) {
//            fileName = fd.getFile();
//            fileDir = fd.getDirectory();
//        }
//
//        File file = new File(fileName);
//
//        try {
//            WritableImage writableImage = new WritableImage(560, 600);
//            canvas.snapshot(null, writableImage);
//            RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
//            ImageIO.write(renderedImage, "png", file);
//        } catch (Exception e) {
//
//        }

    }
}