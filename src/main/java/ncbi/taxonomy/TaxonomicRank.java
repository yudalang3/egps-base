package ncbi.taxonomy;

/**
 * Data container representing the complete taxonomic lineage for an organism.
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Full Lineage:</strong> Contains all major taxonomic ranks from superkingdom to species</li>
 *   <li><strong>Standard Hierarchy:</strong> Follows Linnaean taxonomic hierarchy</li>
 *   <li><strong>Tax ID Association:</strong> Links to NCBI Taxonomy database via taxId</li>
 *   <li><strong>Complete Naming:</strong> Stores scientific name (taxName) and all rank names</li>
 * </ul>
 * 
 * <h2>Taxonomic Ranks (Top to Bottom):</h2>
 * <ol>
 *   <li><strong>superkingdom:</strong> Highest level (e.g., Eukaryota, Bacteria, Archaea)</li>
 *   <li><strong>kingdom:</strong> Kingdom level (e.g., Animalia, Plantae, Fungi)</li>
 *   <li><strong>phylum:</strong> Phylum/Division level (e.g., Chordata, Arthropoda)</li>
 *   <li><strong>clazz:</strong> Class level (e.g., Mammalia, Insecta) - uses "clazz" to avoid keyword conflict</li>
 *   <li><strong>order:</strong> Order level (e.g., Primates, Carnivora)</li>
 *   <li><strong>family:</strong> Family level (e.g., Hominidae, Felidae)</li>
 *   <li><strong>genus:</strong> Genus level (e.g., Homo, Felis)</li>
 *   <li><strong>species:</strong> Species level (e.g., sapiens, catus)</li>
 * </ol>
 * 
 * <h2>Field Descriptions:</h2>
 * <ul>
 *   <li><strong>taxId:</strong> NCBI Taxonomy ID</li>
 *   <li><strong>taxName:</strong> Scientific name of the organism (full binomial/trinomial)</li>
 *   <li><strong>species:</strong> Species epithet</li>
 *   <li><strong>genus:</strong> Genus name</li>
 *   <li><strong>family:</strong> Family name</li>
 *   <li><strong>order:</strong> Order name</li>
 *   <li><strong>clazz:</strong> Class name</li>
 *   <li><strong>phylum:</strong> Phylum name</li>
 *   <li><strong>kingdom:</strong> Kingdom name</li>
 *   <li><strong>superkingdom:</strong> Superkingdom/Domain name</li>
 * </ul>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * TaxonomicRank human = new TaxonomicRank();
 * human.setTaxId(9606);
 * human.setTaxName("Homo sapiens");
 * human.setSpecies("sapiens");
 * human.setGenus("Homo");
 * human.setFamily("Hominidae");
 * human.setOrder("Primates");
 * human.setClazz("Mammalia");
 * human.setPhylum("Chordata");
 * human.setKingdom("Animalia");
 * human.setSuperkingdom("Eukaryota");
 * 
 * System.out.println(human);
 * // Output: TaxonomicRank{taxId=9606, taxName='Homo sapiens', species='sapiens', ...}
 * </pre>
 * 
 * <h2>Use Cases:</h2>
 * <ul>
 *   <li>Display complete taxonomic lineage for organisms</li>
 *   <li>Perform taxonomic filtering at different ranks</li>
 *   <li>Build phylogenetic trees based on taxonomy</li>
 *   <li>Group organisms by taxonomic categories</li>
 *   <li>Generate taxonomic reports and summaries</li>
 *   <li>Resolve common ancestors at different taxonomic levels</li>
 * </ul>
 * 
 * <h2>Note:</h2>
 * <p>The field "clazz" is used instead of "class" to avoid conflict with Java's reserved keyword.
 * When working with this class, remember that "clazz" represents the taxonomic Class rank.</p>
 * 
 * @author yudalang
 * @since 1.0
 * @see TaxonomyNode
 * @see TaxonomyParser
 * @see TaxonomyFullNameLineageParser
 */
public class TaxonomicRank {
    private int taxId;           // 节点ID (tax_id)
    private String taxName;      // 生物体的学名 (tax_name)
    private String species;      // 物种名称 (species)
    private String genus;        // 属名称 (genus)
    private String family;       // 科名称 (family)
    private String order;        // 目名称 (order)
    private String clazz;        // 纲名称 (class) - 使用 clazz 避免与关键字冲突
    private String phylum;       // 门名称 (phylum)
    private String kingdom;      // 界名称 (kingdom)
    private String superkingdom; // 超界名称 (superkingdom)

    // 无参构造函数
    public TaxonomicRank() {
    }

    // 带参构造函数
    public TaxonomicRank(int taxId, String taxName, String species, String genus, String family, String order, String clazz, String phylum, String kingdom, String superkingdom) {
        this.taxId = taxId;
        this.taxName = taxName;
        this.species = species;
        this.genus = genus;
        this.family = family;
        this.order = order;
        this.clazz = clazz;
        this.phylum = phylum;
        this.kingdom = kingdom;
        this.superkingdom = superkingdom;
    }

    // Getter 和 Setter 方法
    public int getTaxId() {
        return taxId;
    }

    public void setTaxId(int taxId) {
        this.taxId = taxId;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getPhylum() {
        return phylum;
    }

    public void setPhylum(String phylum) {
        this.phylum = phylum;
    }

    public String getKingdom() {
        return kingdom;
    }

    public void setKingdom(String kingdom) {
        this.kingdom = kingdom;
    }

    public String getSuperkingdom() {
        return superkingdom;
    }

    public void setSuperkingdom(String superkingdom) {
        this.superkingdom = superkingdom;
    }

    // toString 方法，用于打印对象信息
    @Override
    public String toString() {
        return "TaxonomicRank{" +
                "taxId=" + taxId +
                ", taxName='" + taxName + '\'' +
                ", species='" + species + '\'' +
                ", genus='" + genus + '\'' +
                ", family='" + family + '\'' +
                ", order='" + order + '\'' +
                ", clazz='" + clazz + '\'' +
                ", phylum='" + phylum + '\'' +
                ", kingdom='" + kingdom + '\'' +
                ", superkingdom='" + superkingdom + '\'' +
                '}';
    }
}