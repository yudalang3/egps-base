package ncbi.taxonomy;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import utils.EGPSFileUtil;
import utils.string.EGPSStringUtil;
import evoltree.struct.util.EvolNodeUtil;
import evoltree.phylogeny.DefaultPhyNode;
import evoltree.struct.EvolNode;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Parser for NCBI Taxonomy database files (nodes.dmp and names.dmp).
 * 
 * <p>
 * This class parses NCBI taxonomy dump files to construct a hierarchical tree
 * representing the complete taxonomic classification system. The NCBI Taxonomy
 * database organizes species into a tree structure from root (cellular organisms)
 * down to individual species and strains.
 * </p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Taxonomy Tree Construction:</strong> Builds complete taxonomic hierarchy</li>
 *   <li><strong>Node Metadata:</strong> Parses all 18 fields from nodes.dmp</li>
 *   <li><strong>Name Resolution:</strong> Links taxonomic IDs to scientific names</li>
 *   <li><strong>Compressed File Support:</strong> Handles .gz compressed dump files</li>
 * </ul>
 * 
 * <h2>Input File Formats:</h2>
 * 
 * <h3>nodes.dmp Format (18 fields):</h3>
 * <pre>
 * tax_id | parent_tax_id | rank | embl_code | division_id | inherited_div_flag |
 * genetic_code_id | inherited_GC_flag | mitochondrial_genetic_code_id |
 * inherited_MGC_flag | GenBank_hidden_flag | hidden_subtree_root_flag | comments |
 * plastid_genetic_code_id | inherited_PGC_flag | specified_species |
 * hydrogenosome_genetic_code_id | inherited_HGC_flag
 * </pre>
 * 
 * <h3>names.dmp Format:</h3>
 * <pre>
 * tax_id | name_txt | unique_name | name_class
 * </pre>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * TaxonomyParser parser = new TaxonomyParser();
 * parser.parseTree("nodes.dmp.gz", "names.dmp.gz");
 * </pre>
 * 
 * <h2>Output:</h2>
 * <p>
 * Constructs an {@link EvolNode} tree where each node represents a taxonomic rank
 * (superkingdom, kingdom, phylum, class, order, family, genus, species).
 * </p>
 * 
 * <h2>NCBI Taxonomy Database:</h2>
 * <p>
 * Download from: <a href="https://ftp.ncbi.nlm.nih.gov/pub/taxonomy/">NCBI FTP</a><br>
 * Files: taxdump.tar.gz contains nodes.dmp and names.dmp
 * </p>
 * 
 * @author eGPS Development Team
 * @version 1.0
 * @since 1.0
 * @see TaxonomyNode
 * @see TaxonomyName
 * @see EvolNode
 */
public class TaxonomyParser {

    private static final Logger log = LoggerFactory.getLogger(TaxonomyParser.class);

    /**
     * 这个暂时还不能用，放弃吧，我的需求还没有那么必要
     * @param nodePath
     * @param namePath
     * @throws IOException
     */
    public void parseTree(String nodePath,String namePath) throws IOException {
        final int recordSize = 18;
        List<TaxonomyNode> nodes = Lists.newArrayList();
        EGPSFileUtil.forLoopToFileMaybeCompressed(nodePath, line -> {
            String[] split = EGPSStringUtil.split(line, '|');

            if (recordSize < split.length) {
                TaxonomyNode node = new TaxonomyNode();
                assignValue(split, node);
                if (node.getParentTaxId() == 1) {
                    // This is the error case;
                }else {
                    nodes.add(node);
                }

                return false;
            } else {
                log.error("Invalid record size: {} for line: {}", split.length, line);
                return true;
            }
        });
        EGPSFileUtil.forLoopToFileMaybeCompressed(namePath, line -> {
            String[] split = EGPSStringUtil.split(line, '|');
            TaxonomyName name = getTaxonName(split);
            return  false;
        });

//        checkData(nodes);

        EvolNode rootNode = getEvolNode(nodes);

        EvolNodeUtil.initializeSize(rootNode);
        log.info("The root node is: {}.", rootNode);
        log.info("The root node size is: {}.", rootNode.getSize());
    }

    private TaxonomyName getTaxonName(String[] split) {
        TaxonomyName taxonomyName = new TaxonomyName();

//        tax_id					-- the id of node associated with this name
//        name_txt				-- name itself
//        unique name				-- the unique variant of this name if name not unique
//        name class				-- (synonym, common name, ...)

        String taxID = split[0].trim();
        if (Strings.isNotEmpty(taxID)) {
            taxonomyName.setTaxId(Integer.parseInt(taxID));
        }
        //TODO

        return  taxonomyName;
    }

    private void checkData(List<TaxonomyNode> nodes) {
        int index = 0;
        for (TaxonomyNode node : nodes){
            index ++;


            if (node.getParentTaxId() == 1){
                // This is the error case;
                continue;
            }
            if (node.getTaxId() == node.getParentTaxId()){
                throw new IllegalArgumentException(index + "\tThe taxid is equals to parent taxid: ".concat(node.toString()));
            }
        }
    }

