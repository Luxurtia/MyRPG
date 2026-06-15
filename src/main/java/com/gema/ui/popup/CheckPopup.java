package com.gema.ui.popup;

import com.gema.Game;
import com.gema.system.Action;
import com.gema.system.InputHandler;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class CheckPopup {
    private final InputHandler input;

    private boolean Cvisible = false;          // C = Check
    private String checkMessage;

    private static final int POPUP_W = 400;
    private static final int POPUP_H = 120;
    private static final int POPUP_X = (Game.SCREEN_WIDTH - POPUP_W) / 2;
    private static final int POPUP_Y = (Game.SCREEN_HEIGHT - POPUP_H) / 2;

    public CheckPopup(InputHandler input) {
        this.input = input;
    }

    public void show(String checkMessage) {
        this.checkMessage = checkMessage;
        this.Cvisible = true;
    }

    public void Chide() {
        Cvisible = false;
    }

    public boolean CisVisible() {      // 팝업 표시 여부 반환
        return Cvisible;
    }

    public void update() {
        if(!Cvisible) {
            return;
        }

        if(input.isJustPressed(Action.UI_SELECT) ||
                input.isJustPressed(Action.UI_BACK)) {
            Chide();
        }
    }

    public void render(Graphics2D g2) {
        if(!Cvisible) {
            return;
        }

        g2.setColor(new Color(20, 20, 40));
        g2.fillRoundRect(POPUP_X, POPUP_Y, POPUP_W, POPUP_H, 12, 12);       // 팝업 배경
        g2.setColor(new Color(100, 100, 180));
        g2.drawRoundRect(POPUP_X, POPUP_Y, POPUP_W, POPUP_H, 12, 12);       // 팝업 테두리

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        drawCenteredString(g2, checkMessage, POPUP_X, POPUP_Y + 40, POPUP_W);                    // 메세지 표시

        g2.setColor(Color.YELLOW);
        drawCenteredString(g2, "[ 닫기 ]", POPUP_X + 100, POPUP_Y + 85, POPUP_W / 2);


    }

    private void drawCenteredString(Graphics2D g2, String text, int x, int y, int w) {
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (w - fm.stringWidth(text)) / 2;
        g2.drawString(text, textX, y);
    }
}
