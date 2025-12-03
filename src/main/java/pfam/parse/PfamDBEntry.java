package pfam.parse;

/**
 * Data model for Pfam database entries.
 * 
 * <p>
 * This class represents a single entry in the Pfam protein families database,
 * containing essential information about a protein family or domain including
 * its identifier, description, accession number, and classification details.
 * </p>
 * 
 * <h2>Key Properties:</h2>
 * <ul>
 *   <li><strong>ID:</strong> Internal identifier for the Pfam entry</li>
 *   <li><strong>Description:</strong> Human-readable description of the protein family/domain</li>
 *   <li><strong>Accession:</strong> Pfam accession number (e.g., PF00001)</li>
 *   <li><strong>Clans:</strong> Pfam clan classification for related families</li>
 *   <li><strong>Type:</strong> Type classification of the entry (family, domain, etc.)</li>
 * </ul>
 * 
 * <h2>Pfam Database Integration:</h2>
 * <p>
 * Pfam is a comprehensive collection of protein families and domains, with entries
 * organized into clans of related families. Each entry represents a conserved
 * protein domain or family with specific boundaries and characteristics.
 * </p>
 * 
 * <h2>Usage in Bioinformatics:</h2>
 * <ul>
 *   <li><strong>Domain Annotation:</strong> Identify protein domains in query sequences</li>
 *   <li><strong>Functional Classification:</strong> Assign functions to unknown proteins</li>
 *   <li><strong>Evolutionary Analysis:</strong> Study domain evolution and relationships</li>
 *   <li><strong>Database Integration:</strong> Link proteins to known families</li>
 *   <li><strong>Comparative Analysis:</strong> Compare domain architectures across species</li>
 * </ul>
 * 
 * <h2>Data Source:</h2>
 * <p>Pfam entries are derived from multiple sequence alignments and profile HMMs,
 * providing curated classifications of protein families and domains.</p>
 * 
 * @see PfamScanRecord
 * @see HmmDatParser
 * @author eGPS Development Team
 * @since 1.0
 */
public class PfamDBEntry {
    String id;
    String description;
    String accession;
    String clans;
    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClans() {
        return clans;
    }

    public void setClans(String clans) {
        this.clans = clans;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
