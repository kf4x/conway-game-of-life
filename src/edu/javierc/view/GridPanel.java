package edu.javierc.view;


import edu.javierc.model.GridConnectionHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.VolatileImage;


public class GridPanel extends JPanel implements ActionListener
{

  private GridConnectionHandler connectionHandler;
  private Timer timer = new Timer(120, this);
  private static int zoom = 1;
  private static int viewX = 0, viewY = 0;
  private VolatileImage image;

  public GridPanel ()
  {
    timer.start();
    init();
  }

  private void createBackBuffer ()
  {
    GraphicsConfiguration gc = getGraphicsConfiguration();
    image = gc.createCompatibleVolatileImage(getWidth(), getHeight());
  }

  @Override
  public void paint (Graphics g)
  {
    createBackBuffer();
    do
    {

      GraphicsConfiguration gc = this.getGraphicsConfiguration();
      int valCode = image.validate(gc);

      // This means the device doesn't match up to this hardware accelerated image.
      if (valCode == VolatileImage.IMAGE_INCOMPATIBLE)
      {
        createBackBuffer(); // recreate the hardware accelerated image.
      }

      Graphics offscreenGraphics = image.getGraphics();
      offscreenGraphics.setColor(Color.BLACK);
      offscreenGraphics.fillRect(0, 0, getWidth(), getHeight());
      paintCells(offscreenGraphics);

      // paint back buffer to main graphics
      g.drawImage(image, 0, 0, null);

    }
    while (image.contentsLost());


  }

  /**
   * Set a reference to a connectionHandler.
   *
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
   */
  private void paintCells (Graphics g)
  {

    int cellsX = getWidth() / zoom;
    int cellsY = getHeight() / zoom;

    for (int row = 0; row < cellsY; ++row)
    {
      for (int col = 0; col < cellsX; ++col)
      {

        int x = zoom * col;
        int y = zoom * row;

        boolean cellValue = connectionHandler.getCellValue(col + viewX,
                                                           row + viewY);
        CellView cell = new CellView(cellValue, col + viewX, row + viewY);

        if (zoom >= 5)
        {
          cell.paint(g, x, y, zoom, zoom, true);
        }
        else
        {
          cell.paint(g, x, y, zoom, zoom, false);
        }
      }
    }
  }


  private void init ()
  {
    MouseActions mouse = new MouseActions();
    addMouseListener(mouse);
    addMouseMotionListener(mouse);
    addMouseWheelListener(mouse);
    setBackground(Color.BLACK);
  }

  /**
   * Inner class that handles actions of the mouse.
   */
  protected class MouseActions extends MouseAdapter
  {
    Point mouseDownPoint;


    @Override
    public void mouseClicked (MouseEvent e)
    {
      int currCol = e.getX() / (getWidth() / zoom);
      int currRow = e.getY() / (getHeight() / zoom);
      connectionHandler.toggleCell(currCol, currRow);
    }

    @Override
    public void mousePressed (MouseEvent e)
    {
      mouseDownPoint = e.getPoint();
    }


    @Override
    public void mouseDragged (MouseEvent e)
    {
      // setting the sensitivity to the width of the square
      int graphicalTranslation = (zoom <= 10) ? getWidth() : zoom;
      int SENSITIVITY = getWidth() / graphicalTranslation;
      int cellsX = getWidth() / zoom;
      int cellsY = getHeight() / zoom;


      boolean horizontalTravel = mouseDownPoint.getX() > e.getX();
      boolean verticalTravel = mouseDownPoint.getY() > e.getY();

      boolean inRangeX = Math.abs(
              mouseDownPoint.getX() - e.getX()) > SENSITIVITY;
      boolean inRangeY = Math.abs(
              mouseDownPoint.getY() - e.getY()) > SENSITIVITY;


      if (inRangeX && horizontalTravel && inRangeY && verticalTravel)
      {
        viewX = (1999 <= (cellsX+viewX+20)) ? viewX : (viewX + 20);
        viewY = (1999 <= (cellsY+viewY +20)) ? viewY : (viewY + 20);
        return;
      }

      if (inRangeX && !horizontalTravel && inRangeY && !verticalTravel)
      {
        viewX = (0 <= (viewX -20)) ? (viewX - 20) : 0;
        viewY = (0 <= (viewY-20)) ? (viewY - 20) : 0;

        return;
      }
      if (inRangeX && !horizontalTravel)
      {
        viewX = (0 <= (viewX -20)) ? (viewX - 20) : 0;
        return;

      }
      if (inRangeY && verticalTravel)
      {
        viewY = (1999 <= (cellsY+viewY +20)) ? viewY : (viewY + 20);
        return;
      }
      if (inRangeX && horizontalTravel)
      {
        viewX = (1999 <= (cellsX+viewX+20)) ? viewX : (viewX + 20);
        return;
      }
      if (inRangeY && !verticalTravel)
      {
        viewY = (0 <= (viewY-20)) ? (viewY - 20) : 0;
        return;
      }
    }

    @Override
    public void mouseWheelMoved (MouseWheelEvent e)
    {
      int notches = e.getWheelRotation();

      if (e.isShiftDown())
      {
        if (notches < 0)
        {
          ++zoom;
        }
      }

      else if (notches < 0 && zoom < 50)
      {

        // zoom out
        if (zoom < 50)
        {
          ++zoom;
        }
      }
      else if (notches > 0 && zoom >= 2)
      {
        // zoom in
        if (zoom > 1)
        {
          --zoom;
        }
      }
    }
  }

}
