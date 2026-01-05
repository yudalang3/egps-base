/**
 * 
 */
package msaoperator.io.seqFormat.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import msaoperator.alignment.sequence.BasicSequenceData;
import msaoperator.alignment.sequence.SequenceI;
import msaoperator.io.seqFormat.AbstractWriter;
import msaoperator.io.seqFormat.SequenceFormatInfo;

/**
* @author YFQ
* @date 2019-05-27 14:21:47
* @version 1.0
* <p>Description:</p>
*/
public class GCGMSFWriter extends AbstractWriter {
	
	private BasicSequenceData seqElements;

	public GCGMSFWriter(File outputFile) {
		super(outputFile);
	}

	@Override
	public void write(SequenceFormatInfo sInfo) throws Exception {
		switch (sInfo) {
			
			case NEXML:
				exportDataFromSeqElementBases();
				break;
			case NEXUS:
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
				break;
		}
	}


	private void exportDataFromSeqElementBases() throws IOException {
		int seqLen = seqElements.getLength();
		List<SequenceI> dataSequences = seqElements.getDataSequences();
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
			// Use try-with-resource to get auto-closeable writer instance
			writer.write(getTitle());
			writer.write("\n\n");
			
			for (SequenceI sequence : dataSequences) {
				
				writer.write("Name: " + sequence.getSeqName() + "        Len:  " + seqLen
						+ "  Check:    42716  Weight:  1.00");
				writer.write("\n");
			}
			
			writer.write("\n");
			writer.write("//");
			writer.write("\n");
			
			int numOfIteration = seqLen / 55;
			for (int i = 0; i < numOfIteration; i++) {
				int startIndex = i* 55;
				
				for (SequenceI sequence : dataSequences) {
					writer.write(sequence.getSeqName() + " ");
					writer.write(sequence.getSeqAsString().substring(startIndex, startIndex + 55));
					writer.write("\n");
				}
				
				writer.write("\n\n");
			}
			
			for (SequenceI sequence : dataSequences) {
				writer.write(sequence.getSeqName() + " ");
				writer.write(sequence.getSeqAsString().substring(numOfIteration* 55, seqLen));
				writer.write("\n");
			}
			
			writer.flush();
			writer.close();
		}
	}
	
	private String getTitle() {
		
		Date date = new Date();
		SimpleDateFormat dateFormat= new SimpleDateFormat("MM-dd-yyyy");
		
		int length = seqElements.getLength();
		
		String title = "Sequences in eGPS  MSF: " + length + "  Type: N " + dateFormat.format(date).toString()
				+ " Check:  42716";
		
		return title;
	}

	@Override
	public <E extends BasicSequenceData> void setElements(E eles, boolean isAligned) {
		seqElements = eles;
		this.aligned = isAligned;
	}
	
}
