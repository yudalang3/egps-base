package geneticcodes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import geneticcodes.codeTables.TheBacterialPlantPlastidCode;
import geneticcodes.codeTables.TheCiliateDHexamitaNuclearCode;
import geneticcodes.codeTables.TheEFMitochondrialCode;
import geneticcodes.codeTables.TheEuplotidNuclearCode;
import geneticcodes.codeTables.TheInvertebrateMitochondrialCode;
import geneticcodes.codeTables.TheMPCMCMSCode;
import geneticcodes.codeTables.TheStandardCode;
import geneticcodes.codeTables.TheVertebrateMitochondrialCode;
import geneticcodes.codeTables.TheYeastMitochondrialCode;

/**
 * Abstract base class for genetic code implementations.
 * 
 * <p>
 * This class provides the core functionality for translating nucleotide triplets (codons)
 * into amino acids according to specific genetic code tables defined by the NCBI.
 * Different organisms use different genetic codes, particularly in mitochondria and
 * certain nuclear genomes.
 * </p>
 * 
 * <p>
 * The class implements the codon-to-amino-acid translation using a hash map for efficient
 * lookup. Each concrete subclass (e.g., {@code TheStandardCode}, {@code TheVertebrateMitochondrialCode})
 * initializes the map with its specific codon table.
 * </p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Codon to amino acid translation</li>
 *   <li>Degenerate site analysis (0-fold, 2-fold, 4-fold)</li>
 *   <li>Direct CDS (Coding Sequence) translation</li>
 *   <li>Factory pattern for creating genetic code instances</li>
 * </ul>
 * 
 * <h2>Degeneracy Terminology:</h2>
 * <ul>
 *   <li><strong>0-fold degenerate sites:</strong> All nucleotide changes are non-synonymous</li>
 *   <li><strong>2-fold degenerate sites:</strong> One out of three changes is synonymous</li>
 *   <li><strong>4-fold degenerate sites:</strong> All nucleotide changes are synonymous</li>
 * </ul>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * // Create a genetic code instance
 * GeneticCode code = GeneticCode.geneticCodeFactory("The Standard Code");
 * 
 * // Translate a codon
 * Optional&lt;Character&gt; aa = code.getAminoAcid("ATG");  // Returns 'M' (Methionine)
 * 
 * // Translate a complete CDS
 * String protein = code.translateNotConsiderOther("ATGGGTTAA");  // "MG*"
 * 
 * // Analyze codon degeneracy
 * byte[] degeneracy = code.degenerateAttr(new char[]{'G','G','T'});  // [0, 0, 4]
 * </pre>
 * 
 * <h2>Reference:</h2>
 * <p>
 * NCBI Genetic Codes:
 * <a href="https://www.ncbi.nlm.nih.gov/Taxonomy/taxonomyhome.html/index.cgi?chapter=tgencodes#SG1">
 * https://www.ncbi.nlm.nih.gov/Taxonomy/taxonomyhome.html/index.cgi?chapter=tgencodes#SG1
 * </a>
 * </p>
 * 
 * @author yudalang
 * @version 1.0
 * @since 2019-07-01
 * @see IGeneticCode
 * @see AminoAcid
 * @see AbstractGeneticCodes
 */
public abstract class GeneticCode implements AminoAcid, IGeneticCode {

	/**
	 * Map storing codon (3-letter nucleotide string) to amino acid (single character) mappings.
	 * This map contains all 64 possible codons and their corresponding amino acids or stop signals.
	 */
	protected Map<String, Character> geneticCodeMap = new HashMap<>(64);
	
	/**
	 * Array of the four nucleotide bases used for generating all possible codon variations.
	 */
	protected char[] nn4 = { 'A', 'T', 'C', 'G' };

	/**
	 * Initializes the genetic code map from an array of codon-amino acid pairs.
	 * 
	 * <p>
	 * Each string in the input array should be in the format: "XXX Y" where XXX is the
	 * three-letter codon and Y is the single-letter amino acid code.
	 * </p>
	 * 
	 * @param tripletNNs_aaAbbr array of strings in format "codon amino_acid" (e.g., "ATG M")
	 */
	protected void initializeMap(String[] tripletNNs_aaAbbr) {
		int len = tripletNNs_aaAbbr.length;
		for (int i = 0; i < len; i++) {
			String s = tripletNNs_aaAbbr[i];
			String tripletNN = s.substring(0, 3);
			char aa = s.charAt(4);
			geneticCodeMap.put(tripletNN, aa);
		}
	}
	
	/**
	 * Translates a nucleotide triplet (codon) to its corresponding amino acid.
	 * 
	 * @param tripletNN character array of length 3 representing the codon
	 * @return Optional containing the amino acid character, or empty if codon is invalid
	 * @throws IllegalArgumentException if the array length is not exactly 3
	 * @see #getAminoAcid(String)
	 */
	public Optional<Character> getAminoAcid(char[] tripletNN) {
		Character ret = null;
		if (tripletNN.length != 3) {
			throw new IllegalArgumentException();
		}
		
		ret = geneticCodeMap.get(new String(tripletNN));
		
		return Optional.ofNullable(ret);
	}
	
