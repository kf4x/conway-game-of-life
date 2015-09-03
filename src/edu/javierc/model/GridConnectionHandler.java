package edu.javierc.model;



public class GridConnectionHandler
{
  private Connection c;
  private Grid grid;
  private ConnectionType conType;
  private int threads;

  public GridConnectionHandler(Grid grid, ConnectionType type)
  {
    this(grid, type, 4);
  }

  public GridConnectionHandler (Grid grid, ConnectionType type, int threads)
  {
    this.threads = threads;
    this.conType = type;
    this.grid = grid;
    setConnection();
    System.out.println(grid.getWidth() + " " + grid.getHeight());
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


  public void setConnection(Connection connection)
  {
    c = connection;
  }

  public void removeConnection()
  {
    c = null;
  }

  public boolean isConnected(){
    return !(c == null);
  }

  public synchronized boolean getCellValue(int x, int y)
  {
    return grid.getCell(x, y);

  }

  public synchronized void toggleCell(int x, int y)
  {
    grid.toggleCell(x, y);
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
      c = new SimpleThreadedConnection(grid, threads);
    }
    else if (this.conType == ConnectionType.FORK_JOIN)
    {
      c = new ForkJoinConnection(grid, threads);
    }
    else if (this.conType == ConnectionType.EXECUTOR)
    {
      c = new ExecutorServiceConnection(grid, threads);
    }
  }

  public void setPreferredThreads (int preferredThreads)
  {
    this.threads = preferredThreads;
    setConnection();
  }

  public void step ()
  {
    try
    {
      c.step();
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
  }
}
