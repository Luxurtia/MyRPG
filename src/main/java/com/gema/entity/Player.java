package com.gema.entity;

import com.gema.system.Action;
import com.gema.system.InputHandler;

import java.awt.Color;
import java.awt.Graphics2D;


public class Player extends Entity{
    private final InputHandler input;       // 키 입력 받아서 이동에 사용

    public Player(InputHandler input) {
        this.input = input;

        // 기본 스탯 설정
        speed   = 4;
        maxHp   = 200;
        hp      = 200;
        maxMp   = 200;
        mp      = 200;
        att     = 10;
        Matt    = 15;
        def     = 5;
        Mdef    = 15;
    }

    @Override
    public void update() {
        handleMovement();
    }

    @Override
    public void render(Graphics2D g2, int screenX, int screenY) {
        // 임시 placeholer

        g2.setColor(Color.BLUE);
        g2.fillRect(screenX, screenY, 48, 48);
    }

    private void handleMovement() {
        if(input.isHeld(Action.MOVE_UP)) {
            worldY -= speed;
            direction = Direction.UP;
        }

        if(input.isHeld(Action.MOVE_DOWN)) {
            worldY += speed;
            direction = Direction.DOWN;
        }

        if(input.isHeld(Action.MOVE_LEFT)) {
            worldY -= speed;
            direction = Direction.LEFT;
        }

        if(input.isHeld(Action.MOVE_RIGHT)) {
            worldY += speed;
            direction = Direction.RIGHT;
        }
    }
}
