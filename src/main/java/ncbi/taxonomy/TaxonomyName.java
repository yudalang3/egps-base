package ncbi.taxonomy;

/**
 * Data container representing a taxonomy name entry from NCBI Taxonomy database.
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Name Information:</strong> Stores name text and classification</li>
 *   <li><strong>Unique Variants:</strong> Handles unique name variants when needed</li>
 *   <li><strong>Name Classes:</strong> Distinguishes scientific names, synonyms, common names</li>
 *   <li><strong>Tax ID Linkage:</strong> Links to corresponding TaxonomyNode via taxId</li>
 * </ul>
 * 
 * <h2>Field Descriptions:</h2>
 * <ul>
 *   <li><strong>taxId:</strong> The taxonomy ID of the node associated with this name</li>
 *   <li><strong>nameTxt:</strong> The actual name text (scientific name, synonym, etc.)</li>
 *   <li><strong>uniqueName:</strong> Unique variant of this name if the name is not unique</li>
 *   <li><strong>nameClass:</strong> Classification of the name type</li>
 * </ul>
 * 
 * <h2>Name Class Types:</h2>
 * <ul>
 *   <li><strong>scientific name:</strong> The primary scientific name</li>
 *   <li><strong>synonym:</strong> Alternative scientific name</li>
 *   <li><strong>common name:</strong> Vernacular or common name</li>
 *   <li><strong>genbank common name:</strong> Common name used in GenBank</li>
 *   <li><strong>equivalent name:</strong> Equivalent name in another nomenclature</li>
 *   <li><strong>includes:</strong> Names included under this taxon</li>
 *   <li><strong>blast name:</strong> Name used in BLAST databases</li>
 * </ul>
 * 
 * <h2>NCBI names.dmp Format:</h2>
 * <p>This class maps to the 4 tab-separated fields in NCBI's names.dmp file:</p>
 * <pre>
 * tax_id | name_txt | unique_name | name_class
 * </pre>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * TaxonomyName name = new TaxonomyName(9606, "Homo sapiens", "", "scientific name");
 * System.out.println(name.getNameTxt());   // Output: Homo sapiens
 * System.out.println(name.getNameClass()); // Output: scientific name
 * 
 * // Common name example
 * TaxonomyName commonName = new TaxonomyName(9606, "human", "", "common name");
 * </pre>
 * 
 * <h2>Use Cases:</h2>
 * <ul>
 *   <li>Parse NCBI Taxonomy names.dmp file</li>
 *   <li>Resolve species scientific names from taxonomy IDs</li>
 *   <li>Look up common names for species</li>
 *   <li>Handle synonyms in taxonomic databases</li>
 *   <li>Map between different naming conventions</li>
 * </ul>
 * 
 * @author yudalang
 * @since 1.0
 * @see TaxonomyParser
 * @see TaxonomyNode
 */
public class TaxonomyName {

    private int taxId;               // the id of node associated with this name
    private String nameTxt;          // name itself
    private String uniqueName;       // the unique variant of this name if name not unique
    private String nameClass;        // (synonym, common name, ...)

    // Default constructor
    public TaxonomyName() {
    }

    // Parameterized constructor
    public TaxonomyName(int taxId, String nameTxt, String uniqueName, String nameClass) {
        this.taxId = taxId;
        this.nameTxt = nameTxt;
        this.uniqueName = uniqueName;
        this.nameClass = nameClass;
    }

    // Getters and Setters
    public int getTaxId() {
        return taxId;
    }

    public void setTaxId(int taxId) {
        this.taxId = taxId;
    }

    public String getNameTxt() {
        return nameTxt;
    }

    public void setNameTxt(String nameTxt) {
        this.nameTxt = nameTxt;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public String getNameClass() {
        return nameClass;
    }

    public void setNameClass(String nameClass) {
        this.nameClass = nameClass;
    }

    // Override toString() method for better readability
    @Override
    public String toString() {
        return "TaxonomyName{" +
                "taxId=" + taxId +
                ", nameTxt='" + nameTxt + '\'' +
                ", uniqueName='" + uniqueName + '\'' +
                ", nameClass='" + nameClass + '\'' +
                '}';
    }
}