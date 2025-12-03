package pfam.parse;

import top.signature.IModuleSignature;

/**
 * Module signature for HMMER and Pfam result parsing tools.
 */
public class ZzzModuleSignature implements IModuleSignature {
    @Override
    public String getShortDescription() {
        return "Convenient tools for hmmer and hmmer extension tools results parser";
    }

    @Override
    public String getTabName() {
        return "Hmmer Parser";
    }

}