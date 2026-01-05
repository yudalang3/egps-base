package script.run.once;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import fasta.io.FastaReader;
import tsv.io.KitTable;
import tsv.io.TSVReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExtractSequenceSpecific2Wnt {
    public static void main(String[] args) throws IOException {
        String metaFile = "C:\\Users\\yudal\\Documents\\project\\WntEvolution\\Archieve\\Wnt_QuerySequences\\compoentSet4\\WntQueryFiles.tsv";
        String fastaFile = "C:\\Users\\yudal\\Documents\\project\\WntEvolution\\Archieve\\Wnt_QuerySequences\\compoentSet4\\Homo_sapiens.GRCh38.pep.all_longestCDS.rmAlt.fa.gz";
        String outputFile = "wntPathway_querySeq_compoentSet3.fa";

        Map<String, String> pep2seqMap = Maps.newHashMap();
        FastaReader.readAndProcessFastaPerEntry(fastaFile, (name, seq) -> {
            int indexOfSpace = name.indexOf(' ');
            String substring = name.substring(0, indexOfSpace);
            pep2seqMap.put(substring, seq);
            return false;
        });

        KitTable kitTable = TSVReader.readTsvTextFile(metaFile);
        int indexOfGeneSymbol = 0;
        int indexOfPep = 4;
        List<String> outputs = Lists.newLinkedList();
        Set<String> alreadyExists = Sets.newHashSet();
        kitTable.getContents().forEach(strings -> {
            String geneSymbol = strings.get(indexOfGeneSymbol);
            String pep = strings.get(indexOfPep);
            if (alreadyExists.contains(geneSymbol)){
                System.err.println(geneSymbol + "\tAlready exists.");
                return;
            }

            String s = pep2seqMap.get(pep);
            if (s != null){
                outputs.add(">".concat(geneSymbol));
                outputs.add(s);
            }else {
                System.err.println(geneSymbol + "\tDoes not have the seq.");
            }
            alreadyExists.add(geneSymbol);
        });

        Files.write(Path.of(outputFile), outputs);
    }
}