	/**
	 * Translates a nucleotide triplet (codon) to its corresponding amino acid.
	 * 
	 * @param codon string of length 3 representing the codon (e.g., "ATG")
	 * @return Optional containing the amino acid character, or empty if codon is invalid
	 * @throws IllegalArgumentException if the string length is not exactly 3
	 * @see #getAminoAcid(char[])
	 */
	public Optional<Character> getAminoAcid(String codon) {
		Character ret = null;
		if (codon.length() != 3) {
			throw new IllegalArgumentException();
		}
		ret = geneticCodeMap.get(codon);
		
		return Optional.ofNullable(ret);
	}

	/**
	 * Analyzes the degeneracy of each position in a codon.
	 * 
	 * <p>
	 * Degeneracy measures how many nucleotide changes at each position result in synonymous
	 * (same amino acid) versus non-synonymous (different amino acid) mutations.
	 * </p>
	 * 
	 * <p><strong>Return values for each position:</strong></p>
	 * <ul>
	 *   <li><strong>0:</strong> Non-degenerate site (all 3 possible changes are non-synonymous)</li>
	 *   <li><strong>2:</strong> Two-fold degenerate site (1 out of 3 changes is synonymous)</li>
	 *   <li><strong>4:</strong> Four-fold degenerate site (all 3 changes are synonymous)</li>
	 * </ul>
	 * 
	 * <p><strong>Example:</strong></p>
	 * <pre>
	 * char[] codon = {'G', 'G', 'T'};  // Glycine (GGT)
	 * byte[] deg = degenerateAttr(codon);  // Returns [0, 0, 4]
	 * // Position 0 and 1: non-degenerate
	 * // Position 2: four-fold degenerate (GGA, GGC, GGG, GGT all code for Glycine)
	 * </pre>
	 * 
	 * @param tripletNN the codon as a character array of length 3
	 * @return array of 3 bytes indicating degeneracy at each codon position (0, 2, or 4)
	 * @see #judgeDegenerateAttr(String[], Character)
	 */
	public byte[] degenerateAttr(char[] tripletNN) {
		byte[] degenerateAttrs = new byte[3];

		Character oriAAChar = geneticCodeMap.get(new String(tripletNN));

		for (int i = 0; i < 3; i++) {
			String[] posibleTriplets = possibleTripletNucls(tripletNN, i);
			degenerateAttrs[i] = judgeDegenerateAttr(posibleTriplets, oriAAChar);
		}
		return degenerateAttrs;
	}

	/**
	 * Determines the degeneracy level based on the number of synonymous changes.
	 * 
	 * @param posibleTriplets array of 3 alternative codons at a given position
	 * @param oriAAChar the original amino acid character
	 * @return 0 for non-degenerate, 2 for two-fold degenerate, 4 for four-fold degenerate
	 */
	private byte judgeDegenerateAttr(String[] posibleTriplets, Character oriAAChar) {
		byte numOfSynnonymousChange = getNumOfSynnonymousChange(posibleTriplets,oriAAChar);
		if (numOfSynnonymousChange == 3) {
			return 4;
		} else if (numOfSynnonymousChange == 0) {
			return 0;
		} else {
			return 2;
		}
	}
	
	/**
	 * Counts how many of the possible nucleotide changes result in the same amino acid.
	 * 
	 * @param posibleTriplets array of 3 alternative codons
	 * @param oriAAChar the original amino acid character to compare against
	 * @return number of synonymous changes (0-3)
	 */
	private byte getNumOfSynnonymousChange(String[] posibleTriplets, Character oriAAChar) {
		byte numOfSynnonymousChange = 0;
		for (int i = 0; i < 3; i++) {
			Character aaChar = geneticCodeMap.get(posibleTriplets[i]);
			if (aaChar == oriAAChar) {
				numOfSynnonymousChange++;
			}
		}
		return numOfSynnonymousChange;
	}

	/**
	 * Generates the three possible alternative codons when changing one position.
	 * 
	 * <p>
	 * Given a codon and a position, this method generates the three codons that result
	 * from changing the nucleotide at that position to each of the other three bases.
	 * </p>
	 * 
	 * @param tripletNN original codon (e.g., ['A','T','G'])
	 * @param i position to change (0, 1, or 2)
	 * @return array of 3 alternative codons with the specified position changed
	 */
	private String[] possibleTripletNucls(char[] tripletNN, int i) {
		String[] ret = new String[3];
		char[] tmpTripletNN = new char[3];
		byte assignIndex = 0;

		for (char nn : nn4) {
			if (nn != tripletNN[i]) {
				System.arraycopy(tripletNN, 0, tmpTripletNN, 0, 3);
				tmpTripletNN[i] = nn;
				ret[assignIndex] = new String(tmpTripletNN);
				assignIndex++;
			}
		}

		return ret;
	}

