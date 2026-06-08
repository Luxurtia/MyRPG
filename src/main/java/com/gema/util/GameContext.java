package com.gema.util;

import com.gema.state.GameStateManager;
import com.gema.system.InputHandler;

public class GameContext {
    public final InputHandler input;
    public final GameStateManager stateManager;
    public final AssetManager assets;

    public GameContext(InputHandler input, GameStateManager stateManager, AssetManager assets) {
        this.input = input;
        this.stateManager = stateManager;
        this.assets = assets;
    }
}
