package edu.javierc;

import edu.javierc.view.MainFrame;
import javax.swing.*;


public class GameOfLife
{
  public static void main (String[] args)
  {

    SwingUtilities.invokeLater(() -> {

      MainFrame frame = new MainFrame();

      frame.setVisible(true);
      frame.setSize(720, 720);
      frame.setLocationRelativeTo(null);
      frame.setTitle("Conway Game of Life");
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    });

  }
}
