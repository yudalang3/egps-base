package top.signature;

/**
 * Module signature for the top-level module signature interface definition.
 */
public class ZzzModuleSignature implements IModuleSignature {
    @Override
    public String getShortDescription() {
        return "The top interface of all modules";
    }

    @Override
    public String getTabName() {
        return "Top Signatures";
    }

}