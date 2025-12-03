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
 * Writer for exporting sequences to FASTA format.
 */
public class FastaWriter extends AbstractWriter {

	private BasicSequenceData seqElements;
	
	public FastaWriter(File outputFile) {
		super(outputFile);
	}

	@Override
	public void write(SequenceFormatInfo sInfo) throws Exception {
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
		/**
		 * Check errors here! Exception will throw here!
		 */
		List<SequenceI> dataSequences = seqElements.getDataSequences();
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
			// Use try-with-resource to get auto-closeable writer instance
			for (SequenceI sequence : dataSequences) {
				writer.write(">");
				writer.write(sequence.getSeqName());
				writer.write("\n");
				writer.write(sequence.getSeqAsString());
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