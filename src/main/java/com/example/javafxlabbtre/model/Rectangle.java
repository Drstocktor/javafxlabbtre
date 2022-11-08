package com.example.javafxlabbtre.model;

import javafx.scene.paint.Color;

public class Rectangle extends Shape {
    private double width;
    private double height;

    public Rectangle(double positionX, double positionY, Color color, Size size, double base, double height) {
        super(positionX, positionY, color, size);
        this.width = base;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
