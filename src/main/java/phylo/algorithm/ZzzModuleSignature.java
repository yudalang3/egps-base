package phylo.algorithm;

import top.signature.IModuleSignature;

/**
 * Module signature for phylogenetic algorithm implementations.
 */
public class ZzzModuleSignature implements IModuleSignature {
    @Override
    public String getShortDescription() {
        return "Convenient tools for phylogenetic algorithms";
    }

    @Override
    public String getTabName() {
        return "Phylo. MiniAlgorithm";
    }

}