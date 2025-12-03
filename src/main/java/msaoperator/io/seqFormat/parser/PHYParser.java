/**
 * Parser for PHYLIP format sequence alignment files.
 */
package msaoperator.io.seqFormat.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import msaoperator.alignment.sequence.BasicSequenceData;
import msaoperator.alignment.sequence.Sequence;
import msaoperator.alignment.sequence.SequenceI;
import msaoperator.io.seqFormat.AbstractParser;

public class PHYParser extends AbstractParser{
	
	private SequenceI[] seqElements;

	public PHYParser(File inputFile) {
		super(inputFile);
	}

	@Override
	public void parse() throws Exception {
		FileInputStream inputStream = new FileInputStream(inputFile);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		
		String str = null;
		// This is the header line
		str = bufferedReader.readLine().trim();
		String[] splits = str.split("\\s+");
		int numOfSeqs = Integer.parseInt(splits[0]);
		
		// initialize variables
		
		String[] seqNames = new String[numOfSeqs];
		StringBuilder[] tempBuilders = new StringBuilder[numOfSeqs];
		
		for (int i = 0; i < numOfSeqs; i++) {
			tempBuilders[i] = new StringBuilder(8192);
		}
		
		// parse first block 
		List<String> tempStoredLines = new ArrayList<>(numOfSeqs);
		while ((str = bufferedReader.readLine()) != null ) {
			str = str.trim();
			if (str.length() == 0 && tempStoredLines.size() != 0) {
				
				for (int i = 0; i < numOfSeqs; i++) {
					String[] sps = tempStoredLines.get(i).split("\\s+");
					seqNames[i]= sps[0];
					tempBuilders[i].append(sps[1]);
				}
				tempStoredLines.clear();
				break;
			}
			tempStoredLines.add(  str );
		}
		// parse rest blocks
		while ((str = bufferedReader.readLine()) != null ) {
			str = str.trim();
			if (str.length() == 0 && tempStoredLines.size() != 0) {
				// parse first block 
				for (int i = 0; i < numOfSeqs; i++) {
					tempBuilders[i].append(tempStoredLines.get(i));
				}
				tempStoredLines.clear();
				// Should not add this line anymore!
				continue;
			}
			tempStoredLines.add(  str );
			
		}
		
		if (tempStoredLines.size() != 0) {
			// parse first block 
			for (int i = 0; i < numOfSeqs; i++) {
				tempBuilders[i].append(tempStoredLines.get(i));
			}
		}
		
		//package informations
		seqElements = new Sequence[numOfSeqs];
		for (int i = 0; i < numOfSeqs; i++) {
			seqElements[i] = new Sequence(seqNames[i], tempBuilders[i].toString());
		}
		
		inputStream.close();
		bufferedReader.close();
	}

	

	@SuppressWarnings("unchecked")
	@Override
	public BasicSequenceData getSeqElements() {
		return new BasicSequenceData(Arrays.asList(seqElements));
	}


	public int getBlockNumber() throws Exception{
		
		FileInputStream inputStream = new FileInputStream(inputFile);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		
		String str = null;
		int blockNumber = 0;
		
		// This is the header line
		str = bufferedReader.readLine().trim();
		
		while ((str = bufferedReader.readLine()) != null ) {
			str = str.trim();
			
			String[] sps = str.split("\\s+");
			blockNumber = sps[1].length();
			
			break;
		}
		
		bufferedReader.close();
		
		return blockNumber;
	}

}