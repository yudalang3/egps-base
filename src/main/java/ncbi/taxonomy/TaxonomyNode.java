package ncbi.taxonomy;

/**
 * Data container representing a single node in the NCBI Taxonomy database hierarchy.
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Complete Node Data:</strong> Stores all 18 fields from NCBI nodes.dmp format</li>
 *   <li><strong>Hierarchical Structure:</strong> Contains parent relationship (taxId, parentTaxId)</li>
 *   <li><strong>Genetic Code Information:</strong> Multiple genetic code IDs for different organelles</li>
 *   <li><strong>Taxonomic Rank:</strong> Rank information (species, genus, family, etc.)</li>
 *   <li><strong>Visibility Flags:</strong> GenBank hidden and subtree visibility settings</li>
 * </ul>
 * 
 * <h2>Field Descriptions:</h2>
 * <ul>
 *   <li><strong>taxId:</strong> Node's unique taxonomy ID</li>
 *   <li><strong>parentTaxId:</strong> Parent node's taxonomy ID (for hierarchy)</li>
 *   <li><strong>rank:</strong> Taxonomic rank (e.g., "species", "genus", "family")</li>
 *   <li><strong>emblCode:</strong> EMBL code for this node</li>
 *   <li><strong>divisionId:</strong> Division ID from NCBI taxonomy</li>
 *   <li><strong>inheritedDivFlag:</strong> Whether division is inherited (1) or not (0)</li>
 *   <li><strong>geneticCodeId:</strong> Genetic code table ID</li>
 *   <li><strong>inheritedGCFlag:</strong> Whether genetic code is inherited</li>
 *   <li><strong>mitochondrialGeneticCodeId:</strong> Mitochondrial genetic code ID</li>
 *   <li><strong>inheritedMGCFlag:</strong> Whether mitochondrial code is inherited</li>
 *   <li><strong>genBankHiddenFlag:</strong> Whether hidden in GenBank (1=yes, 0=no)</li>
 *   <li><strong>hiddenSubtreeRootFlag:</strong> Whether this is a hidden subtree root</li>
 *   <li><strong>comments:</strong> Additional comments or notes</li>
 *   <li><strong>plastidGeneticCodeId:</strong> Plastid/chloroplast genetic code ID</li>
 *   <li><strong>inheritedPGCFlag:</strong> Whether plastid code is inherited</li>
 *   <li><strong>specifiedSpecies:</strong> Whether this is a specified species</li>
 *   <li><strong>hydrogenosomeGeneticCodeId:</strong> Hydrogenosome genetic code ID</li>
 *   <li><strong>inheritedHGCFlag:</strong> Whether hydrogenosome code is inherited</li>
 * </ul>
 * 
 * <h2>NCBI nodes.dmp Format:</h2>
 * <p>This class maps to the 18 tab-separated fields in NCBI's nodes.dmp file:</p>
 * <pre>
 * tax_id | parent_tax_id | rank | embl_code | division_id | inherited_div_flag |
 * genetic_code_id | inherited_GC_flag | mitochondrial_genetic_code_id | inherited_MGC_flag |
 * GenBank_hidden_flag | hidden_subtree_root_flag | comments | plastid_genetic_code_id |
 * inherited_PGC_flag | specified_species | hydrogenosome_genetic_code_id | inherited_HGC_flag
 * </pre>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * TaxonomyNode node = new TaxonomyNode();
 * node.setTaxId(9606);
 * node.setParentTaxId(9605);
 * node.setRank("species");
 * node.setGeneticCodeId(1);  // Standard genetic code
 * 
 * System.out.println(node);  // Output: TaxonomyNode{taxId=9606, parentTaxId=9605}
 * </pre>
 * 
 * <h2>Use Cases:</h2>
 * <ul>
 *   <li>Parse NCBI Taxonomy database nodes.dmp file</li>
 *   <li>Build taxonomic hierarchy trees</li>
 *   <li>Resolve species lineages</li>
 *   <li>Determine appropriate genetic code for translation</li>
 *   <li>Filter hidden or deprecated taxa</li>
 * </ul>
 * 
 * @author yudalang
 * @since 1.0
 * @see TaxonomyParser
 * @see TaxonomicRank
 */
