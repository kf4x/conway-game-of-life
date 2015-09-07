package edu.javierc.view;
/**
 * @author Javier Chavez
 * Turns a cell into its graphical representation. supports both graphics2d
 * and graphics
 */

import edu.javierc.model.Cell;
import edu.javierc.model.CellBase;

import java.awt.*;


public class CellView implements CellBase
{

  private Cell cell = null;

  public CellView (Cell cell)
  {
    this.cell = cell;
  }

  public CellView (boolean b, int x, int y)
  {
    this(new Cell(b, x, y));
  }

  @Override
  public Point getGraphLocation ()
  {
    return cell.getGraphLocation();
  }

  @Override
  public boolean isAlive ()
  {
    return cell.isAlive();
  }

  /**
   * Paint the cell to graphics
   *
   * @param g         graphics
   * @param x         column
   * @param y         row
   * @param cellSizeW width of cell
   * @param cellSizeH height of the cell
   * @param boarder   should the cell have a boarder
   */
  public void paint (Graphics g, int x, int y, int cellSizeW, int cellSizeH,
                     boolean boarder)
  {
    if (!cell.isAlive())
    {
      // Not drawing the dead cells to save drawing performance
      return;
    }
    if (boarder)
    {
      cellSizeH -= 1;
      cellSizeW -= 1;
    }

    g.setColor(Color.GREEN);
    g.fillRect(x, y, cellSizeW, cellSizeH);

  }

  /**
   * Paint the cell to graphics
   *
   * @param g         drawing on a graphics 2d object
   * @param x         column
   * @param y         row
   * @param cellSizeW width of cell
   * @param cellSizeH height of the cell
   * @param boarder   should the cell have a boarder
   */
  public void paint (Graphics2D g, int x, int y, int cellSizeW, int cellSizeH,
                     boolean boarder)
  {
    if (boarder)
    {
      cellSizeH -= 1;
      cellSizeW -= 1;
    }

    if (cell.isAlive())
    {
      g.setColor(Color.GREEN);
      g.fillRect(x, y, cellSizeW, cellSizeH);
    }
  }
}
