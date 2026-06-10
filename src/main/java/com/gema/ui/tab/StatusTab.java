package com.gema.ui.tab;

import com.gema.entity.Player;
import com.gema.system.Action;
import com.gema.system.InputHandler;
import com.gema.ui.popup.ConfirmPopup;

import java.awt.*;

public class StatusTab {
    private final Player player;
    private final InputHandler input;
    private final ConfirmPopup confirm;
    
    private int selectedStat = 0;                       // 처음 선택될 스텟의 인덱스
    private boolean statConfirmed = false;              // 스텟의 확정여부
    
    private static final String[] STAT_NAMES = {
            "STR",
            "DEX", 
            "INT", 
            "LUK", 
            "VIT"
    };
    
    public StatusTab(Player player, InputHandler input) {
        this.player = player;
        this.input = input;
        this.confirm = new ConfirmPopup(input);
    }
    
    public void update() {
        if(confirm.isVisible()) {           // 팝업이 떠있다면 팝업외 입력 제한
            confirm.update();               
            return;
        }
        
        if(statConfirmed) {                 // 확정후 입력 제한
            return;
        }
        
        if (input.isJustPressed(Action.UI_UP)) {
            selectedStat = (selectedStat - 1 + 5) % 5;                                 // 위로 이동
        }

        if (input.isJustPressed(Action.UI_DOWN)) {
            selectedStat = (selectedStat + 1) % 5;                                     // 아래로 이동
        }

        if (input.isJustPressed(Action.UI_RIGHT) && player.statPoint > 0) {
            addStat(selectedStat);                                                    // 포인트 있을때만 증가가능
        }

        if (input.isJustPressed(Action.UI_LEFT)) {
            removeStat(selectedStat);                                                 // - 버튼
        }

        if (input.isJustPressed(Action.UI_SELECT)) {                                  // z키로 팝업 열기
            confirm.show(
                    "스탯을 분배하시겠습니까?",
                    () -> statConfirmed = true,                                       // 예 선택시 확정
                    () -> {}                                                          // 아니오 선택시 아무것도 안함
            );
        }
    }

    public void render(Graphics2D g2, int x, int y) {
        g2.setColor(Color.WHITE);
        g2.drawString("미분배 포인트 : " + player.statPoint, x, y);       // 남은 분배 포인트 표시

        drawStatRow(g2, x, y + 40, "STR", player.STR, 0);   // 0은 인덱스
        drawStatRow(g2, x, y + 80, "DEX", player.DEX, 1);
        drawStatRow(g2, x, y + 120, "INT", player.INT, 2);
        drawStatRow(g2, x, y + 160, "LUK", player.LUK, 3);
        drawStatRow(g2, x, y + 200, "VIT", player.VIT, 4);

        if (statConfirmed) {
            g2.setColor(new Color(150, 150, 150));                // 확정후 회색으로 표시
            g2.drawString("스텟이 확정되었습니다!", x, y + 220);
        } else {
            g2.setColor(selectedStat == 5 ? Color.YELLOW : Color.WHITE);   // 확정버튼 선택시 노란색
            g2.drawString("[분배하기]", x, y + 220);
        }

        confirm.render(g2);                // 팝업 렌더링
    }

    private void drawStatRow(Graphics2D g2, int x, int y, String statName, int statValue, int index) {
        g2.setColor(index == selectedStat ? Color.YELLOW : Color.white);
        g2.drawString(statName, x, y);
        g2.drawString("<  " + statValue + "  >", x + 100, y);
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

    public boolean isPopupVisible() {           // 팝업 표시 여부 확인
        return confirm.isVisible();
    }
}
