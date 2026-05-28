package com.gema;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {      // 전용 스레드생성
            JFrame frame = new JFrame("MyRPG");     // 창생성 및 제목
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);   // 닫기 동작을 직접 제어를 위하여 기본 동작 비활성화
            frame.setResizable(false);          // 창크기 고정

            Game game = new Game();     // GAME 패널 생성
            frame.add(game);            // 창안에 패널 부착
            frame.pack();               // 패널크기에 맞게 창크기 조절

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    game.stopGameLoop();        // 게임 루프 종료
                    System.out.println("Game loop stopped");
                    frame.dispose();            // 창 지원 해제
                    System.exit(0);       // 프로그램 완전 종료
                }
            });

            frame.setLocationRelativeTo(null);  //모니터 정중앙에 창 배치
            frame.setVisible(true);             // 창 표시

            game.startGameLoop();  // 창 준비 완료 후 게임 루프 시작
        });
    }
}
