package com.gema.state;

import com.gema.Game;
import com.gema.entity.Player;
import com.gema.system.InputHandler;
import com.gema.system.Renderer;
import com.gema.util.AssetManager;
import com.gema.world.Camera;
import com.gema.world.TileMap;

import java.awt.Graphics2D;

public class PlayState implements GameState {
    private final InputHandler      input;
    private final GameStateManager  stateManager;    // 씬 전환시 사용
    private final Player            player;
    private final TileMap           tileMap;
    private final Camera            camera;
    private final Renderer          renderer;
    private final AssetManager      assets;

    public PlayState(InputHandler input, GameStateManager stateManager, AssetManager assets) {
        this.input = input;
        this.stateManager = stateManager;
        this.assets = assets;

        tileMap  = new TileMap();                    // 맵생성
        player   = new Player(input);                // 플레이어 생성 및 키 입력전달
        camera   = new Camera(player, tileMap);      // 카메라 생성, 플레이어 및 맵 전달
        renderer = new Renderer(tileMap, camera);    // 렌더러 생성, 맵 및 카메라 전달

        player.setTilePosition(5, 5);       // 플레이어 시작 위치
    }

    @Override
    public void update() {
        player.update();                            // 플레이어 이동 처리
        camera.update();                            // 카메라 위치 갱신
    }

    @Override
    public void render(Graphics2D g2) {
        renderer.render(g2, player);                // 정해진 순서에 따라 화면 랜더링
    }
}
