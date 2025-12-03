package phylo.gsefea;

import top.signature.IModuleSignature;

/**
 * Module signature for gene set evolutionary formation enrichment analysis tools.
 */
public class ZzzModuleSignature implements IModuleSignature {
    @Override
    public String getShortDescription() {
        return "GeneSet evolutionary formation enrichment analysis.";
    }

    @Override
    public String getTabName() {
        return "Phylo. GSEFEA";
    }

}