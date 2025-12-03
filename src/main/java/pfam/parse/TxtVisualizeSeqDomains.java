package pfam.parse;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fasta.io.FastaReader;
import tsv.io.TSVReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;


/**
 * Created by yudalang on 2024/04/15.
 * Input the protein fasta file and the pfam_scan output file.
 * Output the summary tsv file with the following format:
 * <protein_id> \t <prot_length> \t <domainWithLocation> \t <domainTypes> \t <domainClans> \t <clanSizes>
 */
public class TxtVisualizeSeqDomains {

    static class PfamRecord implements Comparable<PfamRecord>{
        String proID;
        int protLen;
        String domainName;
        private  int start;
        int end;
        String type;
        String clan;
        String clanSize;
        public String toString() {
            StringJoiner wholeLine = new StringJoiner("\t");
            wholeLine.add(proID);
            wholeLine.add(String.valueOf(protLen));
            wholeLine.add(domainName);
            wholeLine.add(String.valueOf(start));
            wholeLine.add(String.valueOf(end));
            wholeLine.add(type);
            wholeLine.add(clan);
            wholeLine.add(clanSize);
            return wholeLine.toString();
        }

        @Override
        public int compareTo(PfamRecord o) {
            int compareTo = this.start - o.start;
            if (compareTo == 0){
                compareTo = this.end - o.end;
            }
            return compareTo;
        }
    }

    public static void main(String[] args) throws IOException {
        String fastaFile = args[0];
        String domainFile = args[1];
        String domainClanSizeFile = args[2];
        if (args.length < 3){
            System.out.println("Usage: java -jar TxtVisualizeSeqDomains.jar <fastaFile> <domainFile> <domainClanSizeFile>");
            return;
        }

        LinkedHashMap<String, String> name2seqMap = FastaReader.readFastaSequence(fastaFile);
        Map<String, String> clan2countMap = Maps.newHashMap();
        TSVReader.readTsvTextFile(domainClanSizeFile).getContents().forEach(line -> {
            clan2countMap.put(line.get(0), line.get(1));
        });


        List<String> strings = Files.readAllLines(Path.of(domainFile));

        Map<String, List<PfamRecord>> protID2PfamRecords = Maps.newLinkedHashMap();
        for (String line : strings){
            if (line.startsWith("#") || line.isEmpty()){
                continue;
            }

            PfamScanRecord pfamScanRecord = new PfamScanRecord(line);

            PfamRecord pfamRecord = new PfamRecord();
            pfamRecord.proID = pfamScanRecord.getSeqId();
            pfamRecord.protLen = name2seqMap.get(pfamRecord.proID).length();
            pfamRecord.domainName = pfamScanRecord.getHmmName();
            pfamRecord.start = pfamScanRecord.getAlignmentStart();
            pfamRecord.end = pfamScanRecord.getAlignmentEnd();
            pfamRecord.type = pfamScanRecord.getType();
            pfamRecord.clan = pfamScanRecord.getClan();
            pfamRecord.clanSize = clan2countMap.get(pfamRecord.clan);

            List<PfamRecord> pfamRecords = protID2PfamRecords.computeIfAbsent(pfamRecord.proID, k -> Lists.newArrayList());
            pfamRecords.add(pfamRecord);
        }

        System.out.println("proID\tprotLen\tdomainName\tstart\tend\ttype\tclan\tclanSize");
        for (Map.Entry<String, List<PfamRecord>> entry : protID2PfamRecords.entrySet()) {
            String protID = entry.getKey();

            //<protein_id> \t <prot_length> \t <domainWithLocation> \t <domainTypes> \t <domainClans> \t <clanSizes>
            System.out.println("-----------------------------------------------------------------------------");
            List<PfamRecord> values = entry.getValue();
            for (PfamRecord pfamRecord : values){
                String string = pfamRecord.toString();
                System.out.println(string);
            }
        }
    }
}

