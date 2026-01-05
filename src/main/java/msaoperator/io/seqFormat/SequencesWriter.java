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
* <p>Title: FragmentAlignmentParser</p>
* <p>Description: </p>  
* @author yudalang  
* @date 2019-5-17
*/
public interface SequencesWriter {
	
	/**
	 * TestExample function to let child class parse files!<br>
	 * Note: The child class of this interface is the ultimate converted file format!
	 * 
	 * @param sInfo : The format of the source file
	 */
	void write(SequenceFormatInfo sInfo) throws Exception;
	
	/**
	 * Note when the developers call this method! 
	 * <code>eles</code> <b>should be the corresponding type!</b>
	 * 
	 * @param eles : should be the corresponding type!
	 * @param isAligned : if msa file is aligned!
	 *  
	 */
	<E extends BasicSequenceData> void setElements(E eles,boolean isAligned);
	
}
