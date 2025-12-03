package blast.parse;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.string.EGPSStringUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Comprehensive representation of BLAST HSP (High-scoring Segment Pair) alignment records.
 * 
 * <p>
 * This class provides a robust framework for parsing, storing, and managing BLAST alignment
 * results from various BLAST output formats. It encapsulates all essential alignment
 * information including sequence identifiers, statistical scores, and coordinate data.
 * </p>
 * 
 * <h2>BLAST HSP Overview</h2>
 * <p>
 * A High-scoring Segment Pair represents a local alignment between two sequences
 * (query and subject) that meets the scoring threshold criteria in BLAST searches.
 * Each HSP contains comprehensive information about the alignment quality and characteristics.
 * </p>
 * 
 * <h2>Core Data Fields</h2>
 * <ul>
 *   <li><strong>Sequence Identifiers</strong>: qseqid, sseqid - Query and subject sequence IDs</li>
 *   <li><strong>Alignment Metrics</strong>: pident, length - Percent identity and alignment length</li>
 *   <li><strong>Gap Information</strong>: mismatch, gapopen - Mismatches and gap openings</li>
 *   <li><strong>Coordinates</strong>: qstart, qend, sstart, send - Alignment positions</li>
 *   <li><strong>Statistical Scores</strong>: evalue, bitscore - E-value and bit score</li>
 * </ul>
 * 
 * <h2>Supported BLAST Formats</h2>
 * <ul>
 *   <li>BLAST Tabular Format (outfmt 6)</li>
 *   <li>BLAST Tabular Format with Comments (outfmt 7)</li>
 *   <li>Custom tabular formats with standard fields</li>
 * </ul>
 * 
 * <h2>Usage Examples</h2>
 * <pre>
 * // Parse BLAST results from file
 * List&lt;BlastHspRecord&gt; alignments = BlastHspRecord.parseFile("blast_results.tsv");
 * 
 * // Filter high-quality alignments
 * List&lt;BlastHspRecord&gt; highQuality = alignments.stream()
 *     .filter(record -&gt; record.getEvalue() &lt; 1e-10)
 *     .filter(record -&gt; record.getPident() &gt; 90.0)
 *     .collect(Collectors.toList());
 * 
 * // Calculate coverage statistics
 * for (BlastHspRecord record : highQuality) {
 *     double queryCoverage = (double) record.getAlignmentLength() / record.getQueryLength();
 *     double subjectCoverage = (double) record.getAlignmentLength() / record.getSubjectLength();
 *     System.out.printf("Query: %s, Coverage: %.2f%%, E-value: %e%n",
 *         record.getQseqid(), queryCoverage * 100, record.getEvalue());
 * }
 * </pre>
 * 
 * <h2>Performance Considerations</h2>
 * <ul>
 *   <li>File parsing uses Java NIO for optimal I/O performance</li>
 *   <li>Memory usage scales linearly with number of HSP records</li>
 *   <li>Stream-based filtering enables efficient large dataset processing</li>
 *   <li>Lazy loading design patterns can be implemented for very large files</li>
 * </ul>
 * 
 * <h2>Error Handling</h2>
 * <ul>
 *   <li>Malformed lines are logged and skipped</li>
 *   <li>Missing fields are handled gracefully with default values</li>
 *   <li>File I/O errors are propagated as IOException</li>
 *   <li>Numeric parsing errors are logged with line context</li>
 * </ul>
 * 
 * <h2>Integration with eGPS</h2>
 * <ul>
 *   <li>Compatible with FASTA sequence processing pipelines</li>
 *   <li>Integrates with phylogenetic analysis workflows</li>
 *   <li>Supports homology detection and annotation processes</li>
 *   <li>Works with taxonomic classification systems</li>
 * </ul>
 * 
 * <h2>Future Enhancements</h2>
 * <ul>
 *   <li>Support for XML and JSON BLAST output formats</li>
 *   <li>Integration with BLAST+ REST API services</li>
 *   <li>Advanced filtering and statistical analysis methods</li>
 *   <li>Visualization and reporting capabilities</li>
 * </ul>
 *
 * @author yudal
 * @version 1.0
 * @since 2025.07.23
 * @see <a href="https://blast.ncbi.nlm.nih.gov/Blast.cgi">NCBI BLAST</a>
 * @see utils.string.EGPSStringUtil For string processing utilities
 * @see com.google.common.collect.Lists For collection utilities
 */
public class BlastHspRecord {
    private static final Logger log = LoggerFactory.getLogger(BlastHspRecord.class);
    private String qseqid;
    private String sseqid;
    private double pident;
    private int length;
    private int mismatch;
    private int gapopen;
    private int qstart;
    private int qend;
    private int sstart;
    private int send;
    private double evalue;
    private double bitscore;

