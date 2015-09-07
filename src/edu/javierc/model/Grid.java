package edu.javierc.model;

/**
 * @author Javier Chavez
 * POJO that represents the grid. The underlying board is represented as a
 * boolean matrix. true if cell is alive false if dead
 *
 * Useage:
 *  Grid g = new Grid();
 */

import java.util.Random;

public class Grid
{
  private volatile boolean[][] volatileGraph;// = new Cell[10000][10000];
  private volatile boolean[][] displayGraph;// = new Cell[10000][10000];
  private int rows = 0;
  private int cols = 0;
  private long lastTime = 0;
  private double cps;
  private boolean seeded = false;

  /**
   * Creates a 10k X 10k grid.
   */
  public Grid ()
  {
    this(10000, 10000);
  }

  /**
   * Creates a grid based on specified params. This will automatically seed the
   * grid with random values.
   * @param rows number of rows grid should have.
   * @param cols number of cols grid should have.
   */
  public Grid (int rows, int cols)
  {
    this.rows = rows;
    this.cols = cols;
    displayGraph = new boolean[rows][cols];
    volatileGraph = new boolean[rows][cols];

  }

  /**
   * Creates a grid based on specified params.
   * @param rows number of rows grid should have.
   * @param cols number of cols grid should have.
   * @param seedGrid true will seed the grid, false will skip seed.
   */
  public Grid (int rows, int cols, boolean seedGrid)
  {
    this(rows, cols);

    if (seedGrid)
    {
      seed();
    }
    else{
      for (int i = 0; i < rows; i++)
      {
        for (int j = 0; j < cols; j++)
        {
          displayGraph[i][j] = false;
        }
      }
    }
  }

  /**
   * Get the number of alive cells in grid. Checks all 8 locations.
   *        XXX
   *        XOX
   *        XXX
   * @param x col
   * @param y row
   * @return count of how many alive cells.
   */
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
   * Merge one grid into another
   * @param grid Grid object that will overwrite current grid
   */
  public void mergeGrid(Grid grid)
  {
    this.replaceMatrix(grid.getGrid(), 0, 0);
  }

  /**
   * Merge a matrix into current matrix
   * @param matrix boolean matrix
   * @param x column offset (from top left)
   * @param y row offset (from top left)
   */
  public void replaceMatrix (boolean[][] matrix, int x, int y)
  {
    int height = matrix.length;
    int width = matrix[0].length;

    // Should throw an exception the grid being merged is too big
    if (height > this.rows || width > this.cols)
    {
      System.out.println("Too Big for current matrix");
      return;
    }

    this.rows = Math.max(height, rows);
    this.cols = Math.max(width, cols);
    displayGraph = new boolean[rows][cols];
    volatileGraph = new boolean[rows][cols];

    for (int i = y; i < height; i++)
    {
      for (int j = x; j < width; j++)
      {

        displayGraph[i][j] = matrix[i][j];
      }
    }

  }

  /**
   * Get a cells boolean value
   * @param x column
   * @param y row
   * @return true if alive false if dead
   */
  public boolean getCell (int x, int y)
  {
      return displayGraph[y][x];

  }

  /**
   * Get the graph that is currently used to be updated
   * @return matrix of booleans true if alive false if dead
   */
  public boolean[][] getVolatileGraph ()
  {
      return this.volatileGraph;
  }

  /**
   * Grid that is ready to be displayed to user.
   * @return matrix of booleans true if alive false if dead
   */
  public boolean[][] getGrid ()
  {
    return displayGraph;
  }

  /**
   * Commit the changes that update did.
   */
  public void commit ()
  {
    displayGraph = volatileGraph;
    volatileGraph = new boolean[rows][cols];
    cps = 1000000000.0 / (System.nanoTime() - lastTime);
    lastTime = System.nanoTime();
  }

  /**
   * Set a cell's value
   * @param value true if alive false if dead
   * @param x column
   * @param y row
   */
  public void setCell(boolean value, int x, int y)
  {
    displayGraph[y][x] = value;
  }

  /**
   * Toggle a cell's life. i.e. if a cell is alive, toggling it will set it
   * to dead.
   * @param x column
   * @param y row
   */
  public void toggleCell(int x, int y)
  {
    displayGraph[y][x] = !displayGraph[y][x];
  }

  /**
   * Width of grid
   * @return number of columns in the matrix
   */
  public int getWidth ()
  {
    return cols;
  }

  /**
   * Height of grid
   * @return number of row in the matrix
   */
  public int getHeight ()
  {
    return rows;
  }

  /**
   * Update a specified chunk of rows using Conway's algorithm.
   * @param y starting row
   * @param dy ending row
   */
  public void update (int y, int dy)
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
        boolean cell = this.getCell(j, i);
        int neighbors = this.surrounding(j, i);

        if (cell)
        {
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

  /**
   * Returns the time it took for a commit.
   * @return number of commits that went through in one second.
   */
  public double getCommitTime ()
  {
    return cps;
  }

  private void seed ()
  {
    Random random = new Random();
    seeded = true;

    for(int i = 0; i < rows; i++){
      for(int j = 0; j < cols; j++){
        if(random.nextDouble() > 0.5){
          displayGraph[i][j] = true;
        }
        else{
          displayGraph[i][j] = false;
        }
      }
    }
  }

  public boolean isSeeded ()
  {
    return seeded;
  }

  @Override
  public String toString ()
  {
    return this.getWidth() + " " + this.getHeight();
  }
}
