package edu.javierc.model;

import java.util.concurrent.ForkJoinPool;

/**
 * Created by javierc on 8/31/15.
 */
public class ForkJoinConnection extends Connection
{


  private Grid grid;

  public ForkJoinConnection (Grid grid, int y, int dy)
  {
    this.grid = grid;
  }


  @Override
  public void run ()
  {
    ForkJoinPool pool = new ForkJoinPool();
    while (!interrupted())
    {

      GridTask fb = new GridTask(grid, 0, 999);
      pool.invoke(fb);

      synchronized (this)
      {
        grid.commit();

      }
    }

  }
}
