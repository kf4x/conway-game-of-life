package edu.javierc.view;
/**
 * @author Javier Chavez
 * This is repsonsible for drawing, zoom, and toggling grid.
 */


import edu.javierc.model.GridConnectionHandler;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.VolatileImage;


public class GridPanel extends JPanel implements ActionListener
{

  private GridConnectionHandler connectionHandler;
  private Timer timer = new Timer(160, this);
  private static int zoom = 1;
  private static int viewX = 0, viewY = 0;
  private VolatileImage image;

  public GridPanel ()
  {
    timer.start();
    init();
  }

  /**
   * Set a reference to a connectionHandler
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


  @Override
  protected void paintComponent (Graphics g)
  {
    createBackBuffer();
    do
    {
      GraphicsConfiguration gc = this.getGraphicsConfiguration();
      int valCode = image.validate(gc);

      // check if hardware accelerated image available on machine
      if (valCode == VolatileImage.IMAGE_INCOMPATIBLE)
      {
        createBackBuffer();
      }

      Graphics offscreenGraphics = image.getGraphics();
      // Draw a giant black square to fill the background
      offscreenGraphics.setColor(Color.BLACK);
      offscreenGraphics.fillRect(0, 0, getWidth(), getHeight());
      paintCells(offscreenGraphics);
      paintInfo(offscreenGraphics);

      // paint back buffer to main graphics
      g.drawImage(image, 0, 0, null);

    }
    while (image.contentsLost());

  }



  /**
   * Paints the cells to a graphics object
   */
  private void paintCells (Graphics g)
  {

    int cellsX = getWidth() / zoom;
    int cellsY = getHeight() / zoom;

    for (int row = 0; row < cellsY; ++row)
    {
      for (int col = 0; col < cellsX; ++col)
      {

        int lookUpCol = Math.min(col + viewX, connectionHandler.getDimension()
                .getX() -1);
        int lookUpRow = Math.min(row + viewY, connectionHandler.getDimension()
                .getY()-1);
        int x = zoom * col;
        int y = zoom * row;

        boolean cellValue = connectionHandler.getCellValue(lookUpCol,
                                                           lookUpRow);

        // no longer using cellView to prevent more memory use

        if (!cellValue){
          // not drawing black cells to help performance
          continue;
        }

        g.setColor(Color.GREEN);
        if (zoom >= 5)
        {
          g.fillRect(x, y, zoom-1, zoom-1);
        }
        else
        {
          g.fillRect(x, y, zoom, zoom);
        }
      }
    }
  }

  private void paintInfo (Graphics graphics)
  {
    int cellsX = (int) Math.ceil((double) getWidth() / zoom);
    int cellsY = (int) Math.ceil((double)getHeight() / zoom);

    int windowWidth = getWidth();
    graphics.setColor(new Color(0, 0, 0, (float) 0.56));
    graphics.fillRect(windowWidth - 150, 0, 155, 100);
    graphics.setColor(Color.white);
    graphics.drawString("Zoom: " + String.valueOf(zoom), windowWidth - 140, 20);
    graphics.drawString("XY: (" + String.valueOf(viewX) +
                                ", " + String.valueOf(viewY) + ")",
                        windowWidth - 140, 40);
    graphics.drawString("d/dt: " + String.valueOf(connectionHandler
                                                         .getCommitTime()),
                        windowWidth - 140, 60);
    graphics.drawString("Squares: " + String.valueOf(cellsX * cellsY),
                        windowWidth - 140, 80);
  }

  private void createBackBuffer ()
  {
    GraphicsConfiguration gc = getGraphicsConfiguration();
    image = gc.createCompatibleVolatileImage(getWidth(), getHeight());
    image.setAccelerationPriority(1.0f);
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
      if (!connectionHandler.isRunning())
      {
        int currCol = (e.getX() / zoom) + viewX;
        int currRow = (e.getY() / zoom) + viewY;
        connectionHandler.toggleCell(currCol, currRow);
      }
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
        viewX = (connectionHandler.getDimension().getX() <=
                (cellsX+viewX+20)) ? viewX : (viewX + 20);
        viewY = (connectionHandler.getDimension().getY() <= (cellsY+viewY
                +20)) ? viewY : (viewY + 20);
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
        viewY = (connectionHandler.getDimension().getY() <= (cellsY+viewY
                +20)) ? viewY : (viewY + 20);
        return;
      }
      if (inRangeX && horizontalTravel)
      {
        viewX = (connectionHandler.getDimension().getX() <= (cellsX+viewX+20)) ?
                viewX : (viewX + 20);
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