	/**
	 * Translates a nucleotide sequence directly to a protein sequence.
	 * 
	 * <p>
	 * <strong>Important:</strong> This method assumes the input is already in the correct
	 * reading frame (0-ORF) and does not handle start codons, stop codons, or frame shifts.
	 * It simply translates every triplet sequentially.
	 * </p>
	 * 
	 * <p><strong>Note:</strong> The input length should be a multiple of 3.</p>
	 * 
	 * @param nnString nucleotide sequence string (e.g., "ATGGGTTAA")
	 * @return protein sequence string (e.g., "MG*")
	 */
	public String translateNotConsiderOther(String nnString) {
		StringBuilder proteinStr = new StringBuilder(4096);
		int len = nnString.length();
		for (int i = 0; i < len; i += 3) {
			String tripletNN = nnString.substring(i, i + 3);
			Character aaChar = geneticCodeMap.get(tripletNN);
			proteinStr.append(aaChar);
		}

		return proteinStr.toString();
	}

	/**
	 * Returns the NCBI genetic code table index for this genetic code.
	 * 
	 * <p>
	 * Each genetic code variant is assigned a standard index number by NCBI.
	 * For example:
	 * <ul>
	 *   <li>1 (or 0): The Standard Code</li>
	 *   <li>2: The Vertebrate Mitochondrial Code</li>
	 *   <li>11: The Bacterial, Archaeal and Plant Plastid Code</li>
	 * </ul>
	 * </p>
	 * 
	 * @return the NCBI genetic code table index
	 */
	public abstract int getNCBICodeTableIndex();

	/**
	 * Factory method to create a GeneticCode instance by name.
	 * 
	 * <p>
	 * This method uses the table names defined in {@link IGeneticCode#GeneticCodeTableNames}
	 * to instantiate the appropriate genetic code implementation. The comparison is
	 * case-insensitive.
	 * </p>
	 * 
	 * <p><strong>Available table names:</strong></p>
	 * <ul>
	 *   <li>"The Standard Code"</li>
	 *   <li>"The Vertebrate Mitochondrial Code"</li>
	 *   <li>"The Yeast Mitochondrial Code"</li>
	 *   <li>"The Invertebrate Mitochondrial Code"</li>
	 *   <li>"The Ciliate, Dasycladacean and Hexamita Nuclear Code"</li>
	 *   <li>"The Echinoderm and Flatworm Mitochondrial Code"</li>
	 *   <li>"The Euplotid Nuclear Code"</li>
	 *   <li>"The Bacterial, Archaeal and Plant Plastid Code"</li>
	 *   <li>"The Mold, Protozoan, and Coelenterate Mitochondrial Code and the Mycoplasma/Spiroplasma Code"</li>
	 * </ul>
	 * 
	 * @param tableName the name of the genetic code table (case-insensitive)
	 * @return a GeneticCode instance corresponding to the table name, or TheStandardCode
	 *         if the name doesn't match any known table
	 * @see IGeneticCode#GeneticCodeTableNames
	 */
	public static GeneticCode geneticCodeFactory(String tableName) {
		GeneticCode geneticCode = null;

		if (tableName.equalsIgnoreCase(GeneticCodeTableNames[0])) {
			geneticCode = new TheStandardCode();
		} else if (tableName.equalsIgnoreCase(GeneticCodeTableNames[1])) {
			geneticCode = new TheVertebrateMitochondrialCode();
		} else if (tableName.equalsIgnoreCase(GeneticCodeTableNames[2])) {
			geneticCode = new TheYeastMitochondrialCode();
		} else if (tableName.equalsIgnoreCase(GeneticCodeTableNames[3])) {
			geneticCode = new TheInvertebrateMitochondrialCode();
		} else if (tableName.equalsIgnoreCase(GeneticCodeTableNames[4])) {
			geneticCode = new TheCiliateDHexamitaNuclearCode();
		} else if (tableName.equalsIgnoreCase(GeneticCodeTableNames[5])) {
			geneticCode = new TheEFMitochondrialCode();
		} else if (tableName.equalsIgnoreCase(GeneticCodeTableNames[6])) {
			geneticCode = new TheEuplotidNuclearCode();
		} else if (tableName.equalsIgnoreCase(GeneticCodeTableNames[7])) {
			geneticCode = new TheBacterialPlantPlastidCode();
		} else if (tableName.equalsIgnoreCase(GeneticCodeTableNames[8])) {
			geneticCode = new TheMPCMCMSCode();
		} else {
			geneticCode = new TheStandardCode();
		}

		return geneticCode;
	}
	
	public static void main(String[] args) {
		TheStandardCode theStandardCode = new TheStandardCode();
		
		String string = "GGT";
		char[] charArray = string.toCharArray();
		System.out.println(Arrays.toString(theStandardCode.degenerateAttr(charArray)));
	}

}
