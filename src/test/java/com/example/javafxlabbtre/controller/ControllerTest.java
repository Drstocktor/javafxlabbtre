package com.example.javafxlabbtre.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ControllerTest {

    @Test
    void newListShouldBeOldList() {
        var controller = new Controller();

        controller.saveBeforeEdit();

        assertEquals(controller.oldPlacedShapes, controller.placedShapes);
    }

    @Test
    void hasNoSelected() {
        var controller = new Controller();

        assertFalse(controller.hasSelected());
    }
}