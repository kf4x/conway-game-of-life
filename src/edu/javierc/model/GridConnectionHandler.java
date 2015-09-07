package edu.javierc.model;
/**
 * @author Javier Chavez
 * Layer of abstraction to handle the creation of conections. Connections are
 * the way threads and the model are connected. By using a Connection it allows
 * the user to choose what type of thread handling the model should be
 * updated with.
 */


public class GridConnectionHandler
{
  private Connection c;
  private Grid grid;
  private ConnectionType conType;
  private int threads;
  private Dimension dimension;

  public GridConnectionHandler(Grid grid, ConnectionType type)
  {
    this(grid, type, 4);
  }

  public GridConnectionHandler (Grid grid, ConnectionType type, int threads)
  {
    this.threads = threads;
    this.conType = type;
    this.grid = grid;
    dimension = new Dimension(grid.getWidth(), grid.getHeight());
    setConnection();
  }

  public ConnectionType getConnectionType()
  {
    return conType;
  }

  public double getCommitTime ()
  {
    return grid.getCommitTime();
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

  /**
   * Set the connection to use
   * @param connection
   */
  public void setConnection(Connection connection)
  {
    c = connection;
  }

  /**
   * Remove the current connection. Sets connection to null
   */
  public void removeConnection()
  {
    c = null;
  }

  /**
   * Check if a connection exists
   * @return true if exists false if connection is null
   */
  public boolean isConnected()
  {
    return !(c == null);
  }

  /**
   * get thread safe cell value
   * @param x column
   * @param y row
   * @return value of cell true if alive false if dead
   */
  public synchronized boolean getCellValue(int x, int y)
  {
    return grid.getCell(x, y);
  }

  /**
   * Get the dimension of the grid
   * @return rows and cols of grid
   */
  public Dimension getDimension ()
  {
    return dimension;
  }

  public synchronized void toggleCell(int x, int y)
  {
    grid.toggleCell(x, y);
  }

  public boolean isRunning()
  {
    return c.isAlive();
  }

  public void stop()
  {
    c.interrupt();
  }


  public void setPreferredThreads (int preferredThreads)
  {
    this.threads = preferredThreads;
    setConnection();
  }

  /**
   * Only step once
   */
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

  public static class Dimension
  {
    private int x, y;

    public Dimension (int x, int y)
    {
      this.x = x;
      this.y = y;
    }

    public int getX ()
    {
      return x;
    }

    public int getY ()
    {
      return y;
    }
  }

  private void setConnection()
  {
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
    System.out.println(
            "[INFO]\n" + "\t Connection: " + conType +
                    "\n" + "\t Threads: " + threads + "\n" +
                    "\t Grid: ("+ grid.getWidth() + " x " +
                    grid.getWidth() + ")");
  }
}
