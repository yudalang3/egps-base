package msaoperator.alignment;

/**
 * Global settings for alignment operations including gap character configuration.
 */
public class GlobalAlignmentSettings {
	
	public static char gapCharSymbol = '-';
	
	public static void reSetGapCharSymbol(char symbol) {
		gapCharSymbol = symbol;
	}
	
}