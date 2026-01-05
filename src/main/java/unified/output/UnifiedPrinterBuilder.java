package unified.output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

/**
 * Unified Printer Builder, used to create different types of UnifiedPrinter instances.
 * Provides flexible factory methods to obtain printer objects of various implementation types.
 */
public class UnifiedPrinterBuilder {
    
    /**
     * Retrieves a default implementation of UnifiedPrinter.
     * @return Returns a concrete UnifiedPrinter implementation object.
     */
    public static UnifiedPrinter getDefaultPrinter() {
        // A concrete implementation class can be returned here, e.g., a console output implementation.
        return new ConsolePrinter();
    }
    
 
    public static UnifiedPrinter getFilePrinter(String filePath) {
        // Create different printer implementations based on type.
        return new FilePrinter(filePath);
    }
}

/**
 * Example Console Output Implementation Class.
 */
class ConsolePrinter implements UnifiedPrinter {

    @Override
    public void clear() {
        // Console cannot clear historical output, so this method is left empty or outputs a separator.
        System.out.println("\n---------------- New Test Start ----------------");
    }

    @Override
    public void print(String text) {
        System.out.println(text);
    }

    @Override
    public void print(String format, Object... objs) {
        System.out.println(String.format(format, objs));
    }

    @Override
    public void printAll(Collection<String> texts) {
        for (String text : texts) {
            System.out.println(text);
        }
    }
}

/**
 * Example File Output Implementation Class (Simplified).
 */
class FilePrinter implements UnifiedPrinter {

    private final String filePath;
    private BufferedWriter writer;

    public FilePrinter() {
        this("output.log");
    }

    public FilePrinter(String filePath) {
        this.filePath = filePath;
        try {
            writer = new BufferedWriter(new FileWriter(filePath, true)); // Append mode.
        } catch (IOException e) {
            throw new RuntimeException("Could not create file writer", e);
        }
    }

    @Override
    public void clear() {
        try {
            // Clear file content by overwriting.
            writer = new BufferedWriter(new FileWriter(filePath));
        } catch (IOException e) {
            System.err.println("Error clearing file: " + e.getMessage());
        }
    }

    @Override
    public void print(String text) {
        try {
            writer.write(text);
            writer.newLine(); // Add new line.
            writer.flush(); // Flush immediately to file.
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e);
        }
    }

    @Override
    public void print(String format, Object... objs) {
        String formatted = String.format(format, objs);
        print(formatted);
    }

    @Override
    public void printAll(Collection<String> texts) {
        for (String text : texts) {
            print(text);
        }
    }
}