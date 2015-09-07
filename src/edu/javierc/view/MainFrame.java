package edu.javierc.view;
/**
 * @author Javier Chavez
 * Main frame is the class containing the main wrapper frame that holds
 * controls, grid panel, and threads.
 */


import edu.javierc.model.ConnectionType;
import edu.javierc.model.Grid;
import edu.javierc.model.GridConnectionHandler;
import edu.javierc.io.Serializer;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;


public class MainFrame extends JFrame
{
  private GridPanel panel = new GridPanel();
  private JMenuBar menuBar = new JMenuBar();
  private GridConnectionHandler connectionHandler;
  private Grid grid;
  private JButton playButton = new JButton("Play");
  private JButton nextButton = new JButton("Next");
  private JButton resetButton = new JButton("Reset");
  private JRadioButton threaded, forkJoin, exeService;
  private int userThreads = 4;
  private ArrayList<File> boards = new ArrayList<>();

  public MainFrame ()
  {
    init();
    grid = new Grid(2000, 2000, true);
    connectionHandler = new GridConnectionHandler(grid, ConnectionType.SIMPLE);
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
    playButton.addActionListener(e -> toggleGame());

    resetButton.addActionListener(e2 -> reset());

    menuBar.add(menu);
    menuBar.add(playButton);
    menuBar.add(nextButton);
    nextButton.addActionListener(e1 -> {
      connectionHandler.step();
    });
    menuBar.add(resetButton);

    forkJoin = new JRadioButton("Fork Join");
    menuBar.add(forkJoin);
    forkJoin.addActionListener(
            e -> updateThreadConnection(ConnectionType.FORK_JOIN));

    threaded = new JRadioButton("Threaded");
    menuBar.add(threaded);
    threaded.setSelected(true);
    threaded.addActionListener(
            e -> updateThreadConnection(ConnectionType.SIMPLE));
    exeService = new JRadioButton("Executor Service");
    menuBar.add(exeService);
    exeService.addActionListener(
            e -> updateThreadConnection(ConnectionType.EXECUTOR));
    ButtonGroup group = new ButtonGroup();
    group.add(forkJoin);
    group.add(threaded);
    group.add(exeService);

    setJMenuBar(menuBar);
    setLayout(new BorderLayout(0, 0));
    add(panel);

    pack();


  }

  private void reset ()
  {
    if (connectionHandler.isRunning())
    {
      toggleGame();
    }
    grid = new Grid(grid.getHeight(), grid.getWidth(), grid.isSeeded());
    updateThreadConnection();
  }

  private void toggleGame ()
  {
    if (connectionHandler.isRunning())
    {
      connectionHandler.stop();
      forkJoin.setEnabled(true);
      threaded.setEnabled(true);
      exeService.setEnabled(true);
      resetButton.setEnabled(true);
      nextButton.setEnabled(true);
      playButton.setText("Play");
    }
    else
    {
      connectionHandler.start();
      forkJoin.setEnabled(false);
      threaded.setEnabled(false);
      exeService.setEnabled(false);
      resetButton.setEnabled(false);
      nextButton.setEnabled(false);
      playButton.setText("Pause");
    }
  }

  private void showOptionFrame ()
  {

    boards = new ArrayList<>();
    // this is very much a hack. I am not allowing users to pick the 0th
    // element in the dropdown
    boards.add(new File("Select.txt"));

    URL url = MainFrame.class.getResource("../../../assets/");

    if (url != null)
    {
      File dir = null;
      try
      {
        dir = new File(url.toURI());
      }
      catch (URISyntaxException e)
      {
        e.printStackTrace();
      }
      for (File nextFile : dir.listFiles())
      {
        boards.add(nextFile);
      }
    }

    String[] names = new String[boards.size()];
    for (int i = 0; i < boards.size(); i++)
    {
      int pos = boards.get(i).getName().lastIndexOf(".");
      if (pos != -1)
      {
        names[i] = boards.get(i).getName().substring(0, pos);
      }
    }
    if (connectionHandler.isRunning())
    {
      toggleGame();
    }

    JButton button = new JButton("Save");
    JTextField threadNumberTextField = new JTextField("4", 3);
    JTextField gridXTextField = new JTextField(String.valueOf(grid.getWidth()),
                                               3);
    JTextField gridYTextField = new JTextField(String.valueOf(grid.getHeight()),
                                               3);

    JComboBox presets = new JComboBox(names);

    final JFrame optionFrame = new JFrame("Options");
    optionFrame.setPreferredSize(new Dimension(300, 150));
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
      userThreads = Integer.parseInt(userThreadsInput);
      int itemIndex = presets.getSelectedIndex();

      // if they chose a file
      if (itemIndex > 0)
      {
        Grid serialized = null;
        Serializer s = new Serializer();
        try
        {

          serialized = s.decode(boards.get(itemIndex));

          this.setTitle("Conway Game of Life - " + names[itemIndex]);
        }
        catch (Exception e1) { }

        grid = new Grid(gridRows, gridCols, false);
        grid.replaceMatrix(serialized.getGrid(), 10, 10);

      }
      else
      {
        grid = new Grid(gridCols, gridRows, true);
        this.setTitle("Conway Game of Life");
      }

      updateThreadConnection();

      // get rid of the dialog
      optionFrame.dispose();
    });
  }

  private void updateThreadConnection ()
  {
    updateThreadConnection(connectionHandler.getConnectionType());
  }

  private void updateThreadConnection (ConnectionType connectionType)
  {

    threaded.setSelected(false);
    forkJoin.setSelected(false);
    exeService.setSelected(false);

    switch (connectionType)
    {
      case EXECUTOR:
        exeService.setSelected(true);
        break;
      case SIMPLE:
        threaded.setSelected(true);
        break;
      case FORK_JOIN:
        forkJoin.setSelected(true);
        break;
      default:
        System.out.println("[ERROR] Connection not found.");
        break;
    }

    connectionHandler = new GridConnectionHandler(grid, connectionType,
                                                  userThreads);

    panel.setConnectionHandler(null);
    panel.setConnectionHandler(connectionHandler);
  }
}