public class TaxonomyNode {
    private int taxId;
    private int parentTaxId;
    private String rank;
    private String emblCode;
    private int divisionId;
    private int inheritedDivFlag;
    private int geneticCodeId;
    private int inheritedGCFlag;
    private int mitochondrialGeneticCodeId;
    private int inheritedMGCFlag;
    private int genBankHiddenFlag;
    private int hiddenSubtreeRootFlag;
    private String comments;
    private int plastidGeneticCodeId;
    private int inheritedPGCFlag;
    private int specifiedSpecies;
    private int hydrogenosomeGeneticCodeId;
    private int inheritedHGCFlag;

    @Override
    public String toString() {
        return  "TaxonomyNode{" +
                "taxId=" + taxId +
                ", parentTaxId=" + parentTaxId ;
    }

    // Getters and Setters
    public int getTaxId() {
        return taxId;
    }

    public void setTaxId(int taxId) {
        this.taxId = taxId;
    }

    public int getParentTaxId() {
        return parentTaxId;
    }

    public void setParentTaxId(int parentTaxId) {
        this.parentTaxId = parentTaxId;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getEmblCode() {
        return emblCode;
    }

    public void setEmblCode(String emblCode) {
        this.emblCode = emblCode;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public int getInheritedDivFlag() {
        return inheritedDivFlag;
    }

    public void setInheritedDivFlag(int inheritedDivFlag) {
        this.inheritedDivFlag = inheritedDivFlag;
    }

    public int getGeneticCodeId() {
        return geneticCodeId;
    }

    public void setGeneticCodeId(int geneticCodeId) {
        this.geneticCodeId = geneticCodeId;
    }

    public int getInheritedGCFlag() {
        return inheritedGCFlag;
    }

    public void setInheritedGCFlag(int inheritedGCFlag) {
        this.inheritedGCFlag = inheritedGCFlag;
    }

    public int getMitochondrialGeneticCodeId() {
        return mitochondrialGeneticCodeId;
    }

    public void setMitochondrialGeneticCodeId(int mitochondrialGeneticCodeId) {
        this.mitochondrialGeneticCodeId = mitochondrialGeneticCodeId;
    }

    public int getInheritedMGCFlag() {
        return inheritedMGCFlag;
    }

    public void setInheritedMGCFlag(int inheritedMGCFlag) {
        this.inheritedMGCFlag = inheritedMGCFlag;
    }

    public int getGenBankHiddenFlag() {
        return genBankHiddenFlag;
    }

    public void setGenBankHiddenFlag(int genBankHiddenFlag) {
        this.genBankHiddenFlag = genBankHiddenFlag;
    }

    public int getHiddenSubtreeRootFlag() {
        return hiddenSubtreeRootFlag;
    }

    public void setHiddenSubtreeRootFlag(int hiddenSubtreeRootFlag) {
        this.hiddenSubtreeRootFlag = hiddenSubtreeRootFlag;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getPlastidGeneticCodeId() {
        return plastidGeneticCodeId;
    }

    public void setPlastidGeneticCodeId(int plastidGeneticCodeId) {
        this.plastidGeneticCodeId = plastidGeneticCodeId;
    }

    public int getInheritedPGCFlag() {
        return inheritedPGCFlag;
    }

    public void setInheritedPGCFlag(int inheritedPGCFlag) {
        this.inheritedPGCFlag = inheritedPGCFlag;
    }

    public int getSpecifiedSpecies() {
        return specifiedSpecies;
    }

    public void setSpecifiedSpecies(int specifiedSpecies) {
        this.specifiedSpecies = specifiedSpecies;
    }

    public int getHydrogenosomeGeneticCodeId() {
        return hydrogenosomeGeneticCodeId;
    }

    public void setHydrogenosomeGeneticCodeId(int hydrogenosomeGeneticCodeId) {
        this.hydrogenosomeGeneticCodeId = hydrogenosomeGeneticCodeId;
    }

    public int getInheritedHGCFlag() {
        return inheritedHGCFlag;
    }

    public void setInheritedHGCFlag(int inheritedHGCFlag) {
        this.inheritedHGCFlag = inheritedHGCFlag;
    }
}