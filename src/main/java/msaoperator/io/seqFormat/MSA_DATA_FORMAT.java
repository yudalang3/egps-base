package msaoperator.io.seqFormat;

import java.util.InputMismatchException;

/**
 * Enumeration of supported multiple sequence alignment file formats.
 */
public enum MSA_DATA_FORMAT {

	ALIGNED_CLUSTALW("ClustalW"), ALIGNED_FASTA("Aligned fasta"), ALIGNED_GCGMSF("GCG MSF"),
	ALIGNED_PAML("PAML"),
	ALIGNED_MEGA("MEGA"), ALIGNED_PHYLIP("PHYLIP"), NEXUS_SEQ("NEXUS");

	private String name;

	private MSA_DATA_FORMAT(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static String[] getAllSupportedNames() {
		String[] ret = new String[7];
		ret[0] = "Aligned fasta";
		ret[1] = "ClustalW";
		ret[2] = "GCG MSF";
		ret[3] = "PAML";
		ret[4] = "MEGA";
		ret[5] = "PHYLIP";
		ret[6] = "NEXUS";
		
		return ret;
	}

	public static MSA_DATA_FORMAT getFormatAccording2name(String name) {

		MSA_DATA_FORMAT ret = null;
		switch (name) {
		case "ClustalW":
			ret = ALIGNED_CLUSTALW;
			break;
		case "Aligned fasta":
			ret = ALIGNED_FASTA;
			break;
		case "GCG MSF":
			ret = ALIGNED_GCGMSF;
			break;
		case "PAML":
			ret = ALIGNED_PAML;
			break;
		case "MEGA":
			ret = ALIGNED_MEGA;
			break;
		case "PHYLIP":
			ret = ALIGNED_PHYLIP;
			break;
		case "NEXUS":
			ret = NEXUS_SEQ;
			break;
		default:
			throw new InputMismatchException("Please input the supported alignment format.");
		}

		return ret;
	}
}