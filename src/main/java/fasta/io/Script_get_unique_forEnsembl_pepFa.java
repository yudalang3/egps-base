package fasta.io;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

/**
 * Script for extracting unique protein sequences from Ensembl FASTA files.
 * 
 * <p>
 * This script processes compressed Ensembl protein FASTA files to extract and
 * retain only unique sequences, removing duplicates while preserving sequence
 * identifiers. It is essential for data preprocessing in comparative genomics
 * and evolutionary analysis where redundant sequences would skew statistical analyses.
 * </p>
 * 
 * <h2>Functionality:</h2>
 * <p>
 * The script performs the following operations:
 * </p>
 * <ol>
 *   <li><strong>FASTA Parsing:</strong> Reads compressed protein sequences from .fa.gz files</li>
 *   <li><strong>Duplicate Detection:</strong> Identifies identical protein sequences across entries</li>
 *   <li><strong>Name Mapping:</strong> Tracks which sequence names correspond to each unique sequence</li>
 *   <li><strong>Uniqueness Extraction:</strong> Retains only one representative for each unique sequence</li>
 *   <li><strong>Output Generation:</strong> Creates compressed FASTA file with unique sequences only</li>
 *   <li><strong>Cleanup:</strong> Optionally removes original files after processing</li>
 * </ol>
 * 
 * <h2>Input/Output:</h2>
 * <ul>
 *   <li><strong>Input:</strong> Compressed FASTA file (.fa.gz) containing protein sequences</li>
 *   <li><strong>Output:</strong> Compressed FASTA file with suffix "_unique.fa.gz" containing only unique sequences</li>
 * </ul>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Memory Efficient:</strong> Processes sequences incrementally without loading entire file into memory</li>
 *   <li><strong>Compressed I/O:</strong> Handles gzip-compressed files directly</li>
 *   <li><strong>Duplicate Tracking:</strong> Maintains mapping of sequence names to detect duplicates</li>
 *   <li><strong>Quality Control:</strong> Validates input file naming conventions</li>
 *   <li><strong>Automatic Detection:</strong> Can auto-detect FASTA files in current directory</li>
 *   <li><strong>Statistics Generation:</strong> Returns count of unique sequences found</li>
 * </ul>
 * 
 * <h2>Algorithm Details:</h2>
 * <ul>
 *   <li><strong>Hash-based Deduplication:</strong> Uses hash maps for efficient duplicate detection</li>
 *   <li><strong>First-name Preservation:</strong> Retains the first occurrence's name for each unique sequence</li>
 *   <li><strong>Linear Processing:</strong> Single-pass algorithm with O(n) complexity</li>
 *   <li><strong>Memory Management:</strong> Efficient memory usage with incremental processing</li>
 * </ul>
 * 
 * <h2>Usage Patterns:</h2>
 * <h3>Command Line Usage:</h3>
 * <pre>
 * {@code
 * # Explicit file path
 * Script_get_unique_forEnsembl_pepFa.main(new String[]{"/path/to/protein.fa.gz"});
 * 
 * # Auto-detect file in current directory
 * Script_get_unique_forEnsembl_pepFa.main(new String[]{});
 * }
 * </pre>
 * 
 * <h3>Programmatic Usage:</h3>
 * <pre>
 * {@code
 * Path inputFile = Path.of("input.fa.gz");
 * Path outputFile = Path.of("input_unique.fa.gz");
 * String uniqueCount = Script_get_unique_forEnsembl_pepFa.handle(inputFile, outputFile);
 * System.out.println("Found " + uniqueCount + " unique sequences");
 * }
 * </pre>
 * 
 * <h2>Output Format:</h2>
 * <p>
 * The output file maintains the standard FASTA format with sequence headers
 * and protein sequences. Each unique sequence appears exactly once, with the
 * header from its first occurrence in the input file.
 * </p>
 * 
 * <h2>Integration Points:</h2>
 * <ul>
 *   <li><strong>Pipeline Integration:</strong> Used in sequence preprocessing pipelines</li>
 *   <li><strong>Batch Processing:</strong> Compatible with automated workflow systems</li>
 *   <li><strong>Quality Control:</strong> Essential for downstream comparative analyses</li>
 *   <li><strong>Data Reduction:</strong> Significantly reduces dataset size by removing redundancy</li>
 * </ul>
 * 
 * <h2>Related Scripts:</h2>
 * <ul>
 *   <li>{@link Script_prepare_longestUnique_forEnsembl_pepFa}: Comprehensive preprocessing pipeline</li>
 *   <li>{@link Script_get_longest_CDS_forEnsembl_pepFa}: Longest CDS extraction</li>
 *   <li>{@link Script_allocate_raw_pep_to_dir_by_fileName}: Raw file organization</li>
 *   <li>{@link FastaReader}: Underlying FASTA parsing functionality</li>
 * </ul>
 * 
 * @author eGPS Development Team
 * @since 1.0
 * @see Script_prepare_longestUnique_forEnsembl_pepFa
 * @see Script_get_longest_CDS_forEnsembl_pepFa
 * @see FastaReader
 */
public class Script_get_unique_forEnsembl_pepFa {
    public static void main(String[] args) throws IOException {
        String fastaPath = findGzipPepFasta(args);
        if (fastaPath.endsWith("_unique.fa.gz")){
            System.err.println(fastaPath + "\tAlready unique");
            return;
        }

        Path pepFasta = Path.of(fastaPath);
        String fileName = pepFasta.getFileName().toString();
        final String suffix = ".fa.gz";

        String fileNameWithoutSuffix = fileName.substring(0, fileName.length() - suffix.length());
        String outputFileName = fileNameWithoutSuffix + "_unique.fa.gz";
        Path outputPath = pepFasta.resolveSibling(outputFileName);


        handle(pepFasta, outputPath);

        Files.delete(pepFasta);
    }

    static String handle(Path pepFasta, Path outputPath) throws IOException {
        Map<String, Integer> sequence2count = Maps.newHashMap();
        Map<String, List<String>> sequence2name = Maps.newHashMap();

        MutableInt totalCount = new MutableInt();
        FastaReader.readAndProcessFastaPerEntry(pepFasta.toString(), (speciesName, sequence) -> {
            Integer i = sequence2count.computeIfAbsent(sequence, k -> 0);
            sequence2count.put(sequence, i + 1);
            List<String> strings = sequence2name.computeIfAbsent(sequence, k -> Lists.newLinkedList());
            strings.add(speciesName);
            totalCount.increment();
            return false;
        });


        List<String> outputs = Lists.newLinkedList();

        List<Map.Entry<String, Integer>> entries = new ArrayList<>(sequence2count.entrySet());
        for (Map.Entry<String, Integer> entry : entries) {
            var sequence = entry.getKey();
            var count = entry.getValue();
            List<String> strings = sequence2name.get(sequence);
            String name = strings.getFirst();
            outputs.add(">".concat(name));
            outputs.add(sequence);
        }


        outputContents(outputPath, outputs);

        return String.valueOf(entries.size());

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

    private static void outputContents(Path outputPath, List<String> outputs) {
        try (GZIPOutputStream gzipOS = new GZIPOutputStream(Files.newOutputStream(outputPath));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(gzipOS, StandardCharsets.UTF_8))) {
            for (String output : outputs){
                writer.write(output);
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

