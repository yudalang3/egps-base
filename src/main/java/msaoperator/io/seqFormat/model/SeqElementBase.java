package msaoperator.io.seqFormat.model;

/**
 * Base class for sequence elements containing name and sequence data.
 */
public class SeqElementBase {
	
	protected String seqName;
	protected String sequence;
	
	public String getSeqName() {
		return seqName;
	}
	public void setSeqName(String seqName) {
		this.seqName = seqName;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	
}