package com.gema.util;

import com.gema.entity.Player;
import com.gema.system.Renderer;
import com.gema.world.Camera;
import com.gema.world.TileMap;

public class PlayerContext {
    public final Player player;
    public final TileMap tileMap;
    public final Camera camera;
    public final Renderer renderer;

    public PlayerContext(Player player, TileMap tileMap, Camera camera, Renderer renderer) {
        this.player = player;
        this.tileMap = tileMap;
        this.camera = camera;
        this.renderer = renderer;
    }

}
