package com.gema.state;

import com.gema.Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class MenuState implements GameState {       // 메인 메뉴 화면 생성
    private final String[] menuItems = {"새 게임", "이어하기", "설정", "종료"};  // 메뉴 항목
    private int selectedIndex = 0;

    @Override
    public void update() {
        // 차후 키입력으로 selectedIndex 변경
    }

    @Override
    public void render(Graphics2D g2) {
        // 배경
        g2.setColor(new Color(12, 15, 30)); // 현재 남색
        g2.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT); // 화면 전체 채우기

        // 타이틀
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Serif", Font.BOLD, 56));
        drawCenteredString(g2, "MyRPG", Game.SCREEN_HEIGHT / 3);    // 화면 1/3지점에 타이틀

        // 메뉴항목
        g2.setFont(new Font("SansSerif", Font.PLAIN, 32));
        int startY = Game.SCREEN_HEIGHT / 2 + 20;       // 메뉴 시작 Y 좌표

        for(int i = 0; i < menuItems.length; i++) {
            if(i == selectedIndex) {
                g2.setColor(Color.YELLOW);      // 선택된 항목은 노란색으로
                drawCenteredString(g2, "> " + menuItems[i] + " <", startY + i * 52);
            } else {
                g2.setColor(Color.LIGHT_GRAY);  // 나머지는 회색
                drawCenteredString(g2, menuItems[i], startY + i * 52);
            }
        }
    }

    private void drawCenteredString(Graphics2D g2, String text, int y) {
        FontMetrics fm = g2.getFontMetrics();                       // 현재 폰트의 문자 크기 정보
        int x = (Game.SCREEN_WIDTH - fm.stringWidth(text)) / 2;     // 문자열 너비 기준 중앙 x 계산
        g2.drawString(text, x, y);
    }
}
