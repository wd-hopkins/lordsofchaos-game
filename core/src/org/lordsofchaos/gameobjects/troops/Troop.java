package org.lordsofchaos.gameobjects.troops;

import java.util.ArrayList;
import java.util.List;

import javax.security.sasl.RealmCallback;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


import org.lordsofchaos.Game;
import org.lordsofchaos.GameController;
import org.lordsofchaos.GameStart;
import org.lordsofchaos.coordinatesystems.RealWorldCoordinates;
import org.lordsofchaos.coordinatesystems.MatrixCoordinates;
import org.lordsofchaos.gameobjects.DamageType;
import org.lordsofchaos.gameobjects.InteractiveObject;
import org.lordsofchaos.matrixobjects.Path;
import org.lordsofchaos.player.*;


public class Troop extends InteractiveObject
{
    protected float moveTimer;
    protected float moveTimeLimit = 0.1f;
    protected float movementSpeed;
    protected int currentHealth;
    protected int maxHealth;
    protected DamageType armourType;
    protected List < Path > path;
    protected Sprite sprite;
    protected boolean moved;
    protected boolean targeted;
    protected boolean atEnd;


    public Troop(String spriteName, int cost, int damage,
        float movementSpeed, int maxHealth, DamageType armourType, List < Path > path) {
        super(spriteName, new RealWorldCoordinates(path.get(0).getMatrixPosition()), cost, damage);
        setMovementSpeed(movementSpeed);
        setCurrentHealth(maxHealth);
        setMaxHealth(maxHealth);
        setPath(path);
        setAtEnd(false);
        Texture texture = new Texture(Gdx.files.internal("troops/" + spriteName + ".png"));
        this.sprite = new Sprite(texture);

    }

    // Getters and setters
    public void setMovementSpeed(float movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }

    public void setMaxHealth(int health) {
        maxHealth = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setCurrentHealth(int health) {
        currentHealth = health;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setPath(List < Path > path) {
        this.path = path;
    }

    public List < Path > getPath() {
        if (path == null) {
        return new ArrayList < Path > ();
        }
         return path;
    }
    //

    public Sprite getSprite() {
        return sprite;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public boolean getMoved() {
        return moved;
    }

    public boolean getTargeted() {
        return targeted;
    }

    public void setTargeted(boolean targeted) {
        this.targeted = targeted;
    }

    public boolean getAtEnd() {
        return atEnd;
    }

    public void setAtEnd(boolean atEnd) {
        this.atEnd = atEnd;
    }

    private void resetTimer()
    {
        moveTimer = 0;
    }

    public void move(float deltaTime)
    {
        moveTimer+= deltaTime * 3;
        if (moveTimer < moveTimeLimit)
        {
            return;
        }
        resetTimer();

        setMoved(false);
        // move along set path
        MatrixCoordinates currentco = new MatrixCoordinates(realWorldCoordinates);

        Path foundPath = (Path)GameController.getMatrixObject(currentco.getY(),currentco.getX());

        int index = path.indexOf(foundPath);
        if (index != (path.size() - 1)) {
            MatrixCoordinates nexttile;
            nexttile = (getPath().get(index + 1)).getMatrixPosition();
            String direction;

            if ((currentco.getY() - nexttile.getY()) == 0) {
                //x direction 
                if ((currentco.getX() - nexttile.getX()) == 1) {
                    direction = "west";
                } else {
                    direction = "east";
                }
            } else {
                if ((currentco.getY() - nexttile.getY()) == 1) {
                    direction = "north";
                } else {
                    direction = "south";
                }
            }

            switch (direction) {
                case "north":
                    realWorldCoordinates.setY(realWorldCoordinates.getY() - 1);
                    break;
                case "east":
                    realWorldCoordinates.setX(realWorldCoordinates.getX() + 1);
                    break;
                case "south":
                    realWorldCoordinates.setY(realWorldCoordinates.getY() + 1);
                    break;
                case "west":
                    realWorldCoordinates.setX(realWorldCoordinates.getX() - 1);
                    break;

            }

            MatrixCoordinates updatedco = new MatrixCoordinates(realWorldCoordinates);
            //if the path tile that the troop is on changes then it wil; be added to the new troop list;
            if ((currentco.equals(updatedco)) == false) {
                (getPath().get(index)).removeTroop(this);
                (getPath().get(index + 1)).addTroop(this);
                setMoved(true);

            }


        } else {
            (getPath().get(index)).removeTroop(this);
            damageBase();
            setAtEnd(true);
        }

    }



    public void damageBase() {
        int temp;
        temp = GameStart.defender.getHealth() - getDamage();

        if (temp <= 0) {
            GameStart.defender.setHealth(0);
            //end of game and relevant graphics and sound need to be done.
        } else {
            GameStart.defender.setHealth(temp);
        }

    }

}