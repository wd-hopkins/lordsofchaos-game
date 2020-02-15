package org.lordsofchaos;

import java.util.ArrayList;
import java.util.List;

import org.lordsofchaos.EventManager.TowerBuild;
import org.lordsofchaos.coordinatesystems.MatrixCoordinates;
import org.lordsofchaos.gameobjects.GameObject;
import org.lordsofchaos.gameobjects.towers.Tower;
import org.lordsofchaos.gameobjects.towers.TowerType1;
import org.lordsofchaos.gameobjects.troops.Troop;
import org.lordsofchaos.matrixobjects.MatrixObject;
import org.lordsofchaos.matrixobjects.Path;
import org.lordsofchaos.matrixobjects.Tile;
import org.lordsofchaos.player.Defender;


public class GameController {

    private static int scaleFactor = 100;
    //Height and Width of the map
    private int height;
    private int width;
    @SuppressWarnings("unused")
	protected static int wave = 1;
    protected static List<Troop> troops = new ArrayList<Troop>();
    protected static List<Tower> towers = new ArrayList<Tower>();

    // 
    private static List<TowerBuild> towerBuilds = new ArrayList<TowerBuild>();
    
    //A list containing different lists that are have the co-ordinates of a paths
    private static List<List<Path>> paths = new ArrayList<List<Path>>();

    //The 2 dimensional array to represent the map
    private static MatrixObject[][] map;
    
    public static int getScaleFactor()
    {
    	return scaleFactor;
    }
    
    public static List<List<Path>> getPaths()
    {
    	return paths;
    }

    
    public void initialise()
    {
    	height = 10;
    	width = 10;
    	wave = 0;
        paths = MapGenerator.generatePaths();
        map = MapGenerator.generateMap(width, height, paths);
        debugVisualiseMap();
    }
    
    public void sendData()
    {
    	// send towerBuilds and unitBuildPlan over netwoek
    	int[][] unitBuildPlan = EventManager.getUnitBuildPlan();
    	
    	// then clear data ready for next turn
    	EventManager.resetBuildPlan();
    	towerBuilds.clear();
    }
    
    private void debugVisualiseMap()
    {
        for (int x = 0; x < width; x++)
        {
            System.out.println();
            for (int y = 0; y < height; y++)
            {
                if (map[y][x].getClass() == Tile.class)
                {
                    System.out.print("@");
                }
                else if (map[y][x].getClass() == Path.class)
                {
                    System.out.print("P");
                }
                else
                {
                    System.out.print("!");
                }
            }   
        }
    }

    public static MatrixObject getMatrixObject(int y, int x) {
        return map[y][x];
    }
    
    public static void shootTroop(Tower tower, Troop troop) {
        //will have to call sound and graphics for shooting at troop
        int temp;
        temp = troop.getCurrentHealth() - tower.getDamage();
        troop.setCurrentHealth(temp);

        if (troop.getCurrentHealth() <= 0) {
            //sound and graphic to remove the troop;
        }
    }

    // called when user attempts to place a tower
    // - could be an illegal place, has yet to be verified
    public static void towerPlaced(TowerBuild tbp)
    {
    	if (!verifyTowerPlacement(tbp))
    	{
    		return;
    	}
    	// convert realWorldCoords to matrix
    	MatrixCoordinates mc = new MatrixCoordinates(tbp.getRealWorldCoordinates());
    	
    	Tower tower = createTower(tbp);

    	Tile tile = (Tile)map[mc.getY()][mc.getX()];
    	tile.setTower(tower);
    	
    	towerBuilds.add(tbp);
    }
    
    private static Tower createTower(TowerBuild tbp)
    {
    	Tower tower = null;
    	if (tbp.getTowerType() == 0)
    	{
    		tower = new TowerType1(tbp.getRealWorldCoordinates());
    	}
    	// other if's to be added when new towers are added
    	return tower;
    }
    
    private static boolean verifyTowerPlacement(TowerBuild tbp) 
    {
    	// convert realWorldCoords to matrix
    	MatrixCoordinates mc = new MatrixCoordinates(tbp.getRealWorldCoordinates());
    	// check if this matrix position is legal
    	MatrixObject mo = map[mc.getY()][mc.getX()];
    	if (mo.getClass() == Path.class)
    	{
    		return false; // cannot place towers on path
    	}
    	else if ((mo.getClass() == Tile.class)
    			&& (((Tile) mo).getTower()) != null)
    	{
    		return false; // else it is a tile, but a tower exists here already
    	}
    	return true;
    }





}