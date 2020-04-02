package org.lordsofchaos.gameobjects.towers;

import org.lordsofchaos.GameController;
import org.lordsofchaos.Pair;
import org.lordsofchaos.coordinatesystems.MatrixCoordinates;
import org.lordsofchaos.coordinatesystems.RealWorldCoordinates;
import org.lordsofchaos.gameobjects.DamageType;
import org.lordsofchaos.gameobjects.InteractiveObject;
import org.lordsofchaos.gameobjects.TowerType;
import org.lordsofchaos.gameobjects.troops.Troop;
import org.lordsofchaos.matrixobjects.Path;

import java.util.ArrayList;
import java.util.List;

public class Tower extends InteractiveObject
{
    private static final int damageUpgrade = 1;
    private static final float speedUpgrade = 1.2f;
    private static int globalDamageMultiplier = 1;
    private static float globalSpeedMultiplier = 1;
    protected float shootTimer = 0;
    protected float shootTimerLimit = 0.5f;
    protected int range;
    protected DamageType damageType;
    protected Troop target;
    protected List<Path> inRange;
    protected Boolean isCompleted;
    protected TowerType type;
    protected int bonusDamage = 5;
    
    public Tower(String spriteName, RealWorldCoordinates rwc, int cost, int damage, int range, DamageType damageType, TowerType type) {
        super(spriteName, rwc, cost, damage);
        setRange(range);
        setDamageType(damageType);
        isCompleted = false;
        this.type = type;
    }

    /**
     * Increase damage for all towers
     */
    public static void upgradeTowerDamage() {
        globalDamageMultiplier += damageUpgrade;
    }

    /**
     * Decrease the shot cooldown for all towers
     */
    public static void upgradeTowerSpeed() {
        globalSpeedMultiplier *= speedUpgrade;
    }
    
    public int getDamage() {
        return damage * globalDamageMultiplier;
    }
    
    public Boolean getIsCompleted() {
        return isCompleted;
    }
    
