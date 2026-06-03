package com.gema.entity;

import java.awt.Graphics2D;
/*
    interface
        규칙 정의
        여러개 상속 가능
        변수 사실상 없음
        implements

    abstract
        공통기능으로 묶기
        1개만 가능
        변수 가능
        extend
        자식 클래스가 직접 구현 해야됨
        ex) 플레이어, 몹 npc => 체력 위치 이동속도 등 공통 데이터 존재
*/
public abstract class Entity {      // 게임 내 존재 하는 객체
    public int worldX, worldY;      // 월드 좌표 (픽셀)
    public int speed;               // 이동속도     매 프레임 당 이동속도    EX) speed = 4 => 매프레임당 4픽셀 이동 한칸에 12프레임

    public int maxHp;               // 최대 체력
    public int hp;                  // 현재 체력
    public int maxMp;               // 최대 마나
    public int mp;                  // 현재 마나
    public int att;                 // 공격력
    public int Matt;                // 마공
    public int def;                 // 방어력
    public int Mdef;                // 마방

    public enum Direction {     // 방향 열거
        UP,
        DOWN,
        LEFT,
        RIGHT,
        NONE
    }

    public Direction direction = Direction.NONE;        // 현재 방향

    public abstract void update();      // 매 프레임 로직 업뎃

    public abstract void render(Graphics2D g2, int ScreenX, int ScreenY);       // 매 프레임 렌더링

    public void setTilePosition(int col, int row) {     // 타일 좌표를 픽셀 좌표로 변환
        worldX = col * 48;
        worldY = row * 48;
    }

    public boolean isAlive() {      // 체력이 0 보다 크면 살아 있음
        return hp > 0;
    }
}
