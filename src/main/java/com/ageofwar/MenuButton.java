package com.ageofwar;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;

public class MenuButton {
    private Button btn;

    MenuButton(String name, EventHandler<ActionEvent> event) {
        btn = new Button(name);
        DropShadow shadow = new DropShadow();
        btn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> btn.setEffect(shadow));
        btn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> btn.setEffect(null));
        btn.setOnAction(event);
    }

    public Button getBtn() {
        return btn;
    }
}
