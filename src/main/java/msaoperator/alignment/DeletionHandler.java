package msaoperator.alignment;

import java.util.HashSet;

/**
 * Abstract base class for handling gap deletion strategies in multiple sequence alignments.
 */
public abstract class DeletionHandler {
	
	protected HashSet<Character> nucleotides;
	protected double partialDeleteRatio;
	
	public DeletionHandler() {
		nucleotides = new HashSet<Character>(4);
		nucleotides.add('A');
		nucleotides.add('T');
		nucleotides.add('C');
		nucleotides.add('G');
	}
	
	/**
	 * @param ifAB :if ambiguous base treat as candidate bases with equal possibility!
	 */
	public void setIfSupportAmbigousBase(boolean ifAB) {
		if (ifAB) {
			nucleotides = new HashSet<Character>(4);
			nucleotides.add('A');
			nucleotides.add('T');
			nucleotides.add('C');
			nucleotides.add('G');

			nucleotides.add('Y');
			nucleotides.add('R');
			nucleotides.add('W');
			nucleotides.add('S');
			nucleotides.add('K');
			nucleotides.add('M');
			nucleotides.add('D');
			nucleotides.add('V');
			nucleotides.add('H');
			nucleotides.add('B');
			nucleotides.add('N');
		}else {
			nucleotides = new HashSet<Character>(4);
			nucleotides.add('A');
			nucleotides.add('T');
			nucleotides.add('C');
			nucleotides.add('G');
		}
	}
	
	/**
	 * 
	 * @param seqs
	 * @return sequences after deal with deletion.
	 */
	public abstract String[] dealWithDeletion(String[] seqs);
	
	/**
	 * Only for partial deletion!
	 * @param ratio
	 */
	public void setPartialDeleteRatio(double ratio) {
		partialDeleteRatio = ratio;
	}
	
	protected StringBuilder[] getStringBuilders(int numOfSeqs) {
		StringBuilder[] ret = new StringBuilder[numOfSeqs];
		for (int j = 0; j < numOfSeqs; j++) {
			ret[j] = new StringBuilder(8192);
		}
		return ret;
	}
}