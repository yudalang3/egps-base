/**
 * 
 */
package msaoperator.io.seqFormat.model;

/**
* @author YFQ
* @date 2019-05-28 09:15:02
* @version 1.0
* <p>Description:</p>
*/
public class PHYSeqElement extends SeqElementBase {
	
	private int seqCount;
	
	private int seqLength;

	public int getSeqCount() {
		return seqCount;
	}

	public void setSeqCount(int seqCount) {
		this.seqCount = seqCount;
	}

	public int getSeqLength() {
		return seqLength;
	}

	public void setSeqLength(int seqLength) {
		this.seqLength = seqLength;
	}
}
