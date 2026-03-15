package fasta.io;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.commons.lang3.tuple.Pair;
import utils.string.EGPSStringUtil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.zip.GZIPOutputStream;

/**
 * Longest CDS extraction utility for Ensembl protein FASTA files.
 *
 * <p>
 * This script processes Ensembl protein FASTA files and extracts the longest
 * coding sequence (CDS) for each gene, handling multiple transcript variants.
 * It is specifically designed for Ensembl data processing where a single gene
 * may have multiple protein products and we need to select the longest one.
 * </p>
 *
 * <h2>Script Purpose:</h2>
 * <p>
 * When working with Ensembl protein datasets, genes often have multiple transcript
 * variants resulting in different protein isoforms. This script identifies and
 * extracts the longest CDS for each unique gene, creating a non-redundant
 * protein dataset suitable for downstream analysis.
 * </p>
 *
 * <h2>Input Format:</h2>
 * <p>Ensembl protein FASTA files with standard format:</p>
 * <pre>
 * >ENSP00000123456 gene_name:GENE1 transcript:ENST00000123456
 * MASEQUENCEPROTEIN...
 * >ENSP00000234567 gene_name:GENE1 transcript:ENST00000234567
 * MASEQUENCEPROTEINLONGER...
 * </pre>
 *
 * <h2>Processing Logic:</h2>
 * <ol>
 *   <li>Parse Ensembl protein FASTA file</li>
 *   <li>Extract gene name from header line</li>
 *   <li>Group sequences by gene name</li>
 *   <li>Identify longest CDS for each gene</li>
 *   <li>Write the longest sequences to output file</li>
 * </ol>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Gene-based Grouping:</strong> Groups sequences by gene name from headers</li>
 *   <li><strong>Length Selection:</strong> Automatically selects longest CDS</li>
 *   <li><strong>GZIP Support:</strong> Handles compressed input/output files</li>
 *   <li><strong>Ensembl Format:</strong> Parses Ensembl-specific header format</li>
 *   <li><strong>Debug Mode:</strong> Optional detailed logging for troubleshooting</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>
 * java Script_get_longest_CDS_forEnsembl_pepFa -i human_proteins.fa.gz
 *
 * # With debug output:
 * java Script_get_longest_CDS_forEnsembl_pepFa -i proteins.fa.gz --debug
 * </pre>
 *
 * <h2>Output Format:</h2>
 * <p>
 * The script generates a new FASTA file containing only the longest CDS
 * for each gene, with updated headers:
 * </p>
 * <pre>
 * >ENSP00000123456 GENE1_longest_CDS
 * MASEQUENCEPROTEINLONGER...
 * </pre>
 *
 * <h2>Bioinformatics Applications:</h2>
 * <ul>
 *   <li><strong>Non-redundant Datasets:</strong> Create unique gene sets for analysis</li>
 *   <li><strong>Phylogenetic Analysis:</strong> Avoid bias from multiple isoforms</li>
 *   <li><strong>Functional Annotation:</strong> Focus on primary gene products</li>
 *   <li><strong>Comparative Genomics:</strong> Standardize gene representation across species</li>
 *   <li><strong>Database Integration:</strong> Prepare data for various databases</li>
 * </ul>
 *
 * <h2>Configuration Options:</h2>
 * <ul>
 *   <li><strong>removeOriginalFile:</strong> Option to delete input file after processing</li>
 *   <li><strong>debug:</strong> Enable detailed logging for troubleshooting</li>
 *   <li><strong>Compression:</strong> Automatic GZIP compression detection</li>
 * </ul>
 *
 * <h2>Performance Considerations:</h2>
 * <ul>
 *   <li>Memory-efficient streaming processing</li>
 *   <li>Suitable for large Ensembl datasets (millions of sequences)</li>
 *   <li>Progress tracking for long-running operations</li>
 *   <li>Graceful handling of malformed headers</li>
 * </ul>
 *
 * @see FastaReader
 * @see EGPSStringUtil
 * @author eGPS Development Team
 * @since 1.0
 */
public class Script_get_longest_CDS_forEnsembl_pepFa {

    static boolean removeOriginalFile = false;
    static boolean debug = false;