    public void setIsCompleted() {
        isCompleted = true;
    }
    
    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
        findPathInRange();
    }
    
    public DamageType getDamageType() {
        return damageType;
    }
    
    public void setDamageType(DamageType damageType) {
        this.damageType = damageType;
    }
    
    //
    public Troop getTarget() {
        return target;
    }

    /**
     * When the range of this tower is changed/set, get a list of all the path tiles that are in range (can be shot at)
     */
    private void findPathInRange() {
        // use range to find all in-range path objects in matrix
        inRange = new ArrayList<Path>();
        
        // List<Coordinates> temp = new ArrayList<Coordinates>();
        MatrixCoordinates matrixco = new MatrixCoordinates(getRealWorldCoordinates());
        
        //System.out.println("matrixco is: " + matrixco.getY() + "," + matrixco.getX());
        MatrixCoordinates tempco;
        MatrixCoordinates defenderbase = new MatrixCoordinates(GameController.defender.getCoordinates());
        //  System.out.println("defenderbase is at: " + defenderbase.getY() + "," + defenderbase.getX());
        
        // creating the numerical bounds for the tiles that would be in range
        
        int y = (matrixco.getY() - getRange());
        int ylimit = (y + 1 + (range * 2));
        // System.out.println("lower bound is:" + y + "upper bound is:" + ylimit);
        int x = (matrixco.getX() - getRange());
        int xlimit = (x + 1 + (range * 2));
        // System.out.println("lower bound is:" + x + "upper bound is:" + xlimit);
        
        int count = 0;
        
        for (int a = y; a < ylimit; a++) {
            for (int b = x; b < xlimit; b++) {
                
                if (GameController.inBounds(a, b)) {
                    //System.out.println("the inRange coords: " + a + "," + b);
                    if (GameController.getMatrixObject(a, b) instanceof Path) {
                        // System.out.println("the path coords: " + a + "," + b);
                        count++;
                    }
                }
            }
        }
        
        //  System.out.println("count is: " + count);
        
        if (count != 0) {
            
            Pair[] temp = new Pair[count];
            
            count = 0;
            
            int ydistance;
            int xdistance;
            
            double distancetemp;
            
            // loop to add the coordinates of the tiles to that are paths to a pair
            // the key of the pair is the coordinates and the value is the distance from the
            // defenders base to the tiles
            
            for (int a = y; a < ylimit; a++) {
                for (int b = x; b < xlimit; b++) {
                    
                    if (GameController.inBounds(a, b)) {
                        
                        if (GameController.getMatrixObject(a, b) instanceof Path) {
                            tempco = new MatrixCoordinates(a, b);
                            
                            ydistance = defenderbase.getY() - tempco.getY();
                            xdistance = defenderbase.getX() - tempco.getX();
                            System.out.println("the path coords: " + a + "," + b);
                            distancetemp = Math.sqrt((ydistance * ydistance) + (xdistance * xdistance));
                            
                            temp[count] = new Pair(tempco, distancetemp);
                            count++;
                        }
                        
                    }
                    
                }
            }
            
            sort(temp, 0, count - 1);
            for (int i = 0; i < count; i++) {
                //   System.out.println(temp[i].getKey() + "," + temp[i].getValue());
                
            }
            
            // loop to add path tiles to arraylist inRange in descending order of distance
            // to defender base
            for (int i = 0; i < temp.length; i++) {
                MatrixCoordinates tco = (MatrixCoordinates) temp[i].getKey();
                
                // had slight issue with the casting should be fine but could be an issue in
                // debugging
                inRange.add((Path) GameController.getMatrixObject(tco.getY(), tco.getX()));
            }
            
            for (int i = 0; i < count; i++) {
                //System.out.println("this is the inRange: " + inRange.get(i).getMatrixPosition());
            }
            
        }
        
    }
    
    public int partition(Pair[] tiles, int l, int h) {
        
        double pivot = (double) tiles[h].getValue();
        int i = (l - 1);
        for (int j = l; j < h; j++) {
            
            if ((double) tiles[j].getValue() < pivot) {
                
                i++;
                
                Pair temp = tiles[i];
                tiles[i] = tiles[j];
                tiles[j] = temp;
            }
        }
        
        Pair temp = tiles[i + 1];
        tiles[i + 1] = tiles[h];
        tiles[h] = temp;
        
        return i + 1;
    }
    
    public void sort(Pair[] tiles, int l, int h) {
        if (l < h) {
            
            int part = partition(tiles, l, h);
            
            sort(tiles, l, part - 1);
            sort(tiles, part + 1, h);
        }
    }

    /**
     * Loop through each tile in the list of in range tiles and return the closest
     */
    private Troop findNearestTroop() {
        Troop temp;
        if (inRange != null && !inRange.isEmpty()) {
            // loop through inRange path objects to find closest troop
            int count = 0;
            
            while (count < inRange.size()) {
                //System.out.println("this is the current tile path" + inRange.get(count).getMatrixPosition());
                if ((inRange.get(count).getTroops()).isEmpty()) {
                    MatrixCoordinates hi = new MatrixCoordinates(8, 5);
                    if (inRange.get(count).getMatrixPosition().equals(hi)) {
                        // System.out.println("fucking knew it was this bitch ass tile");
                    }
                    count++;
                    
                } else {
                    MatrixCoordinates hi = new MatrixCoordinates(8, 5);
                    if (inRange.get(count).getMatrixPosition().equals(hi)) {
                        // System.out.println("fucking knew it was this bitch ass tile pt 2");
                    }
                    // System.out.println("coord of tile: " + inRange.get(count).getMatrixPosition());
                    return inRange.get(count).getTroops().get(0);
                }
            }
            
            return null;
        }
        
        return null;
    }
    
    public void resetTimer() {
        shootTimer = 0;
    }

    /**
     * when the shootTimerLimit has elapsed, find the nearest troop and notify GameController that this tower
     * is shooting at it, then reset the timer
     */
    public void shoot(float deltaTime) {
        shootTimer += deltaTime;
        if (shootTimer > shootTimerLimit / globalSpeedMultiplier) {
            target = findNearestTroop();
            // if target is null, no troops are in range
            if (target != null) {
                MatrixCoordinates temp = new MatrixCoordinates(target.getRealWorldCoordinates());
                //   System.out.println("Current of target is : " + temp);
                GameController.shootTroop(this, target);
            }
            resetTimer();
        }
    }
    
    public TowerType getType() {
        return type;
    }
}
