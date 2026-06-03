package com.gema.entity;

import com.gema.system.Action;
import com.gema.system.InputHandler;
import com.gema.world.TileMap;

import java.awt.Color;
import java.awt.Graphics2D;


public class Player extends Entity{
    private final InputHandler input;       // 키 입력 받아서 이동에 사용
    private final TileMap tileMap;          // 충돌 판정용

    public Player(InputHandler input, TileMap tileMap) {
        this.input = input;
        this.tileMap = tileMap;

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
        int dx = 0;
        int dy = 0;

        if(input.isHeld(Action.MOVE_UP)) {
            dy -= speed;
            direction = Direction.UP;
        }

        if(input.isHeld(Action.MOVE_DOWN)) {
            dy += speed;
            direction = Direction.DOWN;
        }

        if(input.isHeld(Action.MOVE_LEFT)) {
            dx -= speed;
            direction = Direction.LEFT;
        }

        if(input.isHeld(Action.MOVE_RIGHT)) {
            dx += speed;
            direction = Direction.RIGHT;
        }

        int left        = worldX + dx;                          // 이동후 왼쪽 x
        int right       = worldX + dx + TileMap.TILE_SIZE - 1;  // 이동후 오른쪽 x
        int top         = worldY + dy;                          // 이동후 위쪽 y
        int bot         = worldY + dy + TileMap.TILE_SIZE - 1;  // 이동후 아래쪽 y


        // 충돌 체크
        boolean solid = tileMap.isSolidAt(left, top) ||
                        tileMap.isSolidAt(right, top) ||
                        tileMap.isSolidAt(left, bot) ||
                        tileMap.isSolidAt(left, bot);

        if(!solid) {
            // 충돌 체크 후 이동
            worldX += dx;
            worldY += dy;
        }
    }
}
