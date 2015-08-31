package edu.javierc.model;


import java.util.concurrent.RecursiveAction;

public class GridTask extends RecursiveAction
{


  private boolean[][] displayGraph;
  private boolean[][] volatileGraph;
  private Grid grid;
  private int rows;
  private int cols;
  private int y, dy;

  public GridTask (Grid display, int y, int dy)
  {
    this.grid= display;
    this.displayGraph = display.getGrid();
    this.volatileGraph = display.getVolatileGraph();
    this.cols = display.getWidth();
    this.rows = display.getHeight();
    this.y = y;
    this.dy = dy;
  }

  public int surrounding (int x, int y)
  {

    int count = 0;
    int[][] directions = {{-1, 0}, // down a row
            {1, 0}, // up a row
            {0, -1}, // left
            {0, 1},  // right
            {1, 1}, // up to right
            {1, -1}, // up to left
            {-1, -1}, // down to left
            {-1, 1}, // down to right
    };

    for (int[] direction : directions)
    {
      int row = direction[1] + y;
      int col = direction[0] + x;

      if (displayGraph[row][col])
      {
        count++;
      }

    }
    return count;
  }

  public void update ()
  {

    for (int i = y; i <= dy; i++)
    {

      if (i == 0)
      {
        continue;
      }
      else if (i == rows - 1)
      {
        continue;
      }


      for (int j = 0; j < cols; j++)
      {

        if (j == 0)
        {
          continue;
        }
        else if (j == cols - 1)
        {
          continue;
        }


        boolean cell = displayGraph[i][j];
        int neighbors = surrounding(j, i);

        if (cell)
        {
          // System.out.println("kd");
          if (neighbors < 2 || neighbors > 3)
          {

            volatileGraph[i][j] = false;
          }
          else
          {
            volatileGraph[i][j] = true;
          }
        }
        else
        {

          if (neighbors == 3)
          {
            volatileGraph[i][j] = true;
          }
          else
          {
            volatileGraph[i][j] = false;
          }
        }
      }
    }
  }
  protected static int sThreshold = 1000;

  @Override
  protected void compute ()
  {
    if ((dy-y) < sThreshold) {
      update();
      return;
    }

    int split = (dy-y) / 2;

    invokeAll(new GridTask(grid, y, split),
              new GridTask(grid, y + split, (dy-y) - split));
  }

}
