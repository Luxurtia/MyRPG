package com.gema.world;

import com.gema.Game;
import com.gema.entity.Player;

public class Camera {
    private int x, y;       // 카메라 왼쪽 위 모서리의 좌표

    private final Player player;
    private final TileMap tileMap;

    private final int maxX;
    private final int maxY;

    public Camera(Player player, TileMap tileMap) {
        this.player = player;
        this.tileMap = tileMap;

        maxX = tileMap.getWidthPixels() - Game.SCREEN_WIDTH;   // 맵 끝에서 화면 너비만큼을 뺀 값
        maxY = tileMap.getHeightPixels() - Game.SCREEN_HEIGHT;  // 맵 끝에서 화면 높이만큼을 뺀 값
    }

    public void update() {
        x = player.worldX - Game.SCREEN_WIDTH / 2;      // 플레이어를 화면 가로 중앙에
        y = player.worldY - Game.SCREEN_HEIGHT / 2;     // 플레이어를 화면 세로 중앙에

        x = Math.max(0, Math.min(x, maxX));             // 맵 왼, 오 경계 클램핑(제한)
        y = Math.max(0, Math.min(y, maxY));             // 맵 위, 아래 경계 클램핑(제한)
        // Math.min(x, maxX) => x, maxX중 더 작은값 찾기
        // Math.max(0, ...)  => 0, ... 중 더 큰것 찾기
        // 결과적으로 x는 0~maxX사이만 가능함
    }

    public int toScreenX(int worldX) {
        return worldX - x;      // 월드 좌표를 스크린 좌표로 변환
    }

    public int toScreenY(int worldY) {
        return worldY - y;      // 월드 좌표를 스크린 좌표로 변환
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}