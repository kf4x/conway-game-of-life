package edu.javierc.model;


public class SimpleThreadedConnection extends Connection
{


  private Grid grid;

  public SimpleThreadedConnection (Grid grid, int y, int dy)
  {
    this.grid = grid;
  }

  @Override
  public void run ()
  {
    try
    {
      while (!isInterrupted())
      {

        GridThread[] tasks = new GridThread[4];

        for (int i = 0; i < tasks.length; i++)
        {
          int max = grid.getHeight();
          int f = i == 0 ? 0 : 1;
          tasks[i] = new GridThread(grid, i * max + f, (max / tasks.length) + max % tasks.length);
          tasks[i].start();
        }

        for (int i = 0; i < tasks.length; i++)
        {
          tasks[i].join();
        }

        synchronized (this)
        {
          if (!isInterrupted())
          {
            grid.commit();
          }

        }
      }
    }
    catch (InterruptedException e)
    {
    }
  }
}

