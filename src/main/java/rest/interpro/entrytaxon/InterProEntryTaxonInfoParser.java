package rest.interpro.entrytaxon;

import analysis.AbstractAnalysisAction;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for InterPro entry information with taxonomic annotations.
 * 
 * <p>
 * This class extends AbstractAnalysisAction to provide specialized parsing
 * capabilities for InterPro REST API responses containing protein family
 * information with taxonomic classifications. It converts JSON responses
 * from InterPro endpoints into structured Java objects for further analysis.
 * </p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>JSON Processing:</strong> Converts InterPro JSON responses to Java objects</li>
 *   <li><strong>Taxonomy Integration:</strong> Handles taxonomic annotations within entry data</li>
 *   <li><strong>Structured Output:</strong> Provides easy access to parsed InterPro data</li>
 *   <li><strong>Batch Processing:</strong> Supports processing of multiple entries</li>
 * </ul>
 * 
 * <h2>InterPro Database Integration:</h2>
 * <p>This parser specifically handles responses from InterPro taxonomy-related
 * endpoints that provide protein family information with cross-species
 * annotations and relationships. It processes the complete response structure
 * including metadata, entry details, and additional statistical information.</p>
 * 
 * <h2>Bioinformatics Applications:</h2>
 * <ul>
 *   <li><strong>Protein Family Analysis:</strong> Extract and analyze protein family data</li>
 *   <li><strong>Taxonomic Distribution:</strong> Study protein families across species</li>
 *   <li><strong>Cross-Reference Generation:</strong> Create protein family cross-reference tables</li>
 *   <li><strong>Domain Architecture:</strong> Analyze domain organization across taxa</li>
 *   <li><strong>Database Integration:</strong> Integrate InterPro with other databases</li>
 * </ul>
 * 
 * <h2>Processing Workflow:</h2>
 * <ol>
 *   <li>Read JSON response from InterPro REST API</li>
 *   <li>Parse JSON structure into IPREntryTaxonInfoBean</li>
 *   <li>Extract individual records and metadata</li>
 *   <li>Provide structured access to parsed data</li>
 * </ol>
 * 
 * <h2>Usage Pattern:</h2>
 * <pre>
 * InterProEntryTaxonInfoParser parser = new InterProEntryTaxonInfoParser();
 * parser.setInputPath("interpro_response.json");
 * parser.doIt();
 * 
 * IPREntryTaxonInfoBean data = parser.getParseObject();
 * for (IPREntryTaxonInfoRecordBean record : data.getResults()) {
 *     String accession = record.getMetadata().getAccession();
 *     // Process each entry
 * }
 * </pre>
 * 
 * <h2>Input Format:</h2>
 * <p>JSON response from InterPro taxonomy endpoints containing protein family
 * information with taxonomic annotations.</p>
 * 
 * <h2>Output Structure:</h2>
 * <ul>
 *   <li><strong>IPREntryTaxonInfoBean:</strong> Main container with results list</li>
 *   <li><strong>IPREntryTaxonInfoRecordBean:</strong> Individual entry records</li>
 *   <li><strong>Metadata:</strong> Entry metadata and hierarchical information</li>
 *   <li><strong>Entries:</strong> Detailed entry information with locations</li>
 * </ul>
 * 
 * @see IPREntryTaxonInfoBean
 * @see IPREntryTaxonInfoRecordBean
 * @see AbstractAnalysisAction
 * @author eGPS Development Team
 * @since 1.0
 */
public class InterProEntryTaxonInfoParser extends AbstractAnalysisAction{

	private IPREntryTaxonInfoBean parseObject;

	@Override
	public void doIt() throws Exception {
		
		List<String> readLines = FileUtils.readLines(new File(inputPath), StandardCharsets.UTF_8);
		StringBuilder sBuilder = new StringBuilder();
		for (String string : readLines) {
			sBuilder.append(string);
		}
		
		parseObject = JSONObject.parseObject(sBuilder.toString(), IPREntryTaxonInfoBean.class);
		
	}
	
	public IPREntryTaxonInfoBean getParseObject() {
		return parseObject;
	}
	
	public static void main(String[] args) throws Exception {
		InterProEntryTaxonInfoParser worker = new InterProEntryTaxonInfoParser();
		
		worker.setInputPath("C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\文献管理\\具体文献\\Wnt通路\\素材\\Curated_data\\human\\1_Norrin\\comparaInfo\\Norrin_InterPro\\norrin.domain.protein.all.interPro.json");
		
		worker.doIt();
		
		IPREntryTaxonInfoBean parseObject2 = worker.getParseObject();
		
		
		List<String> output = new ArrayList<>();
		output.add("ID\tName");
		
		for (IPREntryTaxonInfoRecordBean bean : parseObject2.results) {
			Metadata metadata = bean.getMetadata();
			String accession = metadata.getAccession();
			String name = metadata.getName();
			
			output.add(accession + "\t" + name);
		}
//		System.out.println(parseObject2.results.size());
		
		String outPath = "C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\文献管理\\具体文献\\Wnt通路\\素材\\Curated_data\\human\\1_Norrin\\comparaInfo\\Norrin_InterPro\\norrin.domain.protein.all.transfered.tsv";
		FileUtils.writeLines(new File(outPath), output);
	}

}
