package com.gema.state;

import com.gema.Game;
import com.gema.entity.Player;
import com.gema.system.Action;
import com.gema.system.InputHandler;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;

public class GameMenuState implements GameState {
    private final InputHandler      input;
    private final GameStateManager  stateManager;
    private final Player            player;
    private int selectedStat = 0;
    private boolean statConfirmed = false;      // 스텟 확정 여부

    private BufferedImage           background;         // 메뉴 열릴때 화면이 캡쳐되면 배경으로 쓰도록 함

    private final String[] menuarr = {     // 메뉴 목록
            "메인",
            "스테이터스",
            "스킬",
            "장비",
            "인벤토리",
            "저장",
            "불러오기",
            "설정",
            "종료"
    };

    private int selectedMenu = 0;

    public GameMenuState(InputHandler input, GameStateManager stateManager, Player player) {
        this.input        = input;          // 키 입력
        this.stateManager = stateManager;   // 씬 전환
        this.player       = player;         // 스텟 표시용
    }

    public void setBackground(BufferedImage background) {
        this.background = background;       // 매뉴 열때마다 현재 화면으로 갱신
    }

    @Override
    public void update() {
        if(input.isJustPressed(Action.UI_LEFT)) {
            selectedMenu = (selectedMenu - 1 + menuarr.length) % menuarr.length;       // 왼쪽으로 이동
        }

        if(input.isJustPressed(Action.UI_RIGHT)) {
            selectedMenu = (selectedMenu + 1) % menuarr.length;                        // 오른쪽으로 이동
        }

        if(input.isJustPressed(Action.UI_BACK)) {
            stateManager.changeState("PLAY");                                          // x키로 메뉴 닫기
        }

        if(selectedMenu == 1) {
            if(!statConfirmed) {        // 확정전에만 수정 가능
                if(input.isJustPressed(Action.UI_UP)) {
                    selectedStat = (selectedStat - 1 + 5) % 5;                                 // 위로 이동
                }

                if(input.isJustPressed(Action.UI_DOWN)) {
                    selectedStat = (selectedStat + 1) % 5;                                     // 아래로 이동
                }

                if(input.isJustPressed(Action.UI_RIGHT) && player.statPoint > 0) {
                    addStat(selectedStat);                                                    // + 버튼 포인트 있을때만
                }

                if(input.isJustPressed(Action.UI_LEFT)) {
                    removeStat(selectedStat);                                                 // - 버튼
                }

                if(input.isJustPressed(Action.UI_SELECT)) {                                   // x키로 확정
                    statConfirmed = true;
                }
            }
        }
    }

    private void addStat(int index) {
        switch(index) {
            case 0 -> player.STR++;
            case 1 -> player.DEX++;
            case 2 -> player.INT++;
            case 3 -> player.LUK++;
            case 4 -> player.VIT++;
        }
        player.statPoint--;
        player.recalcStat();
    }

    private void removeStat(int index) {
        if(statConfirmed) {
            return;
            
        }
        switch(index) {
            case 0 -> player.STR--;
            case 1 -> player.DEX--;
            case 2 -> player.INT--;
            case 3 -> player.LUK--;
            case 4 -> player.VIT--;
        }
        player.statPoint++;
        player.recalcStat();
    }

