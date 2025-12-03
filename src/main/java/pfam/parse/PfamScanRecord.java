package pfam.parse;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Represents a single Pfam domain annotation record from hmmscan output.
 * 
 * <p>
 * This class parses and stores protein domain annotations from Pfam (Protein families) database
 * scans. Pfam is a comprehensive collection of protein domains and families represented as
 * Hidden Markov Models (HMMs). Each record represents one domain hit on a protein sequence.
 * </p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Domain Boundaries:</strong> Alignment and envelope coordinates</li>
 *   <li><strong>HMM Information:</strong> Pfam accession, name, and HMM coordinates</li>
 *   <li><strong>Statistical Scores:</strong> Bit score, E-value, significance</li>
 *   <li><strong>Classification:</strong> Domain type and clan assignment</li>
 * </ul>
 * 
 * <h2>Record Structure:</h2>
 * <p>Each PfamScanRecord contains:</p>
 * <ul>
 *   <li><strong>Sequence Info:</strong> seqId (protein identifier)</li>
 *   <li><strong>Alignment Coordinates:</strong> alignmentStart, alignmentEnd (on sequence)</li>
 *   <li><strong>Envelope Coordinates:</strong> envelopeStart, envelopeEnd (domain boundary)</li>
 *   <li><strong>HMM Info:</strong> hmmAcc (Pfam ID), hmmName (domain name)</li>
 *   <li><strong>HMM Coordinates:</strong> hmmStart, hmmEnd, hmmLength</li>
 *   <li><strong>Scores:</strong> bitScore, eValue, significance</li>
 *   <li><strong>Classification:</strong> type, clan</li>
 * </ul>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * // Parse hmmscan output file
 * Map&lt;String, List&lt;PfamScanRecord&gt;&gt; results = PfamScanRecord.parseHmmerScanOut("domains.txt");
 * 
 * // Process domains for each protein
 * for (String proteinId : results.keySet()) {
 *     List&lt;PfamScanRecord&gt; domains = results.get(proteinId);
 *     for (PfamScanRecord domain : domains) {
 *         System.out.printf("%s: %s (%d-%d) E-value=%s%n",
 *             proteinId, domain.getHmmName(),
 *             domain.getAlignmentStart(), domain.getAlignmentEnd(),
 *             domain.getEValue());
 *     }
 * }
 * </pre>
 * 
 * <h2>Input Format:</h2>
 * <p>Space-separated columns from hmmscan --domtblout output:</p>
 * <pre>
 * seqId alignStart alignEnd envStart envEnd hmmAcc hmmName type hmmStart hmmEnd
 * hmmLength bitScore eValue significance clan
 * </pre>
 * 
 * <h2>Use Cases:</h2>
 * <ul>
 *   <li>Protein domain architecture analysis</li>
 *   <li>Functional annotation of protein sequences</li>
 *   <li>Domain-based phylogenetic analysis</li>
 *   <li>Protein family classification</li>
 * </ul>
 * 
 * @author eGPS Development Team
 * @version 1.0
 * @since 1.0
 * @see PfamDBEntry
 * @see HmmDatParser
 */
public class PfamScanRecord {
    private String seqId;
    private int alignmentStart;
    private int alignmentEnd;
    private int envelopeStart;
    private int envelopeEnd;
    private String hmmAcc;
    private String hmmName;
    private String type;
    private int hmmStart;
    private int hmmEnd;
    private int hmmLength;
    private double bitScore;
    private String eValue;
    private int significance;
    private String clan;

