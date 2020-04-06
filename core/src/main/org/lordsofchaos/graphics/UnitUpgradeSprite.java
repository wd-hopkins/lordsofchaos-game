package org.lordsofchaos.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.lordsofchaos.GameController;

public class UnitUpgradeSprite {
    private String path;
    private int x;
    private int y;
    private Texture texture;
    private Sprite sprite;
    private int currentUpgrade;
    public  UnitUpgradeSprite(String path,int x,int y){
        this.path = path;
        this.x = x;
        this.y = y;
        currentUpgrade = 1;
        texture = new Texture(Gdx.files.internal(path + currentUpgrade + ".png"));
        sprite = new Sprite(texture);
        sprite.setPosition(x,y);

    }
    public void update(SpriteBatch batch){
        if(GameController.getUnitUpgradeLevel() != currentUpgrade) {
            currentUpgrade = GameController.getUnitUpgradeLevel();
            createSprite(path);
        }
        //System.out.println(currentUpgrade);
        sprite.draw(batch);
    }
    private void createSprite(String path){
        texture.dispose();
        texture = new Texture(Gdx.files.internal(path + currentUpgrade + ".png"));
        sprite = new Sprite(texture);
        sprite.setPosition(x,y);
    }
}
