package com.gema.system;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

public class InputHandler extends KeyAdapter {
                // Key Adapter => java에서 제공하는 클래스
                // 안에 keyPressed, keyReleased, keyTyped 세가지 메소드가 비어서 정의 되어 있음

    private final Map<Integer, List<Action>> keyMap = new HashMap<>();    // 키 코드 => 액션 매핑 테이블
    private final Set<Action> heldActions = new HashSet<>();        // 현재 눌려 있는 액션 목록
    private final Set<Action> justPressedActions = new HashSet<>(); // 이번 프레임에 처음 눌린 액션 목록

    public InputHandler() {
        setDefaultKeyMap();
    }

    public void setDefaultKeyMap() {

        // 플레이어 이동
        bind(KeyEvent.VK_UP,    Action.MOVE_UP);
        bind(KeyEvent.VK_DOWN,  Action.MOVE_DOWN);
        bind(KeyEvent.VK_LEFT,  Action.MOVE_LEFT);
        bind(KeyEvent.VK_RIGHT, Action.MOVE_RIGHT);

        // 플레이어 행동
        bind(KeyEvent.VK_Z, Action.TALK);           // npc대화
        bind(KeyEvent.VK_TAB, Action.INVENTORY);      // 인벤토리

        // UI
        bind(KeyEvent.VK_UP,    Action.UI_UP);
        bind(KeyEvent.VK_DOWN,  Action.UI_DOWN);
        bind(KeyEvent.VK_LEFT,  Action.UI_LEFT);
        bind(KeyEvent.VK_RIGHT, Action.UI_RIGHT);
        bind(KeyEvent.VK_Z, Action.UI_SELECT);
        bind(KeyEvent.VK_ENTER, Action.UI_SELECT);
        bind(KeyEvent.VK_X, Action.UI_BACK);
        bind(KeyEvent.VK_ESCAPE, Action.MENU);

        // bind(KeyEvent.VK_,        Action.);
    }

    public void bind(int keyCode, Action... actions) {
        keyMap.computeIfAbsent(keyCode, k -> new ArrayList<>());    // 키에 리스트 없으면 새로 생성

        for(Action action : actions) {
            keyMap.get(keyCode).add(action);        // 액션추가
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        List<Action> actions = keyMap.get(e.getKeyCode());

        if(actions == null) return;     // 매핑 없는 키는 무시

        for(Action action : actions) {
            if(!heldActions.contains(action)) {
                justPressedActions.add(action);     // 처음 눌린 경우에만 justpressed에 추가
            }
            heldActions.add(action);        // held에는 항상 추가
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {           // 키를 땔때 동작을 멈추는 코드
        List<Action> actions = keyMap.get(e.getKeyCode());      // 현재 눌려 있던 키 코드

        if(actions == null) return;         // 매핑이 안되있는 키는 무시

        for(Action action : actions) {
            heldActions.remove(action);     // 키를 때면 held에서 제거
        }
    }

    public boolean isHeld(Action action) {
        return heldActions.contains(action);            // 키가 눌려 있는지 확인
    }

    public boolean isJustPressed(Action action) {
        return justPressedActions.contains(action);     // 해당 키가 이번 프레임에 처음 눌렸는지 확인
    }

    public void rebind(Action action, int newKeyCode) {     // 키 설정 변경
        for(List<Action> actions : keyMap.values()) {
            actions.remove(action);                         // 기존 키 바인드 해제
        }
        bind(newKeyCode, action);                           // 해당 액션에 새로운 키 바인드
    }

    public void flush() {               // 매 프레임 끝에 justPressed 초기화
        justPressedActions.clear();
    }

}
