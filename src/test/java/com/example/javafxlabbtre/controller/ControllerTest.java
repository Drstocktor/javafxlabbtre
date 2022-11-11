package com.example.javafxlabbtre.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.example.javafxlabbtre.model.Mode;
import org.junit.jupiter.api.Test;

class ControllerTest {

    @Test
    void getSize() {

    }

    @Test
    void rectangleMode() {
        var controller = new Controller();
        controller.rectangleMode();

        Mode testMode = controller.currentMode;
        assertEquals(Mode.RECTANGLE, testMode);
    }

    @Test
    void isInCircleMode() {
    }

    @Test
    void selectMode() {
    }

    @Test
    void newListShouldBeOldList() {
        var controller = new Controller();

        controller.saveBeforeEdit();

        assertEquals(controller.oldPlacedShapes, controller.placedShapes);
    }


    @Test
    void getColor() {

    }

    @Test
    void saveBeforeEdit() {
    }

    @Test
    void sizeShouldChange() {
    }

    @Test
    void saveFile() {
    }
}