    // 命令行入口：用 -i/-o 指定输入/输出，未给 -i 时会自动扫描当前目录（输出为英文）。
    public static void main(String[] args) throws IOException {
        Options options = buildOptions();
        CommandLine commandLine = parseOptions(options, args);
        if (commandLine == null) {
            return;
        }
        removeOriginalFile = commandLine.hasOption("remove-original");
        debug = commandLine.hasOption("debug");
        String input = commandLine.getOptionValue("input");
        if (input == null || input.isBlank()) {
            input = findGzipPepFasta(new String[0]);
        }
        if (input == null) {
            System.err.println("Error: No input .fa.gz file found.");
            System.exit(1);
        }
        Path pepFasta = Path.of(input);

        String fileName = pepFasta.getFileName().toString();
        String outputOverride = commandLine.getOptionValue("output");
        Path outputPath;
        final String suffix = ".fa.gz";
        if (outputOverride != null && !outputOverride.isBlank()) {
            outputPath = Path.of(outputOverride);
        } else {
            if (!fileName.endsWith(suffix)){
                throw new IllegalArgumentException("Input file name must end with .fa.gz");
            }
            String fileNameWithoutSuffix = fileName.substring(0, fileName.length() - suffix.length());
            String outputFileName = fileNameWithoutSuffix + "_longestCDS.fa.gz";
            // 创建输出文件路径，基于输入文件路径并更改文件名
            // 使用resolveSibling确保输出文件与输入文件在同一目录下
            outputPath = pepFasta.resolveSibling(outputFileName);

        }
        if (Files.exists(outputPath)) {
            throw new RuntimeException("File already exists.");
        } else {
            transformProteomicFasta(pepFasta, outputPath);
            if (removeOriginalFile) {
                Files.delete(pepFasta);
            }
        }
    }

    private static Options buildOptions() {
        Options options = new Options();
        options.addOption(Option.builder("i")
                .longOpt("input")
                .hasArg()
                .argName("fa.gz")
                .desc("Input Ensembl protein FASTA (.fa.gz).")
                .build());
        options.addOption(Option.builder("o")
                .longOpt("output")
                .hasArg()
                .argName("fa.gz")
                .desc("Output FASTA path (default: *_longestCDS.fa.gz).")
                .build());
        options.addOption(new Option("r", "remove-original", false, "Remove input file after writing output."));
        options.addOption(new Option("d", "debug", false, "Enable debug mode (no output writing)."));
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
        System.err.println("Purpose: Extract the longest CDS per gene from an Ensembl protein FASTA (.fa.gz).");
        System.err.println("Output: *_longestCDS.fa.gz in the same directory unless --output is provided.");
        System.err.println("Note: If --input is omitted, it auto-picks a .fa.gz in the current directory.");
        formatter.printHelp("java fasta.io.Script_get_longest_CDS_forEnsembl_pepFa", options, true);
    }

    private static String findGzipPepFasta(String[] args) throws IOException {
        if (args.length == 1) {
            return args[0];
        } else {
            Path path = Path.of(".");
            MutableObject<String> filePath = new MutableObject<>();

            Files.list(path).forEach(ppp -> {
                String string = ppp.toString();
                if (string.endsWith("fa.gz")) {
                    if (!string.endsWith("_longestCDS.fa.gz")) {
                        filePath.setValue(string);
                    }
                }
            });

            return filePath.getValue();
        }
    }

