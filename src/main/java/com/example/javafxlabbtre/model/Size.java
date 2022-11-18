package com.example.javafxlabbtre.model;

public enum Size {
    SMALL(15), MEDIUM(30), LARGE(60);

    private int size;

    Size (int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
