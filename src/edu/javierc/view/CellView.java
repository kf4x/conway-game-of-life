package edu.javierc.view;


import edu.javierc.model.Cell;
import edu.javierc.model.CellBase;

import java.awt.*;

public class CellView implements CellBase
{

  private Cell cell = null;

  public CellView(Cell cell){
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

  public void paint (Graphics2D g, int x, int y, int cellSizeW, int cellSizeH)
  {
    cellSizeH -=2;
    cellSizeW -=2;

    if (!cell.isAlive())
    {
      g.setColor(Color.BLACK);
    }
    else
    {
      g.setColor(Color.green);
    }

    g.drawRect(x, y, cellSizeW, cellSizeH);
    g.fillRect(x, y, cellSizeW, cellSizeH);

  }
}
