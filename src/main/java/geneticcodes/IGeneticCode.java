package geneticcodes;

/**
 * Interface defining constants for genetic code tables used in molecular biology.
 * 
 * <p>
 * This interface provides standardized references to the various genetic code tables
 * defined by the NCBI (National Center for Biotechnology Information). Different organisms
 * use different genetic codes for translating nucleotide sequences into amino acid sequences.
 * </p>
 * 
 * <p>
 * The genetic code tables are indexed from 0 to 8, corresponding to different taxonomic
 * groups and organelles (mitochondria, plastids, etc.).
 * </p>
 * 
 * <h2>Available Genetic Codes:</h2>
 * <ul>
 * <li>0: The Standard Code (used by most organisms)</li>
 * <li>1: The Vertebrate Mitochondrial Code</li>
 * <li>2: The Yeast Mitochondrial Code</li>
 * <li>3: The Invertebrate Mitochondrial Code</li>
 * <li>4: The Ciliate, Dasycladacean and Hexamita Nuclear Code</li>
 * <li>5: The Echinoderm and Flatworm Mitochondrial Code</li>
 * <li>6: The Euplotid Nuclear Code</li>
 * <li>7: The Bacterial, Archaeal and Plant Plastid Code</li>
 * <li>8: The Mold, Protozoan, and Coelenterate Mitochondrial Code and the Mycoplasma/Spiroplasma Code</li>
 * </ul>
 * 
 * <p>
 * <strong>Important:</strong> It is recommended to use the predefined constants
 * (e.g., {@link #THE_STANDARD_CODE}) rather than directly accessing the array
 * by index to ensure code clarity and maintainability.
 * </p>
 * 
 * @author eGPS Development Team
 * @version 1.0
 * @since 1.0
 * @see GeneticCode
 * @see AbstractGeneticCodes
 */
public interface IGeneticCode {
	
	/**
	 * Array of genetic code table names corresponding to NCBI genetic code definitions.
	 * 
	 * <p>
	 * <strong>Warning:</strong> Direct array access is not recommended. Use the named
	 * constants like {@link #THE_STANDARD_CODE} for better code readability.
	 * </p>
	 */
	String[] GeneticCodeTableNames = { 
			"The Standard Code", 
			"The Vertebrate Mitochondrial Code",
			"The Yeast Mitochondrial Code", 
			"The Invertebrate Mitochondrial Code",
			"The Ciliate, Dasycladacean and Hexamita Nuclear Code", 
			"The Echinoderm and Flatworm Mitochondrial Code",
			"The Euplotid Nuclear Code", 
			"The Bacterial, Archaeal and Plant Plastid Code",
			"The Mold, Protozoan, and Coelenterate Mitochondrial Code and the Mycoplasma/Spiroplasma Code" 
	};

	/**
	 * Index for the standard genetic code (used by most eukaryotes and prokaryotes).
	 * This is the most commonly used genetic code in nature.
	 */
	int THE_STANDARD_CODE = 0;
	
	/**
	 * Index for the bacterial, archaeal, and plant plastid genetic code.
	 * This code is used by bacteria, archaea, and chloroplasts.
	 */
	int The_Bacterial_ArchaealandPlant_Plastid_Code = 7;
}
