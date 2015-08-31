package edu.javierc.model;



public class GridConnectionHandler
{
  private Connection c;
  private Grid grid;
  private ConnectionType conType;


  public GridConnectionHandler (Grid grid, ConnectionType type)
  {
    this.conType = type;
    this.grid = grid;
    setConnection();
  }

  public void start(){
    if (c.getState() == Thread.State.NEW)
    {
      c.start();
    }
    else
    {
      setConnection();
      c.start();
    }
  }

  public boolean isRunning(){
    return c.isAlive();
  }

  public void stop(){
    c.interrupt();
  }


  private void setConnection(){
    if (this.conType == ConnectionType.SIMPLE)
    {
      c = new SimpleThreadedConnection(grid, 0, 1000);
    }
    else if (this.conType == ConnectionType.FORK_JOIN)
    {
      c = new ForkJoinConnection(grid, 0 , 1000);
    }
  }




}
