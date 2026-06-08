package com.gema.state;

import com.gema.Game;
import com.gema.entity.Player;
import com.gema.system.Action;
import com.gema.system.InputHandler;
import com.gema.system.Renderer;
import com.gema.util.AssetManager;
import com.gema.world.Camera;
import com.gema.world.TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class PlayState implements GameState {
    private final InputHandler      input;           // 키 입력관리
    private final GameStateManager  stateManager;    // 씬 전환시 사용
    private final AssetManager      assets;          // 에셋 로드 관리
    
    private final Player            player;          // 플레이어 객체
    private final TileMap           tileMap;         // 타일 맵 관리
    private final Camera            camera;          // 카메라 관리
    private final Renderer          renderer;        // 랜더 객체 관리

    private final GameMenuState     gamemenustate;   // 인게임 메뉴

    public PlayState(InputHandler input, GameStateManager stateManager, AssetManager assets) {
        this.input = input;
        this.stateManager = stateManager;
        this.assets = assets;

        tileMap  = new TileMap();                    // 맵생성
        player   = new Player(input, tileMap);       // 플레이어 생성 및 키 입력전달
        camera   = new Camera(player, tileMap);      // 카메라 생성, 플레이어 및 맵 전달
        renderer = new Renderer(tileMap, camera);    // 렌더러 생성, 맵 및 카메라 전달
        gamemenustate = new GameMenuState(input, stateManager, player); // 인게임 메뉴 생성 및 플레이어 전달

        player.setTilePosition(5, 5);       // 플레이어 시작 위치
    }

    @Override
    public void update() {
        player.update();                            // 플레이어 이동 처리
        camera.update();                            // 카메라 위치 갱신

        if(input.isJustPressed(Action.GAME_MENU)) {
            openGameMenu();
        }
    }

    private void openGameMenu() {
        BufferedImage background = new BufferedImage(
                Game.SCREEN_WIDTH,
                Game.SCREEN_HEIGHT,
                BufferedImage.TYPE_INT_ARGB     // 투명도 포함한 이미지 타입
        );
        Graphics2D g2 = background.createGraphics();          // 빈 이미지 생성
        renderer.render(g2, player);                          // 현재 화면을 빈 이미지에 캡쳐
        g2.dispose();                                         // 자원 해제

        gamemenustate.setBackground(background);              // 캡쳐한 화면을 전달
        stateManager.changeState("GAME_MENU", gamemenustate); // 씬전환
    }

    @Override
    public void render(Graphics2D g2) {
        renderer.render(g2, player);                // 정해진 순서에 따라 화면 랜더링
    }
}
