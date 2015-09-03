package edu.javierc.model;



public class SimpleThreadedConnection extends Connection
{

  private GridThread[] tasks;


  public SimpleThreadedConnection (Grid grid)
  {
    this(grid, 4);
  }

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
    catch (InterruptedException e) { }
  }

  @Override
  void step () throws InterruptedException
  {
    for (int i = 0; i < tasks.length; i++)
    {
      if (threads-1 == i)
      {
        tasks[i] = new GridThread(grid, i * dy, ((dy*i+1)+ overflow) - 1);
      }
      else
      {
        tasks[i] = new GridThread(grid, i * dy, (dy*(i+1)) - 1);
      }
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

