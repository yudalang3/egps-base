package msaoperator.io.seqFormat.writer;

//public class GeneBankWriter extends AbstructWriter {
//
//	private List<? extends SeqElementBase> seqElements;
//
//	public GeneBankWriter(File outputFile) {
//		super(outputFile);
//	}
//
//	@Override
//	public void write(SequenceFormatInfo sInfo) throws Exception {
//		switch (sInfo) {
//		case GENBANK:
//			exportDataFromGenBank();
//			break;
//		case EMBL:
//			exportDataFromSeqElementBases();
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
//			exportDataFromPIRNBRFformat();
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
//		
//		default:
//			throw new Exception("The source format convertion is not implement yet!");
//		}
//	}
//
//	private void exportDataFromPIRNBRFformat() throws Exception {
//		List<PIRSeqElement> eles = (List<PIRSeqElement>) seqElements;
//
//		List<DNASequence> dnaSequences = new ArrayList<>(eles.size());
//		for (PIRSeqElement gBase : eles) {
//			DNASequence dnaSequence = new DNASequence(gBase.getSequence());
//
//			dnaSequence.setAccession(new AccessionID(gBase.getSeqName()));
//			dnaSequence.setDescription(gBase.getCommont());
//
//			dnaSequences.add(dnaSequence);
//		}
//		GenbankWriterHelper.writeNucleotideSequence(outputFile, dnaSequences);
//	}
//
//
//	private void exportDataFromGenBank() throws Exception{
//		List<GenBankElementBase> eles = (List<GenBankElementBase>) seqElements;
//		
//		List<DNASequence> dnaSequences = new ArrayList<>(eles.size());
//		for (GenBankElementBase gBase : eles) {
//			dnaSequences.add(gBase.getDnaSequence());
//		}
//		GenbankWriterHelper.writeNucleotideSequence(outputFile, dnaSequences);
//		
//	}
//	
//	private void exportDataFromSeqElementBases() throws Exception{
//		List<SeqElementBase> eles = (List<SeqElementBase>) seqElements;
//		
//		List<DNASequence> dnaSequences = new ArrayList<>(eles.size());
//		for (SeqElementBase gBase : eles) {
//			DNASequence dnaSequence = new DNASequence(gBase.getSequence());
//			
//			dnaSequence.setDescription(gBase.getSeqName());
//			dnaSequences.add( dnaSequence);
//		}
//		GenbankWriterHelper.writeNucleotideSequence(outputFile, dnaSequences);
//		
//	}
//
//	@Override
//	public void setElements(List<? extends SeqElementBase> eles, boolean isAligned) {
//		this.seqElements = eles;
//		this.aligned = isAligned;
//	}
//
//
//
//}
