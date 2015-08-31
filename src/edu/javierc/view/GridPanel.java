package edu.javierc.view;

import edu.javierc.model.Grid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class GridPanel extends JPanel implements ActionListener
{


  private Grid grid;
  private Timer timer = new Timer(160, this);
  private static int zoom = 1;
  private static int viewX = 0, viewY = 0;

  public GridPanel ()
  {
    timer.start();
    init();
  }


  @Override
  public void paint (Graphics g)
  {
    if (grid != null)
    {
      paintCells((Graphics2D) g);
    }
  }

  /**
   * Set a reference to a grid.
   * @param grid reference to a Grid object.
   */
  public void setGrid (Grid grid)
  {
    this.grid = grid;
  }

  @Override
  public void actionPerformed (ActionEvent e)
  {
    if (e.getSource() == timer)
    {
      repaint();
    }
  }

  /**
   * Draws the "view port" that will be displayed as a grid.
   * Also takes into account zoom and dragging
   * @param g Graphics2D object from paint method
   */
  private void paintCells(Graphics2D g){
    super.paintComponent(g);

    int width = getWidth();
    int height = getHeight();

    int cellSizeW = width / zoom;
    int cellSizeH = height / zoom;

    // here we need to determine whether i draw only zoomed or what
    for (int row = viewY; row < zoom; ++row)
    {
      for (int col = viewX; col < zoom; ++col)
      {
        CellView b = new CellView(grid.getCell(row, col), col, row);


        int x = cellSizeW * col;
        int y = cellSizeH * row;

        b.paint(g, x, y, cellSizeW, cellSizeH);

      }
    }
  }
  private void init ()
  {
    MouseActions mouse = new MouseActions();
    addMouseListener(mouse);
    addMouseMotionListener(mouse);
    addMouseWheelListener(mouse);
  }

  /**
   * Inner class that handles actions of the mouse.
   */
  protected class MouseActions extends MouseAdapter
  {
    int previousY, prevX;


    @Override
    public void mouseClicked (MouseEvent e)
    {
      int currCol = e.getX()/ (getWidth() / zoom);
      int currRow = e.getY()/ (getHeight() / zoom);

      grid.toggleCell(currRow, currCol);

    }

    @Override
    public void mousePressed(MouseEvent e) {
      previousY = e.getY();
      prevX = e.getX();




      int width = getWidth();
      int height = getHeight();
      if (e.getX() < 0 || e.getY() < 0 || e.getX() > width || e.getY() > height)
      {
        return;
      }


    }

    @Override
    public void mouseDragged(MouseEvent e) {

      int y = e.getY() ;
      int x = e.getX();

      if (y < 0)
      {
        return;
      }

      if (x < 0)
      {
        return;
      }

      if (y < previousY)
      {
        viewY++;
      }
      else if (y > previousY)
      {
        viewY--;
      }
      previousY = y;

      if (x < prevX)
      {
        viewX--;
      }
      else if (x > prevX)
      {
        viewX++;
      }
      prevX = x;
    }

    @Override
    public void mouseWheelMoved (MouseWheelEvent e)
    {

      int notches = e.getWheelRotation();
      if (notches < 0)
      {
        if (zoom < 50)
        {
          zoom++;
        }
      }
      else
      {
        if (zoom > 1)
        {
          zoom--;
        }
      }
    }
  }

}
