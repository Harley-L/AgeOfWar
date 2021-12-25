package com.ageofwar;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import java.util.ArrayList;

public class DropDownMenu {
    MenuBar menuBar;

    DropDownMenu(String name, ArrayList<String> names, ArrayList<EventHandler<ActionEvent>> events) {
        Menu fileMenu = new Menu(name);
        MenuItem item;
        for(int i = 0; i<names.toArray().length;i++) {
            item = new MenuItem(names.get(i));
            item.setOnAction(events.get(i));
            fileMenu.getItems().add(item);
        }
        menuBar = new MenuBar(fileMenu);
    }

    public MenuBar getMenu() {
        return menuBar;
    }
}
