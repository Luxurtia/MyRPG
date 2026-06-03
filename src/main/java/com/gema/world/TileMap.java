package com.gema.world;

import java.awt.Graphics2D;
import java.awt.Color;

import com.gema.Game;

public class TileMap {
    public static final int MAP_COLS = 30;
    public static final int MAP_ROWS = 24;
    public static final int TILE_SIZE = 48;

    private final int[][] mapData;      // 맵 데이터 => 타일 id 저장

    // 타일 아이디 별 색
    private static final Color[] TILE_COLORS = {
            new Color(80, 160,80),      // 0 => 풀
            new Color(100, 100, 100),   // 1 => 돌   충돌
            new Color(40, 80, 200),     // 2 => 물   충돌
    };

    // 충돌 여부
    private static final boolean[] IS_SOLID = {
            false,  // 0
            true,   // 1
            true    // 2
    };

    public TileMap() {
        mapData = new int[MAP_ROWS][MAP_COLS];
        loadDefaultMap();
    }

    private void loadDefaultMap() {
        for(int row = 0; row < MAP_ROWS; row++) {
            for(int col = 0; col < MAP_COLS; col++) {
                if(row == 0 || row == MAP_ROWS - 1 || col == 0 || col == MAP_COLS - 1) {
                    mapData[row][col] = 1;  // 외곽은 돌
                } else {
                    mapData[row][col] = 0;  // 나머지는 풀
                }
            }
        }
    }

    public void render(Graphics2D g2, int camX, int camY) {     // 플레이어 카메라 위치
        for(int row = 0; row < MAP_ROWS; row++) {
            for(int col = 0; col < MAP_COLS; col++) {
                int tileId = mapData[row][col];
                int screenX = col * TILE_SIZE - camX;       // 카메라 오프셋
                int screenY = row * TILE_SIZE - camY;

                Color c = (tileId < TILE_COLORS.length) ? TILE_COLORS[tileId] : Color.MAGENTA;
                g2.setColor(c);
                g2.fillRect(screenX, screenY, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    public boolean isSolidAt(int worldPixelX, int worldPixelY) {
        int col = worldPixelX / TILE_SIZE;
        int row = worldPixelY / TILE_SIZE;

        if(col < 0 || col >= MAP_COLS || row < 0 || row >= MAP_ROWS) {      // 맵 밖으로 나갈경우 col row가 음수처리되서 맵뚫을 막음
            return true;
        }

        int tileId = mapData[row][col];

        return (tileId < IS_SOLID.length) && IS_SOLID[tileId];
    }

    public int getWidthPixels() {       // 맵 전체 너비
        return MAP_COLS * TILE_SIZE;
    }

    public int getHeightPixels() {      // 맵 전체 높이
        return MAP_ROWS * TILE_SIZE;
    }
}
