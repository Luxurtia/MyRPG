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

/*      // 기본 스탯 설정
        speed   = 4;
        maxHp   = 200;
        hp      = 200;
        maxMp   = 200;
        mp      = 200;
        att     = 10;
        Matt    = 15;
        def     = 5;
        Mdef    = 15;
*/
        speed   = 4;
        statPoint = 5;
        STR = 5;
        DEX = 5;
        INT = 5;
        LUK = 5;
        VIT = 5;

        recalcStat();
    }

    @Override
    public void update() {
        handleMovement();
    }

    @Override
    public void render(Graphics2D g2, int screenX, int screenY) {
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

    // 스텟 계산식
    public void recalcStat() {
        att = STR * 2 + DEX;        // 물공
        def = STR + VIT * 2;        // 물방
        Matt = INT * 2 + LUK;       // 마공
        Mdef = VIT * 2;             // 마방
        maxHp = VIT * 50 + STR;      // 최대 체력
        maxMp = VIT * 20 + INT;      // 최대 마나

        hp = maxHp;
        mp = maxMp;
    }
}
