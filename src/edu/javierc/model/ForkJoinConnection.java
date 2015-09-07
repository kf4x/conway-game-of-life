package edu.javierc.model;
/**
 * @author Javier Chavez
 * This is the implementation using ForkJoin
 */

import java.util.concurrent.ForkJoinPool;

public class ForkJoinConnection extends Connection
{
  private ForkJoinPool pool;
  private Grid grid;

  public ForkJoinConnection (Grid grid, int threads)
  {
    super(grid, threads);
    this.grid = grid;
    pool = new ForkJoinPool(threads);
  }


  @Override
  public void run ()
  {
    while (!isInterrupted())
    {
      step();
    }
  }

  @Override
  public void step ()
  {
    GridTask task = new GridTask(grid, 0, grid.getHeight() - 1);
    pool.invoke(task);

    synchronized (this)
    {
      if (!isInterrupted())
      {
        grid.commit();
      }
    }
  }
}
