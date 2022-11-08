package com.example.javafxlabbtre.Model;

import javafx.scene.paint.Color;

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
}
