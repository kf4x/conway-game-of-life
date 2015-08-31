package edu.javierc.model;


import java.util.concurrent.ForkJoinPool;

public class GridConnectionHandler
{
  Connection c;
  public GridConnectionHandler (Grid grid)
  {
    c = new Connection(grid, 0 , 1000);

  }

  public void start(){
    c.start();
  }


  private class Connection extends Thread {

    private Grid grid;

    public Connection(Grid grid, int y, int dy)
    {
      this.grid = grid;
    }

    @Override
    public void run ()
    {
      ForkJoinPool pool = new ForkJoinPool();
      while (!interrupted())
      {
        //        GridTasks[] subUpdaters = new GridTasks[4];
        GridTasks fb = new GridTasks(grid, 0, 999);
        pool.invoke(fb);

        synchronized (this)
        {
          grid.commit();

        }
      }

    }

  }


}
