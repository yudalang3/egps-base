package tsv.io;

import top.signature.IModuleSignature;

/**
 * Module signature for TSV file parsing and manipulation tools.
 */
public class ZzzModuleSignature implements IModuleSignature {
    @Override
    public String getShortDescription() {
        return "Convenient tools for quick operate tsv format files";
    }

    @Override
    public String getTabName() {
        return "TSVFile Parser";
    }

}