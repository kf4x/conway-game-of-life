package edu.javierc.io;
/**
 * @author Javier Chavez
 * http://www.conwaylife.com/wiki/Run_Length_Encoded
 * http://copy.sh/life/
 * https://github.com/copy/life/tree/master/examples
 *
 */


import edu.javierc.model.Grid;

import java.io.File;
import java.io.*;


public class Serializer
{

  private Grid grid = new Grid(2000, 2000, false);
  // taken from RLE wiki
  private static int LINE = 70;
  private static char LIVE = 'o';
  private static char DEAD = 'b';
  private static char EOF = '!';
  private static char EOL = '$';



  public Grid decode (File file) throws Exception {
    String currentLine;

    try (BufferedReader br = new BufferedReader(new FileReader(file)))
    {

      while ((currentLine = br.readLine()) != null) {
        if (currentLine.startsWith("#")) { continue; }
        if (currentLine.startsWith("x = ")) { break; }
      }

      int currentY = 0;
      int currentX = 0;
      int startX = 0;

      String countStr = "";
      int i = 0;
      String board = "";
      while ((currentLine = br.readLine()) != null)
      {
        board += currentLine;
      }
      for (char c : board.toCharArray())
      {

        if (c == DEAD)
        {
          currentX += (countStr.equals("") ? 1 : Integer.parseInt(countStr));
          countStr = "";
        }
        else if (c == LIVE)
        {
          int count = (countStr.equals("") ? 1 : Integer.parseInt(countStr));
          for (int j = 0; j < count; j++)
          {
            if (currentY < 2000 && currentX < 2000)
            {
              grid.setCell(true, currentX+1, currentY+1);
            }
            currentX++;
          }
          countStr = "";
        }
        else if (c == EOL)
        {
          currentX = startX;
          currentY += (countStr.equals("") ? 1 : Integer.parseInt(countStr));
          countStr = "";
        }
        else if (c == EOF)
        {
          break;
        }
        else
        {
          countStr += Integer.parseInt(board.substring(i, i + 1));
        }
        i++;
      }

    }

    return grid;
  }

  /**
   * Method to turn a graph into a text file.
   *
   * @param graph Graph object that will be serialized
   * @param file  File object containing path to file.
   */
//  public void encode (Graph graph, File file) throws Exception
//  {
//    FileWriter writer = new FileWriter(file);
//
//    StringBuilder graphString = new StringBuilder();
//    List<List<Node>> matrix = graph.getMatrix();
//    for (List<Node> nodeList : matrix)
//    {
//      for (Node node : nodeList)
//      {
//        graphString.append(node.getNodeType().getCode());
//      }
//      graphString.append("\n");
//    }
//
//    writer.write(graphString.toString());
//    writer.close();
//
//  }

  /**
   * A line should, not be > 40, but shall be > 5
   *
   * @param line just a plain String
   * @return boolean true if string is proper length
   */
//  private boolean validateLine (char[] line)
//  {
//    if (line.length > MAX_CHARS || line.length < MIN_CHARS)
//    {
//      // System.out.println("line length = " + line.length);
//      return false;
//    }
//    else
//    {
//      return true;
//    }
//  }
}
