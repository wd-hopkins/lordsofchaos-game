package org.lordsofchaos.coordinatesystems;

import org.lordsofchaos.GameController;

public class RealWorldCoordinates extends Coordinates
{
	public RealWorldCoordinates(int y, int x)
	{
		setY(y);
		setX(x);
	}
	
	public RealWorldCoordinates(MatrixCoordinates mc)
	{
		int sf = GameController.getScaleFactor();
		int y = (GameController.getMap().length - mc.getY()) * sf;
		int x = mc.getX() * sf;
		setY(y+32); // offset by 32
		setX(x+32);
	}
}
