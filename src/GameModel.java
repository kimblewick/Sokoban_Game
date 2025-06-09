import javax.swing.*;

/**
 * @author Raj Kavathekar
 * Driver class for my program, was initially going to be my model class but I repurposed it.
 */
public class GameModel extends JFrame {
    private static final int TILE_SIZE = 16;
    private GameBoard board;

    /**
     * Implements UI and loads sprites.
     */
    public GameModel()
    {
        setTitle("Beaver's Day Out");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JMenuBar menuBar = new JMenuBar();
        JMenu helpMenu = new JMenu("Help");
        JMenu infoMenu = new JMenu("Info");
        JMenuItem controls = new JMenuItem("Controls");
        JMenuItem about = new JMenuItem("About");

        about.addActionListener(e -> showAbout());
        controls.addActionListener(e -> showControls());
        helpMenu.add(controls);
        infoMenu.add(about);
        menuBar.add(helpMenu);
        menuBar.add(infoMenu);
        setJMenuBar(menuBar);

        Sprite.loadSprites();

        board = new GameBoard();
        add(board);

        pack();
        setLocationRelativeTo(null);

        System.out.println("Frame size: " + getWidth() + "x" + getHeight());
    }

    /**
     * Displays the controls
     */
    private void showControls()
    {
        String message = """
            Controls:
            ↑ - Move Up
            ↓ - Move Down
            ← - Move Left
            → - Move Right
            R - Reset Level
            N - New Random Level""";

        JOptionPane.showMessageDialog(this,
                message,
                "Game Controls",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows about section for the project.
     */
    private void showAbout()
    {
        String message = """
                Help Mr. Justin Beaver by helping him build a home
                by placing wooden logs on sinkholes, once all
                sinkholes have been filled, the level is complete.
                
                After a level is complete, the game will keep 
                generating random levels that are 10 by 10 with 3
                blocks. In that case, you can pretend you are
                helping a different beaver in some other country.
                                        - Raj.""";

        JOptionPane.showMessageDialog(this, message,"About",JOptionPane.INFORMATION_MESSAGE );
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() ->
        {
            GameModel game = new GameModel();
            game.setVisible(true);
            System.out.println("Game window should be visible now");

        });
    }
}
