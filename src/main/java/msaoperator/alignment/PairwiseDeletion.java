package msaoperator.alignment;

/**
 * Pairwise deletion handler that retains all alignment data without removing gaps.
 */
public class PairwiseDeletion extends DeletionHandler {

	@Override
	public String[] dealWithDeletion(String[] seqs) {
		/**
		 * Don't do anything!
		 */
		return seqs;
	}

}