package utils;

import com.alibaba.fastjson.JSONObject;
import utils.storage.FinalFields;
import utils.storage.MapPersistence;
import utils.storage.ObjectPersistence;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * Utility class for creating simple GUI and CLI interfaces for development and testing.
 * 
 * <p>
 * This class provides convenience methods for quickly setting up minimal IDEs (Integrated
 * Development Environments) for iterative development, data loading, and testing workflows.
 * Particularly useful during development when you need to repeatedly load data and test operations.
 * </p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>GUI IDE:</strong> Simple Swing-based interface with one button</li>
 *   <li><strong>CLI IDE:</strong> Command-line interface with keyboard commands</li>
 *   <li><strong>Window Position Memory:</strong> Saves and restores window location/size</li>
 *   <li><strong>Thread Safety:</strong> Button actions run in separate threads</li>
 *   <li><strong>Always-on-top:</strong> GUI window stays visible</li>
 * </ul>
 * 
 * <h2>Usage Examples:</h2>
 * 
 * <h3>GUI IDE:</h3>
 * <pre>
 * // Create a simple GUI with a button that triggers your analysis
 * EGPSGuiUtil.universalSimplestIDE(() -> {
 *     // Your code here - runs when button is clicked
 *     System.out.println("Running analysis...");
 *     performAnalysis();
 * });
 * </pre>
 * 
 * <h3>CLI IDE:</h3>
 * <pre>
 * // Create a command-line interface
 * EGPSGuiUtil.universalSimplestCLIIDE(() -> {
 *     // Your code here - runs when 't' is entered
 *     System.out.println("Restarting analysis...");
 *     performAnalysis();
 * });
 * // Commands:
 * // t - trigger the action
 * // q - quit
 * </pre>
 * 
 * <h2>Use Cases:</h2>
 * <ul>
 *   <li>Development/testing workflows with time-consuming data loading</li>
 *   <li>Iterative algorithm development and testing</li>
 *   <li>Quick prototyping without full GUI development</li>
 *   <li>Analysis pipeline debugging</li>
 * </ul>
 * 
 * <h2>Window Persistence:</h2>
 * <p>
 * GUI window size and position are automatically saved on close and restored
 * on next launch, providing a consistent development experience.
 * </p>
 * 
 * @author eGPS Development Team
 * @version 1.0
 * @since 1.0
 * @see JFrame
 * @see Runnable
 */
public class EGPSGuiUtil {

    // Specifies the path to save dialog sizes, note that this should be for common dialogs only
    private static String DIALOG_SAVE_PATH = FinalFields.OBJECT_DIR.concat("/simplest.IDE.size.ser");

    /**
     * Creates a simple GUI IDE interface with a single clickable button.
     * 
     * <p>
     * This is useful for applications where data loading is time-consuming.
     * The button action runs in a separate thread to keep the GUI responsive.
     * Window size and position are automatically saved and restored.
     * </p>
     * 
     * <h3>Features:</h3>
     * <ul>
     *   <li>Button click triggers provided Runnable in new thread</li>
     *   <li>Window stays always-on-top for easy access</li>
     *   <li>Window bounds persisted between sessions</li>
     * </ul>
     *
     * @param buttonAction The action to be performed when the button is clicked (runs in separate thread)
     */
    public static void universalSimplestIDE(Runnable buttonAction) {

        JButton jButton = new JButton("Click me");
        jButton.addActionListener(e -> {
            new Thread(buttonAction).start();
        });

        Optional<Rectangle> dialogSize = getDialogSize();


        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Universal simplest IDE");

            JPanel jPanel = new JPanel(new BorderLayout());
            jPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            jPanel.add(jButton, BorderLayout.CENTER);
            frame.add(jPanel);

            frame.setSize(400, 200);

            if (dialogSize.isPresent()){
                Rectangle rectangle = dialogSize.get();
                frame.setSize(rectangle.getSize());
                frame.setLocation(rectangle.getLocation());

            }else {
                frame.setLocationRelativeTo(null);
            }
            frame.setVisible(true);
            frame.setAlwaysOnTop(true);

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    Rectangle bounds = frame.getBounds();
                    ObjectPersistence.saveObjectByObjectOutput(bounds, DIALOG_SAVE_PATH);
                    System.exit(0);
                }
            });

        });
    }

    private static Optional<Rectangle> getDialogSize() {

        Object objectByObjectInput = ObjectPersistence.getObjectByObjectInput(DIALOG_SAVE_PATH);
        if (objectByObjectInput == null) {
            return Optional.empty();
        } else {
            Rectangle object = (Rectangle) objectByObjectInput;
            return Optional.of(object);
        }
    }


    /**
     * Creates a command-line IDE interface with keyboard commands.
     * 
     * <p>
     * Provides a simple REPL (Read-Eval-Print Loop) for triggering actions
     * without a graphical interface. Useful for server environments or
     * when GUI is not needed.
     * </p>
     * 
     * <h3>Commands:</h3>
     * <ul>
     *   <li><strong>t</strong> - Trigger the provided action</li>
     *   <li><strong>q</strong> - Quit the program</li>
     * </ul>
     * 
     * @param buttonAction the action to execute when user enters 't' (runs in same thread)
     */
    public static void universalSimplestCLIIDE(Runnable buttonAction) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Command line: q to exit; t for trigger the re-start");
        while (true) {
            System.out.print("> ");
            if (scanner.hasNextLine()) {
                String command = scanner.nextLine().trim();
                if (command.startsWith("t")) {
                    buttonAction.run();
                } else if (command.startsWith("q")) {
                    break;
                }
            }
        }
    }
}
