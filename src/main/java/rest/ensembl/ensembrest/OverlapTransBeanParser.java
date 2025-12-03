package rest.ensembl.ensembrest;

import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Parser for Ensembl genomic feature overlap data.
 * 
 * <p>
 * This class provides parsing capabilities for JSON responses from
 * the Ensembl REST API that contain protein domain and feature
 * annotations. It converts JSON arrays of feature data into structured
 * Java objects organized by feature type for efficient analysis.
 * </p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>JSON Processing:</strong> Parse Ensembl JSON responses into Java objects</li>
 *   <li><strong>Type Organization:</strong> Group features by their source database type</li>
 *   <li><strong>Efficient Indexing:</strong> Use Guava for fast collection operations</li>
 *   <li><strong>Logging Support:</strong> Comprehensive logging for debugging and monitoring</li>
 * </ul>
 * 
 * <h2>Ensembl REST API Integration:</h2>
 * <p>This parser specifically handles JSON responses from Ensembl's
 * overlap and feature annotation endpoints. It processes protein
 * domain predictions, functional site annotations, and other
 * feature types from multiple member databases.</p>
 * 
 * <h2>Input Format:</h2>
 * <p>JSON array containing OverlapTransElementBean objects, each
 * representing a protein domain or feature annotation with
 * genomic coordinates and functional information.</p>
 * 
 * <h2>Output Structure:</h2>
 * <p>Map<String, List<OverlapTransElementBean>> where:</p>
 * <ul>
 *   <li><strong>Key:</strong> Feature type (Pfam, Prosite, SMART, CDD, etc.)</li>
 *   <li><strong>Value:</strong> List of features of that type for the query protein</li>
 * </ul>
 * 
 * <h2>Bioinformatics Applications:</h2>
 * <ul>
 *   <li><strong>Domain Architecture Analysis:</strong> Map complete protein domain layouts</li>
 *   <li><strong>Comparative Proteomics:</strong> Compare domain content across proteins</li>
 *   <li><strong>Functional Annotation:</strong> Transfer functions based on domain predictions</li>
 *   <li><strong>Database Integration:</strong> Cross-reference with external databases</li>
 *   <li><strong>Structural Prediction:</strong> Guide 3D structure modeling efforts</li>
 * </ul>
 * 
 * <h2>Processing Workflow:</h2>
 * <ol>
 *   <li>Read JSON content from file or API response</li>
 *   <li>Parse JSON array into List&lt;OverlapTransElementBean&gt;</li>
 *   <li>Index features by type using Guava Multimaps</li>
 *   <li>Return organized map for downstream analysis</li>
 * </ol>
 * 
 * <h2>Performance Characteristics:</h2>
 * <ul>
 *   <li>Uses Guava ImmutableListMultimap for efficient indexing</li>
 *   <li>Logging integrated via SLF4J for production monitoring</li>
 *   <li>Supports both File and String path inputs</li>
 *   <li>Handles large JSON arrays efficiently</li>
 * </ul>
 * 
 * <h2>Error Handling:</h2>
 * <p>The parse method throws IOException for file I/O errors and
 * JSON parsing errors. Calling code should handle these exceptions
 * appropriately for robust production use.</p>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * OverlapTransBeanParser parser = new OverlapTransBeanParser();
 * String jsonPath = "ensembl_features.json";
 * 
 * // Parse JSON file
 * Map&lt;String, List&lt;OverlapTransElementBean&gt;&gt; features = parser.parse(jsonPath);
 * 
 * // Analyze by feature type
 * for (Map.Entry&lt;String, List&lt;OverlapTransElementBean&gt;&gt; entry : features.entrySet()) {
 *     String featureType = entry.getKey();
 *     List&lt;OverlapTransElementBean&gt; featuresOfType = entry.getValue();
 *     
 *     System.out.println(featureType + ": " + featuresOfType.size() + " features");
 *     
 *     for (OverlapTransElementBean feature : featuresOfType) {
 *         System.out.println("  " + feature.getDescription() + 
 *                           " [" + feature.getStart() + "-" + feature.getEnd() + "]");
 *     }
 * }
 * </pre>
 * 
 * <h2>Database Sources:</h2>
 * <p>Features can originate from multiple member databases:
 * Pfam (protein families), Prosite (functional sites), SMART (signaling domains),
 * CDD (conserved domains), TIGRFAMs (protein families), and others.</p>
 * 
 * @see OverlapTransElementBean
 * @see RestGetProteinDomains
 * @author eGPS Development Team
 * @since 1.0
 */
public class OverlapTransBeanParser {

	private static final Logger logger = LoggerFactory.getLogger(OverlapTransBeanParser.class);

	public Map<String, List<OverlapTransElementBean>> parse(String path) throws IOException {
		return parse(new File(path));
	}
	public Map<String, List<OverlapTransElementBean>> parse(File path) throws IOException {

		String cont = FileUtils.readFileToString(path, StandardCharsets.US_ASCII);
		List<OverlapTransElementBean> arrays = JSONArray.parseArray(cont, OverlapTransElementBean.class);

		ImmutableListMultimap<String, OverlapTransElementBean> type2beanMap = Multimaps.index(arrays,
				new Function<OverlapTransElementBean, String>() {
					@Override
					public String apply(OverlapTransElementBean input) {
						return input.getType();
					}
				});

		Map<String, List<OverlapTransElementBean>> asMap2 = Multimaps.asMap(type2beanMap);

		for (Entry<String, List<OverlapTransElementBean>> entry : asMap2.entrySet()) {
			logger.trace("{} , size {}", entry.getKey(), entry.getValue().size());
		}

		return asMap2;
	}

	public static void main(String[] args) throws IOException {
		String path = "C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\文献管理\\具体文献\\Wnt通路\\素材\\human\\DVL\\protein\\rest.all.json";
		new OverlapTransBeanParser().parse(path);
	}

}
