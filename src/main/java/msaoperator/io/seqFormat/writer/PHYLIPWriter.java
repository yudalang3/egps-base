package msaoperator.io.seqFormat.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import msaoperator.alignment.sequence.BasicSequenceData;
import msaoperator.alignment.sequence.SequenceI;
import msaoperator.io.seqFormat.AbstractWriter;
import msaoperator.io.seqFormat.SequenceFormatInfo;

/**
 * Writer for exporting sequence alignments to PHYLIP format.
 */
public class PHYLIPWriter extends AbstractWriter {

	private BasicSequenceData seqElements;
	
	public PHYLIPWriter(File outputFile) {
		super(outputFile);
	}

	@Override
	public void write(SequenceFormatInfo sInfo) throws Exception {
		
		if (!aligned) {
			new Exception("The ClustalW sequence format needs to be aligned!");
		}
		
		switch (sInfo) {
		case GENBANK:
			exportDataFromSeqElementBases();
			break;
		case EMBL:
			exportDataFromSeqElementBases();
			break;
		case Stockholm:
			exportDataFromSeqElementBases();
			break;
		case NEXML:
			exportDataFromSeqElementBases();
			break;
		case NEXUS:
			exportDataFromSeqElementBases();
			break;
		case PIRNBRF:
			exportDataFromSeqElementBases();
			break;
		case PHYLIP:
			exportDataFromSeqElementBases();
			break;
		case MEGA:
			exportDataFromSeqElementBases();
			break;
		case PAML:
			exportDataFromSeqElementBases();
			break;
		case CLUSTALW:
			exportDataFromSeqElementBases();
			break;
		case GCGMSF:
			exportDataFromSeqElementBases();
			break;
		case FASTA:
			exportDataFromSeqElementBases();
			break;
		default:
			throw new Exception("The source format convertion is not implement yet!");
		}
	}

	private void exportDataFromSeqElementBases() throws Exception{
		
		List<SequenceI> dataSequences = seqElements.getDataSequences();
		int numOfSeqs = seqElements.getTotalSequenceCount();
		int seqLen = seqElements.getLength();
		String headerLine = numOfSeqs + " " + seqLen;
		int numOfSeqsNameChars = configSeqNames(seqElements.getSequenceNames());
				
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
			// Use try-with-resource to get auto-closeable writer instance
			writer.write(headerLine);
			writer.write("\n");
			
			int numOfIteration = seqLen / 50;
			for (int i = 0; i < numOfIteration; i++) {
				int startIndex = i* 50;
				
				for (SequenceI sequence1 : dataSequences) {
					String leftH = "";
					if (i == 0) {
						leftH = StringUtils.rightPad(sequence1.getSeqName(), numOfSeqsNameChars);
					}else {
						leftH = StringUtils.rightPad("", numOfSeqsNameChars); 
					}
					
					writer.write(leftH);
					writer.write(sequence1.getSeqAsString().substring(startIndex, startIndex + 50));
					writer.write("\n");
				}
				
				writer.write("\n");
			}
			
			for (SequenceI sequence1 : dataSequences) {
				String leftH = "            "; 
				writer.write(leftH);
				writer.write(sequence1.getSeqAsString().substring(numOfIteration* 50, seqLen));
				writer.write("\n");
			}
			
		}
	}



	@Override
	public <E extends BasicSequenceData> void setElements(E eles, boolean isAligned) {
		seqElements = eles;
		this.aligned = isAligned;
	}



}