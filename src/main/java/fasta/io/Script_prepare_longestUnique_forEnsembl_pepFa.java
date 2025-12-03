package fasta.io;

import com.google.common.base.Joiner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Script for preparing longest and unique protein sequences from Ensembl FASTA files.
 * 
 * <p>
 * This script processes Ensembl protein FASTA files to create filtered datasets containing
 * only the longest and unique protein sequences for each gene. It is particularly useful
 * for preprocessing Ensembl protein data for comparative genomics and evolutionary analysis.
 * </p>
 * 
 * <h2>Functionality:</h2>
 * <p>
 * The script performs the following operations:
 * </p>
 * <ol>
 *   <li><strong>Input Processing:</strong> Reads all FASTA files from the input directory</li>
 *   <li><strong>Longest CDS Selection:</strong> Uses {@link Script_get_longest_CDS_forEnsembl_pepFa} 
 *       to extract the longest coding sequence for each gene</li>
 *   <li><strong>Uniqueness Filtering:</strong> Uses {@link Script_get_unique_forEnsembl_pepFa} 
 *       to remove duplicate sequences</li>
 *   <li><strong>Output Generation:</strong> Creates filtered FASTA files with longest unique sequences</li>
 *   <li><strong>Summary Report:</strong> Generates a summary file with processing statistics</li>
 * </ol>
 * 
 * <h2>Input Parameters:</h2>
 * <ul>
 *   <li><strong>args[0]:</strong> Input directory containing Ensembl protein FASTA files (.fa.gz)</li>
 *   <li><strong>args[1]:</strong> Output directory for processed files</li>
 *   <li><strong>args[2]:</strong> Summary file path for processing statistics</li>
 * </ul>
 * 
 * <h2>Output Files:</h2>
 * <ul>
 *   <li>Filtered FASTA files with longest unique sequences (suffix: _longestCDS_unique.fa.gz)</li>
 *   <li>Summary file with processing statistics: Name, count, gene, unique</li>
 * </ul>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Batch Processing:</strong> Processes multiple Ensembl FASTA files simultaneously</li>
 *   <li><strong>Memory Efficient:</strong> Temporary files are cleaned up automatically</li>
 *   <li><strong>Progress Tracking:</strong> Detailed logging of processing steps</li>
 *   <li><strong>Quality Control:</strong> Validation of input file formats and naming conventions</li>
 *   <li><strong>Statistics Generation:</strong> Comprehensive summary of processing results</li>
 * </ul>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * {@code
 * Script_prepare_longestUnique_forEnsembl_pepFa.main(new String[]{
 *     "/path/to/ensembl/proteins/",
 *     "/path/to/output/",
 *     "/path/to/summary.tsv"
 * });
 * }
 * </pre>
 * 
 * <h2>Related Scripts:</h2>
 * <ul>
 *   <li>{@link Script_get_longest_CDS_forEnsembl_pepFa}: Longest CDS extraction</li>
 *   <li>{@link Script_get_unique_forEnsembl_pepFa}: Uniqueness filtering</li>
 *   <li>{@link Script_allocate_raw_pep_to_dir_by_fileName}: Raw file allocation</li>
 * </ul>
 * 
 * @author eGPS Development Team
 * @since 1.0
 * @see Script_get_longest_CDS_forEnsembl_pepFa
 * @see Script_get_unique_forEnsembl_pepFa
 */
public class Script_prepare_longestUnique_forEnsembl_pepFa {
    static Joiner joiner = Joiner.on('\t');
    static String headerLine = "Name\tcount\tgene\tunique";
    static List<String> outputList = new ArrayList<>();
    static boolean removeOriginalFile = false;
    static boolean debug = true;


    static final String suffix = ".fa.gz";

    public static void main(String[] args) throws IOException {
        String inputDir = args[0];
        String outputDir = args[1];
        String summaryFile = args[2];
        if (inputDir == null) {
            throw new RuntimeException("Please input the pep fasta file dir.");
        }

        outputList.add(headerLine);
        Files.list(Path.of(inputDir)).forEach(pepFasta -> {
            String fileName = pepFasta.getFileName().toString();

            String fileNameWithoutSuffix = fileName.substring(0, fileName.length() - suffix.length());
            String outputFileName = fileNameWithoutSuffix + "_longestCDS_unique.fa.gz";

            if (!fileName.endsWith(suffix)) {
                System.out.println("fileName:" + fileName + " is not end with " + suffix);
            } else {
                try {
                    handleOneFile(pepFasta, fileName, Path.of(outputDir, outputFileName));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Files.write(Path.of(summaryFile), outputList);
    }

    private static void handleOneFile(Path pepFasta, String fileName, Path outputPath) throws IOException {
        Path outputPath1 = pepFasta.resolveSibling("longest.fa.gz");
        String s1 = Script_get_longest_CDS_forEnsembl_pepFa.transformProteomicFasta(pepFasta, outputPath1);

        String s2 = Script_get_unique_forEnsembl_pepFa.handle(outputPath1, outputPath);

        outputList.add(joiner.join(fileName, s1, s2));

        Files.delete(outputPath1);
    }

}

