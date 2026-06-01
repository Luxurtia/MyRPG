package com.gema.entity.npc;

import com.gema.entity.Entity;

import java.awt.*;

public abstract class NPC extends Entity {
    protected String name;              // npc 이름
    protected String[] dialogue;        // 대화 내용 배열
    protected int dialogueIndex = 0;    // 현재 대화 인덱스

    public NPC(String name) {
        this.name = name;

        // npc 기본 스탯
        speed = 2;
        MaxHP = 200;
        hp = 1;
        MaxMP = 200;
        mp = 1;
        att = 1;
        Matt = 1000;
        def = 1;
        Mdef = 1000;
    }

    @Override
    public void update() {
        // 추후 npc이동 구현
    }

    @Override
    public void render(Graphics2D g2, int screenX, int screenY) {
        // 임시 holer 추후 스프라이트로 교체

        g2.setColor(Color.YELLOW);
        g2.fillRect(screenX, screenY, 48, 48);      // 노란 사각형으로 표시
    }

    public String talk() {
        if(dialogue == null || dialogue.length == 0) {
            return "";      // 대화가 없으면 빈 문자열
        }

        String line = dialogue[dialogueIndex];
        dialogueIndex = (dialogueIndex + 1) % dialogue.length;      // 다음 대화로 넘어감, 끝나면 처음으로
        return line;
    }

    public String getName() {       // getter
        return name;
    }
}
