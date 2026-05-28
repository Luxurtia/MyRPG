package com.gema.state;

import java.awt.Graphics2D;

public class GameStateManager {
    // currenttState 들고 있다가 update와 render를 넘겨주는 역할
    // 현재 어떤 화면(상태를 실행할지 관리하는 컨트롤러)

    private GameState currentState;     // 현재 실행 중인 상태

    public GameStateManager(GameState initialState) {
        currentState = initialState;    // 처음 시작할 상태를 받아서 세팅
    }

    public void changeState(GameState newState) {
        currentState = newState;        // 새로운 상태로 교체 => 씬전환
    }

    public void update() {
        currentState.update();          // 현재 상태의 update() 실행
    }

    public void render(Graphics2D g2) {
        currentState.render(g2);        // 현재 상태의 render() 실행
    }
}
