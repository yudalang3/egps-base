package utils;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import com.google.common.base.Stopwatch;

/**
 * General-purpose utility class providing common operations for the eGPS system.
 * 
 * <p>
 * This class contains static utility methods for:
 * <ul>
 *   <li>Browser operations (opening URLs)</li>
 *   <li>JVM memory monitoring and reporting</li>
 *   <li>Performance timing and benchmarking</li>
 *   <li>Clipboard operations</li>
 *   <li>CLI utility helper methods</li>
 * </ul>
 * </p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Memory Monitoring:</strong> Track JVM memory usage in MB</li>
 *   <li><strong>Performance Timing:</strong> Measure code execution time using Guava Stopwatch</li>
 *   <li><strong>Browser Integration:</strong> Open URLs in system default browser</li>
 *   <li><strong>Clipboard Fallback:</strong> Copy URLs to clipboard if browser fails</li>
 * </ul>
 * 
 * <h2>Usage Examples:</h2>
 * <pre>
 * // Open a URL in browser
 * EGPSUtil.openNativeBrowser("https://www.example.com");
 * 
 * // Check memory usage
 * long usedMB = EGPSUtil.getAlreadyUsedJVMMemory();
 * System.out.println("Used: " + usedMB + " MB");
 * 
 * // Measure execution time
 * EGPSUtil.obtainRunningTimeSecondBlocked(() -> {
 *     // Your code here
 *     doSomething();
 * });
 * 
 * // Get CLI utility name
 * String cmd = EGPSUtil.getCLIUtilityName(MyClass.class);
 * // Returns: "java -cp \"/path/to/eGPS_lib/*\" MyClass"
 * </pre>
 * 
 * <h2>Thread Safety:</h2>
 * <p>
 * All methods are static and thread-safe unless otherwise noted.
 * Browser operations use SwingUtilities.invokeLater() for thread safety.
 * </p>
 * 
 * @author eGPS Development Team
 * @version 1.0
 * @since 1.0
 * @see EGPSFileUtil
 * @see EGPSGeneralUtil
 */
public class EGPSUtil {

