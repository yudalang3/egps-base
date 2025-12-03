package fasta.comparison;

import top.signature.IModuleSignature;

/**
 * Module signature for FASTA file comparison tools.
 */
public class ZzzModuleSignature implements IModuleSignature {
    @Override
    public String getShortDescription() {
        return "Convenient tools for two fasta file comparison with fmt6 blast output";
    }

    @Override
    public String getTabName() {
        return "Pairwise Fasta Comparer";
    }

}