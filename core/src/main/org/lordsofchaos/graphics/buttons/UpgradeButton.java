package org.lordsofchaos.graphics.buttons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.lordsofchaos.EventManager;
import org.lordsofchaos.Game;
import org.lordsofchaos.graphics.Screen;

public class UpgradeButton extends HoverButton
{
    
    public static boolean maxLevel;
    private Texture infoCardTexture;
    private Sprite infoCardSprite;
    public UpgradeButton(String path, float buttonX1, float buttonY1, Screen screenLocation) {
        super(path, buttonX1, buttonY1, screenLocation);
        infoCardTexture = new Texture("UI/NewArtMaybe/panel.png");
        infoCardSprite = new Sprite(infoCardTexture);
        infoCardSprite.setPosition(10,150);
    }
    
    @Override
    public void leftButtonAction() {
        if (maxLevel)
            return;
        selectSound.play(Game.getSoundEffectsVolume());
        Game.instance.setGhostTowerType(null); // this alerts Game that a tower isn't being placed, janky yes
        EventManager.defenderUpgrade();
    }
    
    @Override
    public void rightButtonAction() {
    }

    @Override
    public void update(int x, int y, SpriteBatch batch) {
        if(checkHover(x,y)){
            infoCardSprite.draw(batch);
        }
    }
}
