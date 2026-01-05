package msaoperator.io.seqFormat.model;


/**
 * 
* <p>Title: NeXMLSeqElement</p>  
* <p>Description: 
*     PIR/NBRF format
* </p>  
* @author yudalang  
* @date 2019-5-22
 */
public class PIRSeqElement extends SeqElementBase {
	
	protected String commont;
	
	public void setCommont(String commont) {
		this.commont = commont;
	}
	
	public String getCommont() {
		return commont;
	}
}
