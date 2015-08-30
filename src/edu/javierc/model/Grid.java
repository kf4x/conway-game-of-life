package edu.javierc.model;


public class Grid implements Runnable
{
  private boolean[][] volatileGraph;// = new Cell[10000][10000];
  private boolean[][] displayGraph;// = new Cell[10000][10000];
  private static int rows = 0;
  private static int cols = 0;
  /*
  what if i extended runnable here
  each class set its bounds
  run the thread

  if volitile is static and display is static bam!

   */
  public Grid ()
  {
    this(10000, 10000);
  }

  public Grid (int row, int col)
  {
    rows = row;
    cols = col;
    displayGraph = new boolean[rows][cols];
    volatileGraph = new boolean[rows][cols];
    for (int i = 0; i < rows; i++)
    {

      for (int j = 0; j < cols; j++)
      {
        displayGraph[i][j] = false;
        volatileGraph[i][j] = false;
      }
    }
//    displayGraph[1][2] = true;
//    displayGraph[2][2] = true;
//    displayGraph[3][2] = true;
//    displayGraph[4][2] = true;

//        displayGraph[1][2] = true;
//        displayGraph[2][3] = true;
//        displayGraph[3][1] = true;
//        displayGraph[3][2] = true;
//        displayGraph[3][3] = true;

    displayGraph[1][2] = true;
    displayGraph[2][3] = true;
    displayGraph[3][1] = true;
    displayGraph[3][2] = true;
    displayGraph[3][3] = true;



    System.out.println("done loading");
    //volatileGraph = displayGraph;


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

  /**
   * This is similar to memory barrier. May compile into memory barrier
   *
   * @param y  int start
   * @param dy int stop
   */
  public void threadSafeUpdate (int y, int dy)
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

  @Override
  public void run() {

  }

  public boolean getCell (int x, int y)
  {
//    return volatileGraph[y][x];
    return displayGraph[y][x];
  }

  public boolean[][] getVolatileGraph ()
  {
      return this.volatileGraph;
  }

  public boolean[][] getGrid ()
  {
    return displayGraph;
  }

  public void commit ()
  {
      displayGraph = null;
      displayGraph = getVolatileGraph();
      volatileGraph = new boolean[rows][cols];
  }


}
