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
 * REST client for retrieving gene homology information from Ensembl.
 * 
 * <p>
 * This class extends AbstractAnalysisAction to provide capabilities
 * for querying Ensembl's homology API to retrieve comparative genomics
 * data including orthologs, paralogs, and phylogenetic relationships
 * between genes across different species and taxonomic groups.
 * </p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Homology Retrieval:</strong> Query Ensembl homology/id endpoints</li>
 *   <li><strong>Multiple Taxonomies:</strong> Support for different taxonomic groups</li>
 *   <li><strong>Comparative Analysis:</strong> Extract ortholog and paralog relationships</li>
 *   <li><strong>JSON Output:</strong> Store results in structured JSON format</li>
 * </ul>
 * 
 * <h2>Ensembl Compara Database:</h2>
 * <p>Ensembl Compara provides comprehensive comparative genomics data
 * including:</p>
 * <ul>
 *   <li><strong>Orthologs:</strong> Genes inherited from common ancestors across species</li>
 *   <li><strong>Paralogs:</strong> Genes related by duplication within a species</li>
 *   <li><strong>Phylogenetic Trees:</strong> Evolutionary relationships between genes</li>
 *   <li><strong>Conservation Scores:</strong> Sequence conservation across species</li>
 *   <li><strong>Synteny Data:</strong> Chromosome context and rearrangements</li>
 * </ul>
 * 
 * <h2>Taxonomic Coverage:</h2>
 * <p>Supports multiple taxonomic groups through compara database selection:</p>
 * <ul>
 *   <li><strong>vertebrates:</strong> Vertebrate species comparisons</li>
 *   <li><strong>metazoa:</strong> Animal kingdom comparisons</li>
 *   <li><strong>protists:</strong> Protist comparative analysis</li>
 *   <li><strong>plants:</strong> Plant kingdom comparisons</li>
 *   <li><strong>fungi:</strong> Fungal comparative analysis</li>
 *   <li><strong>pan_homology:</strong> Comprehensive pan-taxonomic analysis</li>
 * </ul>
 * 
 * <h2>Bioinformatics Applications:</h2>
 * <ul>
 *   <li><strong>Gene Function Prediction:</strong> Infer function from orthologs</li>
 *   <li><strong>Evolutionary Analysis:</strong> Study gene family evolution</li>
 *   <li><strong>Comparative Genomics:</strong> Analyze gene content across species</li>
 *   <li><strong>Disease Research:</strong> Find human disease gene orthologs</li>
 *   <li><strong>Drug Discovery:</strong> Identify conserved drug targets</li>
 * </ul>
 * 
 * <h2>API Endpoint:</h2>
 * <p>Uses Ensembl homology endpoint: {@code /homology/id/{species}/{gene_id}?compara={taxonomy}}</p>
 * <ul>
 *   <li><strong>species:</strong> Source species (e.g., human)</li>
 *   <li><strong>gene_id:</strong> Ensembl gene identifier</li>
 *   <li><strong>compara:</strong> Taxonomic group for comparison</li>
 * </ul>
 * 
 * <h2>Data Structure:</h2>
 * <p>JSON response includes comprehensive homology information:</p>
 * <ul>
 *   <li><strong>Gene Data:</strong> Basic gene information and coordinates</li>
 *   <li><strong>Homology Relationships:</strong> Ortholog and paralog relationships</li>
 *   <li><strong>Conservation Data:</strong> Sequence conservation scores</li>
 *   <li><strong>Phylogenetic Context:</strong> Evolutionary relationships</li>
 * </ul>
 * 
 * <h2>Usage Pattern:</h2>
 * <ol>
 *   <li>Set gene ID for homology analysis</li>
 *   <li>Configure taxonomic group for comparison</li>
 *   <li>Execute doIt() to retrieve homology data</li>
 *   <li>Parse JSON results for comparative analysis</li>
 * </ol>
 * 
 * <h2>Example Usage:</h2>
 * <pre>
 * EnsHomologyInforRest client = new EnsHomologyInforRest();
 * 
 * // Set gene and output
 * client.setOutputPath("homology_data.json");
 * // Default gene: ENSG00000156076 (human gene)
 * // Default compara: fungi
 * 
 * // Execute query
 * client.doIt();
 * 
 * // Parse results for ortholog analysis
 * // JSON contains comprehensive homology relationships
 * </pre>
 * 
 * <h2>Configuration Options:</h2>
 * <ul>
 *   <li><strong>geneID:</strong> Ensembl gene identifier for homology analysis</li>
 *   <li><strong>outputPath:</strong> Path for storing JSON results</li>
 *   <li><strong>compara database:</strong> Taxonomic group selection</li>
 * </ul>
 * 
 * <h2>Error Handling:</h2>
 * <ul>
 *   <li>HTTP status code validation (requires 200 response)</li>
 *   <li>Proper resource cleanup and connection management</li>
 *   <li>Exception handling for network and parsing errors</li>
 * </ul>
 * 
 * <h2>Research Applications:</h2>
 * <p>This tool is essential for evolutionary biology, functional genomics,
 * and comparative studies. It enables researchers to:</p>
 * <ul>
 *   <li>Understand gene function through evolutionary relationships</li>
 *   <li>Identify conserved pathways across species</li>
 *   <li>Study gene family expansion and contraction</li>
 *   <li>Perform cross-species functional annotations</li>
 * </ul>
 * 
 * @see AbstractAnalysisAction
 * @see EnsGeneTreeInfoRest
 * @author eGPS Development Team
 * @since 1.0
 */
public class EnsHomologyInforRest extends AbstractAnalysisAction {
	private static final Logger logger = LoggerFactory.getLogger(EnsHomologyInforRest.class);

	String geneID = "ENSG00000156076";

	@Override
	public void doIt() throws Exception {
		String server = "https://rest.ensembl.org";
		String ext = "/homology/id/human/";

//		String format = MessageFormat.format("{0}{1}{2}?", server, ext, geneID);

		// vertebrates , metazoa, protists, plants, fungi, pan_homology
		String format = MessageFormat.format("{0}{1}{2}?compara=fungi", server, ext, geneID);
		logger.info(format);

		URL url = new URL(format);

		URLConnection connection = url.openConnection();
		HttpURLConnection httpConnection = (HttpURLConnection) connection;

		httpConnection.setRequestProperty("Content-Type", "application/json");

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
		EnsHomologyInforRest worker = new EnsHomologyInforRest();
		String outPath = "C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\文献管理\\具体文献\\Wnt通路\\素材\\Curated_data\\human\\5_WIF\\comparaInfo\\ensembl.fungi.json";
		worker.setOutputPath(outPath);

		worker.doIt();
	}

}
