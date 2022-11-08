package com.example.javafxlabbtre.Model;

import javafx.scene.paint.Color;

import java.util.Objects;

public abstract class Shape {
    private double positionX;
    private double positionY;
    private Color color;
    private Size size;
    private boolean selected;

     Shape(double positionX, double positionY, Color color, Size size) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.color = color;
        this.size = size;
    }
    public double getPositionX() {
        return positionX;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shape shape = (Shape) o;
        return Double.compare(shape.positionX, positionX) == 0 && Double.compare(shape.positionY, positionY) == 0 && selected == shape.selected && Objects.equals(color, shape.color) && size == shape.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionX, positionY, color, size, selected);
    }
}
