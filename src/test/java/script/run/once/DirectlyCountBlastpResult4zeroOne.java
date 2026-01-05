package script.run.once;

import com.google.common.collect.Maps;
import tsv.io.KitTable;
import tsv.io.TSVReader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class DirectlyCountBlastpResult4zeroOne {
    public static void main(String[] args) throws Exception {
        String inputPath = ".";

        Path dir = Path.of(inputPath);
        Map<String, String> species2string = Maps.newHashMap();

        List<Path> list = Files.walk(dir).toList();
        for (Path p : list) {
            if (p.getFileName().toString().equals("1.blastp.result.tsv")) {
                Map<String, Integer> name2countMap = Maps.newHashMap();
                String name = p.getParent().getFileName().toString();
                String blastpResultFile = p.toString();
                KitTable kitTable = TSVReader.readTsvTextFile(blastpResultFile, false);
                kitTable.getContents().forEach(strings -> {
                    String query = strings.get(0);
                    String subject = strings.get(1);
                    name2countMap.put(subject, 1);
                });


                String[] geneSymbols = getGeneSymbol();
                StringJoiner stringJoiner = new StringJoiner("\t");
                for (String geneSymbol : geneSymbols) {
                    if (name2countMap.containsKey(geneSymbol)) {
                        stringJoiner.add("1");
                    } else {
                        stringJoiner.add("0");
                    }
                }

                String string = stringJoiner.toString();

                species2string.put(name, name + "\t" + string);
            }
        }

        for (String s : getSpeciesNames()){
            System.out.println(species2string.get(s));
        }

    }

    private static String[] getSpeciesNames() {
        String aa = """
                Amoebozoa
                Holomycota
                Filasterea
                lchtyosporea
                Choanoflagellata
                Ctenophora
                Demospongiae
                Hexactinellida
                Calcarea
                Homoscleromorpha
                Placozoa
                Anthozoa
                Hydrozoa
                Cubozoa
                Scyphozoa
                Bilateria
                """;
        return aa.split("\n");
    }

    private static String[] getGeneSymbol() {
        String[] genes = {
                "WNT3A",
                "WLS",
                "PORCN",
                "SFRP1",
                "TINAGL1",
                "AFM",
                "WIF1",
                "NOTUM",
                "DKK1",
                "FZD4",
                "LRP6",
                "DVL2",
                "GSK3B",
                "CSNK1A1",
                "APC",
                "AXIN1",
                "CTNNB1",
                "LEF1",
                "TLE1"
        };
        return genes;
    }
}