    static String transformProteomicFasta(Path pepFasta, Path outputPath) throws IOException {
        if (!looksLikeEnsemblFasta(pepFasta, 3)) {
            System.err.println("Warning: Input FASTA does not look like Ensembl protein headers; gene cannot be determined.");
            System.err.println("Output will be a copy of the original file.");
            if (debug) {
                return null;
            }
            Files.copy(pepFasta, outputPath, StandardCopyOption.REPLACE_EXISTING);
            int totalCount = countFastaEntries(pepFasta);
            return totalCount + "\tNA";
        }

        MutableInt totalCount = new MutableInt();

        Set<String> geneTypes = new HashSet<>();

        MutableBoolean parseFailed = new MutableBoolean(false);

        Map<String, Pair<String, String>> pep2entries = new HashMap<>();
        LinkedHashMap<String, List<Pair<String, Integer>>> gene2pepAndLength = new LinkedHashMap<>();

        // 0 1 2
        //>CapteP211078 pep supercontig:Capitella_teleta_v1.0:CAPTEscaffold_1000:482:3795:1
        // 3 4 5 6
        // gene:CapteG211078 transcript:CapteT211078 gene_biotype:protein_coding transcript_biotype:protein_coding
        System.out.println(pepFasta.toString());
        FastaReader.readAndProcessFastaPerEntry(pepFasta.toString(), (header, sequence) -> {
            HeaderInfo headerInfo = parseEnsemblHeader(header);
            if (headerInfo == null) {
                parseFailed.setTrue();
                return true;
            }
            totalCount.increment();
            if (headerInfo.geneBiotype != null) {
                geneTypes.add(headerInfo.geneBiotype);
            }
            pep2entries.put(headerInfo.transcriptName, Pair.of(header, sequence));

            var geneName = headerInfo.geneName;
            List<Pair<String, Integer>> stringIntegerPair = gene2pepAndLength.get(geneName);
            Pair<String, Integer> pair = Pair.of(headerInfo.transcriptName, sequence.length());

            if (stringIntegerPair == null) {
                List<Pair<String, Integer>> list = new ArrayList<>();
                list.add(pair);
                gene2pepAndLength.put(geneName, list);
            } else {
                stringIntegerPair.add(pair);
            }

            return false;
        });

        if (parseFailed.isTrue()) {
            System.err.println("Warning: Input FASTA contains non-Ensembl headers; gene cannot be determined.");
            System.err.println("Output will be a copy of the original file.");
            if (debug) {
                return null;
            }
            Files.copy(pepFasta, outputPath, StandardCopyOption.REPLACE_EXISTING);
            int totalCountFallback = countFastaEntries(pepFasta);
            return totalCountFallback + "\tNA";
        }

        StringJoiner stringJoiner = new StringJoiner("\t");
//        System.out.println("Total number of sequences are: " + totalCount.intValue());
        stringJoiner.add("Summary");
        stringJoiner.add(pepFasta.getFileName().toString());
        stringJoiner.add(totalCount.toString());

        if (pep2entries.size() != totalCount.intValue()) {
            throw new RuntimeException("pep2entries.size() != totalCount.intValue()");
        }

        Comparator<Pair<String, Integer>> comparator = (o1, o2) -> {
            int o1Length = o1.getRight();
            int o2Length = o2.getRight();
            return Integer.compare(o2Length, o1Length);
        };

        Set<Map.Entry<String, List<Pair<String, Integer>>>> entries = gene2pepAndLength.entrySet();
        for (Map.Entry<String, List<Pair<String, Integer>>> entry : entries) {
            entry.getValue().sort(comparator);
        }
//        System.out.println("Total number of genes are: " + entries.size());
        stringJoiner.add(String.valueOf(entries.size()));

        for (String geneType : geneTypes) {
//            System.out.println(geneType);
            stringJoiner.add(geneType);
        }

        if (debug) {
            return null;
        }

        if (entries.size() == totalCount.intValue()) {
            System.out.println("Found one gene one transcript, so directly cp the original file.");
            Files.copy(pepFasta, outputPath);
        } else {
            outputResults(entries, pep2entries, outputPath);
        }

        System.out.println(stringJoiner);

        return totalCount + "\t" + entries.size();
    }

    private static boolean looksLikeEnsemblFasta(Path pepFasta, int sampleSize) throws IOException {
        MutableInt checked = new MutableInt();
        MutableInt matched = new MutableInt();
        FastaReader.readAndProcessFastaPerEntry(pepFasta.toString(), (header, sequence) -> {
            checked.increment();
            if (parseEnsemblHeader(header) != null) {
                matched.increment();
            }
            return checked.intValue() >= sampleSize;
        });
        return checked.intValue() > 0 && matched.intValue() == checked.intValue();
    }

    private static int countFastaEntries(Path pepFasta) throws IOException {
        MutableInt count = new MutableInt();
        FastaReader.readAndProcessFastaPerEntry(pepFasta.toString(), (header, sequence) -> {
            count.increment();
            return false;
        });
        return count.intValue();
    }

    private static HeaderInfo parseEnsemblHeader(String header) {
        if (header == null || header.isBlank()) {
            return null;
        }
        String[] tokens = EGPSStringUtil.split(header, ' ');
        String geneName = null;
        String transcriptName = null;
        String geneBiotype = null;
        for (String token : tokens) {
            if (token.startsWith("gene:")) {
                geneName = token;
            } else if (token.startsWith("transcript:")) {
                transcriptName = token;
            } else if (token.startsWith("gene_biotype:")) {
                geneBiotype = token;
            }
        }
        if (geneName == null || transcriptName == null) {
            return null;
        }
        return new HeaderInfo(geneName, transcriptName, geneBiotype);
    }

    private static class HeaderInfo {
        private final String geneName;
        private final String transcriptName;
        private final String geneBiotype;

        private HeaderInfo(String geneName, String transcriptName, String geneBiotype) {
            this.geneName = geneName;
            this.transcriptName = transcriptName;
            this.geneBiotype = geneBiotype;
        }
    }

    private static void outputResults(Set<Map.Entry<String, List<Pair<String, Integer>>>> entries,
                                      Map<String, Pair<String, String>> pep2entries,
                                      Path outputPath) {

        try (GZIPOutputStream gzipOS = new GZIPOutputStream(Files.newOutputStream(outputPath));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(gzipOS, StandardCharsets.UTF_8))) {
            for (Map.Entry<String, List<Pair<String, Integer>>> entry : entries) {
                List<Pair<String, Integer>> value = entry.getValue();
                Pair<String, Integer> first = value.getFirst();

                Pair<String, String> stringStringPair = pep2entries.get(first.getKey());
                String header = stringStringPair.getLeft();

                writer.write(">");
                writer.write(header);
                writer.write("\n");
                writer.write(stringStringPair.getValue());
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
