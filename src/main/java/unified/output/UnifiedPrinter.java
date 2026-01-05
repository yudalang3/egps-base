package unified.output;

import java.util.Collection;

/**
 * Unified printing interface for standardizing printing behavior across different output methods
 * (e.g., console, file, network).
 * Provides various printing methods to meet different output requirements.
 */
public interface UnifiedPrinter {

    /**
     * Clears the current output content, suitable for visual output scenarios like JTextArea.
     * Has no practical effect on console output like System.out.
     */
    void clear();

    /**
     * Outputs the specified string to the target location.
     * @param text The text content to be output.
     */
    void print(String text);

    /**
     * Supports Log4j2-style formatted output.
     * @param format The format string (e.g., "User %s performed an operation at %s").
     * @param objs A variable argument list used to replace placeholders in the format string.
     */
    void print(String format, Object... objs);

    /**
     * Outputs multiple strings in batch.
     * @param texts A collection of strings containing all content to be output.
     */
    void printAll(Collection<String> texts);
}