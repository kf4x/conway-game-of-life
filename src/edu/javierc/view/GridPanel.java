package edu.javierc.view;


import edu.javierc.model.GridConnectionHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class GridPanel extends JPanel implements ActionListener
{

  private GridConnectionHandler connectionHandler;
  private Timer timer = new Timer(120, this);
  private static int zoom = 50;
  private static int viewX = 0, viewY = 0;

  public GridPanel ()
  {
    timer.start();
    init();
  }


  @Override
  public void paint (Graphics g)
  {
    if (connectionHandler != null)
    {
      paintCells((Graphics2D) g);
    }
  }

  /**
   * Set a reference to a connectionHandler.
   * @param connectionHandler reference to a Grid object.
   */
  public void setConnectionHandler (GridConnectionHandler connectionHandler)
  {
    this.connectionHandler = connectionHandler;
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
   * Draws the "view port" that will be displayed as a connectionHandler.
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

        boolean cellValue = connectionHandler.getCellValue(row, col);
        CellView cell = new CellView(cellValue, col, row);


        int x = cellSizeW * col;
        int y = cellSizeH * row;
        if (zoom > 50)
        {
          cell.paint(g, x, y, cellSizeW, cellSizeH, false);
        }
        cell.paint(g, x, y, cellSizeW, cellSizeH, true);


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
    int previousY, previousX;


    @Override
    public void mouseClicked (MouseEvent e)
    {
      int currCol = e.getX()/ (getWidth() / zoom);
      int currRow = e.getY()/ (getHeight() / zoom);

      connectionHandler.toggleCell(currRow, currCol);

    }

    @Override
    public void mousePressed(MouseEvent e) {
      previousY = e.getY();
      previousX = e.getX();

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
      int cellWidth = getWidth()/zoom;
      int cellHeight = getHeight()/zoom;

      if(Math.abs(e.getX()-previousX)
              >= cellWidth || Math.abs(e.getY()-previousY) >= cellHeight)
      {
        if (Math.abs(e.getX()-previousX) >= cellWidth)
        {
          viewX--;
        }
        else
        {
          viewX++;
        }

        if (Math.abs(e.getY()-previousY) >= cellHeight)
        {
          viewY--;
        }
      }



    }

    @Override
    public void mouseWheelMoved (MouseWheelEvent e)
    {

      int notches = e.getWheelRotation();
      if (e.isShiftDown())
      {
        zoom++;
        return;
      }

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
