package msaoperator.io.seqFormat.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import msaoperator.alignment.sequence.Sequence;
import msaoperator.alignment.sequence.SequenceI;
import msaoperator.io.seqFormat.AbstractParser;
import msaoperator.io.seqFormat.model.ClustalWSequenceData;

/**
 * Parser for ClustalW format multiple sequence alignment files.
 */
public class ClustalWParser extends AbstractParser {
	
	private ArrayList<SequenceI> seqElements;
	private String headerLine;

	public ClustalWParser(File inputFile) {
		super(inputFile);
	}

	@Override
	public void parse() throws Exception {
		List<StringBuilder> sBuilders = new ArrayList<>(30);
		List<String> seqNames = new ArrayList<>(30);
		int numOfsequence = 0;
		
		BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
		
		String str = null;
		headerLine = bufferedReader.readLine();
		
		for (int i = 0; i < 2; i++) {
			if (bufferedReader.readLine().length() != 0) {
				bufferedReader.close();
				throw new IOException("Format error, The first line and second line is not empty!");
			}
		}
		
		// Deal with first block
		while ((str = bufferedReader.readLine()) != null) {
			str = str.trim();
			if (str.length()== 0) {
				break;
			} else {
				String[] splits = str.split("\\s+");
				StringBuilder sequence = new StringBuilder(5000);
				sequence.append(splits[1]);
				sBuilders.add(sequence);
				
				seqNames.add(splits[0]);
				numOfsequence ++;
			}
		}
		
		// Rest blocks
		List<String> tmpStrings = new ArrayList<>(sBuilders.size());
		boolean isDataLoaded = false;
		while ((str = bufferedReader.readLine()) != null) {
			str = str.trim();
			
			if (str.length()== 0 && isDataLoaded) {
				isDataLoaded = false;
				if (tmpStrings.size() == numOfsequence) {
					for (int i = 0; i < numOfsequence; i++) {
						sBuilders.get(i).append(tmpStrings.get(i));
					}
				}else {
					bufferedReader.close();
					throw new IOException("The number of sequences in each block is unequal!");
				}
				
				tmpStrings.clear();
				
			}else {
				if (str.length() != 0) {
					String[] splits = str.split("\\s+");
					tmpStrings.add(splits[1]);
					isDataLoaded = true;
				}
			}
		}

		bufferedReader.close();
		
		
		seqElements = new ArrayList<>(30);
		for (int i = 0; i < numOfsequence; i++) {
			Sequence sequence = new Sequence(seqNames.get(i),sBuilders.get(i).toString());
			seqElements.add(sequence);
		}
		
	}
	
	public ClustalWSequenceData getSeqElements() {
		ClustalWSequenceData clustalWSequenceData = new ClustalWSequenceData(seqElements);
		clustalWSequenceData.setHeaderLineInFile(headerLine);
		return clustalWSequenceData;
	}

	
	public int getBlockNumber() throws Exception {
		
		BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
		
		String str = null;
		headerLine = bufferedReader.readLine();
		int blockNumber = 0;
		
		for (int i = 0; i < 2; i++) {
			if (bufferedReader.readLine().length() != 0) {
				bufferedReader.close();
				throw new IOException("Format error, The first line and second line is not empty!");
			}
		}
		
		// Deal with first block
		while ((str = bufferedReader.readLine()) != null) {
			str = str.trim();
			if (str.length()== 0) {
				break;
			} else {
				String[] splits = str.split("\\s+");
				blockNumber =  splits[1].length();
				break;
			}
		}
		
		bufferedReader.close();
		
		return blockNumber;
	}

	

}