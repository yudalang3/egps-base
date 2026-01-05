package msaoperator.io.seqFormat;

/**  
* <p>Title: DefaultFormat</p>  
* <p>Description: 
*  Every data format should have a default 
* </p>  
* 2024-09-09: 将这个名称改一下，因为它是MSA的格式
* 
* @author yudalang  
* @date 2019-6-13  
*/
public interface MSAFormatSuffixName {
	
	/**
	 * For all data format, we need a default suffix name 
	* @author yudalang
	 */
	String getDefaultFormatName();
}
