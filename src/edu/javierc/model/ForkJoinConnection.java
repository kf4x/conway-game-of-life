package edu.javierc.model;

import java.util.concurrent.ForkJoinPool;


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
    while (!isInterrupted())
    {

    GridTask fb = new GridTask(grid, 0, 999);
    pool.invoke(fb);

    synchronized (this)
    {
      if (!isInterrupted())
      {
        grid.commit();
      }
    }
  }
  }
}
