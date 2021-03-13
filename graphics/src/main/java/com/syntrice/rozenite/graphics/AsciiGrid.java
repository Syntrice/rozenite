package com.syntrice.rozenite.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A simple tool to render grids of ascii characters, in a terminal like fashion.
 * Currently supports a single layer of characters along with solid backgrounds.
 * ALso currently stays at a fixed grid size
 */
public class AsciiGrid {

    private int gridWidth, gridHeight;
    private AsciiTileset tileset;

    private float[][] backgroundColors;
    private float[][] foregroundColors;
    private int[][] glyphs;

    private Texture backgroundTexture;

    private float defaultForegroundColor = Color.WHITE.toFloatBits();
    private float defaultBackgroundColor = Color.BLACK.toFloatBits();

    /** Class Constructor
     * @param tileset an AsciiTileset to use for writing to the grid
     * @param gridWidth the width of the grid in world units (number of tiles on x axis)
     * @param gridHeight the height of the grid in world units (number of tiles on y axis)
     */
    public AsciiGrid(AsciiTileset tileset, int gridWidth, int gridHeight) {
        this.tileset = tileset; this.gridWidth = gridWidth; this.gridHeight = gridHeight;
        generateBackgroundImage();
        backgroundColors = new float[gridWidth][gridHeight];
        foregroundColors = new float[gridWidth][gridHeight];
        glyphs = new int [gridWidth][gridHeight];
    }

    /**
     * Internal use, generates a 1 x 1 RGBA8888 pixmap with the color white, which is then used to create a texture
     * for drawing backgrounds with sprite batch.
     */
    private void generateBackgroundImage() {
        Pixmap backgroundPixmap = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        backgroundPixmap.setColor(Color.WHITE);
        backgroundPixmap.fill();
        backgroundTexture = new Texture(1,1, Pixmap.Format.RGBA8888);
        backgroundTexture.draw(backgroundPixmap,0,0);
        backgroundPixmap.dispose();
    }

    /**
     * Constructor with default included AsciiTileset cp437_16_16
     * @param gridWidth the width of the grid in world units (number of tiles on x axis)
     * @param gridHeight the height of the grid in world units (number of tiles on y axis)
     * @param flipTilesetY whether to flip each texture region in the default tileset or not. Useful for rendering when the y axis points downwards.
     */
    public AsciiGrid(int gridWidth, int gridHeight, boolean flipTilesetY) {
        this(new AsciiTileset(new Texture(Gdx.files.internal("tileset/cp437_8x8.png"),true),8,8,flipTilesetY),gridWidth,gridHeight);
    }

    /**
     * Writes a single character to the grid at the specified position with specified colors
     * @param character AsciiCode for character
     * @param x x position
     * @param y y position
     * @param foreground foreground color
     * @param background background color
     */
    public void write(int character, int x, int y, float foreground, float background) {

        if (x < 0 || x >= gridWidth) {
            throw new IllegalArgumentException("x should be in range [0,"+gridWidth+").");
        }

        if (y < 0 || y >= gridHeight) {
            throw new IllegalArgumentException("y should be in range [0,"+gridHeight+").");
        }

        glyphs[x][y] = character;
        foregroundColors[x][y] = foreground;
        backgroundColors[x][y] = background;
    }

    /**
     * Writes a single character to the grid at the specified position with default foreground and background colors
     * @param character AsciiCode for character
     * @param x x position
     * @param y y position
     */
    public void write(int character, int x, int y) {
        write(character,x,y,defaultForegroundColor,defaultBackgroundColor);
    }

    /**
     * Attempts to write a string of text to the grid, with first character at specified position
     * with specified foreground and background colors.
     * @param string the string to write
     * @param x x position
     * @param y y position
     * @param foreground foreground color in packed float bits form
     * @param background background color in packed float bits form
     */
    public void write(String string, int x, int y, float foreground, float background) {
        if (x + string.length() > gridWidth) {
            throw new IllegalArgumentException("String length exceeds beyond grid width: x + string.length() should be in range [0,"+gridWidth+").");
        }

        for (int i = 0; i < string.length(); i++) {
            write(string.charAt(i),x + i,y,foreground,background);
        }
    }

    /**
     * Attempts to write a string of text to the grid, with first character at specified position
     * with default foreground and background colors
     * @param string the string to write
     * @param x x position
     * @param y y position
     */
    public void write(String string, int x, int y) {
        write(string,x,y,defaultForegroundColor,defaultBackgroundColor);
    }

    public void writeCenter(String string, int y, float foreground, float background) {
        int x = ( gridWidth - string.length() ) / 2;
        write(string,x,y,foreground,background);
    }

    /**
     * Draws the ascii grids current state using a running sprite batch.
     * @param batch a running sprite batch
     * @param scaling the scale factor for each tile ( width and height ).
     */
    public void draw(SpriteBatch batch, float scaling) {
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                drawBackground(x,y,batch, scaling);
                drawGlyph(x,y,batch, scaling);
            }
        }
    }

    private void drawBackground(int x, int y, Batch batch, float scaling) {
        batch.setPackedColor(backgroundColors[x][y]);
        batch.draw(backgroundTexture,x,y,1 * scaling,scaling);
    }

    private void drawGlyph(int x, int y, Batch batch, float scaling) {
        batch.setPackedColor(foregroundColors[x][y]);
        batch.draw(tileset.get(glyphs[x][y]),x,y,1 * scaling,scaling);
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    /**
     * Sets the default background color to render glyph backgrounds with.
     * @param defaultBackgroundColor background color in packed float bits
     */
    public void setDefaultBackgroundColor(float defaultBackgroundColor) {
        this.defaultBackgroundColor = defaultBackgroundColor;
    }

    /**
     * Sets the default foreground color to render glyphs with.
     * @param defaultForegroundColor foreground color in packed float bits.
     */
    public void setDefaultForegroundColor(float defaultForegroundColor) {
        this.defaultForegroundColor = defaultForegroundColor;
    }
}
