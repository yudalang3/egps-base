/**
 * 
 */
package msaoperator.io.seqFormat.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import msaoperator.alignment.sequence.BasicSequenceData;
import msaoperator.alignment.sequence.SequenceI;
import msaoperator.io.seqFormat.AbstractWriter;
import msaoperator.io.seqFormat.SequenceFormatInfo;

/**
* @author YFQ
* @date 2019-05-27 16:12:17
* @version 1.0
* <p>Description:</p>
*/
public class NEXUSWriter extends AbstractWriter {
	
	private BasicSequenceData seqElements;

	public NEXUSWriter(File outputFile) {
		super(outputFile);
	}

	@Override
	public void write(SequenceFormatInfo sInfo) throws Exception {
		switch (sInfo) {
			case FASTA:
				exportDataFromSeqElementBases();
				break;
			case MEGA:
				exportDataFromSeqElementBases();
				break;
			case NEXML:
				exportDataFromSeqElementBases();
				break;
			case NEXUS:
				exportDataFromSeqElementBases();
				break;
			case PHYLIP:
				exportDataFromSeqElementBases();
				break;
			case CLUSTALW:
				exportDataFromSeqElementBases();
				break;
			case GCGMSF:
				exportDataFromSeqElementBases();
				break;
			default:
				break;
		}
	}


	private void exportDataFromSeqElementBases() throws IOException {
		int seqLen = seqElements.getLength();
		int size = seqElements.getTotalSequenceCount();
		List<SequenceI> dataSequences = seqElements.getDataSequences();
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
			// Use try-with-resource to get auto-closeable writer instance
			writer.write("#NEXUS");
			writer.write("\n");
			writer.write("[Sequences in eGPS -- data title]");
			writer.write("\n");
			
			for (SequenceI sequence1 : dataSequences) {
				
				writer.write("[Name: " + sequence1.getSeqName() + " Len: " + seqLen);
				writer.write("\n");
				writer.write("\n");
			}
			
			writer.write("begin data;");
			writer.write("\n");
			writer.write(" dimensions ntax="+size+" nchar=" + seqLen + ";");
			writer.write("\n");
			writer.write(" format datatype=DNA interleave missing=-;");
			writer.write("\n");
			writer.write("  matrix");
			
			final int charsInEachLine = 60;
			int numOfIteration = seqLen / charsInEachLine;
			for (int i = 0; i < numOfIteration; i++) {
				int startIndex = i* charsInEachLine;
				
				for (SequenceI sequence1 : dataSequences) {
					writer.write(sequence1.getSeqName() + " ");
					writer.write(sequence1.getSeqAsString().substring(startIndex, startIndex + charsInEachLine));
					writer.write("\n");
				}
				
				writer.write("\n\n");
			}
			
			for (SequenceI sequence1 : dataSequences) {
				writer.write(sequence1.getSeqName() + " ");
				writer.write(sequence1.getSeqAsString().substring(numOfIteration* charsInEachLine, seqLen));
				writer.write("\n");
			}
			
			writer.write(";");
			writer.write("\n");
			writer.write("end");
			
			writer.flush();
			writer.close();
		}
	}

	@Override
	public <E extends BasicSequenceData> void setElements(E eles, boolean isAligned) {
		seqElements = eles;
		this.aligned = isAligned;
	}
	
}