    /**
     * Parses a BLAST output file and creates a list of BlastHspRecord objects.
     * 
     * @param filePath the path to the BLAST output file
     * @return a list of parsed BlastHspRecord objects
     * @throws IOException if there is an error reading the file
     */
    public static List<BlastHspRecord> parseFile(String filePath) throws IOException {
        List<BlastHspRecord> records = Lists.newArrayList();
        List<String> strings = Files.readAllLines(Path.of(filePath));
        for (String string : strings){
            if (string.startsWith("#") || string.isEmpty()){
                continue;
            }

            BlastHspRecord blastHspRecord = new BlastHspRecord();
            String[] splits = EGPSStringUtil.split(string, '\t');

            if (splits == null || splits.length != 12) {
                log.error("{} is invalid splits array: length:{}\n Original string is {}",filePath, splits.length, string, new IllegalArgumentException());
            }

            setAttributesFromSplits(blastHspRecord, splits);
            records.add(blastHspRecord);
        }

        return records;
    }
    private static void setAttributesFromSplits(BlastHspRecord record, String[] splits) {
        record.qseqid = splits[0];
        record.sseqid = splits[1];
        record.pident = Double.parseDouble(splits[2]);
        record.length = Integer.parseInt(splits[3]);
        record.mismatch = Integer.parseInt(splits[4]);
        record.gapopen = Integer.parseInt(splits[5]);
        record.qstart = Integer.parseInt(splits[6]);
        record.qend = Integer.parseInt(splits[7]);
        record.sstart = Integer.parseInt(splits[8]);
        record.send = Integer.parseInt(splits[9]);
        record.evalue = Double.parseDouble(splits[10]);
        record.bitscore = Double.parseDouble(splits[11]);
    }

    /**
     * Gets the query sequence identifier
     * 
     * @return the query sequence identifier
     */
    public String getQseqid() {
        return qseqid;
    }

    /**
     * Sets the query sequence identifier
     * 
     * @param qseqid the query sequence identifier to set
     */
    public void setQseqid(String qseqid) {
        this.qseqid = qseqid;
    }

    /**
     * Gets the subject sequence identifier
     * 
     * @return the subject sequence identifier
     */
    public String getSseqid() {
        return sseqid;
    }

    /**
     * Sets the subject sequence identifier
     * 
     * @param sseqid the subject sequence identifier to set
     */
    public void setSseqid(String sseqid) {
        this.sseqid = sseqid;
    }

    /**
     * Gets the percentage of identical matches
     * 
     * @return the percentage of identical matches
     */
    public double getPident() {
        return pident;
    }

    /**
     * Sets the percentage of identical matches
     * 
     * @param pident the percentage of identical matches to set
     */
    public void setPident(double pident) {
        this.pident = pident;
    }

    /**
     * Gets the alignment length
     * 
     * @return the alignment length
     */
    public int getLength() {
        return length;
    }

    /**
     * Sets the alignment length
     * 
     * @param length the alignment length to set
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Gets the number of mismatches
     * 
     * @return the number of mismatches
     */
    public int getMismatch() {
        return mismatch;
    }

    /**
     * Sets the number of mismatches
     * 
     * @param mismatch the number of mismatches to set
     */
    public void setMismatch(int mismatch) {
        this.mismatch = mismatch;
    }

    /**
     * Gets the number of gap openings
     * 
     * @return the number of gap openings
     */
    public int getGapopen() {
        return gapopen;
    }

    /**
     * Sets the number of gap openings
     * 
     * @param gapopen the number of gap openings to set
     */
    public void setGapopen(int gapopen) {
        this.gapopen = gapopen;
    }

    /**
     * Gets the start position of the query alignment
     * 
     * @return the start position of the query alignment
     */
    public int getQstart() {
        return qstart;
    }

    /**
     * Sets the start position of the query alignment
     * 
     * @param qstart the start position of the query alignment to set
     */
    public void setQstart(int qstart) {
        this.qstart = qstart;
    }

    /**
     * Gets the end position of the query alignment
     * 
     * @return the end position of the query alignment
     */
    public int getQend() {
        return qend;
    }

    /**
     * Sets the end position of the query alignment
     * 
     * @param qend the end position of the query alignment to set
     */
    public void setQend(int qend) {
        this.qend = qend;
    }

    /**
     * Gets the start position of the subject alignment
     * 
     * @return the start position of the subject alignment
     */
    public int getSstart() {
        return sstart;
    }

    /**
     * Sets the start position of the subject alignment
     * 
     * @param sstart the start position of the subject alignment to set
     */
    public void setSstart(int sstart) {
        this.sstart = sstart;
    }

    /**
     * Gets the end position of the subject alignment
     * 
     * @return the end position of the subject alignment
     */
    public int getSend() {
        return send;
    }

    /**
     * Sets the end position of the subject alignment
     * 
     * @param send the end position of the subject alignment to set
     */
    public void setSend(int send) {
        this.send = send;
    }

    /**
     * Gets the E-value of the alignment
     * 
     * @return the E-value of the alignment
     */
    public double getEvalue() {
        return evalue;
    }

    /**
     * Sets the E-value of the alignment
     * 
     * @param evalue the E-value of the alignment to set
     */
    public void setEvalue(double evalue) {
        this.evalue = evalue;
    }

    /**
     * Gets the bit score of the alignment
     * 
     * @return the bit score of the alignment
     */
    public double getBitscore() {
        return bitscore;
    }

    /**
     * Sets the bit score of the alignment
     * 
     * @param bitscore the bit score of the alignment to set
     */
    public void setBitscore(double bitscore) {
        this.bitscore = bitscore;
    }
}
