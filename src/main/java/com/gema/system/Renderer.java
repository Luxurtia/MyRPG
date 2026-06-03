package com.gema.system;

import com.gema.entity.Player;
import com.gema.world.Camera;
import com.gema.world.TileMap;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;

public class Renderer {
    private final TileMap tileMap;
    private final Camera camera;

    public Renderer(TileMap tileMap, Camera camera) {
        this.tileMap = tileMap;
        this.camera = camera;
    }

    public void render(Graphics2D g2, Player player) {
        renderMap(g2);                  // 맵을 맨 아래에 그림
        renderPlayer(g2, player);       // 그 위에 플래이어를 그림
        // npc나 몬스터
        render(g2, player);             // 그위에 HUD(Head-Up Display => UI창)를 표시
    }

    private void renderMap(Graphics2D g2) {
        tileMap.render(g2, camera.getX(), camera.getY());       // 카메라 좌표를 넘겨서 보이는 부분만 그림
    }

    private void renderPlayer(Graphics2D g2, Player player) {
        int screenX = camera.toScreenX(player.worldX);      // 플레이어 월드 좌표를 스크린 좌표로 변환
        int screenY = camera.toScreenY(player.worldY);      // ㄴ 카메라가 움직여도 항상 플레이어는 화면중앙
        player.render(g2, screenX, screenY);
    }

    private void renderHUD(Graphics2D g2, Player player) {
        // hp/mp 배경
        g2.setColor(new Color(0, 0, 0, 160));                               // 마지막은 투명도(0이 완전 투명)
        g2.fillRoundRect(14, 14, 164, 22, 6, 6);    // hp  // x, y, 너비, 높이, 둥근모서리x, y
        g2.fillRoundRect(14, 37, 164, 22, 6, 6);    // mp

        //hp바 비율만큼 채우기
        float ratiohp = (float)player.hp / player.maxHp;      // 현재 체력 / 최대체력 0.0~1.0
        g2.setColor(Color.RED);
        g2.fillRoundRect(16, 16, (int)(160 * ratiohp), 18, 6, 6);     // 비율 만큼 너비 조절

        //mp바 채우기
        float ratiomp = (float)player.mp / player.maxMp;
        g2.setColor(Color.blue);
        g2.fillRoundRect(16, 39, (int)(160 * ratiomp), 18, 6, 6);

        // hp/mp텍스트
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 13));
        g2.drawString("HP" + player.hp + " / " + player.maxHp, 20, 16);
        g2.drawString("MP" + player.mp + " / " + player.maxMp, 20, 39);
    }

}
