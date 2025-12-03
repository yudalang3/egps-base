package msaoperator.io.seqFormat.model;

/**
 * Base element for ClustalW format sequence data with header information.
 */
public class CLUElementBase extends SeqElementBase {
	
	protected String header;
	
	public void setHeader(String header) {
		this.header = header;
	}
	
	public String getHeader() {
		return header;
	}

}