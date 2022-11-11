package com.example.javafxlabbtre.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.example.javafxlabbtre.model.Mode;
import org.junit.jupiter.api.Test;

class ControllerTest {

    @Test
    void rectangleMode() {
        var controller = new Controller();
        controller.rectangleMode();

        Mode testMode = controller.currentMode;
        assertEquals(Mode.RECTANGLE, testMode);
    }

    @Test
    void newListShouldBeOldList() {
        var controller = new Controller();

        controller.saveBeforeEdit();

        assertEquals(controller.oldPlacedShapes, controller.placedShapes);
    }

    @Test
    void saveBeforeEdit() {
        //todo important test
    }
}