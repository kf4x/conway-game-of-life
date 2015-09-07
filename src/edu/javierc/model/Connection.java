package edu.javierc.model;

/**
 * @author Javier Chavez
 *
 */

public abstract class Connection extends Thread
{
  protected final int overflow;
  protected final int dy;
  protected int y;
  protected final int threads;
  protected Grid grid;


  abstract void step() throws InterruptedException;

  public Connection(Grid grid, int threads)
  {
    this.grid = grid;
    this.threads = threads;

    int arrayLength = grid.getHeight();
    dy = (int) Math.ceil( ((double)arrayLength) / threads);
    overflow = (arrayLength % threads);
  }

//  abstract boolean getCell (int x, int y);
//
//  abstract boolean[][] getVolatileGraph ();
//
//  abstract boolean[][] getGrid ();
//
//  abstract void commit ();
//
//  abstract void setCell(boolean value, int x, int y);
//
//  abstract void toggleCell(int x, int y);
//
//  abstract int getWidth ();
//
//  abstract int getHeight ();
}
