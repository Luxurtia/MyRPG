package com.gema.state;

import com.gema.Game;
import com.gema.system.InputHandler;
import com.gema.util.AssetManager;

import java.awt.Graphics2D;

public class GameStateManager {
    // currenttState 들고 있다가 update와 render를 넘겨주는 역할
    // 현재 어떤 화면(상태를 실행할지 관리하는 컨트롤러)

    private GameState currentState;     // 현재 실행 중인 상태

    private final InputHandler input;
    private final AssetManager assets;

    private Game game;
    private final PlayState playState;

    public GameStateManager(InputHandler input, AssetManager assets, GameState initialState) {
        this.input   = input;
        this.assets  = assets;
        currentState = initialState;    // 처음 시작할 상태를 받아서 세팅
        playState = new PlayState(input, this, assets);
    }

    public void setGame(Game game) {
        if(this.game != null) {                 // game이 설정 되어 있지 않다면 통과해서 아래에서 설정함
            throw new IllegalStateException("Game이 이미 설정되어 있습니다.");
        }
        this.game = game;
    }

    public void changeState(String state) {
        switch(state) {
            case "MENU"     -> currentState = new MenuState(input, game, this);
            case "PLAY"     -> currentState = playState;
            default         -> System.out.println("[GameStateManager] : 알 수 없는 상태 " + state);
        }
    }

    public void changeState(String state, GameState gamestate) {
        currentState = gamestate;
    }

    public void update() {
        currentState.update();          // 현재 상태의 update() 실행
    }

    public void render(Graphics2D g2) {
        currentState.render(g2);        // 현재 상태의 render() 실행
    }

    public InputHandler getInput() {
        return input;
    }

    public AssetManager getAssets() {
        return assets;
    }
}
