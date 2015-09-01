package edu.javierc.model;

import java.util.concurrent.ForkJoinPool;


public class ForkJoinConnection extends Connection
{
  private ForkJoinPool pool = new ForkJoinPool();
  private Grid grid;
  private GridTask task;
  public ForkJoinConnection (Grid grid)
  {
    this.grid = grid;
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
