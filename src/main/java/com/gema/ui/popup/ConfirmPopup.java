package com.gema.ui.popup;

import com.gema.Game;
import com.gema.system.Action;
import com.gema.system.InputHandler;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class ConfirmPopup {
    private final InputHandler input;

    private boolean visible = false;        // 팝업 표시 여부
    private int selectedOption = 0;         // 0 -> 예   1 -> 아니오
    private String message;                 // 팝업에 표시할 메세지
    private Runnable onConfirm;             // 예 선택시 실행할 동작
    private Runnable onCancel;              // 아니오 선택시 실행항 동작

    private static final int POPUP_W = 300;
    private static final int POPUP_H = 120;
    private static final int POPUP_X = (Game.SCREEN_WIDTH - POPUP_W) / 2;      // 화면 중앙 x
    private static final int POPUP_Y = (Game.SCREEN_HEIGHT - POPUP_H) / 2;     // 화면 중앙 y\

    public ConfirmPopup(InputHandler input) {
        this.input = input;
    }

    public void show(String message, Runnable onConfirm, Runnable onCancel) {
        this.message        = message;      // 표시할 메세지 설정
        this.onConfirm      = onConfirm;    // 예 선택시 실행할 동작
        this.onCancel       = onCancel;     // 아니오 선택시 실행할 동작
        this.selectedOption = 1;            // 기본은 아니오
        this.visible        = true;         // 팝업 표시
    }

    public void hide() {
        visible = false;        // 팝업 숨기기
    }

    public boolean isVisible() {
        return visible;         // 팝업 표시 여부 반환
    }

    public void update() {
        if(!visible) {          // 팝업이 false이면 처리 하지 않음
            return;
        }

        if(input.isJustPressed(Action.UI_LEFT) || input.isJustPressed(Action.UI_RIGHT)) {
            selectedOption = (selectedOption + 1) % 2;      // 예 아니오 전환
        }

        if(input.isJustPressed(Action.UI_SELECT)) {
            if(selectedOption == 0) {
                if(onConfirm != null) {
                    onConfirm.run();      // 예 선택시 onConfirm 실행
                }
            } else {
                if(onCancel != null) {
                    onCancel.run();
                }
            }
            hide();                      // 선택 후 팝업 닫기
        }

        if(input.isJustPressed(Action.UI_BACK)) {
            if(onCancel != null) {
                onCancel.run();
            }
            hide();
        }
    }

    // 팝업 그리기
    public void render(Graphics2D g2) {
        if(!visible) {          // 팝업이 false라면 그리지 않음
            return;
        }

        g2.setColor(new Color(20, 20, 40));
        g2.fillRoundRect(POPUP_X, POPUP_Y, POPUP_W, POPUP_H, 12, 12);       // 팝업 배경
        g2.setColor(new Color(100, 100, 180));
        g2.drawRoundRect(POPUP_X, POPUP_Y, POPUP_W, POPUP_H, 12, 12);       // 팝업 테두리

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        drawCenteredString(g2, message, POPUP_X, POPUP_Y + 40, POPUP_W);                    // 메세지 표시

        g2.setColor(selectedOption == 0 ? Color.YELLOW : Color.WHITE);                      // 선택시 노란색
        drawCenteredString(g2, "[ 예 ]", POPUP_X, POPUP_Y + 85, POPUP_W / 2);

        g2.setColor(selectedOption == 1 ? Color.YELLOW : Color.WHITE);
        drawCenteredString(g2, "[ 아니오 ]", POPUP_X + POPUP_W / 2, POPUP_Y + 85, POPUP_W / 2);
    }

    private void drawCenteredString(Graphics2D g2, String text, int x, int y, int w) {
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (w - fm.stringWidth(text)) / 2;
        g2.drawString(text, textX, y);
    }
}
