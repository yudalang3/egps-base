package tsv.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * <h1>目的</h1>
 * <p>
 *  
 * 这个目的是 很多时候我们需要对一些生物的 entity 的名字进行转换。
 * 例如你用Ens ID，它用 NCBI 的Gene ID，还有HUGO ID。
 * 这个时候你需要一个TSV文件，里面有这个ID信息，还需要提供哪一列作为Key，哪一列作为Value。
 * 这三个重要信息就组成了这个类的目的
 *  </p>
 *  
 * <h1>输入/输出</h1>
 * 
 * <p>
 * 输入一个tsv格式的文件，还有哪两列作为key和value，没有输出，它作为一个中介的Bean类。
 * </p>
 * 
 * <h1>使用方法</h1>
 * 
 * <blockquote>
 * new类，然后set就行了，要用的地方 get属性
 * </blockquote>
 * 
 * <h1>注意点</h1>
 * <ol>
 * <li>
 * 在调用主要方法 <code>doIt()</code>时不要忘记设置一些属性
 * </li>
 * <li>
 * 现在只能当做脚本来用，如果要用的话注意设置 <code>setter</code>方法等
 * </li>
 * </ol>
 * 
 * @implSpec
 * 就是一个方便处理tsv文件的中介
 * 
 * @author yudal
 *
 */
public class TsvNameTransversionInfo {
	
	String tsvFilePath;
	/**
	 * 0-based
	 */
	int fromColumnIndex;
	/**
	 * 0-based
	 */
	int toColumnIndex;

	public TsvNameTransversionInfo() {

	}
	
	
	public String getTsvFilePath() {
		return tsvFilePath;
	}
	public void setTsvFilePath(String tsvFilePath) {
		this.tsvFilePath = tsvFilePath;
	}
	public int getFromColumnIndex() {
		return fromColumnIndex;
	}
	public void setFromColumnIndex(int fromColumnIndex) {
		this.fromColumnIndex = fromColumnIndex;
	}
	public int getToColumnIndex() {
		return toColumnIndex;
	}
	public void setToColumnIndex(int toColumnIndex) {
		this.toColumnIndex = toColumnIndex;
	}
	
	/**
	 * 快速得到你要转换的Hash
	 * @return
	 * @throws IOException
	 */
	public Map<String, String> getTransferHash() throws IOException {
		Map<String, String> ret = new HashMap<>();
		
		KitTable readFile = TSVReader.readTsvTextFile(tsvFilePath);
		List<List<String>> contents = readFile.getContents();
		for (List<String> list : contents) {
			ret.put(list.get(fromColumnIndex), list.get(toColumnIndex));
		}
		
		return ret;
	}

}
