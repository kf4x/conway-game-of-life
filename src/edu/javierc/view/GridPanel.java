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
  private static int zoom = 50;
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
    //super.paintComponent(g);

    int width = getWidth();
    int height = getHeight();

    int cellSizeW = width / (zoom);
    int cellSizeH = height / (zoom);
    if(zoom >= 15){
      g.translate(cellSizeH+2, cellSizeW+2);

    }

    // here we need to determine whether i draw only zoomed or what
    for (int row = 0; row < zoom; ++row)
    {
      for (int col = 0; col < zoom; ++col)
      {
        int x = cellSizeW * col;
        int y = cellSizeH * row;

        if (((x + cellSizeW+2) >= width-cellSizeW ||
                (y + cellSizeH+2) >= height-cellSizeH) && zoom > 15)
        {
          break;
        }

        boolean cellValue = connectionHandler.getCellValue(col + viewX,
                                                           row + viewY);
        CellView cell = new CellView(cellValue, col + viewX, row + viewY);


        if (zoom >= 51)
        {
          cell.paint(g, x, y, cellSizeW, cellSizeH, false);

        }
        else
        {
          cell.paint(g, x, y, cellSizeW, cellSizeH, true);
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
      int graphicalTranslation = (zoom <= 5) ? 5 : zoom;
      int SENSITIVITY = getWidth() / graphicalTranslation;

      boolean horizontalTravel = mouseDownPoint.getX() > e.getX();
      boolean verticalTravel = mouseDownPoint.getY() > e.getY();

      boolean inRangeX = Math.abs(
              mouseDownPoint.getX() - e.getX()) > SENSITIVITY;
      boolean inRangeY = Math.abs(
              mouseDownPoint.getY() - e.getY()) > SENSITIVITY;


      if (inRangeX && horizontalTravel && inRangeY && verticalTravel)
      {
        viewX++;
        viewY++;
        return;
      }
      if (inRangeX && horizontalTravel)
      {
        // increment horizontal
        viewX++;
        return;
      }
      if (inRangeX && !horizontalTravel)
      {

        viewX = (viewX <= 0) ? 0 : (viewX - 1);
        return;

      }
      if (inRangeY && verticalTravel)
      {
        viewY++;
        repaint();
        return;
      }

      if (inRangeY && !verticalTravel)
      {
        viewY = (viewY <= 0) ? 0 : (viewY - 1);
        repaint();
        return;
      }
    }

    @Override
    public void mouseWheelMoved (MouseWheelEvent e)
    {

      int notches = e.getWheelRotation();

      if (e.isShiftDown() && notches < 0)
      {
        ++zoom;
        return;
      }

      else if (notches < 0 && zoom < 50)
      {
        // zoom out
        if (zoom < 50)
        {
          ++zoom;
        }
      }
      else if (notches > 0 && zoom > 1)
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
