package ncbi.taxonomy;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import utils.EGPSFileUtil;
import utils.string.EGPSStringUtil;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Parser for NCBI taxonomy ranked lineage files with full taxonomic hierarchy.
 */
public class TaxonomyFullNameLineageParser {

    private static final Logger log = LoggerFactory.getLogger(TaxonomyFullNameLineageParser.class);
    private Map<Integer, TaxonomicRank> taxin2linageRankMap;

    public List<TaxonomicRank> getAllTaxonomicRanks() {
        return  new ArrayList<>(taxin2linageRankMap.values());
    }
    /**
     * 这个暂时还不能用，放弃吧，我的需求还没有那么必要
     * @param namePath
     * @throws IOException
     */
    public void parseTree(String namePath) throws IOException {
        final int recordSize = 18;
        List<TaxonomicRank> taxonomicRanks = Lists.newArrayList();
        EGPSFileUtil.forLoopToFileMaybeCompressed(namePath, line -> {
            String[] split = EGPSStringUtil.split(line, '|');
            TaxonomicRank rank = getTaxonName(split);
            taxonomicRanks.add(rank);
            return false;
        });

        log.trace("The size is: {}.", taxonomicRanks.size());

        taxin2linageRankMap = Maps.newHashMap();
        for (TaxonomicRank taxonomicRank : taxonomicRanks){
            if (taxin2linageRankMap.containsKey(taxonomicRank.getTaxId())){
                throw new IllegalArgumentException("The taxid is exists: ".concat(taxonomicRank.toString()));
            }
            taxin2linageRankMap.put(taxonomicRank.getTaxId(), taxonomicRank);
        }

    }

    private TaxonomicRank getTaxonName(String[] split) {
        TaxonomicRank taxonomicRank = new TaxonomicRank();

        // 确保 split 数组长度足够
        if (split.length < 10) {
            throw new IllegalArgumentException("Invalid input: split array must have at least 10 elements.");
        }

        // 处理 tax_id
        String taxID = split[0].trim();
        if (Strings.isNotEmpty(taxID)) {
            taxonomicRank.setTaxId(Integer.parseInt(taxID));
        }

        // 处理 tax_name
        String taxName = split[1].trim();
        if (Strings.isNotEmpty(taxName)) {
            taxonomicRank.setTaxName(taxName);
        }

        // 处理 species
        String species = split[2].trim();
        if (Strings.isNotEmpty(species)) {
            taxonomicRank.setSpecies(species);
        }

        // 处理 genus
        String genus = split[3].trim();
        if (Strings.isNotEmpty(genus)) {
            taxonomicRank.setGenus(genus);
        }

        // 处理 family
        String family = split[4].trim();
        if (Strings.isNotEmpty(family)) {
            taxonomicRank.setFamily(family);
        }

        // 处理 order
        String order = split[5].trim();
        if (Strings.isNotEmpty(order)) {
            taxonomicRank.setOrder(order);
        }

        // 处理 class（使用 clazz 避免关键字冲突）
        String clazz = split[6].trim();
        if (Strings.isNotEmpty(clazz)) {
            taxonomicRank.setClazz(clazz);
        }

        // 处理 phylum
        String phylum = split[7].trim();
        if (Strings.isNotEmpty(phylum)) {
            taxonomicRank.setPhylum(phylum);
        }

        // 处理 kingdom
        String kingdom = split[8].trim();
        if (Strings.isNotEmpty(kingdom)) {
            taxonomicRank.setKingdom(kingdom);
        }

        // 处理 superkingdom
        String superkingdom = split[9].trim();
        if (Strings.isNotEmpty(superkingdom)) {
            taxonomicRank.setSuperkingdom(superkingdom);
        }

        return taxonomicRank;
    }

    public TaxonomicRank getLineage(int taxId) {
        TaxonomicRank taxonomicRank = taxin2linageRankMap.get(taxId);
        return taxonomicRank;
    }

    public Optional<TaxonomicRank> getLineage(String speciesName) {
        Optional<TaxonomicRank> taxonomicRankOpt;
        List<TaxonomicRank> allTaxonomicRanks = getAllTaxonomicRanks();
        for (TaxonomicRank taxonomicRank : allTaxonomicRanks) {
            if (Objects.equals(taxonomicRank.getTaxName(), speciesName)) {
                taxonomicRankOpt = Optional.of(taxonomicRank);
                break;
            }
        }

        taxonomicRankOpt = Optional.empty();
        return taxonomicRankOpt;
    }
}