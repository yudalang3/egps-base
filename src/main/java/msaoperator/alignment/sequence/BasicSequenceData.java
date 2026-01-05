package msaoperator.alignment.sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * @ClassName BasicSequenceData
 * 
 * @author mhl
 * 
 * @Date Created on:2019-12-20 17:22
 * 
 */
public class BasicSequenceData {

	protected List<SequenceI> dataSequences;

	public BasicSequenceData(List<SequenceI> dataSequences) {
		this.dataSequences = dataSequences;
	}

	public List<SequenceI> getDataSequences() {
		return dataSequences;
	}

	/**
	 * 
	 * The total number of rows of data
	 * 
	 * @author mhl
	 *
	 * @Date Created on:2019-07-16 17:14
	 */
	public int getTotalSequenceCount() {
		if (dataSequences == null) {
			return 0;
		}
		return dataSequences.size();
	}

	public SequenceI getSequenceI(int index) {
		return dataSequences.get(index);
	}

	public List<String> getSequences() {
		List<String> seqs = dataSequences.stream().map(sequence -> sequence.getSeqAsString())
				.collect(Collectors.toList());
		return seqs;
	}

	public List<String> getSequenceNames() {
		List<String> seqNames = new ArrayList<>();
		for (SequenceI sequence : dataSequences) {
			String seqName = sequence.getSeqName();
			seqNames.add(seqName);
		}
		return seqNames;
	}

	/**
	 * 
	 * Length of Seq
	 *
	 * @Date Created on:2019-07-16 17:16
	 */
	public int getLength() {
		if (dataSequences.size() == 0) {
			return 0;
		}

		int maxLength = dataSequences.stream().mapToInt(sequenceI -> sequenceI.getLength()).max().getAsInt();
		return maxLength;
	}

	public String getName(int index) {
		return dataSequences.get(index).getSeqName();
	}


	public List<SequenceComponentRatio> getRatio() {
		return null;
	}
}
