package com.syntrice.rozenite.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class DefaultResources {

    private AsciiTileset defaultAsciiTileset;

    public static AsciiTileset getDefaultAsciiTileset(boolean flipY) {
        Texture texture = new Texture(Gdx.files.internal("tileset/cp437_8x8.png"),true);
        return new AsciiTileset(texture,8,8,flipY);
    }

}
