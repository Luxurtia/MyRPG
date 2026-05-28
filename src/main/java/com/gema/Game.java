package com.gema;

import com.gema.state.GameStateManager;
import com.gema.state.MenuState;

import javax.swing.JPanel;
import java.awt.*;

public class Game extends JPanel implements Runnable {

    static final int TILE_SIZE = 48;    // 티일 한 칸의 픽셀 크기
    static final int SCREEN_COLS = 20;  // 가로 타일의 수
    static final int SCREEN_ROWS = 14;  // 세로 타일의 수
    public static final int SCREEN_WIDTH = TILE_SIZE * SCREEN_COLS;    // 960PX
    public static final int SCREEN_HEIGHT = TILE_SIZE * SCREEN_ROWS;   // 672PX
    static final int FPS = 60;          // 목표 프레임

    private Thread gameThread;  // 게임 루프를 돌릴 쓰레드
    private boolean running = false;    // 게임 루프 실행 여부 플래그
    private GameStateManager stateManager;        // 상태관리자 추가

    private int currentFps = 0;
    private int frameCount = 0;

    public Game() {
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));   // 패널 크기 지정
        setBackground(Color.BLACK);
        setFocusable(true);     // 키보드 입력하려면 필요

        stateManager = new GameStateManager(new MenuState());   // 처음 상태를 menustate로 지정
    }

    public void startGameLoop() {
        running = true;                      // 루프 실행 여부 변경
        gameThread = new Thread(this);  // this = Runnable => run을 실행할 쓰레드
        gameThread.start();                  // 쓰레드 시작
        System.out.println("Starting game loop");
    }

    public void stopGameLoop() {
        running = false;
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
            }
        }
    }

    private void update() {
        stateManager.update();      // stateManager에게 update 위임
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);                // 부모 클래서 먼저 실행 => 배경 지움
        Graphics2D g2 = (Graphics2D) g;         // Graphics2D로 캐스팅
        stateManager.render(g2);

        g2.dispose();                           // 사용한 자원 해제
    }
}
