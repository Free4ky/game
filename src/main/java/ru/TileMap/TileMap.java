/**
 * Класс карты уровня.
 * Отвечает за считывание спрайтов, шаблона карты и их инициализацию.
 * Отвечает за главную логику отрисовки уровня.
 * @version dev
 */

package ru.TileMap;

import ru.Main.Game;
import ru.Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileMap {

    // position
    private double x;
    private double y;

    // bounds
    private int xmin;
    private int ymin;

    private int xmax;
    private int ymax;

    private double tween;

    // map
    private int[][] map;
    private int tileSize;
    private int numRows;
    private int numCols;
    private int width;
    private int height;

    //tileset
    private BufferedImage tileset;
    private int numTilesAcross;
    private Tile[][] tiles;

    //drawing
    // draw tiles which are on the screen
    private int rowOffset; // which raw to start drawing
    private int colOffset;
    private int numRowsToDraw;
    private int numColsToDraw;

    public TileMap(int tileSize){
        this.tileSize = tileSize;
        numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
        numColsToDraw = GamePanel.WIDTH / tileSize + 2;
        tween = 1;
    }
    // load tiles into the memory
    public void loadTiles(String s){
        try{
            // get Resources
            tileset = ImageIO.read(getClass().getResourceAsStream(s));
            numTilesAcross = tileset.getWidth() / tileSize;
            tiles = new Tile[2][numTilesAcross];
            BufferedImage subimage;
            for (int col = 0; col < numTilesAcross; col++){
                //first row
                subimage = tileset.getSubimage(
                        col*tileSize,0,tileSize,tileSize);
                tiles[0][col] = new Tile(subimage,Tile.NORMAL);
                //second row
                subimage = tileset.getSubimage(
                        col*tileSize,tileSize,tileSize,tileSize);
                tiles[1][col] = new Tile(subimage,Tile.BLOCKED);
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    // load map into the memory
    public void loadMap(String s){

        try{
            // get Resources
            InputStream in = getClass().getResourceAsStream(s);
            // effective reading map symbols (loading map)
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(in));

            numCols = Integer.parseInt(br.readLine());
            numRows = Integer.parseInt(br.readLine());
            map = new int[numRows][numCols];
            width = numCols * tileSize;
            height = numCols * tileSize;

            // установка границ карты. Координаты карты отрицательны
            xmin = GamePanel.WIDTH - width;
            xmax = 0;
            ymin = GamePanel.HEIGHT - height;
            ymax = 0;

            String delims = "\\s+"; //white space
            // read map into array
            for (int row = 0; row < numRows; row++){
                String line = br.readLine();
                String[] tokens = line.split(delims);
                for (int col = 0; col < numCols; col++){
                    map[row][col] = Integer.parseInt(tokens[col]);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public int getTileSize() {
        return tileSize;
    }
    public int getX(){
        return (int)x;
    }
    public int getY(){
        return (int)y;
    }
    public int getWidth(){
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getType(int row,int col){
        int rc = map[row][col];
        int r =  rc/numTilesAcross;
        int c = rc % numTilesAcross;
        return tiles[r][c].getType();
    }
    public void setPosition(double x, double y){

        this.x += (x - this.x)*tween;
        this.y += (y - this.y)*tween;

        fixBounds();

        // find out where start drawing
        colOffset = (int)-this.x/tileSize;
        rowOffset = (int)-this.y/tileSize;
    }
    private void fixBounds(){
        if (x < xmin){
            x = xmin;
        }
        if (y < ymin){
            y = ymin;
        }
        if (y > ymax){
            y = ymax;
        }
        if (x > xmax){
            x = xmax;
        }
    }
    public void draw(Graphics2D g){
        for (int row = rowOffset; row < rowOffset + numRowsToDraw;row++){
            if (row >= numRows) {break;}
            for(int col = colOffset; col < colOffset + numColsToDraw; col++){
                if (col >= numCols){break;}
                if (map[row][col] == 0) continue;

                // which tile to draw
                int rc = map[row][col];
                int r = rc / numTilesAcross;
                int c = rc % numTilesAcross;

                g.drawImage(
                        tiles[r][c].getImage(),
                        (int)x + col*tileSize,
                        (int)y + row*tileSize,
                        null);
            }
        }
    }
}