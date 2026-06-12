package com.gema.state;

import com.gema.Game;
import com.gema.entity.Player;
import com.gema.system.Action;
import com.gema.system.InputHandler;
import com.gema.ui.tab.MainTab;
import com.gema.ui.tab.StatusTab;

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

    // 메뉴와 메뉴 내부 내용 선택 분리
    private enum MenuMode {
        Tab_Select,     // 탭 메뉴 조작
        Tab_Content     // 탭 내용 조작
    }

    private MenuMode mode = MenuMode.Tab_Select;        // 기본은 탭 선택 모드


    // 스텟 찍기 관련
    private int selectedMenu = 0;

    // 각 탭들
    private final MainTab maintab;
    private final StatusTab statustab;


    public GameMenuState(InputHandler input, GameStateManager stateManager, Player player) {
        this.input        = input;          // 키 입력
        this.stateManager = stateManager;   // 씬 전환
        this.player       = player;         // 스텟 표시용

        maintab = new MainTab(player);
        statustab = new StatusTab(player, input);
    }

    public void setBackground(BufferedImage background) {
        this.background = background;       // 매뉴 열때마다 현재 화면으로 갱신
    }

    @Override
    public void update() {
        if (statustab.isPopupVisible()) {        // 팝업 떠있다면 팝업만 처리
            statustab.update();
            return;
        }

        if (mode == MenuMode.Tab_Select) {                                                 // 탭 선택 모드일때
            if (input.isJustPressed(Action.UI_LEFT)) {
                selectedMenu = (selectedMenu - 1 + menuarr.length) % menuarr.length;       // 왼쪽으로 이동
            }

            if (input.isJustPressed(Action.UI_RIGHT)) {
                selectedMenu = (selectedMenu + 1) % menuarr.length;                        // 오른쪽으로 이동
            }

            if(input.isJustPressed(Action.UI_SELECT)) {                                    // 탭선택시 진입 및 스텟 백업
                mode = MenuMode.Tab_Content;
                if(selectedMenu == 1) {
                    statustab.backup();
                }
            }

            if (input.isJustPressed(Action.UI_BACK)) {
                stateManager.changeState("PLAY");                                          // x키로 메뉴 닫기
            }
        } else {                                                                           // 탭 내용 조작 모드일때 back 입력시
            if(input.isJustPressed(Action.UI_BACK)) {
                mode = MenuMode.Tab_Select;                                                // 탭 선택으로 복귀
                resetCurrentTab();                                                         // 미확정 스텟 초기화
                return;
            }
        }
            if (selectedMenu == 1) {                                                        // 스테이터스 탭일경우에만 update
                statustab.update();
            }
    }

    private void resetCurrentTab() {                                                        // 스테이터스 탭 초기화
        if(selectedMenu == 1) {
            statustab.reset();
        }
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

        // 메뉴 그리기
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

    private void drawCenteredString(Graphics2D g2, String text, int x, int y, int width) {
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (width - fm.stringWidth(text)) / 2;

        g2.drawString(text, textX, y);
    }

    private void renderTabContent(Graphics2D g2, int x, int y, int w, int h) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 16));
        switch(selectedMenu) {
            case 0 -> maintab.render(g2, x + 20, y + 30);             // 메인
            case 1 -> statustab.render(g2, x + 20, y + 30);           // 스테이터스 분배창
            case 2 -> renderSkill(g2, x + 20, y + 30);            // 스킬
            case 3 -> renderEquip(g2, x + 20, y + 30);            // 장비
            case 4 -> renderIncentory(g2, x + 20, y + 30);        // 인벤토리
            case 5 -> renderSave(g2, x + 20, y + 30);             // 저장
            case 6 -> renderLoad(g2, x + 20, y + 30);             // 불러오기
            case 7 -> renderSetting(g2, x + 20, y + 30);          // 설정
            case 8 -> renderEnd(g2, x + 20, y + 30);              // 종료
        }
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



