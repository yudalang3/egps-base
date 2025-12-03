package cli.tools;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

/**
 * Cross-platform clipboard utility for normalizing file path separators.
 * 
 * <p>
 * This class provides a convenient tool for normalizing file paths copied from
 * various sources (Windows, Unix, network paths, etc.) by converting all path
 * separators to forward slashes. This ensures consistent file path handling
 * across different operating systems and prevents common issues with hardcoded
 * file paths in scripts and configuration files.
 * </p>
 * 
 * <h2>Problem Solved</h2>
 * <p>
 * Different operating systems use different path separators:
 * </p>
 * <ul>
 *   <li><strong>Windows:</strong> Backslash (\) - e.g., {@code C:\Users\Documents\file.txt}</li>
 *   <li><strong>Unix/Linux/macOS:</strong> Forward slash (/) - e.g., {@code /home/user/documents/file.txt}</li>
 *   <li><strong>Network Paths:</strong> Often use UNC format - e.g., {@code \\server\share\file.txt}</li>
 * </ul>
 * 
 * <h2>Path Normalization Process</h2>
 * <ul>
 *   <li><strong>Input:</strong> Reads text from system clipboard</li>
 *   <li><strong>Processing:</strong> Converts all backslashes to forward slashes using regex</li>
 *   <li><strong>Output:</strong> Writes normalized text back to clipboard</li>
 *   <li><strong>Display:</strong> Shows original and processed text to user</li>
 * </ul>
 * 
 * <h2>Transformation Examples</h2>
 * <pre>
 * Original:    C:\Program Files\MyApp\data\sequences.fasta
 * Normalized:  C:/Program Files/MyApp/data/sequences.fasta
 * 
 * Original:    \\network\storage\project\results\analysis.txt  
 * Normalized:  //network/storage/project/results/analysis.txt
 * 
 * Original:    /home/researcher/Desktop/../../work/data/sample.csv
 * Normalized:  /home/researcher/Desktop/../../work/data/sample.csv  (unchanged)
 * </pre>
 * 
 * <h2>Use Cases</h2>
 * <ul>
 *   <li><strong>Cross-Platform Development:</strong> Normalize paths for portable scripts</li>
 *   <li><strong>Database Import:</strong> Ensure consistent path format for data import scripts</li>
 *   <li><strong>Configuration Files:</strong> Standardize paths in application configurations</li>
 *   <li><strong>Documentation:</strong> Create universally readable file path examples</li>
 *   <li><strong>Data Migration:</strong> Convert paths when moving between systems</li>
 *   <li><strong>Batch Processing:</strong> Prepare file paths for automated workflows</li>
 * </ul>
 * 
 * <h2>Technical Implementation</h2>
 * <ul>
 *   <li><strong>AWT Integration:</strong> Uses Java AWT Clipboard API for system clipboard access</li>
 *   <li><strong>Regex Processing:</strong> Uses {@code replaceAll("\\\\+", "/")} to handle multiple consecutive backslashes</li>
 *   <li><strong>Cross-Platform:</strong> Works on any system with Java AWT support</li>
 *   <li><strong>Error Handling:</strong> Gracefully handles clipboard access errors and unsupported flavors</li>
 * </ul>
 * 
 * <h2>Performance Characteristics</h2>
 * <ul>
 *   <li><strong>Processing Time:</strong> O(n) where n is the length of clipboard text</li>
 *   <li><strong>Memory Usage:</strong> Minimal - only stores the text being processed</li>
 *   <li><strong>Clipboard Operations:</strong> Two read/write operations, negligible overhead</li>
 * </ul>
 * 
 * <h2>Limitations</h2>
 * <ul>
 *   <li>Requires AWT support (not available in headless environments)</li>
 *   <li>Only processes text content in clipboard (ignores images, files, etc.)</li>
 *   <li>Limited to basic path normalization (doesn't resolve relative paths, drive letters, etc.)</li>
 *   <li>No undo functionality - clipboard content is immediately replaced</li>
 * </ul>
 * 
 * <h2>Alternative Tools</h2>
 * <ul>
 *   <li>Manual find/replace in text editors</li>
 *   <li>Operating system-specific path tools</li *   <li>Online path normalizers</li>
 *   <li>IDE/editor plugins for path normalization</li>
 * </ul>
 *
 * @author yudal
 * @version 1.0
 * @since 2025.07.23
 * @see java.awt.Toolkit For system clipboard access
 * @see java.awt.datatransfer For clipboard data transfer functionality
 */
public class ClipboardPathNormalized {
    /**
     * Main method to execute the clipboard path normalization utility.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        new ClipboardPathNormalized().run();
    }

    /**
     * Runs the clipboard normalization process.
     * Reads from clipboard, normalizes paths, and writes back to clipboard.
     */
    public void run() {
        // 从剪贴板读取字符串
        String clipboardText = getClipboardText();
        if (clipboardText != null) {
            System.out.println("Original string:");
            System.out.println(clipboardText);

            // 将 \\ 和 \ 替换为 /
            String processedText = clipboardText.replaceAll("\\\\+", "/");

            System.out.println("Processed string:");
            System.out.println(processedText);

            // 将处理后的文本写回剪贴板
            setClipboardText(processedText);
            System.out.println("Already paste to clipboard...");
        } else {
            System.out.println("No text in the clipboard...");
        }
    }

    /**
     * Gets text content from the system clipboard.
     * 
     * @return the text content of the clipboard, or null if no text is available
     */
    private String getClipboardText() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        try {
            Transferable contents = clipboard.getContents(null);
            if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return (String) contents.getTransferData(DataFlavor.stringFlavor);
            }
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sets text content to the system clipboard.
     * 
     * @param text the text to set in the clipboard
     */
    private void setClipboardText(String text) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(text);
        clipboard.setContents(stringSelection, null);
    }
}
