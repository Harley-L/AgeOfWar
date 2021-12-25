package com.ageofwar;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import com.almasb.fxgl.entity.Entity;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;


public class ageofwar extends GameApplication {
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1000);
        settings.setHeight(600);
        settings.setTitle("Age of War");
        settings.setVersion("0.1");
    }

    @Override
    protected void initGame() {
        final int WIDTH = FXGL.getAppWidth();
        final int HEIGHT = FXGL.getAppHeight();

        FXGL.getGameWorld().addEntityFactory(new ageofwarFactory());
        FXGL.getGameWorld().addEntity(FXGL.entityBuilder().buildScreenBoundsAndAttach(40));

        // Menu Buttons
        MenuButton upgrade_btn = new MenuButton("Upgrade", actionEvent -> System.out.println("UPGRADE"));
        FXGL.addUINode(upgrade_btn.getBtn(), WIDTH-120, 30);

        MenuButton special_btn = new MenuButton("Special", actionEvent -> System.out.println("SPECIAL"));
        FXGL.addUINode(special_btn.getBtn(), WIDTH-200, 30);

        ArrayList<String> troops = new ArrayList<>(Arrays.asList("Soldier", "Range", "Heavy", "Super"));
        ArrayList<EventHandler<ActionEvent>> troop_events = new ArrayList<>(Arrays.asList(
                actionEvent -> FXGL.spawn("soldier", 50, HEIGHT-120),
                actionEvent -> FXGL.spawn("range", 50, HEIGHT-120),
                actionEvent -> System.out.println("COMING SOON: Heavy"),
                actionEvent -> System.out.println("COMING SOON: Super")));
        DropDownMenu troop_dropDownMenu = new DropDownMenu("Troops",troops,troop_events);
        FXGL.addUINode(troop_dropDownMenu.getMenu(), WIDTH-300, 30);

        ArrayList<String> turrets = new ArrayList<>(Arrays.asList("Light", "Medium", "Heavy","Add Turret"));
        ArrayList<EventHandler<ActionEvent>> turret_events = new ArrayList<>(Arrays.asList(
                actionEvent -> System.out.println("COMING SOON: Light"),
                actionEvent -> System.out.println("COMING SOON: Medium"),
                actionEvent -> System.out.println("COMING SOON: Heavy"),
                actionEvent -> System.out.println("COMING SOON: Add Turret")));
        DropDownMenu turret_dropDownMenu = new DropDownMenu("Turrets",turrets,turret_events);
        FXGL.addUINode(turret_dropDownMenu.getMenu(), WIDTH-400, 30);

        ArrayList<String> enemy_troops = new ArrayList<>(Arrays.asList("Soldier", "Range"));
        ArrayList<EventHandler<ActionEvent>> enemy_troop_events = new ArrayList<>(Arrays.asList(
                actionEvent -> FXGL.spawn("enemy_soldier", WIDTH-50, HEIGHT-120),
                actionEvent -> FXGL.spawn("enemy_range", WIDTH-50, HEIGHT-120)));
        DropDownMenu enemy_troop_dropDownMenu = new DropDownMenu("Enemy Troops",enemy_troops,enemy_troop_events);
        FXGL.addUINode(enemy_troop_dropDownMenu.getMenu(), WIDTH-450, 30);

        // Bases
        FXGL.spawn("base",10,HEIGHT-110);
        FXGL.spawn("base",WIDTH-90,HEIGHT-110);

    }

    private boolean attack(Entity s1, Entity s2) {
        try {
            var hp = s2.getComponent(HealthIntComponent.class);
//            System.out.println(s1.getTypeComponent().getAttack());
            hp.damage(50);
            if (hp.getValue() <= 0) {
                s2.removeFromWorld();
                return true;
            }
            return false;
        } catch(Exception e) {
            return true;
        }
        }

    @Override
    protected void initPhysics() {
        CollisionHandler ch = new CollisionHandler(EntityType.SOLDIER, EntityType.SOLDIER) {
            @Override
            protected void onCollisionBegin(Entity soldier1, Entity soldier2) {
                if(soldier1.getX() > soldier2.getX()) {
                    VelocityComponent comp = soldier2.getComponent(VelocityComponent.class);
                    comp.begin_collision();
                } else {
                    VelocityComponent comp = soldier1.getComponent(VelocityComponent.class);
                    comp.begin_collision();
                }
            }

            @Override
            protected void onCollisionEnd(Entity soldier1, Entity soldier2) {
                if(soldier1.getX() > soldier2.getX()) {
                    VelocityComponent comp = soldier2.getComponent(VelocityComponent.class);
                    comp.end_collision();
                } else {
                    VelocityComponent comp = soldier1.getComponent(VelocityComponent.class);
                    comp.end_collision();
                }
            }
        };
        FXGL.getPhysicsWorld().addCollisionHandler(ch);
        FXGL.getPhysicsWorld().addCollisionHandler(ch.copyFor(EntityType.RANGE,EntityType.RANGE));
        FXGL.getPhysicsWorld().addCollisionHandler(ch.copyFor(EntityType.RANGE,EntityType.SOLDIER));


        CollisionHandler ch_enemy = new CollisionHandler(EntityType.SOLDIER, EntityType.SOLDIER) {
            @Override
            protected void onCollisionBegin(Entity soldier1, Entity soldier2) {
                if(soldier1.getX() < soldier2.getX()) {
                    VelocityComponent comp = soldier2.getComponent(VelocityComponent.class);
                    comp.begin_collision();
                } else {
                    VelocityComponent comp = soldier1.getComponent(VelocityComponent.class);
                    comp.begin_collision();
                }
            }

            @Override
            protected void onCollisionEnd(Entity soldier1, Entity soldier2) {
                if(soldier1.getX() < soldier2.getX()) {
                    VelocityComponent comp = soldier2.getComponent(VelocityComponent.class);
                    comp.end_collision();
                } else {
                    VelocityComponent comp = soldier1.getComponent(VelocityComponent.class);
                    comp.end_collision();
                }
            }
        };
        FXGL.getPhysicsWorld().addCollisionHandler(ch_enemy.copyFor(EntityType.ENEMY_SOLDIER,EntityType.ENEMY_SOLDIER));
        FXGL.getPhysicsWorld().addCollisionHandler(ch_enemy.copyFor(EntityType.ENEMY_RANGE,EntityType.ENEMY_SOLDIER));
        FXGL.getPhysicsWorld().addCollisionHandler(ch_enemy.copyFor(EntityType.RANGE,EntityType.ENEMY_RANGE));

        CollisionHandler ch_enemy_vs = new CollisionHandler(EntityType.SOLDIER, EntityType.ENEMY_SOLDIER) {
            @Override
            protected void onCollisionBegin(Entity soldier1, Entity soldier2) {
                soldier2.getComponent(VelocityComponent.class).begin_collision();
                soldier1.getComponent(VelocityComponent.class).begin_collision();
                Timer timer = new Timer();
                timer.schedule( new TimerTask() {
                    public void run() {
                        if(attack(soldier2,soldier1)) {
                            timer.cancel();
                        }

                    }
                }, 3000, 3000);

                timer.schedule( new TimerTask() {
                    public void run() {
                        if(attack(soldier1,soldier2)) {
                            timer.cancel();
                        }
                    }
                }, 2000, 2000);

            }

            @Override
            protected void onCollisionEnd(Entity soldier1, Entity soldier2) {
                try {
                    soldier1.getComponent(VelocityComponent.class).end_collision();
                } catch(Exception ignored) {}
                try {
                    soldier2.getComponent(VelocityComponent.class).end_collision();
                } catch(Exception ignored) {}
            }
        };
        FXGL.getPhysicsWorld().addCollisionHandler(ch_enemy_vs);
        FXGL.getPhysicsWorld().addCollisionHandler(ch_enemy_vs.copyFor(EntityType.RANGE,EntityType.ENEMY_RANGE));
        FXGL.getPhysicsWorld().addCollisionHandler(ch_enemy_vs.copyFor(EntityType.SOLDIER,EntityType.ENEMY_RANGE));
        FXGL.getPhysicsWorld().addCollisionHandler(ch_enemy_vs.copyFor(EntityType.RANGE,EntityType.ENEMY_SOLDIER));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
// TODO
// Set speed and set attack dmg of entities properly (Stop Hard coding it)
// Health bar leaves red trace when damaged
// Bug where things cant spawn after something dies

// TIPS
//        FXGL.run(() -> FXGL.spawn("range", 50, 550), Duration.seconds(6.0));
//        .viewWithBBox(FXGL.texture("range.png",35,50)) // Change size of texture
//        Cursor cursor = FXGL.getGameScene().getRoot().getCursor();
