package msaoperator.alignment;

/**
 * Complete deletion handler that removes any alignment column containing gaps or missing data.
 */
public class CompeleDeletion extends DeletionHandler {

	@Override
	public String[] dealWithDeletion(String[] seqs) {
		int numOfSeqs = seqs.length;
		String[] ret = new String[numOfSeqs];

		int seqLength = seqs[0].length();
		StringBuilder[] seqsTemp = getStringBuilders(numOfSeqs);

		// The operating procedure!
		for (int i = 0; i < seqLength; i++) {

			boolean ifGapOrMiss = false;
			for (int j = 0; j < numOfSeqs; j++) {
				char c = seqs[j].charAt(i);
				if (!nucleotides.contains(c)) {
					ifGapOrMiss = true;
					break;
				}
			}

			if (ifGapOrMiss) {
				continue;
			} else {
				// append the char to stingBuilder
				for (int j = 0; j < numOfSeqs; j++) {
					seqsTemp[j].append(seqs[j].charAt(i));
				}
			}

		}

		for (int j = 0; j < numOfSeqs; j++) {
			ret[j] = seqsTemp[j].toString();
		}
		return ret;
	}

}