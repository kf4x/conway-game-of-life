package edu.javierc.model;

/**
 * @author Javier Chavez
 * Cell class is a representation of cell. Should be used when pulling data out
 * of the grid.
 */

public class Cell implements CellBase
{
  private Point point;
  private boolean status;

  public Cell ()
  {
    this(false, 0, 0);
  }

  public Cell (boolean life, Point point)
  {
    this.point = point;
    this.status = life;
  }

  public Cell (boolean life, int x, int y)
  {
    this(life, new Point(x, y));
  }

  public Cell (int x, int y)
  {
    this(false, new Point(x, y));
  }


  public void bringAlive ()
  {
    status = true;
  }

  public void setLife (boolean isAlive)
  {
    this.status = isAlive;
  }

  public void toggleLife ()
  {
    status = !status;
  }

  @Override
  public Point getGraphLocation ()
  {
    return point;
  }

  @Override
  public boolean isAlive ()
  {
    return status;
  }
}
