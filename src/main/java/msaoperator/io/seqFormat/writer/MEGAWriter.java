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
 * @date 2019-05-23 10:13:48
 * @version 1.0
 *          <p>
 *          Description:
 *          </p>
 */
public class MEGAWriter extends AbstractWriter {

	private BasicSequenceData seqElements;

	public MEGAWriter(File outputFile) {
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
		List<SequenceI> dataSequences = seqElements.getDataSequences();

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
			// Use try-with-resource to get auto-closeable writer instance
			writer.write("#mega\n");
			writer.write("!TITLE TestFile");
			writer.write("\n");
			writer.write(getTitle());
			writer.write("\n");
			writer.write("      Output on " + getDate());
			writer.write(";\n\n");

			for (SequenceI sequence1 : dataSequences) {
				writer.write("#" + sequence1.getSeqName());
				writer.write("\n");

				String sequence = sequence1.getSeqAsString();
				int len = sequence.length();
				int numOfIteration = len / 60;

				for (int i = 0; i < numOfIteration; i++) {
					int startIndex = i * 60;
					writer.write(sequence.substring(startIndex, startIndex + 60));
					writer.write("\n");
				}
				// Final one
				writer.write(sequence.substring(numOfIteration * 60, len));
				writer.write("\n");
			}

			writer.write("\n\n");

			writer.close();
		}
	}

	private String getTitle() {

		int size = seqElements.getTotalSequenceCount();
		int length = seqElements.getLength();

		String title = "      Number of Sequences:" + size + ". Sequence Length: " + length;

		return title;
	}

	private String getDate() {

		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		return dateFormat.format(date).toString();
	}

	@Override
	public <E extends BasicSequenceData> void setElements(E eles, boolean isAligned) {
		seqElements = eles;
		this.aligned = isAligned;
	}
}
