package rest.interpro.entrytaxon;

/**
 * Statistical counters for InterPro entry information.
 * 
 * <p>
 * This class represents various statistical counters associated with
 * InterPro entries, providing quantitative information about the
 * scope and coverage of protein family/domain annotations.
 * </p>
 * 
 * <h2>Key Properties:</h2>
 * <ul>
 *   <li><strong>entries:</strong> Total number of entries related to this protein family</li>
 *   <li><strong>structures:</strong> Number of 3D protein structures available</li>
 *   <li><strong>proteins:</strong> Number of proteins annotated with this domain/family</li>
 *   <li><strong>sets:</strong> Number of related protein sets or collections</li>
 *   <li><strong>proteomes:</strong> Number of proteomes containing this family</li>
 * </ul>
 * 
 * <h2>Statistical Analysis Applications:</h2>
 * <ul>
 *   <li><strong>Domain Prevalence:</strong> Assess how common a domain is across proteomes</li>
 *   <li><strong>Structural Coverage:</strong> Evaluate available 3D structure information</li>
 *   <li><strong>Protein Universe Analysis:</strong> Understand family distribution</li>
 *   <li><strong>Database Completeness:</strong> Assess annotation coverage</li>
 *   <li><strong>Comparative Analysis:</strong> Compare families across different metrics</li>
 * </ul>
 * 
 * <h2>Bioinformatics Context:</h2>
 * <p>These counters provide crucial metrics for understanding the biological
 * significance and utility of protein families/domains. High protein counts
 * may indicate broad functional relevance, while high structure counts
 * suggest good 3D characterization.</p>
 * 
 * <h2>Database Integration:</h2>
 * <p>Counters are aggregated from multiple member databases of InterPro,
 * providing a unified view of the combined evidence for each protein family.</p>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * Counters stats = new Counters();
 * stats.setEntries(25);
 * stats.setProteins(12000);
 * stats.setStructures(300);
 * stats.setProteomes(450);
 * 
 * // Calculate domain prevalence
 * double coverage = (double) stats.getProteins() / stats.getProteomes();
 * </pre>
 * 
 * @see Extra_fields
 * @see IPREntryTaxonInfoRecordBean
 * @author eGPS Development Team
 * @since 1.0
 */
public class Counters {
	
	int entries;
	int structures;
	
	int proteins;
	
	int sets;
	
	int proteomes;

	public int getEntries() {
		return entries;
	}

	public void setEntries(int entries) {
		this.entries = entries;
	}

	public int getStructures() {
		return structures;
	}

	public void setStructures(int structures) {
		this.structures = structures;
	}

	public int getProteins() {
		return proteins;
	}

	public void setProteins(int proteins) {
		this.proteins = proteins;
	}

	public int getSets() {
		return sets;
	}

	public void setSets(int sets) {
		this.sets = sets;
	}

	public int getProteomes() {
		return proteomes;
	}

	public void setProteomes(int proteomes) {
		this.proteomes = proteomes;
	}
	
	

}
