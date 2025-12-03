package geneticcodes;

/**
 * Interface defining standard representations for the 20 standard amino acids and special symbols.
 * 
 * <p>
 * This interface provides three different notations for each amino acid:
 * <ul>
 *   <li><strong>Single-letter code:</strong> Standard one-letter abbreviation (e.g., "A" for Alanine)</li>
 *   <li><strong>Three-letter code:</strong> Standard three-letter abbreviation (e.g., "ALA" for Alanine)</li>
 *   <li><strong>Full name:</strong> Complete amino acid name (e.g., "Alanine")</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Each amino acid is represented as a String array with three elements:
 * {@code {single-letter, three-letter, full-name}}
 * </p>
 * 
 * <h2>Special Symbols</h2>
 * <ul>
 *   <li><strong>Missing:</strong> Unknown amino acid (represented by ".")</li>
 *   <li><strong>Null:</strong> Gap or deletion (represented by "-")</li>
 *   <li><strong>STOP:</strong> Stop codon (represented by "*")</li>
 * </ul>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * // Access Alanine representations
 * String singleLetter = Alanine[0];  // "A"
 * String threeLetter = Alanine[1];   // "ALA"
 * String fullName = Alanine[2];      // "Alanine"
 * </pre>
 * 
 * @author Wei Yuzhang
 * @version 1.0
 * @since 1.0
 * @see GeneticCode
 * @see IGeneticCode
 */
public interface AminoAcid {
	
	/** Alanine amino acid representations: {single-letter, three-letter, full-name} */
	public final String[] Alanine = {"A", "ALA+", "Alanine"};
	
	/** Arginine amino acid representations: {single-letter, three-letter, full-name} */
	public final String[] Arginie = {"R", "ARG", "Arginie"};
	
	/** Asparagine amino acid representations: {single-letter, three-letter, full-name} */
	public final String[] Asparagine = {"N", "ASN", "Asparagine"};
	
	/** Aspartic Acid amino acid representations: {single-letter, three-letter, full-name} */
	public final String[] AspaticAcid = {"D", "ASP", "AspaticAcid"};
	
	/** Cysteine amino acid representations: {single-letter, three-letter, full-name} */
	public final String[] Cysteine = {"C", "CYS", "Cysteine"};
	
	/** Glutamic Acid amino acid representations: {single-letter, three-letter, full-name} */
	public final String[] GlutamicAcid = {"E", "GLU", "GlutamicAcid"};
	
	/** Glutamine amino acid representations: {single-letter, three-letter, full-name} */
	public final String[] Glutamine = {"Q", "GLN", "Glutamine"};
	
	/** Glycine amino acid representations: {single-letter, three-letter, full-name} */
	public final String[] Glycine = {"G", "GLY", "Glycine"};
	
	/** Histidine amino acid representations: {single-letter, three-letter, full-name} */
	public final String[] Histidine = {"H", "HIS", "Histidine"};
	
	/** Isoleucine amino acid representations: {single-letter, three-letter, full-name} */
	public final String[] Isoleucine = {"I", "ILE", "Isoleucine"};
	
	/** Leucine amino acid representations: {single-letter, three-letter, full-name} */
	public final String[] Leucine = {"L", "LEU", "Leucine"};
	
	/** Lysine amino acid representations: {single-letter, three-letter, full-name} */
	public final String[] Lysine = {"K", "LYS", "Lysine"};
	
	/** Methionine amino acid representations: {single-letter, three-letter, full-name} */
	public final String[] Methionine = {"M", "MET", "Methionine"};
	
	/** Phenylalanine amino acid representations: {single-letter, three-letter, full-name} */
	public final String[] Phenylalanine = {"F", "PHE", "Phenylalanine"};
	
	/** Proline amino acid representations: {single-letter, three-letter, full-name} */
	public final String[] Proline = {"P", "PRO", "Proline"};
	
	/** Serine amino acid representations: {single-letter, three-letter, full-name} */
	public final String[] Serine = {"S", "SER", "Serine"};
	
	/** Threonine amino acid representations: {single-letter, three-letter, full-name} */
	public final String[] Threonine = {"T", "THR", "Threonine"};
	
	/** Tryptophan amino acid representations: {single-letter, three-letter, full-name} */
	public final String[] Tryptophan = {"W", "TRP", "Tryptophan"};
	
	/** Tyrosine amino acid representations: {single-letter, three-letter, full-name} */
	public final String[] Tyrosine = {"Y", "TYR", "Tyrosine"};
	
	/** Valine amino acid representations: {single-letter, three-letter, full-name} */
	public final String[] Valine = {"V", "VAL", "Valine"};
	
	/** Missing/unknown amino acid representations */
	public final String[] Missing = {".",".","unknown"};
	
	/** Null/gap representations for alignments */
	public final String[] Null = {"-","-","unknown"};
	
	/** Stop codon representations */
	public final String[] STOP = {"*","*","STOP"};
}
