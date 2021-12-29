package com.ageofwar;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import com.sun.javafx.geom.Rectangle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.Cursor;
import javafx.scene.text.Text;
import javafx.util.Duration;


import java.util.*;


public class ageofwar extends GameApplication {
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("Gold", 0);
    }

    @Override
    protected void initUI() {
//        FXGL.addVarText("Gold",50,50);
    }

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
                actionEvent -> FXGL.spawn("enemy_soldier", WIDTH-100, HEIGHT-120),
                actionEvent -> FXGL.spawn("enemy_range", WIDTH-100, HEIGHT-120)));
        DropDownMenu enemy_troop_dropDownMenu = new DropDownMenu("Enemy Troops",enemy_troops,enemy_troop_events);
        FXGL.addUINode(enemy_troop_dropDownMenu.getMenu(), WIDTH-450, 30);

        // Bases
        FXGL.spawn("base",10,HEIGHT-110);
        FXGL.spawn("enemy_base",WIDTH-90,HEIGHT-110);

//        FXGL.addVarText("Gold",50,50);
//        FXGL.addText("Gold: ",50,50);
        FXGL.getGameScene().addUI();
    }

    private boolean attack(Entity s1, Entity s2) {
        if(!s1.isActive() || !s2.isActive())
            return true;
        var hp = s2.getComponent(HealthIntComponent.class);
        hp.damage(EntityType.getEntityType(s1).getAttack());
        if (hp.getValue() <= 0) {
            s2.removeFromWorld();
            return true;
        }
        return false;
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
                    if(soldier2.isActive()) {
                        VelocityComponent comp = soldier2.getComponent(VelocityComponent.class);
                        comp.end_collision();
                    }
                } else {
                    if(soldier1.isActive()) {
                        VelocityComponent comp = soldier1.getComponent(VelocityComponent.class);
                        comp.end_collision();
                    }
                }
            }
        };
        FXGL.getPhysicsWorld().addCollisionHandler(ch);
        FXGL.getPhysicsWorld().addCollisionHandler(ch.copyFor(EntityType.RANGE,EntityType.RANGE));
        FXGL.getPhysicsWorld().addCollisionHandler(ch.copyFor(EntityType.RANGE,EntityType.SOLDIER));


        CollisionHandler ch_enemy = new CollisionHandler(EntityType.ENEMY_SOLDIER, EntityType.ENEMY_SOLDIER) {
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
                    if(soldier2.isActive()) {
                        VelocityComponent comp = soldier2.getComponent(VelocityComponent.class);
                        comp.end_collision();
                    }
                } else {
                    if(soldier1.isActive()) {
                        VelocityComponent comp = soldier1.getComponent(VelocityComponent.class);
                        comp.end_collision();
                    }
                }
            }
        };
        FXGL.getPhysicsWorld().addCollisionHandler(ch_enemy);
        FXGL.getPhysicsWorld().addCollisionHandler(ch_enemy.copyFor(EntityType.ENEMY_RANGE,EntityType.ENEMY_SOLDIER));
        FXGL.getPhysicsWorld().addCollisionHandler(ch_enemy.copyFor(EntityType.ENEMY_RANGE,EntityType.ENEMY_RANGE));

        CollisionHandler ch_enemy_vs = new CollisionHandler(EntityType.SOLDIER, EntityType.ENEMY_SOLDIER) {
            @Override
            protected void onCollisionBegin(Entity soldier1, Entity soldier2) {
                soldier2.getComponent(VelocityComponent.class).begin_collision();
                soldier1.getComponent(VelocityComponent.class).begin_collision();

                FXGL.getGameTimer().runAtInterval(() -> {
                    if(soldier1.isActive() && soldier2.isActive())
                        attack(soldier1, soldier2);
                }, Duration.seconds(EntityType.getEntityType(soldier1).getAttack_speed()));

                FXGL.getGameTimer().runAtInterval(() -> {
                    if(soldier1.isActive() && soldier2.isActive())
                        attack(soldier2, soldier1);
                }, Duration.seconds(EntityType.getEntityType(soldier2).getAttack_speed()));
            }

            @Override
            protected void onCollisionEnd(Entity soldier1, Entity soldier2) {
                if(soldier1.isActive()) {
                    soldier1.getComponent(VelocityComponent.class).end_collision();
                    FXGL.inc("Gold", +50);
                }
                if(soldier2.isActive())
                    soldier2.getComponent(VelocityComponent.class).end_collision();

                FXGL.getGameTimer().clear();
            }
        };
        FXGL.getPhysicsWorld().addCollisionHandler(ch_enemy_vs);
        FXGL.getPhysicsWorld().addCollisionHandler(ch_enemy_vs.copyFor(EntityType.RANGE,EntityType.ENEMY_RANGE));
        FXGL.getPhysicsWorld().addCollisionHandler(ch_enemy_vs.copyFor(EntityType.SOLDIER,EntityType.ENEMY_RANGE));
        FXGL.getPhysicsWorld().addCollisionHandler(ch_enemy_vs.copyFor(EntityType.RANGE,EntityType.ENEMY_SOLDIER));

        CollisionHandler ch_base = new CollisionHandler(EntityType.SOLDIER, EntityType.ENEMY_BASE) {
            @Override
            protected void onCollisionBegin(Entity soldier1, Entity base) {
                soldier1.getComponent(VelocityComponent.class).begin_collision();

                FXGL.getGameTimer().runAtInterval(() -> {
                    if(soldier1.isActive() && base.isActive()) {
                        attack(soldier1, base);
                    }
                }, Duration.seconds(EntityType.getEntityType(soldier1).getAttack_speed()));
            }

            @Override
            protected void onCollisionEnd(Entity soldier1, Entity base) {
                if(soldier1.isActive())
                    soldier1.getComponent(VelocityComponent.class).end_collision();

                FXGL.getGameTimer().clear();
            }
        };
        FXGL.getPhysicsWorld().addCollisionHandler(ch_base);
        FXGL.getPhysicsWorld().addCollisionHandler(ch_base.copyFor(EntityType.RANGE,EntityType.ENEMY_BASE));
        FXGL.getPhysicsWorld().addCollisionHandler(ch_base.copyFor(EntityType.ENEMY_SOLDIER,EntityType.BASE));
        FXGL.getPhysicsWorld().addCollisionHandler(ch_base.copyFor(EntityType.ENEMY_RANGE,EntityType.BASE));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
// TODO
// Set speed and set attack dmg of entities properly (Stop Hard coding it)
// Health bar leaves red trace when damaged

// TIPS
//        FXGL.run(() -> FXGL.spawn("range", 50, 550), Duration.seconds(6.0));
//        .viewWithBBox(FXGL.texture("range.png",35,50)) // Change size of texture
//        Cursor cursor = FXGL.getGameScene().getRoot().getCursor();
