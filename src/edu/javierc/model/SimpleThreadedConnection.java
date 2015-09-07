package edu.javierc.model;

/**
 * @author Javier Chavez
 * Uses the Simple threads to update the grid. Once updated the chages are
 * commited.
 * The useage should be looked at in GridConnectionHandler
 */


public class SimpleThreadedConnection extends Connection
{
  private GridThread[] tasks;

  public SimpleThreadedConnection (Grid grid, int threads)
  {
    super(grid, threads);
    tasks = new GridThread[threads];
  }


  @Override
  public void run ()

  {
    try
    {
      while (!isInterrupted())
      {
        step();
      }
    }
    catch (InterruptedException e)
    {
    }
  }

  @Override
  void step () throws InterruptedException
  {
    // creating threads since waiting poses a risk of the notify being
    // missed and synchronising takes a hamper on performance.

    for (int i = 0; i < tasks.length; i++)
    {
      if (threads - 1 == i)
      {
        tasks[i] = new GridThread(grid, i * dy, ((dy * (i + 1)) + overflow) - 1);
      }
      else
      {
        tasks[i] = new GridThread(grid, i * dy, (dy * (i + 1)) - 1);
      }
      tasks[i].start();
    }

    // this will not pass until all threads are complete
    for (GridThread task : tasks)
    {
      task.join();
    }

    // once all threads are done commit
    if (!isInterrupted())
    {
      grid.commit();
    }



  }
}

