package edu.javierc.model;

/**
 * @author Javier Chavez
 * Updating grid with thread. The commit of the update needs to be handled
 * somewhere else.
 */

public class GridThread extends Thread
{

  private final Grid grid;
  private int y, dy;

  public GridThread (Grid display, int y, int dy)
  {
    this.grid = display;
    this.y = y;
    this.dy = dy;
  }

  @Override
  public void run ()
  {
    grid.update(y, dy);
  }

}
