package fasta.stat;

import top.signature.IModuleSignature;

/**
 * Module signature for FASTA file statistical analysis tools.
 */
public class ZzzModuleSignature implements IModuleSignature {
    @Override
    public String getShortDescription() {
        return "Convenient tools for fasta statistical summary";
    }

    @Override
    public String getTabName() {
        return "Fasta File Statistics";
    }

}