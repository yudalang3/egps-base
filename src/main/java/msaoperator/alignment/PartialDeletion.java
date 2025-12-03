package msaoperator.alignment;

/**
 * Partial deletion handler that removes alignment columns exceeding a specified gap ratio threshold.
 */
public class PartialDeletion extends DeletionHandler {

	@Override
	public String[] dealWithDeletion(String[] seqs) {
		int numOfSeqs = seqs.length;
		String[] ret = new String[numOfSeqs];

		int seqLength = seqs[0].length();
		StringBuilder[] seqsTemp = getStringBuilders(numOfSeqs);
		// The operating procedure!
		for (int i = 0; i < seqLength; i++) {

			double numOfGapOrMiss = 0;
            for (String seq : seqs) {
                char c = seq.charAt(i);
                if (!nucleotides.contains(c)) {
                    numOfGapOrMiss++;
                }
            }

			if (numOfGapOrMiss / seqLength >= partialDeleteRatio) {
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