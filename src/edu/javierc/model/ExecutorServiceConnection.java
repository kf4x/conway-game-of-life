package edu.javierc.model;

import java.util.concurrent.*;

/**
 * Created by javierc on 9/2/15.
 */
public class ExecutorServiceConnection extends Connection
{
  private ExecutorService pool;
  private Grid grid;
  public ExecutorServiceConnection (Grid grid, int threads)
  {
    super(grid, threads);
    this.grid = grid;
    pool = Executors.newFixedThreadPool(threads);

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
    Future<?> submit = null;

    for (int i = 0; i < threads; i++)
    {
      if (threads-1 == i)
      {
        submit = pool.submit(
                new GridThread(grid, i * dy, ((dy * (i + 1)) + overflow) - 1));
      }
      else
      {
        submit = pool.submit(new GridThread(grid, i * dy, (dy * (i + 1)) - 1));
      }
    }

    try
    {
      submit.get();
    }
    catch (InterruptedException e)
    {
      this.interrupt();
    }
    catch (ExecutionException e) { }
    synchronized (this)
    {
      if (!isInterrupted())
      {
        grid.commit();
      }
    }
  }

}
