package com.gema.ui.tab;

import com.gema.entity.Player;

import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Color;

public class MainTab {
    private final Player player;

    public MainTab(Player player) {
        this.player = player;
    }

    public void render(Graphics2D g2, int x, int y) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 16));

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
}
