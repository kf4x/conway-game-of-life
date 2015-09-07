package edu.javierc.io;
/**
 * @author Javier Chavez
 * Class for serializing data. At this point the implementation only includes
 * capability sufficient only for decoding a file into a real grid object.
 * More information about reading in these types of files and examples of the
 * file formats are in these links:
 *  http://www.conwaylife.com/wiki/Run_Length_Encoded
 *  http://copy.sh/life/
 *  https://github.com/copy/life/tree/master/examples
 *
 * Useage:
 * Can be treated as a POJO.
 *
 * Serializer s = new Serializer();
 * grid = s.decode(file)
 *
 */


import edu.javierc.model.Grid;

import java.io.File;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Serializer
{
  // private Grid grid;// = new Grid(2000, 2000, false);

  // taken from RLE wiki
  private static int LINE = 70;
  private static char LIVE = 'o';
  private static char DEAD = 'b';
  private static char EOF = '!';
  private static char EOL = '$';


  /**
   * Take a file and turn it into a Grid object
   * @param file File object pointing to a RLE file
   * @return Grid with its matrix filled
   * @throws Exception
   */
  public Grid decode (File file) throws IOException
  {
    String currentLine;
    int width = 0, height = 0;
    Grid _grid = null;

    try (BufferedReader br = new BufferedReader(new FileReader(file)))
    {
      // skip all the lines that we are not using
      while ((currentLine = br.readLine()) != null) {
        if (currentLine.startsWith("#")) { continue; }
        if (currentLine.startsWith("x = "))
        {
          for (String s : currentLine.split(","))
          {
            if (s.contains("x = "))
            {
              width = Integer.valueOf(s.replaceAll("\\D+", ""));
            }
            else if (s.contains("y = "))
            {
              height = Integer.valueOf(s.replaceAll("\\D+", ""));
            }
            else
            {
              break;
            }
          }
          break;
        }
      }
      _grid = new Grid(height, width, false);

      int currentY = 0;
      int currentX = 0;
      int startX = 0;
      int i = 0;

      String kNumber = ""; // The number of times block should be repeated.
      String board = "";

      // convert the board to a long string
      while ((currentLine = br.readLine()) != null)
      {
        board += currentLine;
      }

      for (char c : board.toCharArray())
      {
        if (c == DEAD)
        {
          currentX += (kNumber.equals("") ? 1 : Integer.parseInt(kNumber));
          kNumber = "";
        }
        else if (c == LIVE)
        {
          int count = (kNumber.equals("") ? 1 : Integer.parseInt(kNumber));
          for (int j = 0; j < count; j++)
          {
            if (currentY < height && currentX < width)
            {
              _grid.setCell(true, currentX, currentY);
            }
            currentX++;
          }
          kNumber = "";
        }
        else if (c == EOL)
        {
          currentX = startX;
          currentY += (kNumber.equals("") ? 1 : Integer.parseInt(kNumber));
          kNumber = "";
        }
        else if (c == EOF)
        {
          break;
        }
        else
        {
          kNumber += Integer.parseInt(board.substring(i, i + 1));
        }
        i++;
      }

    }
    return _grid;
  }
}
