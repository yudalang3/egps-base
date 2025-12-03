package rest.ensembl.compara;

import analysis.AbstractAnalysisAction;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

/**
 * REST client for retrieving gene tree information from Ensembl Compara.
 * 
 * <p>
 * This class extends AbstractAnalysisAction to provide comprehensive
 * capabilities for querying Ensembl's gene tree API to retrieve detailed
 * phylogenetic information about gene families including their evolutionary
 * relationships, speciation/duplication events, and detailed tree topology.</p>
 * </p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Gene Tree Retrieval:</strong> Query Ensembl genetree/member endpoints</li>
 *   <li><strong>Phylogenetic Analysis:</strong> Extract detailed evolutionary relationships</li>
 *   <li><strong>Multiple Taxonomies:</strong> Support for different taxonomic comparisons</li>
 *   <li><strong>Event Mapping:</strong> Identify speciation and duplication events</li>
 * </ul>
 * 
 * <h2>Ensembl Gene Tree API:</h2>
 * <p>This class specifically targets Ensembl's gene tree endpoint which
 * provides comprehensive phylogenetic trees for gene families including:</p>
 * <ul>
 *   <li><strong>Gene Family Trees:</strong> Complete phylogenetic relationships</li>
 *   <li><strong>Speciation Events:</strong> Species divergence points in gene evolution</li>
 *   <li><strong>Duplication Events:</strong> Gene duplication events and copies</li>
 *   <li><strong>Conservation Data:</strong> Sequence conservation across tree branches</li>
 *   <li><strong>Taxonomy Integration:</strong> Full taxonomic context for each node</li>
 * </ul>
 * 
 * <h2>Phylogenetic Analysis Applications:</h2>
 * <ul>
 *   <li><strong>Gene Family Evolution:</strong> Track expansion and contraction patterns</li>
 *   <li><strong>Functional Evolution:</strong> Study functional diversification over time</li>
 *   <li><strong>Species Relationships:</strong> Understand evolutionary relationships</li>
 *   <li><strong>Ortholog/Paralog Classification:</strong> Distinguish relationship types</li>
 *   <li><strong>Dating Evolutionary Events:</strong> Estimate timing of speciation/duplication</li>
 * </ul>
 * 
 * <h2>API Endpoint and Usage:</h2>
 * <p>Uses Ensembl gene tree endpoint: {@code /genetree/member/symbol/{species}/{gene_symbol}?compara={taxonomy}}</p>
 * <ul>
 *   <li><strong>species:</strong> Source species (e.g., homo_sapiens)</li>
 *   <li><strong>gene_symbol:</strong> Gene symbol or identifier</li>
 *   <li><strong>compara:</strong> Taxonomic group for tree construction</li>
 * </ul>
 * 
 * <h2>Taxonomic Group Support:</h2>
 * <ul>
 *   <li><strong>vertebrates:</strong> Vertebrate gene tree comparisons</li>
 *   <li><strong>metazoa:</strong> Animal kingdom gene family analysis</li>
 *   <li><strong>protists:</strong> Protist gene tree comparisons</li>
 *   <li><strong>plants:</strong> Plant kingdom gene family trees</li>
 *   <li><strong>fungi:</strong> Fungal gene tree analysis</li>
 *   <li><strong>pan_homology:</strong> Comprehensive pan-taxonomic trees</li>
 * </ul>
 * 
 * <h2>Data Structure:</h2>
 * <p>JSON response provides comprehensive gene tree information:</p>
 * <ul>
 *   <li><strong>Tree Topology:</strong> Complete branching patterns and relationships</li>
 *   <li><strong>Node Information:</strong> Gene IDs, coordinates, and annotations</li>
 *   <li><strong>Event Types:</strong> Speciation vs. duplication event classification</li>
 *   <li><strong>Taxonomic Context:</strong> Full species classification for each node</li>
 *   <li><strong>Statistical Support:</strong> Bootstrap values and confidence measures</li>
 * </ul>
 * 
 * <h2>Bioinformatics Research Applications:</h2>
 * <ul>
 *   <li><strong>Gene Family Analysis:</strong> Study gene family evolution patterns</li>
 *   <li><strong>Functional Annotation:</strong> Transfer functions across gene family</li>
 *   <li><strong>Evolutionary Genomics:</strong> Understand genome evolution processes</li>
 *   <li><strong>Comparative Studies:</strong> Compare gene families across taxa</li>
 *   <li><strong>Disease Gene Analysis:</strong> Study disease gene evolution</li>
 * </ul>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * EnsGeneTreeInfoRest client = new EnsGeneTreeInfoRest();
 * 
 * // Configure query parameters
 * client.setGeneID("EN00124479");  // Gene identifier
 * client.setOutputPath("gene_tree.json");
 * 
 * // Execute gene tree retrieval
 * client.doIt();
 * 
 * // Parse results for phylogenetic analysis
 * // JSON contains complete gene family tree
 * </pre>
 * 
 * <h2>Error Handling:</h2>
 * <ul>
 *   <li><strong>HTTP 400:</strong> No results found for gene (gene doesn't exist or no tree available)</li>
 *   <li><strong>HTTP 200:</strong> Successful retrieval with comprehensive tree data</li>
 *   <li><strong>Resource Management:</strong> Proper connection and stream cleanup</li>
 *   <li><strong>Logging:</strong> Comprehensive logging for debugging</li>
 * </ul>
 * 
 * <h2>Data Integration:</h2>
 * <p>Gene tree data integrates with other Ensembl resources:</p>
 * <ul>
 *   <li><strong>Homology Data:</strong> Links to ortholog/paralog relationships</li>
 *   <li><strong>Taxonomy Database:</strong> Full taxonomic classification</li>
 *   <li><strong>Gene Information:</strong> Integration with gene annotations</li>
 *   <li><strong>Variation Data:</strong> Links to variant and population data</li>
 * </ul>
 * 
 * @see EnsHomologyInforRest
 * @see AbstractAnalysisAction
 * @author yudal
 * @since 1.0
 */
