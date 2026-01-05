package msaoperator.io.seqFormat.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import msaoperator.alignment.sequence.BasicSequenceData;
import msaoperator.alignment.sequence.Sequence;
import msaoperator.alignment.sequence.SequenceI;
import msaoperator.io.seqFormat.AbstractParser;

/**
* @author YFQ
* @date 2019-05-21 10:04:13
* @version 1.0
* <p>Description:</p>
*/
public class MEGAParser extends AbstractParser{
	
	private ArrayList<SequenceI> seqElements;

	public MEGAParser(File inputFile) {
		super(inputFile);
	}

	@Override
	public void parse() throws Exception {
		LinkedHashMap<String, String> dnaSequences = new LinkedHashMap<String, String>();
		
		BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
		
		String str = "";
		String speciesName = "";
		StringBuilder sequence = new StringBuilder(5000);
		
		// Read and deal with first line, this is "#mega"
		str = bufferedReader.readLine();
		
		while ((str = bufferedReader.readLine()) != null) {
			if (str.startsWith("!")) {
				while ((str = bufferedReader.readLine()) != null) {
					if (str.endsWith(";")) {
						str = bufferedReader.readLine();
						break;
					}
				}
			}
			
			str = str.trim();
						
			if (str.startsWith("#")) {
				if (sequence.length() > 0) {
					dnaSequences.put(speciesName, sequence.toString().trim());
					sequence.setLength(0);
				}
				
				speciesName = str.substring(1);
			} else {
				sequence.append(str);
			}
			
		}

		if (sequence.length() > 0) {
			dnaSequences.put(speciesName, sequence.toString().trim());
			sequence.setLength(0);
		}
		bufferedReader.close();
		
		seqElements = new ArrayList<>(30);
		
		for (Map.Entry<String, String> megSeqElement : dnaSequences.entrySet()) {
			seqElements.add(new Sequence(megSeqElement.getKey(), megSeqElement.getValue()));
		}

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public BasicSequenceData getSeqElements() {
		return new BasicSequenceData(seqElements);
	}


}