    /**
     * Opens a URL in the system's default web browser.
     * 
     * <p>
     * This method attempts to launch the native browser. If the operation fails
     * (e.g., no browser available, permission issues), the URL is copied to the
     * system clipboard and an error message is printed to stderr.
     * </p>
     * 
     * <p>
     * The browser opening operation is performed on the Swing Event Dispatch Thread
     * for thread safety.
     * </p>
     * 
     * <p><strong>Fallback Behavior:</strong></p>
     * <p>
     * If browser opening fails, the URL is automatically copied to the clipboard
     * and the user can manually paste it into their browser.
     * </p>
     * 
     * @param url the URL to open (e.g., "https://www.example.com")
     * @throws URISyntaxException if the URL is malformed
     * @see #operationsIfFailed(String)
     */
    public static void openNativeBrowser(String url) throws URISyntaxException {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                URI uri = new URI(url);
                SwingUtilities.invokeLater(() -> {
                    try {
                        desktop.browse(uri);
                    } catch (Exception e) {
                        operationsIfFailed(url);
                    }
                });

            } else {
                operationsIfFailed(url);
            }
        } else {
            operationsIfFailed(url);
        }
    }

    /**
     * Handles failure when browser cannot be opened.
     * 
     * <p>
     * This method is called as a fallback when {@link #openNativeBrowser(String)} fails.
     * It copies the URL to the system clipboard and prints an error message.
     * </p>
     * 
     * @param homeUrl the URL that failed to open
     */
    public static void operationsIfFailed(String homeUrl) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(homeUrl);
        clipboard.setContents(tText, null);

        String title = "Network error";
        String msg = "URL redirection is failed. Network link has been copied to your clipboard."
                + " Please paste the link to your website browser. \n" + homeUrl;
        System.err.println(title + "\n" + msg);
    }

    /**
     * Returns the current JVM memory usage in megabytes.
     * 
     * <p>
     * Calculates used memory as: (totalMemory - freeMemory) / 1048576
     * where 1048576 = 1024 * 1024 (conversion to MB).
     * </p>
     * 
     * <p><strong>Note:</strong> This reflects the JVM's current memory allocation,
     * not the total system memory usage.</p>
     * 
     * @return the used JVM memory in megabytes
     * @see #printUsedJVMMemory()
     */
    public static long getAlreadyUsedJVMMemory() {
        // 1048576 = 1024 * 1024
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576;
    }

    /**
     * Prints detailed JVM memory statistics to standard output.
     * 
     * <p>
     * Displays:
     * <ul>
     *   <li>Max memory (JVM maximum heap size)</li>
     *   <li>Allocated memory (currently allocated heap)</li>
     *   <li>Free memory (free space in allocated heap)</li>
     *   <li>Used memory (actually used heap space)</li>
     * </ul>
     * All values are reported in megabytes (MB).
     * </p>
     * 
     * <p><strong>Output Format:</strong></p>
     * <pre>
     * ------------------------------------------------------------
     * Max memory (MB): 1024.0
     * Allocated memory (MB): 512.0
     * Free memory (MB): 256.0
     * Used memory (MB): 256.0
     * </pre>
     * 
     * @see #getAlreadyUsedJVMMemory()
     */
    public static void printUsedJVMMemory() {
        // Get the Runtime instance
        Runtime runtime = Runtime.getRuntime();

        // Get the maximum memory of the JVM
        long maxMemory = runtime.maxMemory();
        // Get the allocated memory of the JVM
        long allocatedMemory = runtime.totalMemory();
        // Get the free memory of the JVM
        long freeMemory = runtime.freeMemory();

        // Convert bytes to MB
        double maxMemoryMB = bytesToMB(maxMemory);
        double allocatedMemoryMB = bytesToMB(allocatedMemory);
        double freeMemoryMB = bytesToMB(freeMemory);
        double usedMemoryMB = bytesToMB(allocatedMemory - freeMemory);

        System.out.println("------------------------------------------------------------");
        System.out.println("Max memory (MB): " + maxMemoryMB);
        System.out.println("Allocated memory (MB): " + allocatedMemoryMB);
        System.out.println("Free memory (MB): " + freeMemoryMB);
        System.out.println("Used memory (MB): " + usedMemoryMB);
    }

    /**
     * Converts bytes to megabytes.
     * 
     * @param bytes the number of bytes
     * @return the equivalent value in megabytes (as double)
     */
    private static double bytesToMB(long bytes) {
        return bytes / (1024.0 * 1024.0);
    }

    /**
     * Executes a Runnable and measures its execution time in a new thread.
     * 
     * <p>
     * This method creates a new thread, runs the provided code, and prints
     * the execution time in milliseconds. The calling thread continues immediately
     * without waiting for completion.
     * </p>
     * 
     * <p><strong>Note:</strong> Use this for non-blocking time measurements.
     * The timing result is printed asynchronously.</p>
     * 
     * <p><strong>Output Example:</strong></p>
     * <pre>Code execution time: 1234 milliseconds</pre>
     * 
     * @param run the code to execute and measure
     * @see #obtainRunningTimeSecondBlocked(Runnable)
     */
    public static void obtainRunningTimeNewThread(Runnable run) {
        new Thread() {
            @Override
            public void run() {

                Stopwatch stopwatch = Stopwatch.createStarted();
                run.run();
                stopwatch.stop();

                long elapsedTimeInMillis = stopwatch.elapsed(TimeUnit.MILLISECONDS);
                System.out.printf("Code execution time: %d milliseconds\n", elapsedTimeInMillis);
            }
        }.start();
    }

    /**
     * Executes a Runnable and measures its execution time, blocking until completion.
     * 
     * <p>
     * This method runs the provided code synchronously and prints the execution
     * time in seconds. The calling thread is blocked until the Runnable completes.
     * </p>
     * 
     * <p><strong>Use Case:</strong> Suitable for benchmarking long-running operations
     * where second-level precision is sufficient.</p>
     * 
     * <p><strong>Output Example:</strong></p>
     * <pre>Code execution time: 42 seconds</pre>
     * 
     * @param run the code to execute and measure
     * @see #obtainRunningTimeNewThread(Runnable)
     */
    public static void obtainRunningTimeSecondBlocked(Runnable run) {

        // Create a Stopwatch instance
        Stopwatch stopwatch = Stopwatch.createStarted();
        run.run();
        stopwatch.stop();
        long elapsedTimeInMillis = stopwatch.elapsed(TimeUnit.SECONDS);

        System.out.printf("Code execution time: %d seconds\n", elapsedTimeInMillis);
    }


    /**
     * Prints elapsed time since a start time and current memory usage.
     * 
     * <p>
     * Calculates and displays:
     * <ul>
     *   <li>Time elapsed in milliseconds and seconds</li>
     *   <li>Current JVM memory usage in MB</li>
     * </ul>
     * </p>
     * 
     * <p><strong>Output Example:</strong></p>
     * <pre>Time since start is: 5000 ms, ( 5 s )! And 128 MB memory has used!</pre>
     * 
     * @param startTime the start time in milliseconds (from System.currentTimeMillis())
     * @return the current time in milliseconds (end time)
     * @see System#currentTimeMillis()
     * @see #getAlreadyUsedJVMMemory()
     */
    public static long printTimeSinceStartAndPrintUsedMemory(long startTime) {
        long endTime = System.currentTimeMillis();

        long timeWithMs = endTime - startTime;

        long memory2 = Runtime.getRuntime().totalMemory() / 1024 / 1024
                - Runtime.getRuntime().freeMemory() / 1024 / 1024;
        System.out.println("Time since start is: " + timeWithMs + " ms, ( " + timeWithMs / 1000 + " s )! And " + memory2
                + " MB memory has used!");

        return endTime;
    }

    /**
     * Measures the memory consumed by executing a Runnable.
     * 
     * <p>
     * This method performs garbage collection before and after running the code
     * to get a more accurate measurement of memory usage. The result represents
     * the approximate memory allocated during execution.
     * </p>
     * 
     * <p><strong>Important Notes:</strong></p>
     * <ul>
     *   <li>Calls {@code System.gc()} which may impact performance</li>
     *   <li>Result is an approximation due to JVM memory management</li>
     *   <li>Not suitable for micro-benchmarks</li>
     * </ul>
     * 
     * @param runningCode the code whose memory usage should be measured
     * @return memory usage in bytes (approximation)
     * @see Runtime#gc()
     */
    public static long getTheMemoryUsageSize(Runnable runningCode) {
        Runtime r = Runtime.getRuntime();
        r.gc();
        long startMem = r.freeMemory();
        runningCode.run();

        return Math.abs(startMem - r.freeMemory());
    }

    /**
     * Generates a CLI command template for running a Java class from eGPS library.
     * 
     * <p>
     * This utility method creates a standardized command line string for executing
     * CLI utilities from the eGPS library. It's primarily used for generating help
     * documentation and usage instructions.
     * </p>
     * 
     * <p><strong>Example Output:</strong></p>
     * <pre>
     * EGPSUtil.getCLIUtilityName(MyTool.class)
     * // Returns: "java -cp \"/path/to/eGPS_lib/*\" com.example.MyTool"
     * </pre>
     * 
     * <p><strong>Usage:</strong> Copy this command, replace the path, and add arguments as needed.</p>
     * 
     * @param clz the CLI utility class
     * @return a formatted command line string for executing the class
     */
    public static String getCLIUtilityName(Class<?> clz) {
        return "java -cp \"/path/to/eGPS_lib/*\" ".concat(clz.getName());
    }

}
