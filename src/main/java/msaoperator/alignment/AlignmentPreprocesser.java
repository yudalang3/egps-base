package msaoperator.alignment;

import java.util.List;


/**
 * 
* <p>Title: AlignmentPreprocess</p>  
* <p>Description: 
* There are four tree reconstruction methods, every method's first step is to process 
* the alignment. Namely:
* 1. deal with gaps
* 
* <br>
* Things you need to consider is 
* 1. this class can be use once or several times !  So you need to use two constructors 
* 2. the input seqs and seqNames will be either <code> List\\<String\\> String[] </code>
* </p>  
* @author yudalang  
* @date 2019-6-18
 */
public class AlignmentPreprocesser {

	/** the length of alignment */
	protected int seqLength;

	protected String[] sequences;
	protected String[] seqNames;

	// 1 complete deletion; 2 pair-wise deletion; 3 partial deletion
	protected final int GAP_MISS_COMPLETE_DELETION = 1;
	protected final int GAP_MISS_PAIRWISE_DELETION = 2;
	protected final int GAP_MISS_PARTIAL_DELETION = 3;
	protected DeletionHandler deletionHandler = new CompeleDeletion();
	
	public AlignmentPreprocesser() {}
	
	public AlignmentPreprocesser(String[] sequences, String[] seqNames) {
		this.sequences = sequences;
		this.seqNames = seqNames;
	}
	
	public AlignmentPreprocesser(List<String> seqs, List<String> seq_names) {
		seqNames = new String[seq_names.size()];
		seqNames = seq_names.toArray(seqNames);
		
		sequences = new String[seqs.size()];
		sequences = seqs.toArray(sequences);
	}

	public final void setSeqsAndSeqNames(List<String> seqs, List<String> seq_names) {
		seqNames = new String[seq_names.size()];
		seqNames = seq_names.toArray(seqNames);
		
		sequences = new String[seqs.size()];
		sequences = seqs.toArray(sequences);
	}
	
	public final void setSeqNames(List<String> seq_names) {
		seqNames = new String[seq_names.size()];
		seqNames = seq_names.toArray(seqNames);
	}
	
	public final void setSeqNames(String[] seq_names) {
		this.seqNames = seq_names;
	}
	
	public final void setSequences(List<String> seqs) {
		sequences = new String[seqs.size()];
		sequences = seqs.toArray(sequences);
	}
	
	public void setGap_miss_dataTreatment(int gap_miss_dataTreatment) {
		if (gap_miss_dataTreatment == GAP_MISS_PAIRWISE_DELETION) {
			deletionHandler = new PairwiseDeletion();
		}else if (gap_miss_dataTreatment == GAP_MISS_PARTIAL_DELETION) {
			deletionHandler = new PartialDeletion();
		}else {
			deletionHandler = new CompeleDeletion();
		}
	}

	public void setPartialDeleteRatio(double ratio) {
		deletionHandler.setPartialDeleteRatio(ratio);
	}
	
	public void setIfSupportFuzzyBase(boolean ifAB) {
		deletionHandler.setIfSupportAmbigousBase(ifAB);
	}
	
	/**
	 * Three situations:<br>
	 * 
	 * YDL 20190705,There is no meanings to consider the null values!
	 * So, If you want to invoke this method, you need to make sure not 
	 * input null values!
	 * 
	 * 1. complete deletion; 
	 *     if contains null, return {"","","" . . .}.
	 *     
	 * 2. pair-wise deletion: Directly return ! 
	 * 3. partial deletion;
	 * 
	 * @author yudalang
	 * @date 2018-11-6
	 */
	public String[] dealWithDeletion(String[] seqs) {
		return deletionHandler.dealWithDeletion(seqs);
	}
	
	public String[] getProcessedAlignment() {
		return dealWithDeletion(sequences);
	}
	
	/**
	 * When use this method, be caution to set the <b> GAP or MISS data treatment. </b>  
	 */
	public int getValidateLength(List<String> seqs) {
		String[] sequences = new String[seqs.size()];
		sequences = seqs.toArray(sequences);
		
		return dealWithDeletion(sequences)[0].length();
	}
}
