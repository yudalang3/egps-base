package msaoperator.io.seqFormat;

import java.io.File;
import java.util.List;

/**
 * Abstract base class for sequence file writers implementing common formatting utilities.
 */
public abstract class AbstractWriter implements SequencesWriter {

	protected final File outputFile;
	protected boolean aligned;
	
	protected AbstractWriter(File outputFile) {
		this.outputFile = outputFile;
	}
	
	protected int configSeqNames(List<String> sequenceNames) {
		int maxSequenceNameChars = sequenceNames.get(0).length();
		
		for (int i = 1; i < sequenceNames.size(); i++) {
			int length = sequenceNames.get(i).length();
			if (length > maxSequenceNameChars) {
				maxSequenceNameChars = length;
			}
		}
		
		if (maxSequenceNameChars >= 12) {
			return maxSequenceNameChars + 2;
		}
		return 12;
	}
}