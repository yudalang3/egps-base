package utils.storage;

import java.io.File;

/**
 * Configuration constants defining storage directories for the eGPS utility system.
 * 
 * <p>
 * This class defines the standard directory paths used by various eGPS utilities
 * for storing configuration files, serialized objects, and JSON data. All directories
 * are created under the user's home directory in a dedicated ".egps2.yu.lab.utils"
 * folder to keep user data organized and isolated from system files.
 * </p>
 * 
 * <h2>Directory Structure</h2>
 * <p>
 * Under {@code ~/.egps2.yu.lab.utils/}, the following directories are created:
 * </p>
 * <ul>
 *   <li><strong>storage/</strong> - General storage directory (PROPERTIES_DIR)</li>
 *   <li><strong>config/jsonData/</strong> - JSON configuration files (JSON_DIR)</li>
 *   <li><strong>config/objects/</strong> - Serialized Java objects (OBJECT_DIR)</li>
 * </ul>
 * 
 * <h2>Directory Locations</h2>
 * <p>
 * <strong>Example paths:</strong>
 * </p>
 * <ul>
 *   <li>Windows: {@code C:\Users\[username]\.egps2.yu.lab.utils\storage}</li>
 *   <li>Linux/Mac: {@code /home/[username]/.egps2.yu.lab.utils/storage}</li>
 * </ul>
 * 
 * <h2>Usage Examples</h2>
 * <pre>
 * // Save a configuration file
 * File configFile = new File(FinalFields.JSON_DIR, "my_config.json");
 * 
 * // Save a serialized object
 * File objectFile = new File(FinalFields.OBJECT_DIR, "cached_data.ser");
 * 
 * // Access general storage
 * File storageFile = new File(FinalFields.PROPERTIES_DIR, "app.properties");
 * </pre>
 * 
 * <h2>Initialization</h2>
 * <p>
 * All directories are automatically created when the class is loaded using the
 * static initialization block. The {@link #createIfNotExists(File)} method ensures
 * that the directory structure exists before any utility tries to access it.
 * </p>
 * 
 * <h2>Design Notes</h2>
 * <ul>
 *   <li>All paths use forward slashes regardless of OS for consistency</li>
 *   <li>Hidden directory (.egps2.yu.lab.utils) prevents accidental user modification</li>
 *   <li>Automatic directory creation eliminates manual setup requirements</li>
 *   <li>Centralized configuration makes path management easier across utilities</li>
 * </ul>
 * 
 * @author eGPS Development Team
 * @version 1.0
 * @since 1.0
 * @see System#getProperty(String) For accessing system properties like user.home
 */
public class FinalFields {
    public static final String PROPERTIES_DIR;
    public static final String JSON_DIR;
    public static final String OBJECT_DIR;

    static {
        String userHomeDir = System.getProperty("user.home");
        File dir = new File(userHomeDir, ".egps2.yu.lab.utils");
        createIfNotExists( dir);
        File storage = new File(dir, "storage");
        PROPERTIES_DIR = storage.getAbsolutePath().replace("\\", "/");
        File jsonDir = new File(dir, "config/jsonData");
        createIfNotExists( jsonDir);
        JSON_DIR = jsonDir.getAbsolutePath().replace("\\", "/");
        File objectsDir = new File(dir, "config/objects");
        createIfNotExists( objectsDir);
        OBJECT_DIR = objectsDir.getAbsolutePath().replace("\\", "/");

    }

    /**
     * Creates the specified file or directory if it does not already exist.
     * 
     * <p>
     * This utility method ensures that all required directories exist before they are used
     * by other parts of the application. It creates nested directory structures as needed.
     * </p>
     * 
     * <h2>Method Behavior:</h2>
     * <ul>
     *   <li>If the file/directory exists: does nothing</li>
     *   <li>If the file/directory doesn't exist: creates all necessary parent directories</li>
     *   <li>Handles nested directory structures automatically</li>
     *   <li>Silent operation - no errors or logging</li>
     * </ul>
     * 
     * <h2>Implementation Details:</h2>
     * <ul>
     *   <li>Uses {@link File#mkdirs()} for directory creation</li>
     *   <li>Creates all intermediate directories in the path</li>
     *   <li>Does not throw exceptions for existing directories</li>
     *   <li>Private method called during static initialization</li>
     * </ul>
     * 
     * @param file the file or directory to create if it doesn't exist
     * @see File#mkdirs()
     * @see File#exists()
     */
    private static void createIfNotExists(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
