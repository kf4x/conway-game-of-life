package edu.javierc.model;


public class SimpleThreadedConnection extends Connection
{

  private Grid grid;
  private GridThread[] tasks;

  public SimpleThreadedConnection (Grid grid)
  {
    this(grid, 4);
  }
  public SimpleThreadedConnection (Grid grid, int threads)
  {
    this.grid = grid;
    this.threads = threads;
    tasks  = new GridThread[threads];
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
    catch (InterruptedException e) { }
  }

  @Override
  void step () throws InterruptedException
  {
    for (int i = 0; i < tasks.length; i++)
    {
      int max = grid.getHeight();
      int f = i == 0 ? 0 : 1;
      int fy = i == tasks.length - 1 ? tasks.length : i + 1;
      int start = (i * (max / tasks.length)) + f;
      int end = (fy * (max / tasks.length)) + max % tasks.length;
      end = i == tasks.length - 1 ? end - 1 : end;

      tasks[i] = new GridThread(grid, start, end);
      tasks[i].start();
    }

    for (GridThread task : tasks)
    {
      task.join();
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

