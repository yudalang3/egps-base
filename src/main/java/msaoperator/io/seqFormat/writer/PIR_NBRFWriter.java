package msaoperator.io.seqFormat.writer;

//public class PIR_NBRFWriter extends AbstructWriter {
//
//	private List<? extends SeqElementBase> seqElements;
//	
//	public PIR_NBRFWriter(File outputFile) {
//		super(outputFile);
//	}
//
//	@Override
//	public void write(SequenceFormatInfo sInfo) throws Exception {
//		switch (sInfo) {
//		case GENBANK:
//			exportDataFromGenBankFormat();
//			break;
//		case EMBL:
//			exportDataFromEMBLFormat();
//			break;
//		case Stockholm:
//			exportDataFromSeqElementBases();
//			break;
//		case NEXML:
//			exportDataFromSeqElementBases();
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
//			// Don't forget the sequence type (P1, F1, DL, DC, RL, RC, or XX),
//			exportDataFromSeqElementBases();
//			break;
//		default:
//			throw new Exception("The source format convertion is not implement yet!");
//		}
//	}
//		
//		
//	private void exportDataFromEMBLFormat() throws Exception {
//		List<EMBLSeqElement> eles = (List<EMBLSeqElement>) seqElements;
//
//		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
//			// Use try-with-resource to get auto-closeable writer instance
//			for (EMBLSeqElement tt : eles) {
//				writer.write(">");
//				writer.write("P1;" + tt.getSeqName());
//				writer.write("\n");
//				writer.write(tt.getDe());
//				writer.write("\n");
//				writer.write(tt.getSequence() + "*");
//				writer.write("\n");
//			}
//		}
//	}
//
//	private void exportDataFromGenBankFormat() throws Exception {
//		List<GenBankElementBase> eles = (List<GenBankElementBase>) seqElements;
//		
//		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
//			// Use try-with-resource to get auto-closeable writer instance
//			for (GenBankElementBase tt : eles) {
//				writer.write(">");
//				writer.write("P1;" + tt.getSeqName());
//				writer.write("\n");
//				writer.write(tt.getDnaSequence().getDescription());
//				writer.write("\n");
//				writer.write(tt.getSequence() + "*");
//				writer.write("\n");
//			}
//		}
//	}
//
//	private void exportDataFromSeqElementBases() throws Exception {
//		List<SeqElementBase> eles = (List<SeqElementBase>) seqElements;
//
//		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
//			// Use try-with-resource to get auto-closeable writer instance
//			for (SeqElementBase tt : eles) {
//				writer.write(">");
//				writer.write("P1;" + tt.getSeqName());
//				writer.write("\n");
//				writer.write("No description here!");
//				writer.write("\n");
//				writer.write(tt.getSequence() + "*");
//				writer.write("\n");
//			}
//		}
//	}
//
//	private void exportDataFromPIRNBRF() throws Exception{
//		List<PIRSeqElement> eles = (List<PIRSeqElement>) seqElements;
//		
//		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
//			// Use try-with-resource to get auto-closeable writer instance
//			for (PIRSeqElement tt : eles) {
//				writer.write(">");
//				writer.write(tt.getSeqName());
//				writer.write("\n");
//				writer.write(tt.getCommont());
//				writer.write("\n");
//				writer.write(tt.getSequence()+"*");
//				writer.write("\n");
//			}
//		}
//	}
//
//	@Override
//	public void setElements(List<? extends SeqElementBase> eles, boolean isAligned) {
//		seqElements = eles;
//		this.aligned = isAligned;
//	}
//}
