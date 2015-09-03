package edu.javierc.model;


import java.util.Random;

public class Grid
{
  private volatile boolean[][] volatileGraph;// = new Cell[10000][10000];
  private volatile boolean[][] displayGraph;// = new Cell[10000][10000];
  private int rows = 0;
  private int cols = 0;


  public Grid ()
  {
    this(10000, 10000);
  }

  public Grid (int rows, int cols)
  {

    this.rows = rows;
    this.cols = cols;
    displayGraph = new boolean[rows][cols];
    volatileGraph = new boolean[rows][cols];

    seed();
  }

  public Grid (int rows, int cols, boolean seedGrid)
  {
    this.rows = rows;
    this.cols = cols;
    displayGraph = new boolean[rows][cols];
    volatileGraph = new boolean[rows][cols];

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

  public void mergeGrid(Grid grid)
  {
    int height = grid.getHeight();
    int width = grid.getWidth();

    // Should throw an exception the grid being merded is too big
    if (height > this.rows || width > this.cols)
    {
      return;
    }
    for (int i = 0; i < height; i++)
    {
      for (int j = 0; j < width; j++)
      {
        displayGraph[i][j] = grid.getCell(j,i);
      }
    }

  }

  public void mergeMatrix(boolean[][] matrix, int x, int y)
  {
    int height = matrix.length;
    int width = matrix[0].length;

    // Should throw an exception the grid being merded is too big
    if (height > this.rows || width > this.cols)
    {
      System.out.println("Too Big for current matrix");
      return;
    }

    this.rows = height;
    this.cols = width;

    for (int i = y; i < height; i++)
    {
      for (int j = x; j < width; j++)
      {

        displayGraph[i][j] = matrix[i][j];
      }
    }

  }

  public boolean getCell (int x, int y)
  {
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
      //displayGraph = null;
      displayGraph = volatileGraph;

      volatileGraph = new boolean[rows][cols];
  }

  public void setCell(boolean value, int x, int y)
  {
    displayGraph[y][x] = value;
  }

  public void toggleCell(int x, int y)
  {
    displayGraph[y][x] = !displayGraph[y][x];
  }

  public int getWidth ()
  {
    return cols;
  }

  public int getHeight ()
  {
    return rows;
  }

  private void seed ()
  {
    Random random = new Random();


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


}
