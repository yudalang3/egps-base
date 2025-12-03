package fasta.comparison;

import fasta.io.FastaReader;
import tsv.io.TSVReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Compares two FASTA files using BLAST/Diamond alignment results to analyze sequence matching coverage.
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Alignment-Based Comparison:</strong> Uses fmt6 (BLAST tabular) output for pairwise matches</li>
 *   <li><strong>Coverage Analysis:</strong> Calculate match ratio for both query and subject datasets</li>
 *   <li><strong>Unmapped Detection:</strong> Identify sequences not found in alignment results</li>
 *   <li><strong>Name Parsing:</strong> Handles sequence names with spaces (uses first word as ID)</li>
 * </ul>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * // Command-line usage
 * java fasta.comparison.FastaComparer query.fasta subject.fasta alignment.fmt6
 * 
 * // Output:
 * // The fasta1 matched count is 850, total count is 1000. 0.85
 * // The fasta2 matched count is 920, total count is 950. 0.9684
 * </pre>
 * 
 * <h2>Input Files:</h2>
 * <ul>
 *   <li><strong>file1 (query.fasta):</strong> Query FASTA file</li>
 *   <li><strong>file2 (subject.fasta):</strong> Subject FASTA file</li>
 *   <li><strong>fmt6outFile:</strong> BLAST/Diamond alignment output in fmt6 (tabular) format</li>
 * </ul>
 * 
 * <h2>Fmt6 Format:</h2>
 * <p>Expected BLAST tabular output format with at least 2 columns:</p>
 * <pre>
 * query_id\tsubject_id\t[other_columns...]
 * </pre>
 * <p>Columns are automatically named V1, V2, V3, etc. by TSVReader.</p>
 * 
 * <h2>Algorithm:</h2>
 * <ol>
 *   <li>Read all sequence names from both FASTA files</li>
 *   <li>Parse alignment file to get query (V1) and subject (V2) IDs</li>
 *   <li>Build unique sets of matched sequences from alignment</li>
 *   <li>Calculate match ratios: matched_count / total_count for each file</li>
 *   <li>If ratio &gt; 1.0 (anomaly), identify unmatched sequences</li>
 * </ol>
 * 
 * <h2>Output Interpretation:</h2>
 * <ul>
 *   <li><strong>Matched count:</strong> Number of unique sequences found in alignment</li>
 *   <li><strong>Total count:</strong> Total sequences in FASTA file</li>
 *   <li><strong>Ratio:</strong> Coverage percentage (0.0 to 1.0)</li>
 * </ul>
 * 
 * <h2>Use Cases:</h2>
 * <ul>
 *   <li>Validate BLAST/Diamond search completeness</li>
 *   <li>Compare ortholog coverage between genomes</li>
 *   <li>Assess sequence database coverage</li>
 *   <li>Quality control for homology search results</li>
 *   <li>Identify unmapped sequences requiring re-analysis</li>
 * </ul>
 * 
 * <h2>Note:</h2>
 * <p>Sequence names are parsed to handle FASTA headers with descriptions.
 * Only the first word (before space) is used as the sequence ID for matching.</p>
 * 
 * @author yudalang
 * @since 1.0
 * @see tsv.io.TSVReader
 * @see fasta.io.FastaReader
 */
public class FastaComparer {
    public static void main(String[] args) throws Exception {
        FastaComparer comparator = new FastaComparer();
        comparator.run(args[0],args[1], args[2]);
    }

    private void run(String file1, String file2, String fmt6outFile) throws IOException {
        List<String> file1Set = new ArrayList<>();
        List<String> file2Set = new ArrayList<>();
        readFasta(file1, file1Set);
        readFasta(file2, file2Set);

        Map<String, List<String>> stringListMap = TSVReader.readAsKey2ListMap(fmt6outFile, false);
        List<String> file1SetsInFmt6file = stringListMap.get("V1");
        List<String> file2SetsInFmt6file = stringListMap.get("V2");
        HashSet<String> file1uniqueSet = new HashSet<>(file1SetsInFmt6file);
        HashSet<String> file2uniqueSet = new HashSet<>(file2SetsInFmt6file);

        System.out.printf("The fasta1 matched count is %d, total count is %d. ", file1uniqueSet.size(), file1Set.size());
        double ratio1 = file1uniqueSet.size() / (double) file1Set.size();
        System.out.println(ratio1);
        double ratio2 = file2uniqueSet.size() / (double) file2Set.size();
        System.out.printf("The fasta2 matched count is %d, total count is %d. ", file2uniqueSet.size(), file2Set.size());
        System.out.println(ratio2);

        if (ratio2 > 1.0) {
            file2uniqueSet.removeAll(file2Set);
            System.out.println("The remaining unique count is " + file2uniqueSet.size());
            for (String s : file2uniqueSet) {
                System.out.println(s);
            }
        }


    }

    private void readFasta(String file1, List<String> file1Set) throws IOException {
        FastaReader.readAndProcessFastaPerEntry(file1, (key, seq) -> {
            var index = key.indexOf(' ');
            String name;
            if (index > 0){
                name = key.substring(0, index);
            }else {
                name = key;
            }
            file1Set.add(name);

            return  false;
        });
    }
}
