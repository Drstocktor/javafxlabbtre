package com.example.javafxlabbtre.Model;

import javafx.scene.paint.Color;

public class Circle extends Shape {
    private double radius;

    public Circle(double positionX, double positionY, Color color, Size size, double radius) {
        super(positionX, positionY, color, size);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
