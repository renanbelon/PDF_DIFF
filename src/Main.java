import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Class for frontend
 */
public class Main extends JPanel implements ActionListener {
    static private final String newline = "\n";
    JButton sourceButton;
    JButton targetButton;
    JTextArea log;
    JFileChooser fc;
    private static int first = 0, second = 0;
    static Controller controller;

    public Main() {
        super(new BorderLayout());
        // Create the log first, because the action listeners
        // need to refer to it.
        log = new JTextArea(5, 20);
        log.setMargin(new Insets(5, 5, 5, 5));
        log.setEditable(false);
        log.setFont(new Font("Arial Black", 12, 12));
        log.append("Please open the source file..\n");
        JScrollPane logScrollPane = new JScrollPane(log);

        // Create a file chooser
        fc = new JFileChooser();

        // Create the buttons
        sourceButton = new JButton("Souce file..");
        sourceButton.addActionListener(this);
        targetButton = new JButton("Target file..");
        targetButton.addActionListener(this);

        // For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); // use FlowLayout
        buttonPanel.add(sourceButton);
        buttonPanel.add(targetButton);

        // Add the buttons and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
    }

    /**
     * Do the job when one button is clicked
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == sourceButton && first == 0) {
            int returnVal = fc.showOpenDialog(Main.this);
            first = 1;
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                controller.setSource(file);
                log.append("Ready: " + file.getAbsolutePath() + "." + newline + "\nPlease open the target file...");
            } else {
                first = 0;
                log.append("Open command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());

        } else if (e.getSource() == targetButton) {
            int returnVal = fc.showOpenDialog(Main.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                controller.setTarget(file);
                log.append("\nReady: " + file.getAbsolutePath() + "." + newline + "----------------------------------------\n");
                log.append("\nProcessing files...\nThis will take up to 50 seconds..\n");
                synchronized (controller) {
                    try {
                        controller.wait(50000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                controller.printOutput();
                first = 0;
                second = 0;
            } else {
                log.append("Open command cancelled by user." + newline);
                second = 0;
            }
        }
    }

    /**
     * Create and set up the main window
     */
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("NANCY'S DREAM :D");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Add content to the window.
        frame.add(new Main());
        // set icon image
        BufferedImage image = null;
        try {
            frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/resources/images/icon.png")));
            image = ImageIO.read(Main.class.getResource("/resources/images/icon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        frame.setIconImage(image);
        // Display the window.
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        int value = JOptionPane.showOptionDialog(null, "Choose the option you want to use", "PDF",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                new Object[]{"Compare PDFS", "Spell checking"}, null);
        controller = new Controller();
        if (value == 0) {
            SwingUtilities.invokeLater(() -> {
                // Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            });
        } else if (value == 1)
            controller.spellChecking();
    }
}