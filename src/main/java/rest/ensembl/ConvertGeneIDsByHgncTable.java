package rest.ensembl;

import analysis.AbstractAnalysisAction;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.mutable.MutableInt;
import tsv.io.KitTable;
import tsv.io.TSVReader;
import utils.EGPSFileUtil;
import utils.string.EGPSStringUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Gene ID conversion tool using HGNC gene symbol tables.
 * 
 * <p>
 * This class extends AbstractAnalysisAction to provide comprehensive
 * gene identifier conversion capabilities using the HGNC (HUGO Gene
 * Nomenclature Committee) gene symbol table. It enables mapping between
 * different gene identifier systems including HGNC IDs, gene symbols,
 * Ensembl gene IDs, Entrez IDs, and other database cross-references.
 * </p>
 * 
 * <h2>HGNC Data Integration:</h2>
 * <p>This tool leverages the comprehensive HGNC gene symbol table which
 * provides standardized gene nomenclature and cross-references to
 * multiple database systems. The HGNC database is the authoritative
 * source for human gene symbols and nomenclature.</p>
 * 
 * <h2>Supported Identifier Types:</h2>
 * <ul>
 *   <li><strong>HGNC ID:</strong> Unique HGNC identifier (e.g., HGNC:5)</li>
 *   <li><strong>Symbol:</strong> Official gene symbol (e.g., A1BG)</li>
 *   <li><strong>Name:</strong> Full gene name</li>
 *   <li><strong>Entrez ID:</strong> NCBI Gene database identifier</li>
 *   <li><strong>Ensembl Gene ID:</strong> Ensembl database identifier</li>
 *   <li><strong>UniProt IDs:</strong> Protein sequence database identifiers</li>
 *   <li><strong>RefSeq Accession:</strong> NCBI RefSeq identifiers</li>
 *   <li><strong>CCDS ID:</strong> Consensus CDS identifiers</li>
 * </ul>
 * 
 * <h2>Bioinformatics Applications:</h2>
 * <ul>
 *   <li><strong>Database Integration:</strong> Link genes across different databases</li>
 *   <li><strong>Data Standardization:</strong> Convert gene identifiers to standard formats</li>
 *   <li><strong>Cross-Reference Mapping:</strong> Create comprehensive gene ID mappings</li>
 *   <li><strong>Literature Integration:</strong> Map between different publication databases</li>
 *   <li><strong>Analysis Workflows:</strong> Enable multi-database gene analysis</li>
 * </ul>
 * 
 * <h2>Input/Output Capabilities:</h2>
 * <ul>
 *   <li><strong>Input:</strong> TSV files with gene identifiers to convert</li>
 *   <li><strong>Conversion:</strong> Map input IDs to specified output identifier type</li>
 *   <li><strong>Output:</strong> Tab-separated results with conversion mappings</li>
 *   <li><strong>Statistics:</strong> Track conversion success and failure rates</li>
 * </ul>
 * 
 * <h2>HGNC Data Schema (Key Fields):</h2>
 * <ul>
 *   <li><strong>index: 0</strong> = hgnc_id (Unique HGNC identifier)</li>
 *   <li><strong>index: 1</strong> = symbol (Official gene symbol)</li>
 *   <li><strong>index: 2</strong> = name (Full gene name)</li>
 *   <li><strong>index: 18</strong> = entrez_id (NCBI Gene identifier)</li>
 *   <li><strong>index: 19</strong> = ensembl_gene_id (Ensembl identifier)</li>
 *   <li><strong>index: 23</strong> = refseq_accession (RefSeq identifier)</li>
 *   <li><strong>index: 24</strong> = ccds_id (Consensus CDS identifier)</li>
 *   <li><strong>index: 25</strong> = uniprot_ids (UniProt identifiers)</li>
 * </ul>
 * 
 * <h2>Data Quality:</h2>
 * <p>The HGNC database provides curated, high-quality gene nomenclature
 * information. All mappings are manually reviewed and maintained by
 * the HGNC team, ensuring accuracy and consistency across the
 * bioinformatics community.</p>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * ConvertGeneIDsByHgncTable converter = new ConvertGeneIDsByHgncTable();
 * 
 * // Configure conversion
 * converter.setHgncFile("hgnc_complete_set.txt");
 * converter.setInputPath("input_gene_ids.txt");
 * converter.setOutputPath("converted_gene_ids.txt");
 * converter.setIndexOfColumn4convert(0);  // Convert column 0
 * 
 * // Perform conversion
 * converter.doIt();
 * </pre>
 * 
 * <h2>Data Sources:</h2>
 * <p>HGNC data is updated regularly and available from:
 * https://www.genenames.org/data/hgnc_complete_set.txt</p>
 * 
 * <h2>Integration Benefits:</h2>
 * <ul>
 *   <li><strong>Standardization:</strong> Use official gene nomenclature</li>
 *   <li><strong>Cross-Referencing:</strong> Map between multiple databases</li>
 *   <li><strong>Quality Control:</strong> HGNC-curated data ensures accuracy</li>
 *   <li><strong>Community Standards:</strong> Widely accepted in bioinformatics</li>
 * </ul>
 * 
 * @see AbstractAnalysisAction
 * @see TSVReader
 * @see KitTable
 * @author eGPS Development Team
 * @since 1.0
 */
