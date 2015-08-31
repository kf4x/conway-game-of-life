package edu.javierc.model;


import java.util.Random;

public class Grid// extends GridTasks // extends Thread
{
  private boolean[][] volatileGraph;// = new Cell[10000][10000];
  private boolean[][] displayGraph;// = new Cell[10000][10000];
  private int rows = 0;
  private  int cols = 0;


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

//    for (int i = 0; i < rows; i++)
//    {
//
//      for (int j = 0; j < cols; j++)
//      {
//        displayGraph[i][j] = false;
//        volatileGraph[i][j] = false;
//      }
//    }
//    displayGraph[1][2] = true;
//    displayGraph[2][2] = true;
//    displayGraph[3][2] = true;
//    displayGraph[4][2] = true;

//        displayGraph[1][2] = true;
//        displayGraph[2][3] = true;
//        displayGraph[3][1] = true;
//        displayGraph[3][2] = true;
//        displayGraph[3][3] = true;

    //glider
//    displayGraph[1][2] = true;
//    displayGraph[2][3] = true;
//    displayGraph[3][1] = true;
//    displayGraph[3][2] = true;
//    displayGraph[3][3] = true;

    Random random = new Random(3874);


    for(int i = 0; i < rows; i++){
      for(int j = 0; j < cols; j++){
        if(random.nextDouble() > 0.6){
          displayGraph[i][j] = true;
        }
      }
    }

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
      //displayGraph = null;
      displayGraph = getVolatileGraph();
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
}
