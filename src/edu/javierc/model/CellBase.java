package edu.javierc.model;

/**
 * @author Javier Chavez
 * Most basic representation of a cell
 */

public interface CellBase
{


  class Point {
    private int x, y;

    public Point (int x, int y)
    {
      this.x = x;
      this.y = y;
    }

    public int getX ()
    {
      return x;
    }

    public int getY ()
    {
      return y;
    }

  }

  Point getGraphLocation();

  boolean isAlive();


}
