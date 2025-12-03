package msaoperator.io.seqFormat.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fasta.io.FastaReader;
import msaoperator.alignment.sequence.BasicSequenceData;
import msaoperator.alignment.sequence.Sequence;
import msaoperator.alignment.sequence.SequenceI;
import msaoperator.io.seqFormat.AbstractParser;

/**
 * Parser for FASTA format sequence files including aligned FASTA.
 */
public class FastaParser extends AbstractParser {
	
	private List<SequenceI> seqElements;

	public FastaParser(File inputFile) {
		super(inputFile);
	}

	@Override
	public void parse() throws Exception {
		FastaReader fastaReader = new FastaReader(inputFile);
		fastaReader.processReader();
		List<String> sequences = fastaReader.getSequence();
		List<String> seqNames = fastaReader.getSpeciesNames();
		
		int len = sequences.size();
		seqElements = new ArrayList<>(30);
		for (int i = 0; i < len; i++) {
			seqElements.add(new Sequence(seqNames.get(i), sequences.get(i)));
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public BasicSequenceData getSeqElements() {
		return new BasicSequenceData(seqElements);
	}


}