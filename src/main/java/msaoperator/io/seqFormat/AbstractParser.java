package msaoperator.io.seqFormat;

import java.io.File;

/**
 * Abstract base class for sequence file parsers implementing common parsing infrastructure.
 */
public abstract class AbstractParser implements SequenceParser {

    protected final File inputFile;
    protected boolean aligned = false;


    protected AbstractParser(File inputFile) {
        this.inputFile = inputFile;
    }

}