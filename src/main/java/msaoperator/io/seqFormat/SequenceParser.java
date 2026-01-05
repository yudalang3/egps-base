/**  
* <p>Title: FregmentAlignmentParser.java</p>  
* <p>Description: </p>  
* <p>Copyright: Copyright (c) 2017, PICB</p>  
* <p>Owner: http://www.picb.ac.cn/evolgen/</p>  
* @author yudalang 
* @date 2019年5月17日  
* @version 1.0  

*/ 
package msaoperator.io.seqFormat;

import msaoperator.alignment.sequence.BasicSequenceData;

/**  
* <p>Title: FregmentAlignmentParser</p>  
* <p>Description: Common sequence parser interface!
* I separate sequence parse process and get process!
* 
* </p>  
* @author yudalang  
* @date 2019-5-17
*/
public interface SequenceParser {
	
	/** TestExample function to let child class parse files!*/
	void parse() throws Exception;
	
	/** The intermediate data structure */
	
	<E extends BasicSequenceData> E getSeqElements();
}
