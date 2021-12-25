package com.troops;

import java.util.Objects;

public class Character {
    private String name;
    private int hp;
    private int attack_dmg;
    private int attack_speed;
    private int speed;

    public Character(String name, int hp, int attack_dmg, int attack_speed, int speed) {
        this.name = name;
        this.hp = hp;
        this.attack_dmg = attack_dmg;
        this.attack_speed = attack_speed;
        this.speed = speed;
    }

    // Accessors
    public int getAttack_dmg() {
        return attack_dmg;
    }
    public int getHp() {
        return hp;
    }
    public int getAttack_speed() {
        return attack_speed;
    }
    public int getSpeed() {
        return speed;
    }
    public String getName() {
        return name;
    }

    // Mutators
    public void setAttack_dmg(int attack_dmg) {
        this.attack_dmg = attack_dmg;
    }
    public void setAttack_speed(int attack_speed) {
        this.attack_speed = attack_speed;
    }
    public void setHp(int hp) {
        this.hp = hp;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Character character = (Character) o;
//        return hp == character.hp && attack_dmg == character.attack_dmg && attack_speed == character.attack_speed && speed == character.speed && Objects.equals(name, character.name);
//    }

    @Override
    public int hashCode() {
        return Objects.hash(name, hp, attack_dmg, attack_speed, speed);
    }

    public <T extends Character> void attack(T obj) {
        obj.setHp(obj.getHp()-this.attack_dmg);
    }

    public void print() {
        System.out.print("\n");
        System.out.print("Name: ");
        System.out.print(this.name);
        System.out.print("\nHP: ");
        System.out.print(this.hp);
        System.out.print("\nAttack DMG: ");
        System.out.print(this.attack_dmg);
        System.out.print("\nAttack speed: ");
        System.out.print(this.attack_speed);
        System.out.print("\nSpeed: ");
        System.out.print(this.speed);
        System.out.print("\n");
    }
}
