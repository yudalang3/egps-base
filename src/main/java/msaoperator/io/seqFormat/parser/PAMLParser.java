package msaoperator.io.seqFormat.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import msaoperator.alignment.sequence.BasicSequenceData;
import msaoperator.alignment.sequence.Sequence;
import msaoperator.alignment.sequence.SequenceI;
import msaoperator.io.seqFormat.AbstractParser;

/**
 * Parser for PAML format sequence alignment files.
 */
public class PAMLParser extends AbstractParser {
	
	private ArrayList<SequenceI> seqElements;

	public PAMLParser(File inputFile) {
		super(inputFile);
	}

	@Override
	public void parse() throws Exception {
		List<String> sequences = new ArrayList<>();
		List<String> seqNames = new ArrayList<>();
		
		BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
		
		String str = null;
		StringBuilder sequence = new StringBuilder(5000);
		
		// Read and deal with first line
		str = bufferedReader.readLine();
		String[] tt = str.split("\\s+");
		int numOfsequence = Integer.parseInt( tt[tt.length - 2] );
		int sequenceLen = Integer.parseInt( tt[tt.length - 1] );
		// Read and deal with second line, this is a sequence name!
		str = bufferedReader.readLine();
		if (str.length() == 0) {
			str = bufferedReader.readLine();
		}
		seqNames.add(str);
		
		while ((str = bufferedReader.readLine()) != null) {
			str = str.trim();
			if (str.length() == 0) {
				continue;
			}
						
			if (sequence.length() >= sequenceLen) {
				sequences.add(sequence.toString());
				sequence.setLength(0);
				seqNames.add(str);
			}else {
				sequence.append(str);
			}
			
		}

		bufferedReader.close();
		
		if(sequence.length() >= sequenceLen) {
			sequences.add(sequence.toString());
		}else if (sequence.length() > 0) {
			throw new IOException("The final sequence is not compelete!!");
		} 
		
		seqElements = new ArrayList<>(30);
		for (int i = 0; i < numOfsequence; i++) {
			seqElements.add(new Sequence(seqNames.get(i), sequences.get(i)));
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public BasicSequenceData getSeqElements() {
		return new BasicSequenceData(seqElements);
	}


}