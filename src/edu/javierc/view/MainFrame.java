/**
 * @author Javier Chavez
 * Main frame is the class containing the main wrapper frame that holds
 * controls, grid panel, and threads.
 */
package edu.javierc.view;


import edu.javierc.model.Grid;
import edu.javierc.model.GridConnectionHandler;

import javax.swing.*;
import java.awt.*;


public class MainFrame extends JFrame
{
  private GridPanel panel = new GridPanel();
  private JMenuBar menuBar = new JMenuBar();
  private Thread[] threads; // = new Thread[4];
  private Grid grid;
  private JButton playButton = new JButton("Play");
  private JButton nextButton = new JButton("Next");
  private JButton resetButton = new JButton("Reset");


  public MainFrame ()
  {
    init();
    grid = new Grid(10000, 10000);
    panel.setGrid(grid);
    //    for(i = 0; i < threads.length; i++)
    //      threads[i].join();
  }

  /**
   * Initialize the GUI and add appropriate listeners.
   */
  private void init ()
  {
//    grid = new Grid();
    JMenu menu;
    JMenuItem menuItem;

    // add menu
    menu = new JMenu("File");

    // add open item
    menuItem = new JMenuItem("Options");
    menuItem.addActionListener(e -> showOptionFrame());

    menu.add(menuItem);
    menu.addSeparator();

    menuItem = new JMenuItem("Exit");
    menuItem.addActionListener(e -> System.exit(0));

    menu.add(menuItem);
    playButton.addActionListener(e -> start());



    menuBar.add(menu);
    menuBar.add(playButton);
    menuBar.add(nextButton);
    menuBar.add(resetButton);



    setJMenuBar(menuBar);
    setLayout(new BorderLayout(0, 0));
    add(panel);
    pack();

  }

  private void start ()
  {
    GridConnectionHandler connectionHandler = new GridConnectionHandler(grid);
    connectionHandler.start();
  }

  private void showOptionFrame ()
  {
    final String presetList[] = {"one", "two", "three", "four"};

    JButton button = new JButton("Save");
    JTextField threadNumberTextField = new JTextField("4", 3);
    JTextField gridXTextField = new JTextField("10000", 3);
    JTextField gridYTextField = new JTextField("10000", 3);

    JComboBox presets = new JComboBox(presetList);
    final JFrame optionFrame = new JFrame();



    GridLayout experimentLayout = new GridLayout(0, 3, 5, 0);


//    GridLayout sizeLayout = new GridLayout(0, 2, 5, 0);

    optionFrame.setLayout(experimentLayout);

    optionFrame.add(new JLabel("Threads: "));
    optionFrame.add(threadNumberTextField);
    optionFrame.add(new JLabel(" "));
    optionFrame.add(new JLabel("Grid size: "));
    optionFrame.add(gridXTextField);
    optionFrame.add(gridYTextField);
    optionFrame.add(new JLabel("Preset: "));
    optionFrame.add(presets);
    optionFrame.add(new JLabel(" "));
    optionFrame.add(button);
    optionFrame.pack();
    optionFrame.setVisible(true);
    optionFrame.setLocationRelativeTo(null); //center on screen

    button.addActionListener(e -> {
      // get the input
      String userThreadsInput = threadNumberTextField.getText();
      String userGridXInput = threadNumberTextField.getText();
      String userGridYInput = threadNumberTextField.getText();
      int gridCols = Integer.parseInt(userGridXInput);
      int gridRows = Integer.parseInt(userGridYInput);
//      grid = new Grid(gridRows, gridCols);
//      panel.setGrid(grid);
      // parse the string into an int
      int userThreads = Integer.parseInt(userThreadsInput);
      threads = new Thread[userThreads];
      // get rid of the dialog
      optionFrame.dispose();
    });
  }

}