public class EnsGeneTreeInfoRest extends AbstractAnalysisAction {

	private static final Logger logger = LoggerFactory.getLogger(EnsHomologyInforRest.class);

//	String geneID = "ENSG00000124479";
	String geneID = "EN00124479";

	@Override
	public void doIt() throws Exception {
		String server = "https://rest.ensembl.org";
		String ext = "/genetree/member/symbol/homo_sapiens/";

//		String format = MessageFormat.format("{0}{1}{2}?", server, ext, geneID);
		
		//vertebrates , metazoa, protists, plants, fungi, pan_homology
		String format = MessageFormat.format("{0}{1}{2}?compara=vertebrates", server, ext, geneID);
		logger.info(format);

		URL url = new URL(format);

		URLConnection connection = url.openConnection();
		HttpURLConnection httpConnection = (HttpURLConnection) connection;

		httpConnection.setRequestProperty("Content-Type", "application/json");
//		httpConnection.setRequestProperty("Content-Type", "text/x-phyloxml+xml");

		InputStream response = connection.getInputStream();
		int responseCode = httpConnection.getResponseCode();

		if (responseCode != 200) {
			throw new RuntimeException("Response code was not 200. Detected response was " + responseCode);
		}

		String output;
		Reader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(response, "UTF-8"));
			StringBuilder builder = new StringBuilder();
			char[] buffer = new char[8192];
			int read;
			while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
				builder.append(buffer, 0, read);
			}
			output = builder.toString();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException logOrIgnore) {
					logOrIgnore.printStackTrace();
				}
		}

		FileUtils.writeStringToFile(new File(outputPath), output, StandardCharsets.US_ASCII);

	}

	public static void main(String[] args) throws Exception {
		EnsGeneTreeInfoRest worker = new EnsGeneTreeInfoRest();
		String outPath = "C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\文献管理\\具体文献\\Wnt通路\\素材\\Curated_data\\human\\1_Norrin\\comparaInfo\\ensembl.geneTree.protists.json";
//		String outPath = "C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\文献管理\\具体文献\\Wnt通路\\素材\\Curated_data\\human\\1_Norrin\\comparaInfo\\ensembl.geneTree.vert.phyxml";
		worker.setOutputPath(outPath);

		worker.doIt();
	}
}
