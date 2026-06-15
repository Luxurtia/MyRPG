package com.gema.ui.tab;

import com.gema.entity.Player;
import com.gema.system.Action;
import com.gema.system.InputHandler;
import com.gema.ui.popup.ConfirmPopup;
import com.gema.ui.popup.CheckPopup;

import java.awt.Graphics2D;
import java.awt.Color;

public class StatusTab {
    private final Player player;
    private final InputHandler input;
    private final ConfirmPopup confirm;
    private final CheckPopup CPopup;
    
    private int selectedStat = 0;                       // 처음 선택될 스텟의 인덱스

    // 스테이터스 탭 진입시 현재 스텟 백업     // 현재 디버그를 위해 public 이지만 추후 private로 변경 해야됨
    public int backupSTR;
    public int backupDEX;
    public int backupINT;
    public int backupLUK;
    public int backupVIT;
    public int backupStatPoint;

     public void backup() {
         backupSTR        = player.STR;
         backupDEX        = player.DEX;
         backupINT        = player.INT;
         backupLUK        = player.LUK;
         backupVIT        = player.VIT;
         backupStatPoint  = player.statPoint;

     }

    public void reset() {
        player.STR = backupSTR;
        player.DEX = backupDEX;
        player.INT = backupINT;
        player.LUK = backupLUK;
        player.VIT = backupVIT;
        player.statPoint = backupStatPoint;

        player.recalcStat();
        selectedStat = 0;
    }

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
        this.CPopup = new CheckPopup(input);
    }
    
    public void update() {
        if(confirm.isVisible()) {           // 팝업이 떠있다면 팝업외 입력 제한
            confirm.update();               
            return;
        }

        if(CPopup.CisVisible()) {
            CPopup.update();
            return;
        }
        
        if (input.isJustPressed(Action.UI_UP)) {
            selectedStat = (selectedStat - 1 + 6) % 6;                                 // 위로 이동
        }

        if (input.isJustPressed(Action.UI_DOWN)) {
            selectedStat = (selectedStat + 1) % 6;                                     // 아래로 이동
        }

        if (input.isJustPressed(Action.UI_RIGHT) && player.statPoint > 0 && selectedStat != 5) {
            addStat(selectedStat);                                                    // 포인트 있을때만 증가가능
        }

        if (input.isJustPressed(Action.UI_LEFT) && selectedStat != 5) {
            removeStat(selectedStat);                                                 //
        }

        if (selectedStat == 5 && input.isJustPressed(Action.UI_SELECT)) {             // z키로 팝업 열기\\
            if(player.statPoint < backupStatPoint) {
                confirm.show(
                        "스탯을 분배하시겠습니까?",
                        () -> {
                            backupStatPoint = player.statPoint;
                            backupSTR = player.STR;
                            backupDEX = player.DEX;
                            backupINT = player.INT;
                            backupLUK = player.LUK;
                            backupVIT = player.VIT;
                        },                         // 예 선택시 확정 및 포인트 갱신
                        () -> {}                                                          // 아니오 선택시 아무것도 안함
                );
            } else {
                CPopup.show(
                        "변경사항이 없습니다."
                );
            }
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

        g2.setColor(selectedStat == 5 ? Color.YELLOW : Color.WHITE);   // 확정버튼 / 선택시 노란색
        g2.drawString("[분배하기]", x, y + 250);

        confirm.render(g2);                // 팝업 렌더링
        CPopup.render(g2);
    }

    private void drawStatRow(Graphics2D g2, int x, int y, String statName, int statValue, int index) {
        g2.setColor(index == selectedStat ? Color.YELLOW : Color.white);
        g2.drawString(statName, x, y);
        g2.drawString("<  " + statValue + "  >", x + 100, y);
    }

    private void addStat(int index) {
        switch (index) {
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
        switch(index) {                     // 스텟 감소 조정시 기존 스텟 밑으로 감소 불가
            case 0 -> {
                if(player.STR <= backupSTR) {
                    CPopup.show(
                            "현재 스텟 미만으로 스텟을 내릴 수 없습니다."
                    );
                } else {
                    player.STR--;
                    player.statPoint++;
                }
            }
            case 1 -> {
                if(player.DEX <= backupDEX) {
                    CPopup.show(
                            "현재 스텟 미만으로 스텟을 내릴 수 없습니다."
                    );
                } else {
                    player.DEX--;
                    player.statPoint++;
                }
            }
            case 2 -> {
                if(player.INT <= backupINT) {
                    CPopup.show(
                            "현재 스텟 미만으로 스텟을 내릴 수 없습니다."
                    );
                } else {
                    player.INT--;
                    player.statPoint++;
                }
            }
            case 3 -> {
                if(player.LUK <= backupLUK) {
                    CPopup.show(
                            "현재 스텟 미만으로 스텟을 내릴 수 없습니다."
                    );
                } else {
                    player.LUK--;
                    player.statPoint++;
                }
            }
            case 4 -> {
                if(player.VIT <= backupVIT) {
                    CPopup.show(
                            "현재 스텟 미만으로 스텟을 내릴 수 없습니다."
                    );
                } else {
                    player.VIT--;
                    player.statPoint++;
                }
            }
        }
        player.recalcStat();
    }

    public boolean isPopupVisible() {           // 팝업 표시 여부 확인
        return confirm.isVisible() || CPopup.CisVisible();
    }
}
