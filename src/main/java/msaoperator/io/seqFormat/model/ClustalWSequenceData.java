package msaoperator.io.seqFormat.model;

import java.util.List;

import msaoperator.alignment.sequence.BasicSequenceData;
import msaoperator.alignment.sequence.SequenceI;

/**
 * Data container for ClustalW format sequence data including header metadata.
 */
public class ClustalWSequenceData extends BasicSequenceData{

	private String headerLineInFile;
	
	public ClustalWSequenceData(List<SequenceI> dataSequences) {
		super(dataSequences);
	}
	
	public void setHeaderLineInFile(String headerLineInFile) {
		this.headerLineInFile = headerLineInFile;
	}
	
	public String getHeaderLineInFile() {
		return headerLineInFile;
	}

}