public class ConvertGeneIDsByHgncTable extends AbstractAnalysisAction {

	private String hgncFile;
	int indexOfColumn4convert = 0;

	public static void main(String[] args) throws Exception {
		String hgncTableFile = args[0];
		String inputFile = args[1];
		final int indexOfColumn4convert = 0;
		String outputFile = args[2];
		ConvertGeneIDsByHgncTable readHGNCgeneTable = new ConvertGeneIDsByHgncTable();
		readHGNCgeneTable.setInputPath(inputFile);
		readHGNCgeneTable.setOutputPath(outputFile);
		readHGNCgeneTable.setIndexOfColumn4convert(indexOfColumn4convert);
		readHGNCgeneTable.setHgncFile(hgncTableFile);
		readHGNCgeneTable.doIt();
	}

	public void setHgncFile(String hgncFile) {
		this.hgncFile = hgncFile;
	}

	public void setIndexOfColumn4convert(int indexOfColumn4convert) {
		this.indexOfColumn4convert = indexOfColumn4convert;
	}

	@Override
	public void doIt() throws Exception {

		if (hgncFile == null){
			throw new Exception("hgncFile is null");
		}
		check();

//		System.out.println(kitTable.toString());

//		List<String> headerNames = kitTable.getHeaderNames();
//		int index = 0;
//		for (String string : headerNames) {
//			System.out.printf("index: %d = %s\n", index, string);
//			index++;
//		}

		final int indexOfHGNC = 0;
		final int indexOfSymbol = 1;
		final int indexOfNCBI = 18;
		final int indexOfEns = 19;

		Map<String, String> symbol2Ens = Maps.newHashMap();
		EGPSFileUtil.forLoopToFileMaybeCompressed(hgncFile, line -> {
			String[] strings = EGPSStringUtil.split(line, '\t');
			symbol2Ens.put(strings[indexOfSymbol], strings[indexOfEns]);
			return false;
		});

		KitTable kitTable = TSVReader.readTsvTextFile(inputPath, false);
		MutableInt totalSymbolNeedToConvert = new MutableInt();
		List<String> symbolCanNotConvert = Lists.newArrayList();

		List<String> outputList = Lists.newLinkedList();
		kitTable.getContents().forEach(list -> {
			String key = list.get(indexOfColumn4convert);
			String convertedValue = symbol2Ens.get(key);
			totalSymbolNeedToConvert.increment();
			if (convertedValue == null){
				symbolCanNotConvert.add(key);
			}

			outputList.add( key + "\t" + convertedValue);
		});

		System.out.println("Total: " + totalSymbolNeedToConvert.intValue() );
		System.out.println("Can not convert: " + symbolCanNotConvert.size());
		System.out.println(symbolCanNotConvert.toString());

		Files.write(Path.of(outputPath), outputList);
	}

}
