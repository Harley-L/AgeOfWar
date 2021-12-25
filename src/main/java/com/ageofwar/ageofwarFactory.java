package com.ageofwar;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.dsl.components.KeepOnScreenComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;

import com.almasb.fxgl.ui.ProgressBar;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;


public class ageofwarFactory implements EntityFactory {

    private Pair<HealthIntComponent,ProgressBar> createHPBar(int maxHP, int translateX) {
        var hp = new HealthIntComponent(maxHP);

        var hpView = new ProgressBar(false);
        hpView.setFill(Color.LIGHTGREEN);
//        hpView.setBackgroundFill(Color.WHITE);
        hpView.setTraceFill(Color.RED);
        hpView.setWidth(50);
        hpView.setTranslateY(-20);
        hpView.setTranslateX(translateX);
        hpView.setMaxValue(maxHP);
        hpView.currentValueProperty().bind(hp.valueProperty());
        return new Pair<>(hp,hpView);
    }

    @Spawns("soldier")
    public Entity newSoldier(SpawnData data) {
        Pair<HealthIntComponent,ProgressBar> barPair = createHPBar(EntityType.SOLDIER.getHp(),5);
        var hp = barPair.getKey();
        var hpView = barPair.getValue();

        return FXGL.entityBuilder(data)
                        .collidable()
                        .type(EntityType.SOLDIER)
                        .viewWithBBox("soldier.png")
                        .with(new ProjectileComponent(new Point2D(1,0),EntityType.SOLDIER.getSpeed()))
                        .with(new VelocityComponent())
                        .with(new KeepOnScreenComponent())
                        .view(hpView)
                        .with(hp)
                        .build();
    }

    @Spawns("enemy_soldier")
    public Entity newEnemySoldier(SpawnData data) {
        Pair<HealthIntComponent,ProgressBar> barPair = createHPBar(EntityType.SOLDIER.getHp(),5);
        var hp = barPair.getKey();
        var hpView = barPair.getValue();

        return FXGL.entityBuilder(data)
                .collidable()
                .type(EntityType.ENEMY_SOLDIER)
                .viewWithBBox("soldier.png")
                .with(new ProjectileComponent(new Point2D(-1,0),EntityType.SOLDIER.getSpeed()))
                .rotate(180)
                .with(new VelocityComponent())
                .with(new KeepOnScreenComponent())
                .view(hpView)
                .with(hp)
                .build();
    }

    @Spawns("range")
    public Entity newRange(SpawnData data) {
        Pair<HealthIntComponent,ProgressBar> barPair = createHPBar(EntityType.RANGE.getHp(),15);
        var hp = barPair.getKey();
        var hpView = barPair.getValue();

        return FXGL.entityBuilder(data)
                .with(new CollidableComponent(true))
                .type(EntityType.RANGE)
                .viewWithBBox("range.png")
                .with(new ProjectileComponent(new Point2D(1,0),EntityType.RANGE.getSpeed()))
                .with(new VelocityComponent())
                .with(new KeepOnScreenComponent())
                .view(hpView)
                .with(hp)
                .build();
    }

    @Spawns("enemy_range")
    public Entity newEnemyRange(SpawnData data) {
        Pair<HealthIntComponent,ProgressBar> barPair = createHPBar(EntityType.RANGE.getHp(),15);
        var hp = barPair.getKey();
        var hpView = barPair.getValue();

        return FXGL.entityBuilder(data)
                .with(new CollidableComponent(true))
                .type(EntityType.ENEMY_RANGE)
                .viewWithBBox("range.png")
                .with(new ProjectileComponent(new Point2D(-1,0),EntityType.RANGE.getSpeed()))
                .with(new VelocityComponent())
                .rotate(180)
                .with(new KeepOnScreenComponent())
                .view(hpView)
                .with(hp)
                .build();
    }

    @Spawns("base")
    public Entity newBase(SpawnData data) {
        Pair<HealthIntComponent,ProgressBar> barPair = createHPBar(EntityType.BASE.getHp(),15);
        var hp = barPair.getKey();
        var hpView = barPair.getValue();

        return FXGL.entityBuilder(data)
                .with(new CollidableComponent(true))
                .type(EntityType.BASE)
                .viewWithBBox(new Rectangle(80,100,Color.BROWN))
                .view(hpView)
                .with(hp)
                .build();
    }
}