    @Override
    public void render(Graphics2D g2) {
        if(background != null) {
            g2.drawImage(background, 0, 0, null);       // 없으면 일단 넘어감
        }

        // 반투명 검정 오버레이
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));     // 50% 투명도
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));     // 투명도 원래대로

        // 매뉴 창 배경
        int menuX = 50;
        int menuY = 50;
        int menuW = Game.SCREEN_WIDTH - 100;
        int menuH = Game.SCREEN_HEIGHT - 100;
        g2.setColor(new Color(20, 20, 40));
        g2.fillRoundRect(menuX, menuY, menuW, menuH, 5, 5);
        g2.setColor(new Color(20, 20, 40));
        g2.drawRoundRect(menuX, menuY, menuW, menuH, 16, 16);

        // 매뉴 그리기
        int tabW = menuW / menuarr.length;

        for(int i = 0; i < menuarr.length; i++) {
            int tabX = menuX + i * tabW;

            if(i == selectedMenu) {
                g2.setColor(new Color(60, 60, 120));        // 선택된 탭 배경
                g2.fillRoundRect(tabX, menuY, tabW, 40, 8, 8);
                g2.setColor(Color.YELLOW);                          // 선택된 탭은 노란색으로
            } else {
                g2.setColor(new Color(60, 60, 120));
            }
            g2.setFont(new Font("SansSerif", Font.BOLD, 16));
            drawCenteredString(g2, menuarr[i], tabX, menuY + 26, tabW);
        }
        // 탭 내용 그리기
        renderTabContent(g2, menuX, menuY + 50, menuW, menuH);
    }

    private void renderTabContent(Graphics2D g2, int x, int y, int w, int h) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 16));
        switch(selectedMenu) {
            case 0 -> renderMain(g2, x + 20, y + 30);             // 현재 상태
            case 1 -> renderStatus(g2, x + 20, y + 30);           // 스테이터스 분배창
            case 2 -> renderSkill(g2, x + 20, y + 30);            // 스킬
            case 3 -> renderEquip(g2, x + 20, y + 30);            // 장비
            case 4 -> renderIncentory(g2, x + 20, y + 30);        // 인벤토리
            case 5 -> renderSave(g2, x + 20, y + 30);             // 저장
            case 6 -> renderLoad(g2, x + 20, y + 30);             // 불러오기
            case 7 -> renderSetting(g2, x + 20, y + 30);          // 설정
            case 8 -> renderEnd(g2, x + 20, y + 30);              // 종료
        }
    }

    private void drawCenteredString(Graphics2D g2, String text, int x, int y, int width) {
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (width - fm.stringWidth(text)) / 2;

        g2.drawString(text, textX, y);
    }

    // 이 아래 각 tab 구현
    private void renderMain(Graphics2D g2, int x, int y) {
        g2.drawString("HP : " + player.hp + " / " + player.maxHp, x, y);
        g2.drawString("MP : " + player.mp + " / " + player.maxMp, x, y + 30);
        g2.drawString("공격력 : " + player.att, x, y + 60);
        g2.drawString("방어력 : " + player.def, x, y + 90);
        g2.drawString("마법 공격력 : " + player.Matt, x, y + 120);
        g2.drawString("마법 방어력 : " + player.Mdef, x, y + 150);

        g2.drawString("STR : " + player.STR, x + 150, y + 30);
        g2.drawString("DEX : " + player.DEX, x + 150, y + 60);
        g2.drawString("INT : " + player.INT, x + 150, y + 90);
        g2.drawString("LUK : " + player.LUK, x + 150, y + 120);
        g2.drawString("VIT : " + player.VIT, x + 150, y + 150);
    }

    private void renderStatus(Graphics2D g2, int x, int y) {
        g2.drawString("미분배 포인트 : " + player.statPoint, x ,y);       // 남은 분배 포인트 표시

        if(statConfirmed) {
            g2.setColor(new Color(150 ,150, 150));                // 확정후 회색으로 표시
            g2.drawString("스텟이 확정되었습니다!", x, y + 220);
        } else {
            g2.setColor(Color.white);
            g2.drawString("Z : 확정", x, y + 220);
        }

        drawStatRow(g2, x, y + 40, "STR", player.STR, 0);   // 0은 인덱스
        drawStatRow(g2, x, y + 80, "DEX", player.DEX, 1);
        drawStatRow(g2, x, y + 120, "INT", player.INT, 2);
        drawStatRow(g2, x, y + 160, "LUK", player.LUK, 3);
        drawStatRow(g2, x, y + 200, "VIT", player.VIT, 4);

    }

    private void drawStatRow(Graphics2D g2, int x, int y, String statName, int statValue, int index) {
        g2.setColor(index == selectedStat ? Color.YELLOW : Color.white);
        g2.drawString(statName + " : " + statValue, x, y);
        g2.drawString("[ - ]", x + 200, y);
        g2.drawString("[ + ]", x + 260, y);
    }

    private void renderSkill(Graphics2D g2, int x, int y) {
        g2.drawString("스킬 구현 예정", x ,y);
    }

    private void renderEquip(Graphics2D g2, int x, int y) {
        g2.drawString("장비 착용 구현 예정", x ,y);
    }

    private void renderIncentory(Graphics2D g2, int x, int y) {
        g2.drawString("인벤 구현 예정", x ,y);
    }

    private void renderSave(Graphics2D g2, int x, int y) {
        g2.drawString("게임 저장 구현 예정", x ,y);
    }

    private void renderLoad(Graphics2D g2, int x, int y) {
        g2.drawString("게임 로드 구현 예정", x ,y);
    }

    private void renderSetting(Graphics2D g2, int x, int y) {
        g2.drawString("설정 구현 예정", x ,y);
    }

    private void renderEnd(Graphics2D g2, int x, int y) {
        g2.drawString("게임 종료 구현 예정", x ,y);
    }
}



