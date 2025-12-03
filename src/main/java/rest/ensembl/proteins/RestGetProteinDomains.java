package rest.ensembl.proteins;

import analysis.AbstractAnalysisAction;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.string.EGPSStringUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.List;

/**
 * REST client for retrieving protein domain annotations from Ensembl.
 * 
 * <p>
 * This class extends AbstractAnalysisAction to provide comprehensive
 * capabilities for querying Ensembl's REST API to retrieve protein
 * domain and feature annotations. It processes TSV files containing
 * protein IDs and queries the Ensembl overlap/translation endpoint
 * to obtain detailed domain information.
 * </p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Batch Processing:</strong> Process multiple protein IDs from TSV files</li>
 *   <li><strong>REST API Integration:</strong> Query Ensembl overlap/translation endpoint</li>
 *   <li><strong>Rate Limiting:</strong> Built-in randomization to avoid API limits</li>
 *   <li><strong>Flexible Input:</strong> Support TSV files with configurable columns</li>
 *   <li><strong>JSON Output:</strong> Store results in structured JSON format</li>
 * </ul>
 * 
 * <h2>Ensembl API Integration:</h2>
 * <p>This class specifically targets the Ensembl overlap/translation endpoint
 * which provides protein domain annotations including Pfam domains,
 * PROSITE patterns, SMART domains, CDD classifications, and other
 * feature types.</p>
 * 
 * <h2>Usage Pattern:</h2>
 * <ol>
 *   <li>Set input TSV file path containing protein IDs</li>
 *   <li>Configure column index (0-based) and header settings</li>
 *   <li>Set output directory path for JSON results</li>
 *   <li>Execute doIt() to process all proteins in batch</li>
 * </ol>
 * 
 * <h2>Bioinformatics Applications:</h2>
 * <ul>
 *   <li><strong>Protein Annotation:</strong> Retrieve comprehensive domain annotations</li>
 *   <li><strong>Large-Scale Analysis:</strong> Process hundreds to thousands of proteins</li>
 *   <li><strong>Domain Architecture:</strong> Build protein domain maps</li>
 *   <li><strong>Database Integration:</strong> Cross-reference with multiple databases</li>
 *   <li><strong>Comparative Analysis:</strong> Analyze domain content across protein sets</li>
 * </ul>
 * 
 * <h2>Input Format:</h2>
 * <p>TSV file with protein IDs in specified columns. The class reads
 * protein identifiers from the configured column index and processes
 * each ID through the Ensembl REST API.</p>
 * 
 * <h2>Output Structure:</h2>
 * <p>JSON files stored in the output directory, one file per protein ID,
 * containing detailed domain annotation data from Ensembl.</p>
 * 
 * <h2>Rate Limiting:</h2>
 * <p>To respect Ensembl API limits, this class includes randomized
 * delays between requests (3-4 seconds) to prevent overwhelming
 * the service and ensure reliable data retrieval.</p>
 * 
 * <h2>Error Handling:</h2>
 * <ul>
 *   <li><strong>HTTP Errors:</strong> Throws RuntimeException for non-200 responses</li>
 *   <li><strong>Connection Issues:</strong> Proper resource cleanup and exception handling</li>
 *   <li><strong>File I/O:</strong> Comprehensive error handling for file operations</li>
 * </ul>
 * 
 * <h2>Configuration Parameters:</h2>
 * <ul>
 *   <li><strong>columnIndex:</strong> 0-based index of protein ID column in TSV file</li>
 *   <li><strong>hasHeader:</strong> Whether TSV file contains header row</li>
 *   <li><strong>inputPath:</strong> Path to TSV file with protein IDs</li>
 *   <li><strong>outputPath:</strong> Directory for storing JSON results</li>
 * </ul>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * RestGetProteinDomains client = new RestGetProteinDomains();
 * 
 * // Configure for batch processing
 * client.setInputPath("protein_ids.tsv");
 * client.setOutputPath("ensembl_domains/");
 * client.setColumnIndex(8);  // 9th column (0-based)
 * client.setHasHeader(true);
 * 
 * // Process all proteins
 * client.doIt();
 * 
 * // Or process single protein
 * client.doOnceRest("ENSP00000363827");
 * </pre>
 * 
 * <h2>API Endpoint:</h2>
 * <p>Uses Ensembl endpoint: {@code /overlap/translation/{protein_id}?}
 * This provides comprehensive overlap analysis including domains,
 * repeats, and other protein features.</p>
 * 
 * @see AbstractAnalysisAction
 * @see OverlapTransElementBean
 * @see OverlapTransBeanParser
 * @author yudal
 * @since 1.0
 */
public class RestGetProteinDomains extends AbstractAnalysisAction {
	
	private static final Logger logger = LoggerFactory.getLogger(RestGetProteinDomains.class);

	private MessageFormat formatter = new MessageFormat("/overlap/translation/{0}?");
	private String server = "https://rest.ensembl.org";
	
	/**
	 * 0-based index
	 */
	private int columnIndex = 8;
	private boolean hasHeader = true;

	@Override
	public void doIt() throws Exception {
		List<String> allLines = Files.readLines(new File(inputPath), StandardCharsets.UTF_8);
		if (hasHeader) {
			allLines = allLines.subList(1, allLines.size());
		}
		for (String line : allLines) {
			String[] split = EGPSStringUtil.split(line, '\t', columnIndex + 1);
			String string = split[columnIndex];
			
			logger.info("Start to do the Rest request...");
			doOnceRest(string);
			logger.info("Finish the request process.");
			sleepRandomly();
		}

	}
	
	private void sleepRandomly() {
	    try {
	        Thread.sleep(getMillis());
	    } catch (InterruptedException e) {
	        Thread.currentThread().interrupt();
	        throw new RuntimeException(e);
	    }
	}

	private long getMillis() {
	    return (long) (Math.random() * 1_000 + 3_000);
	}

	public void doOnceRest(String protID) throws Exception {
		String ext = formatter.format( new Object[]{protID});
		logger.info(ext);
		URL url = new URL(server + ext);

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

		File dir = new File(outputPath);
		if (!dir.exists()) {
			FileUtils.forceMkdir(dir);
		}
		Files.write(output.getBytes(StandardCharsets.US_ASCII), new File(dir, protID.concat(".json")));
	}
	
	
	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public void setHasHeader(boolean hasHeader) {
		this.hasHeader = hasHeader;
	}
}
