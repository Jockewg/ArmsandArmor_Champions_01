package render;

import world.Map;
import menus.Activator;
import menus.ActivatorType;
import menus.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.rapplebob.ArmsAndArmorChampions.*;

public abstract class Renderer {

    public Renderer() {
        loadFonts();
    }
    public String ID = "DEFAULT";
    public State state = null;

    public abstract void render(ShapeRenderer shapeBatch, SpriteBatch batch);

    public abstract void specificUpdate();

    public void generalUpdate() {
        specificUpdate();
    }
    public boolean active = false;

    public abstract void loadResources() throws Exception;

    public void drawImage(SpriteBatch batch, Texture img, int x, int y, float scale, int rotation) {
        batch.draw(img, x, y, img.getWidth() / 2, img.getHeight() / 2, img.getWidth(), img.getHeight(), scale, scale, rotation, 0, 0, img.getWidth(), img.getHeight(), false, false);
    }

    public void drawImage(SpriteBatch batch, Texture img, int x, int y, int width, int height, int rotation) {
        batch.draw(img, x, y, img.getWidth() / 2, img.getHeight() / 2, width, height, 1, 1, rotation, 0, 0, img.getWidth(), img.getHeight(), false, false);
    }

    public void loadFonts() {
        com64 = new BitmapFont(Gdx.files.internal("data/fonts/com64.fnt"), Gdx.files.internal("data/fonts/com64.png"), false, false);
        com32 = new BitmapFont(Gdx.files.internal("data/fonts/com32.fnt"), Gdx.files.internal("data/fonts/com32.png"), false, false);
        com16 = new BitmapFont(Gdx.files.internal("data/fonts/com16.fnt"), Gdx.files.internal("data/fonts/com16.png"), false, false);
        com10 = new BitmapFont(Gdx.files.internal("data/fonts/com10.fnt"), Gdx.files.internal("data/fonts/com10.png"), false, false);
        com32_BI = new BitmapFont(Gdx.files.internal("data/fonts/com32_BI.fnt"), Gdx.files.internal("data/fonts/com32_BI.png"), false, false);
        com16_BI = new BitmapFont(Gdx.files.internal("data/fonts/com16_BI.fnt"), Gdx.files.internal("data/fonts/com16_BI.png"), false, false);
        com10_BI = new BitmapFont(Gdx.files.internal("data/fonts/com10_BI.fnt"), Gdx.files.internal("data/fonts/com10_BI.png"), false, false);
    }
    public BitmapFont com64;
    public BitmapFont com32;
    public BitmapFont com16;
    public BitmapFont com10;
    public BitmapFont com32_BI;
    public BitmapFont com16_BI;
    public BitmapFont com10_BI;

    public int getScreenX() {
        return -(AAA_C.screenWidth / 2);
    }

    public int getScreenY() {
        return -(AAA_C.screenHeight / 2);
    }

    public void drawString(SpriteBatch batch, String string, int x, int y, BitmapFont font, Color col) {
        LabelStyle style = new LabelStyle(font, col);
        Label lab = new Label(string, style);
        lab.setPosition(x, y);
        lab.draw(batch, 1);
    }

    public void drawMenu(SpriteBatch batch, Menu m, int x, int y) {
        drawString(batch, m.title, x - 24, y, com32_BI, Color.WHITE);
        drawString(batch, "______________________", x - 24, y - 8, com32, Color.WHITE);
        if (m.acts.size() > 0) {
            for (int i = 0; i < m.acts.size(); i++) {
                Activator a = m.acts.get(i);
                Color col = getActColor(a.AT);
                String print = a.title;
                if(AAA_C.debug){
                    print += " - " + a.script + " - " + a.AT.toString();
                }
                drawString(batch, print, x, y - 50 - (i * 40), com32, col);
                if(i == m.activeAct){
                    String marker;
                    switch(a.AT){
                        case BUTTON:marker = "*";
                            break;
                        case INPUT:marker = ">";
                            break;
                        case TEXT:marker = "-";
                            break;
                        default:marker = "";
                    }
                    drawString(batch, marker, x - 32, y - 50 - (i * 40), com32, Color.WHITE);
                }
            }
        } else {
            drawString(batch, "EMPTY MENU", x, y - 50, com32_BI, Color.WHITE);
        }
    }

    public Color getActColor(ActivatorType type) {
        Color col;
        switch (type) {
            case BUTTON:
                col = Color.WHITE;
                break;
            case INPUT:
                col = Color.YELLOW;
                break;
            case TEXT:
                col = Color.CYAN;
                break;
            default:
                col = Color.WHITE;
        }
        return col;
    }
    
    
    public void drawMap(ShapeRenderer shapeBatch, SpriteBatch batch, Map map, int x, int y, boolean central){
        x += getCentralMapX(map);
        y += getCentralMapY(map);
        if(map.loaded && map.cells.size() > 0){
            int width = map.width;
            //draw backdrop
            for(int i = 0; i < map.cells.size(); i++){
//DRAW IMAGE OF CELL
//IF DEBUG: DRAW CELLSHAPE
                if(map.cells.get(i).ACTIVE){
                    drawPolygon(shapeBatch, map.cells.get(i).getPolygons(), (int) map.getCellX(i) + x, (int) map.getCellY(i) + y, Color.YELLOW);
                }else{
                    drawPolygon(shapeBatch, map.cells.get(i).getPolygons(), (int) map.getCellX(i) + x, (int) map.getCellY(i) + y, Color.WHITE);
                }
                drawString(batch, "" + i, (int) map.getCellX(i) + x - 12, (int) map.getCellY(i) + y + 12, com10, Color.RED);
            }
        }else{
            drawString(batch, "Map empty or not yet loaded.", -200, 50, com32, Color.RED);
        }
    }
    
    public void drawPolygon(ShapeRenderer shapeBatch, Polygon pol[], int x, int y, Color col){
        shapeBatch.setColor(col);
        for(int i = 0; i < pol.length; i++){
            shapeBatch.triangle(pol[i].getVertices()[0] + x, pol[i].getVertices()[1] + y, pol[i].getVertices()[2] + x, pol[i].getVertices()[3] + y, pol[i].getVertices()[4] + x, pol[i].getVertices()[5] + y);
        }
    }
    
    public void drawPolygon(ShapeRenderer shapeBatch, float[] verts, int x, int y, Color col){
        shapeBatch.setColor(col);
        shapeBatch.triangle(verts[0] + x, verts[1] + y, verts[2] + x, verts[3] + y, verts[4] + x, verts[5] + y);
    }
    
    public int getCentralMapX(Map map){
        float x = -(map.cells.get(0).SIDE * (map.width / 2));
        x += map.cells.get(0).RADIUS;
        return (int) x;
    }
    public int getCentralMapY(Map map){
        float y = (map.getHeight() / 2) * map.cells.get(0).SIDE;
        return (int) y;
    }
}
