package edu.javierc.model;

public abstract class Connection extends Thread
{
  protected int dy;
  protected int y;
  protected int threads;
  protected boolean step;

  public void setThreads(int threads)
  {
    this.threads = threads;
  }

  abstract void step() throws InterruptedException;

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
