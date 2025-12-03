package script.run.once;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import tsv.io.KitTable;
import tsv.io.TSVReader;
import utils.string.EGPSStringUtil;

import java.io.IOException;
import java.util.*;


class Count_Wnt_CompNumber {
    public static void main(String[] args) throws IOException {
        String pathOfWntQuery = null;
        String pathOfOrthoFinderResults = null;
        if (args.length == 0){
            pathOfWntQuery = "C:\\Users\\yudal\\Documents\\project\\WntEvolution\\Archieve\\SpeciesDataSet1_oneSpeciesOnePhylum\\processed\\OrthoFinder_run1\\summary.wnt.query.og.tsv";
            pathOfOrthoFinderResults = "C:\\Users\\yudal\\Documents\\project\\WntEvolution\\Archieve\\SpeciesDataSet1_oneSpeciesOnePhylum\\processed\\OrthoFinder_run1\\Results_Apr06\\Orthogroups\\Orthogroups.GeneCount.tsv";
        }else {
            pathOfWntQuery = args[0];
            pathOfOrthoFinderResults = args[1];
        }

        final int indexOfWntComp = 1;
        final int indexOfOrthGroup = 0;
        String[] geneSymbols = getGeneSymbols();

        Map<String, List<String>> og2originalLine = new HashMap<>();


        KitTable kitTable = TSVReader.readTsvTextFile(pathOfOrthoFinderResults, true);
        List<String> headerNames = kitTable.getHeaderNames();
        Iterator<String> originalLineIterator = kitTable.getOriginalLines().iterator();
        int index = 0;
        kitTable.getContents().forEach(row -> {
            String og = row.get(indexOfOrthGroup);
            og2originalLine.put(og, row);
        });

        Map<String, String> symbol2og = new HashMap<>();
        TSVReader.readTsvTextFile(pathOfWntQuery).getContents().forEach(row -> {
            symbol2og.put(row.get(0), row.get(5));
        });


        Joiner joiner = Joiner.on('\t');

        List<List<String>> arrayList2output = Lists.newArrayList();
        List<String> header = kitTable.getHeaderNames().subList(0, kitTable.getHeaderNames().size());
        header.set(0, "Name");
        arrayList2output.add(header);


        for (String geneSymbol : geneSymbols) {
            String og = symbol2og.get(geneSymbol);
            List<String> eles = og2originalLine.get(og);
            if (eles != null){
                eles.set(0, geneSymbol);
                arrayList2output.add(eles);
            }
        }

        List<List<String>> transposed = EGPSStringUtil.transpose(arrayList2output);
        for (List<String> array : transposed){
            String join = joiner.join(array);
            System.out.println(join);
        }

    }


    public static String[] getGeneSymbols(){
        String  geneSymbol = """
                SHISA2
                MESD
                PORCN
                WLS
                NDP
                WNT3A
                GPC4
                FZD4
                TSPAN12
                RECK
                ADGRA2
                EGFR
                LRP6
                MACF1
                DVL2
                CXXC4
                SENP2
                AXIN1
                CSNK1A1
                CSNK1E
                CSNK2A1
                GSK3B
                APC
                PPP1CA
                AMER1
                PPP2CA
                BTRC
                SKP1
                RBX1
                CUL1
                FRAT1
                TNKS
                NKD1
                CTNNBIP1
                CTBP1
                CTNNB1
                LEF1
                TLE1
                HDAC1
                PYGO1
                BCL9
                CDC73
                SMARCA4
                TBP
                EP300
                CREBBP
                CHD8
                SOX17
                IFT140
                RAPGEF5
                IPO11
                WIF1
                SERPINF1
                SFRP1
                TRABD2A
                CER1
                NOTUM
                RSPO1
                LGR5
                HSPG2
                RNF43
                ZNRF3
                SOST
                DKK1
                KREMEN1
                MYC
                CCND1
                FOSL1
                JUN
                LGR5
                AXIN2
                RNF43
                ZNRF3
                NFATC1
                CAMK2A
                PPP3CA
                PRKCA
                PLCB1
                PRICKLE1
                MAPK8
                INVS
                DAAM1
                RHOA
                ROCK2
                RAC1
                NLK
                MAP3K7
                VANGL1
                ROR1
                RYK
                """;
        String[] strings = geneSymbol.split("\n");
        Set<String> objects = new LinkedHashSet<>();
        for (String string : strings) {
            objects.add(string.trim());
        }
        return objects.toArray(new String[0]);
    }

}

