package msaoperator.io;

import msaoperator.alignment.sequence.BasicSequenceData;
import msaoperator.io.seqFormat.MSA_DATA_FORMAT;
import msaoperator.io.seqFormat.SequenceParser;
import msaoperator.io.seqFormat.parser.*;

import java.io.File;

/**
 * Parser for multiple sequence alignment files supporting various MSA formats.
 */
public class MSAFileParser {

    public BasicSequenceData parserMSAFile(File inputFile, MSA_DATA_FORMAT dataFormat) {
        // ALIGNED_CLUSTALW("ClustalW"), ALIGNED_FASTA("Aligned fasta"),
        // ALIGNED_GCGMSF("GCG MSF"),
        // ALIGNED_PAML("PAML"),
        // ALIGNED_MEGA("MEGA"), ALIGNED_PHYLIP("PHYLIP"), NEXUS_SEQ("NEXUS");

        SequenceParser seqPar = null;

        switch (dataFormat) {
            case ALIGNED_CLUSTALW:
                seqPar = new ClustalWParser(inputFile);
                break;
            case ALIGNED_FASTA:
                seqPar = new FastaParser(inputFile);
                break;
            case ALIGNED_GCGMSF:
                seqPar = new GCGMSFParser(inputFile);
                break;
            case ALIGNED_PAML:
                seqPar = new PAMLParser(inputFile);
                break;
            case ALIGNED_MEGA:
                seqPar = new MEGAParser(inputFile);
                break;
            case ALIGNED_PHYLIP:
                seqPar = new PHYParser(inputFile);
                break;

            default:
                seqPar = new NEXUSParser(inputFile);
                break;
        }

        try {
            seqPar.parse();
        } catch (Exception e1) {
            throw new IllegalArgumentException(e1);
        }

        BasicSequenceData seqElements = (BasicSequenceData) seqPar.getSeqElements();
        return seqElements;
    }

}