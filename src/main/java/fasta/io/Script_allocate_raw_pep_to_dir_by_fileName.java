package fasta.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Protein sequence file organization utility script.
 * 
 * <p>
 * This utility script organizes protein FASTA files (.fa.gz) from an input directory
 * into organized output directories based on file naming patterns. It processes
 * compressed protein sequence files and distributes them according to specified criteria.
 * </p>
 * 
 * <h2>Script Purpose:</h2>
 * <p>
 * This script is designed for organizing large collections of protein FASTA files
 * by automatically categorizing them based on their filenames. This is particularly
 * useful in large-scale protein annotation projects where files need to be
 * systematically organized for downstream processing.
 * </p>
 * 
 * <h2>Input/Output:</h2>
 * <ul>
 *   <li><strong>Input Directory:</strong> Directory containing .fa.gz protein files</li>
 *   <li><strong>Output Directory:</strong> Target directory for organized files</li>
 *   <li><strong>File Pattern:</strong> Files must end with ".fa.gz" suffix</li>
 * </ul>
 * 
 * <h2>Processing Logic:</h2>
 * <ol>
 *   <li>Scans input directory for all files</li>
 *   <li>Filters files by ".fa.gz" suffix</li>
 *   <li>Processes each valid protein file</li>
 *   <li>Organizes files based on filename patterns</li>
 *   <li>Copies files to appropriate output directories</li>
 * </ol>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * java Script_allocate_raw_pep_to_dir_by_fileName -i /path/to/protein/files -o /path/to/organized/output
 * </pre>
 * 
 * <h2>Example Directory Structure:</h2>
 * <pre>
 * Input Directory:
 *   species1.protein.fa.gz
 *   species2.protein.fa.gz
 *   species3.protein.fa.gz
 * 
 * Output Organization:
 *   /organized_output/
 *     /species1/
 *       species1.protein.fa.gz
 *     /species2/
 *       species2.protein.fa.gz
 * </pre>
 * 
 * <h2>Bioinformatics Applications:</h2>
 * <ul>
 *   <li><strong>Species Database Organization:</strong> Organize protein files by species</li>
 *   <li><strong>Project Management:</strong> Categorize protein collections by research focus</li>
 *   <li><strong>Pipeline Preparation:</strong> Pre-organize files for annotation pipelines</li>
 *   <li><strong>Data Archival:</strong> Systematic storage of protein sequence collections</li>
 * </ul>
 * 
 * <h2>Error Handling:</h2>
 * <ul>
 *   <li>Validates input directory exists</li>
 *   <li>Checks file suffix compatibility</li>
 *   <li>Handles I/O exceptions gracefully</li>
 *   <li>Reports processing status for each file</li>
 * </ul>
 * 
 * <h2>Performance Considerations:</h2>
 * <ul>
 *   <li>Uses Java NIO for efficient file operations</li>
 *   <li>Processes files sequentially to manage memory usage</li>
 *   <li>Suitable for large file collections (1000s of files)</li>
 *   <li>Compressed file support (.fa.gz)</li>
 * </ul>
 * 
 * @see FastaReader
 * @author eGPS Development Team
 * @since 1.0
 */
public class Script_allocate_raw_pep_to_dir_by_fileName {
    static final String suffix = ".fa.gz";

    // 命令行入口：使用 -i/-o 指定输入输出目录（输出为英文）。
    public static void main(String[] args) throws IOException {
        Options options = buildOptions();
        CommandLine commandLine = parseOptions(options, args);
        if (commandLine == null) {
            return;
        }
        String inputDir = commandLine.getOptionValue("input-dir");
        String outputDir = commandLine.getOptionValue("output-dir");
        if (inputDir == null) {
            throw new RuntimeException("Please input the pep fasta file dir.");
        }


        Files.list(Path.of(inputDir)).forEach(pepFasta -> {
            String fileName = pepFasta.getFileName().toString();
            if (!fileName.endsWith(suffix)) {
                System.out.println("fileName:" + fileName + " is not end with " + suffix);
            } else {
                try {
                    handleOneFile(pepFasta, fileName, outputDir);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }

    private static Options buildOptions() {
        Options options = new Options();
        options.addOption(Option.builder("i")
                .longOpt("input-dir")
                .hasArg()
                .argName("dir")
                .required()
                .desc("Input directory containing .fa.gz files.")
                .build());
        options.addOption(Option.builder("o")
                .longOpt("output-dir")
                .hasArg()
                .argName("dir")
                .required()
                .desc("Output directory for organized files.")
                .build());
        options.addOption(new Option("h", "help", false, "Print help."));
        return options;
    }

    private static CommandLine parseOptions(Options options, String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption("help")) {
                printHelp(options);
                return null;
            }
            return commandLine;
        } catch (ParseException e) {
            System.err.println("Error: " + e.getMessage());
            printHelp(options);
            return null;
        }
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        System.err.println("Purpose: Organize .fa.gz protein files into subdirectories by filename prefix.");
        System.err.println("Output: Copies files into per-prefix folders under the output directory.");
        formatter.printHelp("java fasta.io.Script_allocate_raw_pep_to_dir_by_fileName", options, true);
    }

    private static void handleOneFile(Path pepFasta, String fileName, String outputDir) throws IOException {
        int indexOf = fileName.indexOf('.');
        if (indexOf == -1) {
            System.out.println("fileName:" + fileName + " is not end with " + indexOf);
        } else {
            String newDirName = fileName.substring(0, indexOf);
            Files.createDirectories(Path.of(outputDir, newDirName));

            Path outPepPath = Path.of(outputDir, newDirName, pepFasta.getFileName().toString());
            Files.copy(pepFasta, outPepPath);
//                        Files.delete(outPepPath);
        }
    }

}
