package cli.tools;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Cross-platform clipboard utility for converting Windows paths to WSL (Windows Subsystem for Linux) paths.
 * 
 * <p>
 * This class provides a convenient tool for converting Windows file paths to their WSL equivalents.
 * It automatically handles drive letter conversion and path separator normalization, making it easy
 * to work with files across Windows and WSL environments.
 * </p>
 * 
 * <h2>Problem Solved</h2>
 * <p>
 * When working with WSL, Windows paths need to be converted to a format that WSL can understand:
 * </p>
 * <ul>
 *   <li><strong>Drive Letters:</strong> Windows drive letters (C:, D:, etc.) must be converted to /mnt/c, /mnt/d, etc.</li>
 *   <li><strong>Path Separators:</strong> Backslashes (\) must be converted to forward slashes (/)</li>
 *   <li><strong>Path Format:</strong> Windows absolute paths must follow WSL's mounting convention</li>
 * </ul>
 * 
 * <h2>Path Conversion Process</h2>
 * <ul>
 *   <li><strong>Input:</strong> Reads Windows path from system clipboard</li>
 *   <li><strong>Processing:</strong> Converts drive letter and path separators to WSL format</li>
 *   <li><strong>Output:</strong> Writes WSL-compatible path back to clipboard</li>
 *   <li><strong>Display:</strong> Shows original and converted path to user</li>
 * </ul>
 * 
 * <h2>Transformation Examples</h2>
 * <pre>
 * Original:    C:\Users\yudal\Documents\project\eGPS2\jars\egps-main.gui
 * Converted:   /mnt/c/Users/yudal/Documents/project/eGPS2/jars/egps-main.gui/
 * 
 * Original:    D:\Data\sequences.fasta
 * Converted:   /mnt/d/Data/sequences.fasta/
 * 
 * Original:    E:\Projects\Analysis\results\output.txt
 * Converted:   /mnt/e/Projects/Analysis/results/output.txt/
 * </pre>
 * 
 * <h2>Use Cases</h2>
 * <ul>
 *   <li><strong>WSL Development:</strong> Quickly convert paths for use in WSL bash scripts</li>
 *   <li><strong>Cross-Environment Work:</strong> Share paths between Windows and Linux environments</li>
 *   <li><strong>Docker Integration:</strong> Convert paths for Docker volumes in WSL</li>
 *   <li><strong>Build Scripts:</strong> Prepare paths for makefiles and build tools running in WSL</li>
 *   <li><strong>SSH/SCP Commands:</strong> Format paths for file transfer commands</li>
 * </ul>
 * 
 * <h2>Technical Implementation</h2>
 * <ul>
 *   <li><strong>Regex Pattern Matching:</strong> Detects Windows drive letter patterns (e.g., C:\, D:\)</li>
 *   <li><strong>Drive Letter Conversion:</strong> Converts drive letters to lowercase /mnt/x format</li>
 *   <li><strong>Separator Normalization:</strong> Replaces all backslashes with forward slashes</li>
 *   <li><strong>Trailing Slash:</strong> Adds trailing slash for WSL path convention</li>
 * </ul>
 * 
 * <h2>Conversion Algorithm</h2>
 * <ol>
 *   <li>Extract Windows drive letter (e.g., C:)</li>
 *   <li>Convert to lowercase and prepend with /mnt/ (e.g., /mnt/c)</li>
 *   <li>Replace all backslashes with forward slashes</li>
 *   <li>Append trailing slash if not present</li>
 * </ol>
 * 
 * <h2>Limitations</h2>
 * <ul>
 *   <li>Only supports Windows absolute paths with drive letters</li>
 *   <li>Does not handle UNC network paths (\\server\share)</li>
 *   <li>Requires AWT support (not available in headless environments)</li>
 *   <li>No validation of path existence or accessibility</li>
 *   <li>Does not handle relative paths</li>
 * </ul>
 * 
 * <h2>WSL Path Format</h2>
 * <p>
 * WSL mounts Windows drives under the /mnt directory:
 * </p>
 * <ul>
 *   <li>C:\ → /mnt/c/</li>
 *   <li>D:\ → /mnt/d/</li>
 *   <li>E:\ → /mnt/e/</li>
 * </ul>
 *
 * @author yudal
 * @version 1.0
 * @since 2025.12.04
 * @see java.awt.Toolkit For system clipboard access
 * @see java.awt.datatransfer For clipboard data transfer functionality
 * @see ClipboardPathNormalized For basic path normalization
 */
public class ClipboardPath4Win2WSL {
    /**
     * Pattern to match Windows drive letter paths (e.g., C:\, D:\).
     * Captures the drive letter in group 1.
     */
    private static final Pattern WINDOWS_PATH_PATTERN = Pattern.compile("^([A-Za-z]):");

    /**
     * Main method to execute the Windows to WSL path conversion utility.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        new ClipboardPath4Win2WSL().run(args);
    }

    /**
     * Runs the clipboard path conversion process.
     * If args are provided, converts each argument; otherwise reads Windows path from clipboard,
     * converts to WSL format, and writes back to clipboard.
     */
    public void run(String[] args) {
        if (args != null && args.length > 0) {
            // Convert each argument
            for (String arg : args) {
                System.out.println("Original Windows path:");
                System.out.println(arg);

                String wslPath = convertWindowsPathToWSL(arg);

                System.out.println("Converted WSL path:");
                System.out.println(wslPath);
                System.out.println();
            }
        } else {
            // Use clipboard flow
            String clipboardText = getClipboardText();
            if (clipboardText != null) {
                System.out.println("Original Windows path:");
                System.out.println(clipboardText);

                String wslPath = convertWindowsPathToWSL(clipboardText);

                System.out.println("Converted WSL path:");
                System.out.println(wslPath);

                setClipboardText(wslPath);
                System.out.println("Already paste to clipboard...");
            } else {
                System.out.println("No text in the clipboard...");
            }
        }
    }

    /**
     * Converts a Windows path to WSL (Windows Subsystem for Linux) path format.
     * 
     * <p>
     * Conversion rules:
     * </p>
     * <ul>
     *   <li>Drive letter (e.g., C:) → /mnt/c</li>
     *   <li>Backslashes (\) → Forward slashes (/)</li>
     *   <li>Adds trailing slash at the end</li>
     * </ul>
     * 
     * @param windowsPath the Windows path to convert (e.g., C:\Users\Documents)
     * @return the WSL-formatted path (e.g., /mnt/c/Users/Documents/)
     */
    private String convertWindowsPathToWSL(String windowsPath) {
        String result = windowsPath;
        
        // Convert drive letter (e.g., C:\ → /mnt/c/)
        Matcher matcher = WINDOWS_PATH_PATTERN.matcher(result);
        if (matcher.find()) {
            String driveLetter = matcher.group(1).toLowerCase();
            result = matcher.replaceFirst("/mnt/" + driveLetter + "/");
        }
        
        // Replace remaining backslashes with forward slashes
        result = result.replaceAll("\\\\+", "/");
        
        // Add trailing slash if not present
        if (!result.endsWith("/")) {
            result += "/";
        }
        
        return result;
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
