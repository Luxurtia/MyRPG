package com.gema.state;

import java.awt.Graphics2D;

public interface GameState {
    void update();
    void render(Graphics2D g2);
}