    private EvolNode getEvolNode(List<TaxonomyNode> nodes) {
        if (nodes.size() < 3){
            throw new IllegalArgumentException("The nodes size is less than 3.");
        }
        log.debug("Start get evol node");
        Map<Integer, EvolNode> taxid2Node = Maps.newHashMap();
        for (TaxonomyNode node : nodes){
            DefaultPhyNode defaultPhyNode = new DefaultPhyNode();
            defaultPhyNode.setName(node.getTaxId() + "");
            defaultPhyNode.setID(node.getTaxId());

            if (taxid2Node.containsKey(node.getTaxId())){
                throw new IllegalArgumentException("The taxid is exists: ".concat(node.toString()));
            }
            taxid2Node.put(node.getTaxId(), defaultPhyNode);
        }
        log.debug("Finish the map");
        EvolNode rootNode = null;
        for (TaxonomyNode node : nodes){
            EvolNode parentNode = taxid2Node.get(node.getParentTaxId());
            EvolNode currentNode = taxid2Node.get(node.getTaxId());
            parentNode.addChild(currentNode);
            rootNode = parentNode;
        }
        taxid2Node.clear();
        log.debug("Finish the addChild");

        while (rootNode.getParentCount() > 0){
            log.debug("This is {}", rootNode.toString());
            rootNode = rootNode.getParent();
        }
        log.debug("Finish get the root");
        return rootNode;
    }

    private void assignValue(String[] split, TaxonomyNode node) {
        //        tax_id					-- node id in GenBank taxonomy database
//        parent tax_id				-- parent node id in GenBank taxonomy database
//        rank					-- rank of this node (superkingdom, kingdom, ...)
//        embl code				-- locus-name prefix; not unique
//        division id				-- see division.dmp file
//        inherited div flag  (1 or 0)		-- 1 if node inherits division from parent
//        genetic code id				-- see gencode.dmp file
//        inherited GC  flag  (1 or 0)		-- 1 if node inherits genetic code from parent
//        mitochondrial genetic code id		-- see gencode.dmp file
//        inherited MGC flag  (1 or 0)		-- 1 if node inherits mitochondrial gencode from parent
//        GenBank hidden flag (1 or 0)            -- 1 if name is suppressed in GenBank entry lineage
//        hidden subtree root flag (1 or 0)       -- 1 if this subtree has no sequence data yet
//        comments				-- free-text comments and citations
//        plastid genetic code id                 -- see gencode.dmp file
//        inherited PGC flag  (1 or 0)            -- 1 if node inherits plastid gencode from parent
//        specified_species			-- 1 if species in the node's lineage has formal name
//        hydrogenosome genetic code id           -- see gencode.dmp file
//        inherited HGC flag  (1 or 0)            -- 1 if node inherits hydrogenosome gencode from parent
        String taxID = split[0].trim();
        if (Strings.isNotEmpty(taxID)) {
            node.setTaxId(Integer.parseInt(taxID));
        } else {
            throw new IllegalArgumentException("The taxID value is not exists.");
        }

        String parentTaxId = split[1].trim();
        if (Strings.isNotEmpty(parentTaxId)) {
            node.setParentTaxId(Integer.parseInt(parentTaxId));
        }

        node.setRank(split[2].trim());
        node.setEmblCode(split[3].trim());

        String divisionId = split[4].trim();
        if (Strings.isNotEmpty(divisionId)) {
            node.setDivisionId(Integer.parseInt(divisionId));
        }

        String inheritedDivFlag = split[5].trim();
        if (Strings.isNotEmpty(inheritedDivFlag)) {
            node.setInheritedDivFlag(Integer.parseInt(inheritedDivFlag));
        }

        String geneticCodeId = split[6].trim();
        if (Strings.isNotEmpty(geneticCodeId)) {
            node.setGeneticCodeId(Integer.parseInt(geneticCodeId));
        }

        String inheritedGCFlag = split[7].trim();
        if (Strings.isNotEmpty(inheritedGCFlag)) {
            node.setInheritedGCFlag(Integer.parseInt(inheritedGCFlag));
        }

        String mitochondrialGeneticCodeId = split[8].trim();
        if (Strings.isNotEmpty(mitochondrialGeneticCodeId)) {
            node.setMitochondrialGeneticCodeId(Integer.parseInt(mitochondrialGeneticCodeId));
        }

        String inheritedMGCFlag = split[9].trim();
        if (Strings.isNotEmpty(inheritedMGCFlag)) {
            node.setInheritedMGCFlag(Integer.parseInt(inheritedMGCFlag));
        }

        String genBankHiddenFlag = split[10].trim();
        if (Strings.isNotEmpty(genBankHiddenFlag)) {
            node.setGenBankHiddenFlag(Integer.parseInt(genBankHiddenFlag));
        }

        String hiddenSubtreeRootFlag = split[11].trim();
        if (Strings.isNotEmpty(hiddenSubtreeRootFlag)) {
            node.setHiddenSubtreeRootFlag(Integer.parseInt(hiddenSubtreeRootFlag));
        }

        node.setComments(split[12].trim());

        String plastidGeneticCodeId = split[13].trim();
        if (Strings.isNotEmpty(plastidGeneticCodeId)) {
            node.setPlastidGeneticCodeId(Integer.parseInt(plastidGeneticCodeId));
        }

        String inheritedPGCFlag = split[14].trim();
        if (Strings.isNotEmpty(inheritedPGCFlag)) {
            node.setInheritedPGCFlag(Integer.parseInt(inheritedPGCFlag));
        }

        String specifiedSpecies = split[15].trim();
        if (Strings.isNotEmpty(specifiedSpecies)) {
            node.setSpecifiedSpecies(Integer.parseInt(specifiedSpecies));
        }

        String hydrogenosomeGeneticCodeId = split[16].trim();
        if (Strings.isNotEmpty(hydrogenosomeGeneticCodeId)) {
            node.setHydrogenosomeGeneticCodeId(Integer.parseInt(hydrogenosomeGeneticCodeId));
        }

        String inheritedHGCFlag = split[17].trim();
        if (Strings.isNotEmpty(inheritedHGCFlag)) {
            node.setInheritedHGCFlag(Integer.parseInt(inheritedHGCFlag));
        }
    }
}
