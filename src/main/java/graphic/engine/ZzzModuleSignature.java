package graphic.engine;

import top.signature.IModuleSignature;

/**
 * Module signature for graphics engine and Swing visualization tools.
 */
public class ZzzModuleSignature implements IModuleSignature {
    @Override
    public String getShortDescription() {
        return "Infrastructure Structure and Convenient tools for GUI operations, Swing operation";
    }

    @Override
    public String getTabName() {
        return "Swing Engine";
    }

}