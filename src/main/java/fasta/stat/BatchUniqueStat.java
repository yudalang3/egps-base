package fasta.stat;

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
import org.apache.commons.lang3.mutable.MutableInt;

/**
 * Batch processor for calculating unique sequence statistics across multiple FASTA files.
 */
public class BatchUniqueStat {
    // 命令行入口：使用 -d/-s 指定目录与后缀（输出为英文）。
    public static void main(String[] args) throws IOException {
        Options options = buildOptions();
        CommandLine commandLine = parseOptions(options, args);
        if (commandLine == null) {
            return;
        }
        String dirPath = commandLine.getOptionValue("dir");
        String suffix = commandLine.getOptionValue("suffix");

        Path pathOfInput = Path.of(dirPath);

        UniqueStat.output = false;
        MutableInt mutableInt = new MutableInt(0);
        Files.walk(pathOfInput).forEach(path -> {
            if (path.toString().endsWith(suffix)) {
                mutableInt.increment();
                System.err.println("Processing " + mutableInt + " " + path);
                doStats(path);
            }
        });

    }

    private static Options buildOptions() {
        Options options = new Options();
        options.addOption(Option.builder("d")
                .longOpt("dir")
                .hasArg()
                .argName("dir")
                .required()
                .desc("Directory to scan.")
                .build());
        options.addOption(Option.builder("s")
                .longOpt("suffix")
                .hasArg()
                .argName("suffix")
                .required()
                .desc("File suffix to match (e.g. .fa, .fa.gz).")
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
        System.err.println("Purpose: Batch-calculate duplicate ratios for FASTA files under a directory.");
        System.err.println("Output: One TSV line per file: filename and repeat ratio.");
        formatter.printHelp("java fasta.stat.BatchUniqueStat", options, true);
    }

    private static void doStats(Path path) {

        try {
            double ratio = UniqueStat.getRatio(path.toString());
            System.out.println(path.getFileName().toString() + "\t" + ratio);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
