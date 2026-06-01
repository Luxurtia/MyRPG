package com.gema;

import com.gema.state.GameStateManager;
import com.gema.state.MenuState;
import com.gema.system.InputHandler;

import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Game extends JPanel implements Runnable {  // jpanel 상속 runnable 인터페이스 구현체

    static final int TILE_SIZE = 48;    // 타일 한 칸의 픽셀 크기
    static final int SCREEN_COLS = 20;  // 가로 타일의 수
    static final int SCREEN_ROWS = 14;  // 세로 타일의 수
    public static final int SCREEN_WIDTH = TILE_SIZE * SCREEN_COLS;    // 960PX
    public static final int SCREEN_HEIGHT = TILE_SIZE * SCREEN_ROWS;   // 672PX
    static final int FPS = 60;          // 목표 프레임

    private Thread gameThread;  // 게임 루프를 돌릴 쓰레드
    private boolean running = false;    // 게임 루프 실행 여부 플래그

    private GameStateManager stateManager;        // 상태관리자 추가
    private InputHandler inputHandler;

    private int currentFps = 0;                   //현재 fps
    private int frameCount = 0;                 // 1초동안 몇 프래임 그렸는지 카운트
    private long fpsTimer = System.nanoTime();  // 1초 측정용 타이머

    public Game() {
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));   // 패널 크기 지정
        setBackground(Color.BLACK);
        setFocusable(true);     // 키보드 입력하려면 필요

        inputHandler = new InputHandler();
        addKeyListener(inputHandler);       //키 이벤트 등록

        stateManager = new GameStateManager(new MenuState(inputHandler, this));   // 처음 상태를 Menustate로 지정 => menustate에 inputHandler 전달
    }                                                                                   // this = game 객체

    public void startGameLoop() {
        running = true;                      // 루프 실행 여부 변경
        gameThread = new Thread(this);  // this = Runnable => run을 실행할 쓰레드
        gameThread.start();                  // 쓰레드 시작
        System.out.println("Starting game loop");
    }

    public void stopGameLoop() {
        running = false;
        System.out.println("Stopping game loop");
    }

    @Override
    public void run() {
        double nsPerFrame = 1_000_000_000.0 / FPS;  // 1프레임 당 나노초
        long lastTime = System.nanoTime();
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerFrame; // 시간이 얼마나 흘렀는지 계산
            lastTime = now;

                                // delta는 일종의 카운터 역할임
            if (delta >= 1) {   // 1프레임치 시간이 쌓이면
                update();       // update 실행
                repaint();      // 화면 다시그래기 실행
                delta--;        // 처리하는 동안 쌓인 delta를 유지하며 처리한 1프레임을 뺌
                frameCount++;   // 프레임마다 카운트 증가
            }
            
            if (now - fpsTimer >= 1_000_000_000) {  // 현재에서 1초가 지나면
                currentFps = frameCount;            // 현재 fps 갱신
                frameCount = 0;                     // 카운트 초기화
                fpsTimer += now;                    // 타이머 초기화
            }
        }
    }

    private void update() {
        stateManager.update();      // stateManager에게 update 위임
        inputHandler.flush();       // 매 프래임 끝에 justPressed 초기화
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);                // 부모 클래서 먼저 실행 => 배경 지움
        Graphics2D g2 = (Graphics2D) g;         // Graphics2D로 캐스팅
        stateManager.render(g2);

        g2.setColor(Color.WHITE);               // 글자색
        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));  // 폰트
        g2.drawString("FPS: " + currentFps, 10, 20);    // 해당 좌표에 출력

        g2.dispose();                           // 사용한 자원 해제
    }
}
