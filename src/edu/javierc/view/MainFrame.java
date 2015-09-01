/**
 * @author Javier Chavez
 * Main frame is the class containing the main wrapper frame that holds
 * controls, grid panel, and threads.
 */
package edu.javierc.view;


import edu.javierc.model.ConnectionType;
import edu.javierc.model.Grid;
import edu.javierc.model.GridConnectionHandler;
import edu.javierc.io.Serializer;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;


public class MainFrame extends JFrame
{
  private GridPanel panel = new GridPanel();
  private JMenuBar menuBar = new JMenuBar();
  private GridConnectionHandler connectionHandler;
  private Grid grid;
  private JButton playButton = new JButton("Play");
  private JButton nextButton = new JButton("Next");
  private JButton resetButton = new JButton("Reset");
  private JRadioButton threaded, forkJoin;


  public MainFrame ()
  {
    init();
    grid = new Grid(2000, 2000);

    connectionHandler = new GridConnectionHandler(grid,
                                                  ConnectionType.SIMPLE);
    panel.setConnectionHandler(connectionHandler);

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
    nextButton.addActionListener(e1 -> {
      connectionHandler.step();
    });
    menuBar.add(resetButton);

    forkJoin = new JRadioButton("Fork Join");
    menuBar.add(forkJoin);
    forkJoin.addActionListener(e -> {
      if (!connectionHandler.isRunning())
      {
        connectionHandler = new GridConnectionHandler(grid,
                                                      ConnectionType.FORK_JOIN);
      }
    });

    threaded = new JRadioButton("Threaded");
    menuBar.add(threaded);
    threaded.setSelected(true);
    threaded.addActionListener(e -> {
      if (!connectionHandler.isRunning())
      {
        connectionHandler = new GridConnectionHandler(grid,
                                                      ConnectionType.SIMPLE);
      }
    });
    ButtonGroup group = new ButtonGroup();
    group.add(forkJoin);
    group.add(threaded);

    setJMenuBar(menuBar);
    setLayout(new BorderLayout(0, 0));
    add(panel);
    pack();

  }

  private void start ()
  {
    if (connectionHandler.isRunning()){
      connectionHandler.stop();
      forkJoin.setEnabled(true);
      threaded.setEnabled(true);
      playButton.setText("Play");
    }
    else
    {
      connectionHandler.start();
      forkJoin.setEnabled(false);
      threaded.setEnabled(false);

      playButton.setText("Pause");
    }
  }

  private void showOptionFrame ()
  {
    final String[] presetList = {
            "Choose preset",
            "Glider",
            "Glider Gun",
            "Gun star",
            "Breeder1",
            "Turing machine",
            "Hacksaw"};

    final String fileNames[] = {
            "",
            "glider",
            "glider-gun",
            "gunstar",
            "breeder1",
            "turing-machine",
            "hacksaw"};

    connectionHandler.stop();

    JButton button = new JButton("Save");
    JTextField threadNumberTextField = new JTextField("4", 3);
    JTextField gridXTextField = new JTextField("2000", 3);
    JTextField gridYTextField = new JTextField("2000", 3);

    JComboBox presets = new JComboBox(presetList);

    final JFrame optionFrame = new JFrame();
    GridLayout experimentLayout = new GridLayout(0, 3, 5, 0);

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
      connectionHandler.removeConnection();

      // get the input
      String userThreadsInput = threadNumberTextField.getText();
      String userGridXInput = gridXTextField.getText();
      String userGridYInput = gridYTextField.getText();

      int gridCols = Integer.parseInt(userGridXInput);
      int gridRows = Integer.parseInt(userGridYInput);
      int userThreads = Integer.parseInt(userThreadsInput);
      int itemIndex = presets.getSelectedIndex();

      // if they chose a file
      if (itemIndex > 0)
      {
        Grid serialized = null;
        Serializer s = new Serializer();
        InputStream f = getClass().getClassLoader().getResourceAsStream(
                "assets/" + fileNames[itemIndex]);

        try
        {

          serialized = s.decode(f);

        }
        catch (Exception e1) {
          System.out.println(e1.getMessage());
        }

        grid = new Grid(gridCols, gridRows, false);
        grid.mergeMatrix(serialized.getGrid(), 0 , 0);
      }
      else
      {
        grid = new Grid(gridCols, gridRows);
      }

      connectionHandler = new GridConnectionHandler(grid,
                                                    ConnectionType.SIMPLE,
                                                    userThreads);

      panel.setConnectionHandler(null);
      panel.setConnectionHandler(connectionHandler);

      // get rid of the dialog
      optionFrame.dispose();
    });
  }

}
