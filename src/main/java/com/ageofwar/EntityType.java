package com.ageofwar;

import com.almasb.fxgl.entity.Entity;

import java.io.Serializable;

public enum EntityType {
    SOLDIER(50,25,100,1,15),
    RANGE(60,20,75,0.75,25),
    TANK(50,50,200,2,100),
    SUPER(50,60,200,1,250),
    BULLET(150, RANGE.attack,-1,0,0),
    BASE(0,0,1000,0,0),
    ENEMY_SOLDIER(SOLDIER.speed, SOLDIER.attack, SOLDIER.hp, SOLDIER.attack_speed,20),
    ENEMY_RANGE(RANGE.speed, RANGE.attack, RANGE.hp, RANGE.attack_speed,35),
    ENEMY_TANK(TANK.speed, TANK.attack, TANK.hp, TANK.attack_speed,150),
    ENEMY_SUPER(SUPER.speed, SUPER.attack, SUPER.hp, SUPER.attack_speed,300),
    ENEMY_BULLET(BULLET.speed, BULLET.attack, BULLET.hp, BULLET.attack_speed,0),
    ENEMY_BASE(BASE.speed, BASE.attack, BASE.hp, BASE.attack_speed,0);

    private final double attack_speed;
    private final int speed;
    private int attack;
    private int hp;
    private int cost;

    EntityType(int speed, int attack, int hp, double attack_speed, int cost) {
        this.speed = speed;
        this.attack = attack;
        this.hp = hp;
        this.attack_speed = attack_speed;
        this.cost = cost;
    }

    public int getSpeed() {
        return speed;
    }

    public int getAttack() {
        return attack;
    }

    public int getHp() {
        return hp;
    }

    public double getAttack_speed() {
        return attack_speed;
    }

    public int getCost() {
        return cost;
    }

    public void evolve(int multiplier) {
        this.attack = this.attack*multiplier;
        this.hp = this.hp*multiplier;
        this.cost = this.cost*multiplier;
    }

    public static EntityType getEntityType(Entity s) { return EntityType.valueOf(s.getType().toString()); }
}
