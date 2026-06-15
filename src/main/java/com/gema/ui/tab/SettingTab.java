package com.gema.ui.tab;

import com.gema.Game;
import com.gema.system.Action;
import com.gema.system.InputHandler;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;

public class SettingTab {
    private final InputHandler input;
    private final GameSettings settings;

    public SettingTab(InputHandler input) {
        this.input = input;
        this.settings = Game.settings;      // Game에서 전역 설정 가져옴
    }

    // 설정 항목 목록
    private final String[] settingItems = {
            "대쉬 모드",
            "마스터 볼륨",
            "배경음악",
            "효과음",
            "대사음량",
            "키보드 설정"
    };

    // 설정 항목 인덱스
    private int selectedIndex = 0;

    public void update() {
        if(input.isJustPressed(Action.UI_UP)) {
            selectedIndex = (selectedIndex - 1 + settingItems.length) % settingItems.length;
        }

        if(input.isJustPressed(Action.UI_DOWN)) {
            selectedIndex = (selectedIndex + 1) % settingItems.length;
        }

        switch(selectedIndex) {
            case 0 -> {                                                 // 대쉬모드
                if(input.isJustPressed((Action.UI_SELECT))) {
                    settings.dashMode = !settings.dashMode;         // 선택키로 토글
                }
            }
            case 1 -> handleVolume(0);                                  // 마스터 볼륨
            case 2 -> handleVolume(1);                                  // 배경음
            case 3 -> handleVolume(2);                                  // 효과음
            case 4 -> handleVolume(3);                                  // 대사음량
            case 5 -> {                                                 // 키보드 설정
                // 키보드 설정
            }
        }
    }

    private void handleVolume(int volume) {
        if(input.isJustPressed((Action.UI_RIGHT))) {                    // 볼륨 증가
            settings.increaseVolume(type);
        }

        if(input.isJustPressed(Action.UI_LEFT)) {
            settings.decreaseVolume(type);                              // 볼륨 감소
        }
    }

    public void render(Graphics2D g2, int x, int y) {
        g2.setFont(new Font("SansSerif", Font.PLAIN, 16));

        // 대쉬
        g2.setColor(selectedIndex == 0 ? Color.YELLOW : Color.WHITE);
        g2.drawString("대쉬모드", x, y);
        g2.drawString(settings.dashMode ? "[ ON ]" : "[ OFF ]", x + 200, y);    // 온오프 표시

        // 볼륨 항목들
        drawVolumeRow(g2, x, y + 50,  "마스터 볼륨", settings.masterVolume, 1);
        drawVolumeRow(g2, x, y + 100, "배경음 볼륨", settings.bgmVolume,    2);
        drawVolumeRow(g2, x, y + 150, "효과음 볼륨", settings.sfcVolume,    3);
        drawVolumeRow(g2, x, y + 200, "대사음량 볼륨", settings.voiceVolume, 4);

        // 키보드 설정
        g2.setColor(selectedIndex == 5 ? Color.YELLOW : Color.WHITE);
        g2.drawString("키보드 설정", x, y + 250);
        g2.drawString(">", x + 200, y + 250);                           // 진입 가능 표시
    }

    private void drawVolumeRow(Graphics2D g2, int x, int y, String label, int volume, int index) {
        g2.setColor(selectedIndex == index ? Color.YELLOW : Color.WHITE);
        g2.drawString(label, x, y);

        // 볼륨 바 그리기
        int barX = x + 200;
        int barW = 200;
        int barH = 14;
        int fillW = (int)(barW * (volume / 100.0));             // 볼륨 비율만큼 채움

        g2.setColor(new Color(60, 60, 60));
        g2.fillRoundRect(barX, y - 12, barW, barH, 4, 4);       // 바 배경
        g2.setColor(selectedIndex == index ? new Color(100, 200, 100) : new Color(80, 150, 80));
        g2.fillRoundRect(barX, y - 12, fillW, barH, 4, 4);      // 채워진 부분

        g2.setColor(selectedIndex == index ? Color.YELLOW : Color.WHITE);
        g2.drawString(volume + "%", barX + barW + 10, y);                    // 볼륨 수치 표시
    }
}
