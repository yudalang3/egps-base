package fasta.stat;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fasta.io.FastaReader;
import org.apache.commons.lang3.mutable.MutableInt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Statistical utility for analyzing sequence uniqueness and detecting duplicates in FASTA files.
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Duplicate Detection:</strong> Identify identical sequences across different entries</li>
 *   <li><strong>Repeat Ratio:</strong> Calculate percentage of duplicate sequences</li>
 *   <li><strong>Frequency Ranking:</strong> Sort duplicates by occurrence count</li>
 *   <li><strong>Entry Tracking:</strong> List all sequence names sharing the same sequence</li>
 * </ul>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * // Command-line usage
 * java fasta.stat.UniqueStat input.fasta
 * 
 * // Programmatic usage
 * double repeatRatio = UniqueStat.getRatio("sequences.fasta");
 * System.out.println("Repeat ratio: " + repeatRatio);
 * // Output:
 * // 3	seq1;seq2;seq3
 * // 2	seq4;seq5
 * // Repeat ratio: 0.45
 * </pre>
 * 
 * <h2>Output Format:</h2>
 * <p>When duplicates are detected, prints:</p>
 * <pre>
 * [count]\t[name1;name2;name3...]
 * </pre>
 * <p>Where:</p>
 * <ul>
 *   <li><strong>count:</strong> Number of sequences sharing identical sequence data</li>
 *   <li><strong>names:</strong> Semicolon-separated list of sequence entry names</li>
 * </ul>
 * 
 * <h2>Algorithm:</h2>
 * <ol>
 *   <li>Read all FASTA entries and build sequence-to-count and sequence-to-names mappings</li>
 *   <li>Sort sequences by duplicate count (descending)</li>
 *   <li>Print entries with count &gt; 1 (duplicates)</li>
 *   <li>Calculate repeat ratio: duplicate_count / total_count</li>
 * </ol>
 * 
 * <h2>Use Cases:</h2>
 * <ul>
 *   <li>Quality control for sequence datasets</li>
 *   <li>Detect redundant sequences before phylogenetic analysis</li>
 *   <li>Identify PCR duplicates or sequencing artifacts</li>
 *   <li>Assess dataset diversity</li>
 *   <li>Find contamination or mislabeling</li>
 * </ul>
 * 
 * <h2>Return Value:</h2>
 * <p>Returns a double representing the repeat ratio (0.0 to 1.0):</p>
 * <ul>
 *   <li><strong>0.0:</strong> All sequences are unique</li>
 *   <li><strong>0.5:</strong> Half of the sequences are duplicates</li>
 *   <li><strong>1.0:</strong> All sequences are identical</li>
 * </ul>
 * 
 * @author yudalang
 * @since 1.0
 */
public class UniqueStat {
    static boolean output = true;
    public static void main(String[] args) throws IOException {
        String fastaPath = args[0];
        double ratio = getRatio(fastaPath);
        System.out.println("Repeat ratio: " + ratio);
    }

    public static double getRatio(String fastaPath) throws IOException {
        Map<String, Integer> sequence2count = Maps.newHashMap();
        Map<String, List<String>> sequence2name = Maps.newHashMap();

        MutableInt totalCount = new MutableInt();
        FastaReader.readAndProcessFastaPerEntry(fastaPath, (speciesName, sequence) -> {
            Integer i = sequence2count.computeIfAbsent(sequence, k -> 0);
            sequence2count.put(sequence, i + 1);
            List<String> strings = sequence2name.computeIfAbsent(sequence, k -> Lists.newLinkedList());
            strings.add(speciesName);
            totalCount.increment();
            return false;
        });
        MutableInt dupCount = new MutableInt();
        ArrayList<Map.Entry<String, Integer>> entries = new ArrayList<>(sequence2count.entrySet());
        entries.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        for (Map.Entry<String, Integer> entry : entries){
            var sequence = entry.getKey();
            var count = entry.getValue();
            if (count > 1){
                if (output){
                    List<String> strings = sequence2name.get(sequence);
                    StringJoiner stringJoiner = new StringJoiner(";");
                    for (String string : strings) {
                        stringJoiner.add(string);
                    }
                    System.out.println(count + "\t" + stringJoiner.toString());
                }
                dupCount.add(count);
            }
        }

        double ratio = dupCount.doubleValue() / totalCount.doubleValue();
        return ratio;
    }
}
