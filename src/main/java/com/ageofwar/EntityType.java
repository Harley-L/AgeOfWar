package com.ageofwar;

public enum EntityType {
    SOLDIER(50,25,100),
    RANGE(75,25,100),
    TANK(50,50,200),
    SUPER(50,60,200),
    BULLET(150, RANGE.attack,-1),
    BASE(0,0,1000),
    ENEMY_SOLDIER(50,25,100),
    ENEMY_RANGE(75,25,100),
    ENEMY_TANK(50,50,200),
    ENEMY_SUPER(50,60,200),
    ENEMY_BULLET(150, RANGE.attack,-1),
    ENEMY_BASE(0,0,1000);


    private int speed;
    private int attack;
    private int hp;

    EntityType(int speed, int attack, int hp) {
        this.speed = speed;
        this.attack = attack;
        this.hp = hp;
    }

    public int getSpeed() {
        return speed;
    }

    public int getAttack() {
        return attack;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getHp() {
        return hp;
    }
}
