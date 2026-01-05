package msaoperator.io.seqFormat.writer;

//public class NexmlWriter extends AbstructWriter {
//
//	private List<? extends SeqElementBase> seqElements;
//	
//	public NexmlWriter(File outputFile) {
//		super(outputFile);
//	}
//
//	@Override
//	public void write(SequenceFormatInfo sInfo) throws Exception {
//		switch (sInfo) {
//		case GENBANK:
//			exportDataFromSeqElementBases();
//			break;
//		case EMBL:
//			exportDataFromSeqElementBases();
//			break;
//		case Stockholm:
//			exportDataFromSeqElementBases();
//			break;
//		case NEXML:
//			writeNexmlFromNeXML();
//			break;
//		case NEXUS:
//			exportDataFromSeqElementBases();
//			break;
//		case PIRNBRF:
//			exportDataFromPIRNBRF();
//			break;
//		case PHYLIP:
//			exportDataFromSeqElementBases();
//			break;
//		case MEGA:
//			exportDataFromSeqElementBases();
//			break;
//		case PAML:
//			exportDataFromSeqElementBases();
//			break;
//		case CLUSTALW:
//			exportDataFromSeqElementBases();
//			break;
//		case GCGMSF:
//			exportDataFromSeqElementBases();
//			break;
//		case FASTA:
//			exportDataFromSeqElementBases();
//			break;
//		default:
//			throw new Exception("The source format convertion is not implement yet!");
//		}
//		
//		
//	}
//
//	private void writeNexmlFromNeXML() throws Exception{
//		List<NeXMLSeqElement> eles = (List<NeXMLSeqElement>) seqElements;
//		Document doc = eles.get(0).getDoc();
//		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
//			writer.write(doc.getXmlString());
//		}
//	}
//
//	private void exportDataFromSeqElementBases() throws Exception {
//		List<SeqElementBase> eles = (List<SeqElementBase>) seqElements;
//		Document doc = DocumentFactory.createDocument();
//
//		// create a taxa block with 5 taxa
//		OTUs otus = doc.createOTUs();
//		otus.setId("From_eGPS");
//		otus.setLabel("From_eGPS");
//
//		
//		// create a DNA matrix for the taxa
//		MolecularMatrix matrix = doc.createMolecularMatrix(otus, MolecularMatrix.DNA);
//
//		int t = 1;
//		for (SeqElementBase tt : eles) {
//			OTU otu = otus.createOTU();
//			otu.setId("t" + t);
//			otu.setLabel(tt.getSeqName());
//			t++;
//			
//			MatrixRow<CharacterState> row = matrix.getRowObject(otu);
//			row.setSeq(tt.getSequence());
//		}
//
//		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
//			writer.write(doc.getXmlString());
//		}
//	}
//	
//	private void exportDataFromPIRNBRF() throws Exception {
//		List<PIRSeqElement> eles = (List<PIRSeqElement>) seqElements;
//		Document doc = DocumentFactory.createDocument();
//
//		// create a taxa block with 5 taxa
//		OTUs otus = doc.createOTUs();
//		otus.setId("From_eGPS");
//		otus.setLabel("From_eGPS");
//		
//		
//		// create a DNA matrix for the taxa
//		MolecularMatrix matrix = doc.createMolecularMatrix(otus, MolecularMatrix.DNA);
//
//		int t = 1;
//		for (PIRSeqElement tt : eles) {
//			OTU otu = otus.createOTU();
//			otu.setId("t" + t);
//			otu.setLabel(tt.getSeqName());
//			otu.setAbout(tt.getCommont());
//			t++;
//			
//			MatrixRow<CharacterState> row = matrix.getRowObject(otu);
//			row.setSeq(tt.getSequence());
//		}
//
//		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
//			writer.write(doc.getXmlString());
//		}
//	}
//
//	@Override
//	public void setElements(List<? extends SeqElementBase> eles, boolean isAligned) {
//		this.seqElements = eles;
//		this.aligned = isAligned;
//	}
//}