    public PfamScanRecord(String line) {
        int indexOfSeqId = 0;
        int indexOfPfamEntryID = 5;
        int indexOfDomainName = 6;
        int indexOfType = 7;
        int indexOfAlignmentStart = 1;
        int indexOfAlignmentEnd = 2;
        int indexOfClanName = 14;
        int indexOfEnvelopeStart = 3;
        int indexOfEnvelopeEnd = 4;
        int indexOfHmmStart = 8;
        int indexOfHmmEnd = 9;
        int indexOfHmmLength = 10;
        int indexOfBitScore = 11;
        int indexOfEValue = 12;
        int indexOfSignificance = 13;

        List<String> splits = List.of(StringUtils.split(line, ' '));
        seqId = splits.get(indexOfSeqId);

        alignmentStart = Integer.parseInt(splits.get(indexOfAlignmentStart));
        alignmentEnd = Integer.parseInt(splits.get(indexOfAlignmentEnd));
        envelopeStart = Integer.parseInt(splits.get(indexOfEnvelopeStart));
        envelopeEnd = Integer.parseInt(splits.get(indexOfEnvelopeEnd));
        hmmAcc = splits.get(indexOfPfamEntryID);
        hmmName = splits.get(indexOfDomainName);
        type = splits.get(indexOfType);
        hmmStart = Integer.parseInt(splits.get(indexOfHmmStart));
        hmmEnd = Integer.parseInt(splits.get(indexOfHmmEnd));
        hmmLength = Integer.parseInt(splits.get(indexOfHmmLength));
        bitScore = Double.parseDouble(splits.get(indexOfBitScore));
        eValue = splits.get(indexOfEValue);
        significance = Integer.parseInt(splits.get(indexOfSignificance));
        clan = splits.get(indexOfClanName);
    }
    public PfamScanRecord(String seqId, int alignmentStart, int alignmentEnd, int envelopeStart, int envelopeEnd, 
                         String hmmAcc, String hmmName, String type, int hmmStart, int hmmEnd, 
                         int hmmLength, double bitScore, String eValue, int significance, String clan) {
        this.seqId = seqId;
        this.alignmentStart = alignmentStart;
        this.alignmentEnd = alignmentEnd;
        this.envelopeStart = envelopeStart;
        this.envelopeEnd = envelopeEnd;
        this.hmmAcc = hmmAcc;
        this.hmmName = hmmName;
        this.type = type;
        this.hmmStart = hmmStart;
        this.hmmEnd = hmmEnd;
        this.hmmLength = hmmLength;
        this.bitScore = bitScore;
        this.eValue = eValue;
        this.significance = significance;
        this.clan = clan;
    }

    // Getters and Setters
    public String getSeqId() { return seqId; }
    public void setSeqId(String seqId) { this.seqId = seqId; }

    public int getAlignmentStart() { return alignmentStart; }
    public void setAlignmentStart(int alignmentStart) { this.alignmentStart = alignmentStart; }

    public int getAlignmentEnd() { return alignmentEnd; }
    public void setAlignmentEnd(int alignmentEnd) { this.alignmentEnd = alignmentEnd; }

    public int getEnvelopeStart() { return envelopeStart; }
    public void setEnvelopeStart(int envelopeStart) { this.envelopeStart = envelopeStart; }

    public int getEnvelopeEnd() { return envelopeEnd; }
    public void setEnvelopeEnd(int envelopeEnd) { this.envelopeEnd = envelopeEnd; }

    public String getHmmAcc() { return hmmAcc; }
    public void setHmmAcc(String hmmAcc) { this.hmmAcc = hmmAcc; }

    public String getHmmName() { return hmmName; }
    public void setHmmName(String hmmName) { this.hmmName = hmmName; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getHmmStart() { return hmmStart; }
    public void setHmmStart(int hmmStart) { this.hmmStart = hmmStart; }

    public int getHmmEnd() { return hmmEnd; }
    public void setHmmEnd(int hmmEnd) { this.hmmEnd = hmmEnd; }

    public int getHmmLength() { return hmmLength; }
    public void setHmmLength(int hmmLength) { this.hmmLength = hmmLength; }

    public double getBitScore() { return bitScore; }
    public void setBitScore(double bitScore) { this.bitScore = bitScore; }

    public String getEValue() { return eValue; }
    public void setEValue(String eValue) { this.eValue = eValue; }

    public int getSignificance() { return significance; }
    public void setSignificance(int significance) { this.significance = significance; }

    public String getClan() { return clan; }
    public void setClan(String clan) { this.clan = clan; }

    @Override
    public String toString() {
        return seqId + "\t" + alignmentStart + "\t" + alignmentEnd + "\t" + envelopeStart + "\t" + envelopeEnd + "\t" +
               hmmAcc + "\t" + hmmName + "\t" + type + "\t" + hmmStart + "\t" + hmmEnd + "\t" +
               hmmLength + "\t" + bitScore + "\t" + eValue + "\t" + significance + "\t" + clan;
    }

    public static Map<String, List<PfamScanRecord>> parseHmmerScanOut(String domainFile) throws IOException {
        List<String> strings = Files.readAllLines(Path.of(domainFile));

        Map<String, List<PfamScanRecord>> protID2PfamRecords = Maps.newLinkedHashMap();
        for (String line : strings){
            if (line.startsWith("#") || line.isEmpty()){
                continue;
            }

            PfamScanRecord pfamScanRecord = new PfamScanRecord(line);
            List<PfamScanRecord> pfamScanRecords = protID2PfamRecords.computeIfAbsent(pfamScanRecord.getSeqId(), k -> Lists.newArrayList());
            pfamScanRecords.add(pfamScanRecord);
        }

        return protID2PfamRecords;
    }
}
