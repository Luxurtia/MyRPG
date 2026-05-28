package com.gema.state;

import java.awt.*;

public interface GameState {
    void update();
    void render(Graphics2D g2);
}
