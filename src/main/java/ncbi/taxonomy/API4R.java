package ncbi.taxonomy;

import com.google.common.base.Joiner;

import java.util.StringJoiner;

/**
 * R language API for NCBI taxonomy lineage retrieval operations.
 */
public class API4R {
    public static void main(String[] args) throws Exception {
        String path_rankedLin = "C:/Users/yudal/Documents/project/WntEvolution/Wnt_evol_in_animalPhylum/EnsemblSpeciesStatements/NCBI_taxonomy/data/new_taxdump_2025-01-01/rankedlineage.dmp";
        API4R api4R = new API4R();
//        String[] rankedLineages = api4R.getRankedLineages(path_rankedLin, new int[]{2, 3, 4});
        String[] rankedLineages = api4R.getRankedLineages(path_rankedLin, new int[]{9606, 10090, 10116, 10088, 10090});
        System.out.println(Joiner.on('\n').join(rankedLineages));
    }

    public String test(String filePath, int[] taxonIDs) throws Exception {
        int a = 1 + 1;
        return "test";
    }

    public String[] getRankedLineages(String filePath, int[] taxonIDs) throws Exception {
        TaxonomyFullNameLineageParser parser = new TaxonomyFullNameLineageParser();
        parser.parseTree(filePath);

        String[] ret = new String[taxonIDs.length];
        int index = 0;
        for (int taxonID : taxonIDs) {
            TaxonomicRank lineage = parser.getLineage(taxonID);
//            return "TaxonomicRank{" +
//                    "taxId=" + taxId +
//                    ", taxName='" + taxName + '\'' +
//                    ", species='" + species + '\'' +
//                    ", genus='" + genus + '\'' +
//                    ", family='" + family + '\'' +
//                    ", order='" + order + '\'' +
//                    ", clazz='" + clazz + '\'' +
//                    ", phylum='" + phylum + '\'' +
//                    ", kingdom='" + kingdom + '\'' +
//                    ", superkingdom='" + superkingdom + '\'' +
//                    '}';

            String[] lineageArr = null;
            if (lineage == null) {
                lineageArr = new String[]{
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                };
            } else {
                lineageArr = new String[]{
                        lineage.getTaxName(),
                        lineage.getSpecies(),
                        lineage.getGenus(),
                        lineage.getFamily(),
                        lineage.getOrder(),
                        lineage.getClazz(),
                        lineage.getPhylum(),
                        lineage.getKingdom(),
                        lineage.getSuperkingdom()
                };
            }
            StringJoiner stringJoiner = new StringJoiner("\t");
            for (String s : lineageArr) {
                if (s == null) {
                    stringJoiner.add("NA");
                } else {
                    stringJoiner.add(s);
                }
            }
            ret[index] = stringJoiner.toString();
            index++;
        }

        return ret;
    }
}