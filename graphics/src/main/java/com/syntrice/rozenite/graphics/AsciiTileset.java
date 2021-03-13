package com.syntrice.rozenite.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * An CP437 / EASCII Tilest
 * Supports up to 256 glyphs
 */
public class AsciiTileset {

    private TextureRegion[] tileset;
    private int glyphWidth, glyphHeight;
    private boolean flipY;

    /**
     * @param texture the texture providing the ASCII tileset. Should have a transparent background as apposed to all black
     * @param glyphWidth the width of each glyph in the tileset
     * @param glyphHeight the height of each glyph in the tileset
     * @param flipY whether to flip each texture region on the y axis
     */
    public AsciiTileset(Texture texture, int glyphWidth, int glyphHeight, boolean flipY) {
        this.glyphHeight = glyphHeight;
        this.glyphWidth = glyphWidth;
        this.flipY = flipY;
        splitTexture(texture);
    }

    /**
     *
     * @param texture The texture providing the ASCII tileset. Should have a transparent background as apposed to all black
     * @param glyphWidth the width of each glyph in the tileset
     * @param glyphHeight the height of each glyph in the tileset
     */
    public AsciiTileset(Texture texture, int glyphWidth, int glyphHeight) {
        this(texture,glyphWidth,glyphHeight,false);
    }

    /**
     * Splits a texture into texture regions which get stored in tileset.
     * This creates a new 1 dimensional array of a maximum size 256 and minimum size of
     * the total possible tiles in the texture
     * @param texture the texture to split
     */
    private void splitTexture(Texture texture) {
        int tiledWidth = texture.getWidth() / glyphWidth;    // Number of columns in texture
        int tiledHeight = texture.getHeight() / glyphHeight; // Number of rows in texture

        // Create a array of maximum size 256, and a minimum size of the total possible tiles in the texture.
        // This makes sure that if a tileset is incomplete, the array wont be unnecessarily large.
        tileset = new TextureRegion[Math.min( (tiledWidth * tiledHeight), 256)];

        int currentAsciiCode = 0;
        for (int y = 0; y < tiledHeight; y++) {
            for (int x = 0; x < tiledWidth; x++) {
                if (currentAsciiCode >= 256) break;
                tileset[currentAsciiCode] = new TextureRegion(texture, x * glyphWidth, y * glyphHeight, glyphWidth, glyphHeight);
                tileset[currentAsciiCode].flip(false,flipY);
                currentAsciiCode++;
            }
        }
    }

    /**
     * Gets a texture region in the tileset from a specific ascii code
     * @param code an integer representing the ascii code. Should be a maximum of 256 and a minimum of 0.
     * @return the corresponding texture region.
     */
    public TextureRegion get(int code) {
        try {
            return tileset[code];
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("ASCII code should be in range " + "[0,"+tileset.length+")");
        }
    }

    /**
     * @return height of each glyph
     */
    public int getGlyphHeight() {
        return glyphHeight;
    }

    /**
     * @return width of each glyph
     */
    public int getGlyphWidth() {
        return glyphWidth;
    }
}
