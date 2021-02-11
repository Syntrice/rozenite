package com.syntrice.rozenite.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Maps texture regions from a tileset to characters (glyphs)
 */
public class GlyphSet {
    TextureRegion[] glyphSet;
    private int glyphWidth, glyphHeight;

    public GlyphSet(Texture texture, int glyphWidth, int glyphHeight) {
        this.glyphWidth = glyphWidth;
        this.glyphHeight = glyphHeight;
        mapToGlyphs(texture,glyphWidth,glyphHeight);
    }

    /**
     * Works from top left to bottem right of a texture, splitting it up into individual texture regions.
     * Then, each texture region is placed in an array, starting from index 0 in the order the texture was split.
     * TODO: Improve efficiency of this method by replacing TextureRegion.split with integrated code
     * @param texture the texture to split into texture regions
     */
    private void mapToGlyphs(Texture texture,int glyphWidth, int glyphHeight) {
        TextureRegion[][] textures = TextureRegion.split(texture,glyphWidth,glyphHeight);
        int currentAsciiChar = 0;
        for (int x = 0; x < textures.length; x++) {
            for (int y = 0; y < textures[0].length; y++) {
                glyphSet[currentAsciiChar] = textures[x][y];
            }
        }
    }

    /**
     * Returns a texture region which corresponds to a specified character
     * @param glyph glyph to get
     * @return a texture region representing the glyph. Returns null if a mapping is not found.
     */
    public TextureRegion get(char glyph) {
        try {
            return glyphSet[glyph];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Returns a texture region which corresponds to a specified char
     * Takes an int which is cast to a char
     * @param glyph integer value representing an ascii char
     * @return a texture region representing the glyph. Returns null if a mapping is not found.
     */
    public TextureRegion get(int glyph) {
        try {
            return glyphSet[glyph];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public int getGlyphWidth() {
        return glyphWidth;
    }

    public int getGlyphHeight() {
        return glyphHeight;
    }
}
