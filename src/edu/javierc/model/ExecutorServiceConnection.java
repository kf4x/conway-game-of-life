package edu.javierc.model;
/**
 * @author Javier Chavez
 * Uses the ExecutorService to update the grid. Once updated the chages are
 * commited.
 * The useage should be looked at in GridConnectionHandler
 */

import java.util.ArrayList;
import java.util.concurrent.*;


public class ExecutorServiceConnection extends Connection
{
  private ExecutorService pool;
  private Grid grid;
  private GridThread[] gridThreads;
  public ExecutorServiceConnection (Grid grid, int threads)
  {
    super(grid, threads);
    this.grid = grid;
    this.gridThreads = new GridThread[threads];
    pool = Executors.newFixedThreadPool(threads);

    // set up the threads to minimize thread creation overhead
    for (int i = 0; i < threads; i++)
    {
      if (threads-1 == i)
      {
        gridThreads[i] = new GridThread(grid,
                                        i * dy,
                                        ((dy * (i + 1)) + overflow) - 1);
      }
      else
      {
        gridThreads[i] = new GridThread(grid,
                                        i * dy,
                                        (dy * (i + 1)) - 1);
      }
    }
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
    ArrayList<Future<?>> submit = new ArrayList<>(threads);

    for (int i = 0; i < threads; i++)
    {
      submit.add(pool.submit(gridThreads[i]));
    }

    for (int i = 0; i < threads; i++)
    {

      try
      {
        submit.get(i).get();
      }
      catch (InterruptedException e)
      {
        this.interrupt();
      }
      catch (ExecutionException e)
      {
      }
    }

    if (!isInterrupted())
    {
      grid.commit();
    }

  }

}
