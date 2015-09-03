package edu.javierc.model;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;


public class ForkJoinConnection extends Connection
{
  private ForkJoinPool pool;
  private Grid grid;
  private GridTask task;
  public ForkJoinConnection (Grid grid, int threads)
  {
    super(grid, threads);
    this.grid = grid;
    pool = new ForkJoinPool(threads);
    this.task = new GridTask(grid);
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
    task = new GridTask(grid);
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
