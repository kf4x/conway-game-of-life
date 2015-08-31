package edu.javierc.model;



public class GridConnectionHandler
{
  private Connection c;
  private Grid grid;
  private ConnectionType conType;


  public GridConnectionHandler (Grid grid, ConnectionType type)
  {
    this.conType = type;
    c = getConnection();
    this.grid = grid;
  }

  public void start(){
    if (c.getState() == Thread.State.NEW)
    {
      c.start();
    }
    else
    {
      c = getConnection();
      c.start();
    }
  }

  public boolean isRunning(){
    return c.isAlive();
  }

  public void stop(){
    c.interrupt();
  }


  private Connection getConnection(){
    if (this.conType == ConnectionType.SIMPLE)
    {
      return new SimpleThreadedConnection(grid, 0, 1000);
    }
    else if (this.conType == ConnectionType.FORK_JOIN)
    {

    }

    return null;
  }




}
