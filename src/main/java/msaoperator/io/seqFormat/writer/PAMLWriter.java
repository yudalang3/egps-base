package msaoperator.io.seqFormat.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import msaoperator.alignment.sequence.BasicSequenceData;
import msaoperator.alignment.sequence.SequenceI;
import msaoperator.io.seqFormat.AbstractWriter;
import msaoperator.io.seqFormat.SequenceFormatInfo;

/**
 * Writer for exporting sequence alignments to PAML format.
 */
public class PAMLWriter extends AbstractWriter {

	private BasicSequenceData seqElements;
	
	public PAMLWriter(File outputFile) {
		super(outputFile);
	}

	@Override
	public void write(SequenceFormatInfo sInfo) throws Exception {
		
		if (!aligned) {
			new Exception("The PAML sequence format needs to be aligned!");
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

	private void exportDataFromSeqElementBases() throws Exception {
		/**
		 * Check errors here! Exception will throw here!
		 */
		List<SequenceI> dataSequences = seqElements.getDataSequences();
		int numOfSeqs = seqElements.getTotalSequenceCount();
		int seqLen = seqElements.getLength();
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
			// Write header line
			writer.write(numOfSeqs + "  " + seqLen);
			writer.write("\n\n");
			
			for (SequenceI sequence1 : dataSequences) {
				writer.write(sequence1.getSeqName());
				writer.write("\n");
				
				String sequence = sequence1.getSeqAsString();
				int len = sequence.length();
				int numOfIteration = len / 60;
				for (int i = 0; i < numOfIteration; i++) {
					int startIndex = i* 60;
					writer.write(sequence.substring(startIndex, startIndex + 60));
					writer.write("\n");
				}
				// Final one
				writer.write(sequence.substring(numOfIteration* 60, len));
				writer.write("\n\n");
			}
		}
	}

	@Override
	public <E extends BasicSequenceData> void setElements(E eles, boolean isAligned) {
		seqElements = eles;
		this.aligned = isAligned;
	}



}