package edu.javierc.model;
/**
 * @author Javier Chavez
 * Recursive Action is to be used with a ForkJoin. This takes in the entire
 * board and recursively splits it into small tasks. The threshold is what
 * determins wheather the rows needs to be split into smaller chunks.
 */

import java.util.concurrent.RecursiveAction;

public class GridTask extends RecursiveAction
{
  protected static int threshold = 100;
  private Grid grid;
  private int y, dy;

  public GridTask (Grid display, int y, int dy)
  {
    this.grid = display;
    this.y = y;
    this.dy = dy;
  }

  @Override
  protected void compute ()
  {

    if ((dy-y) <= threshold)
    {

      grid.update(y, dy);
      return;
    }

    // Merge sort type split
    int mid = (int) Math.floor(((double)y+dy) / 2);

    invokeAll(new GridTask(grid, y, mid),
              new GridTask(grid, mid+1, dy));

  }

}
