/**
 * 
 */
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
 * @author yudalang,YFQ
 * @date 2019-05-22 14:37:23
 * @version 1.0
 *          <p>
 *          Description:
 *          </p>
 */
public class GCGMSFParser extends AbstractParser {

	private ArrayList<SequenceI> seqElements;

	public GCGMSFParser(File inputFile) {
		super(inputFile);
	}

	@Override
	public void parse() throws Exception {
		List<StringBuilder> sBuilders = new ArrayList<>(30);
		List<String> seqNames = new ArrayList<>(30);
		int numOfsequence = 0;

		BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));

		String str = null;
		// Deal with first block
		while ((str = bufferedReader.readLine()) != null) {
			str = str.trim();
			if (str.startsWith("//")) {
				break;
			} else if (str.startsWith("Name")) {
				String[] splits = str.split("\\s+");
				StringBuilder sequence = new StringBuilder(5000);
				sBuilders.add(sequence);
				seqNames.add(splits[1]);
				numOfsequence++;
			}
		}

		// Rest blocks
		List<String> tmpStrings = new ArrayList<>(sBuilders.size());
		while ((str = bufferedReader.readLine()) != null) {
			str = str.trim();

			if (str.length() == 0) {
				if (tmpStrings.size() == numOfsequence) {
					for (int i = 0; i < numOfsequence; i++) {
						String line = tmpStrings.get(i);
						String tmpSeq = line.substring(seqNames.get(i).length()).replaceAll("\\s+", "");
						sBuilders.get(i).append(tmpSeq);
					}
				} else {
					bufferedReader.close();
					throw new IOException("The number of sequences in each block is unequal!");
				}

				tmpStrings.clear();

			} else {
				if (str.length() != 0) {
					tmpStrings.add(str);
				}
			}
		}

		// Tail-in work
		if (tmpStrings.size() == numOfsequence) {
			for (int i = 0; i < numOfsequence; i++) {
				String line = tmpStrings.get(i);
				String tmpSeq = line.substring(seqNames.get(i).length()).replaceAll("\\s+", "");
				sBuilders.get(i).append(tmpSeq);
			}
		} else {
			bufferedReader.close();
			throw new IOException("The number of sequences in each block is unequal!");
		}

		bufferedReader.close();

		seqElements = new ArrayList<>(30);
		for (int i = 0; i < numOfsequence; i++) {
			seqElements.add(new Sequence(seqNames.get(i), sBuilders.get(i).toString()));
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public BasicSequenceData getSeqElements() {
		return new BasicSequenceData(seqElements);
	}


	public int getBlockNumber() throws Exception {

		BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));

		String str = null;
		int blockNumber = 0;

		// Deal with first block
		while ((str = bufferedReader.readLine()) != null) {
			str = str.trim();
			if (str.startsWith("//")) {
				break;
			}
		}

		while ((str = bufferedReader.readLine()) != null) {
			str = str.trim();

			if (str.length() != 0) {

				String[] sps = str.split("\\s+");
				for (int i = 1; i < sps.length; i++) {
					blockNumber += sps[i].length();
				}

				break;
			}

		}

		bufferedReader.close();

		return blockNumber;
	}

}
