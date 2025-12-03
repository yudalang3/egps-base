package pfam.parse;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.mutable.MutableInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Parser for Pfam HMM database annotation files (Pfam-A.hmm.dat format).
 */
public class HmmDatParser {

    private static final Logger log = LoggerFactory.getLogger(HmmDatParser.class);

    public static void main(String[] args) throws IOException {
        Path path = Path.of("C:\\Users\\yudal\\Documents\\project\\eGPS2\\jars\\eGPS3\\work\\Pfam-A.hmm.dat");
        Path outPath = Path.of("C:\\Users\\yudal\\Documents\\project\\eGPS2\\jars\\eGPS3\\work\\clan_mapping.tsv");
        Path outPathClanSize = Path.of("C:\\Users\\yudal\\Documents\\project\\eGPS2\\jars\\eGPS3\\work\\clan_size.tsv");

        List<String> contents = Files.readAllLines(path);

        HmmDatParser hmmDatParser = new HmmDatParser();
        List<PfamDBEntry> pfamDBEntryList = hmmDatParser.getPfamEntries(contents);

        List<String> outputs = Lists.newLinkedList();
        outputs.add("clan\tpfamId\tpfamAccession\tpfamDescription\tsize");
        Map<String, Integer> clan2count = new HashMap<>();
        for (PfamDBEntry pfamDBEntry : pfamDBEntryList) {
            String clans = pfamDBEntry.getClans();
            Integer value = clan2count.computeIfAbsent(clans, k -> 0);
            clan2count.put(clans, value + 1);
            if (clans != null){
                outputs.add(clans + "\t" + pfamDBEntry.getId() + "\t" + pfamDBEntry.getAccession() + "\t" + pfamDBEntry.getDescription());
            }
            if (Objects.equals(clans, "CL0192")){
                System.out.println(pfamDBEntry.getId() + "\t" + pfamDBEntry.getAccession() + "\t" + pfamDBEntry.getDescription());
            }
        }
        Files.write(outPath, outputs);
        outputs.clear();
        outputs.add("clan\tcount");
        ArrayList<Map.Entry<String, Integer>> entries = new ArrayList<>(clan2count.entrySet());
        entries.sort((o1, o2) -> o2.getValue() - o1.getValue());
        for (Map.Entry<String, Integer> entry : entries){
            if (entry.getValue() > 1){
                String clans = entry.getKey();
                if (clans != null){
                    outputs.add(clans + "\t" + entry.getValue());
                }
            }else {
                log.info("Clan: {} count: {}", entry.getKey(), entry.getValue());
            }
        }
        Files.write(outPathClanSize, outputs);
        System.out.println(clan2count.get("CL0079"));
    }

    public List<PfamDBEntry> getPfamEntries(List<String> contents) {
        MutableInt count = new MutableInt();
        List<String> arrayList = Lists.newArrayList();
        List<PfamDBEntry> pfamDBEntryList = Lists.newArrayList();
        int maxSize = 0;
        List<String> maxArrayList = null;

        for (String line : contents) {
            if (line.startsWith("//")) {
                PfamDBEntry entry = generateEntry(arrayList);
                count.increment();
                int size = arrayList.size();
                if (size > maxSize) {
                    maxSize = size;
                    maxArrayList = new ArrayList<>(arrayList);
                }
                arrayList.clear();
                pfamDBEntryList.add(entry);
            } else {
                arrayList.add(line);
            }

        }

        log.info("Total count is: {}", count);
        log.info("maxSize is: {}", maxSize);

        // to see if the entry id has multiple types and clans
        Set<String> entryIds = Sets.newHashSet();
        for (PfamDBEntry pfamDBEntry : pfamDBEntryList) {
            if (entryIds.contains(pfamDBEntry.getId())) {
                System.err.println("Exist duplicated entry: " + pfamDBEntry.getId());
            }
            entryIds.add(pfamDBEntry.getId());
        }
        return pfamDBEntryList;
    }

    private PfamDBEntry generateEntry(List<String> lines) {
        PfamDBEntry pfamDBEntry = new PfamDBEntry();
        final String ID_LINE = "#=GF ID   ";
        final String AC_LINE = "#=GF AC   ";
        final String DE_LINE = "#=GF DE   ";
        final String TP_LINE = "#=GF TP   ";
        final String CL_LINE = "#=GF CL   ";
        int numOfTP = 0;
        int numOfCL = 0;
        for (String line : lines) {
            if (line.startsWith("# STOCKHOLM")) {

            } else if (line.startsWith(ID_LINE)) {
                String substring = line.substring(ID_LINE.length(), line.length());
                pfamDBEntry.setId(substring);
            } else if (line.startsWith(AC_LINE)) {
                String substring = line.substring(AC_LINE.length(), line.length());
                pfamDBEntry.setAccession(substring);
            } else if (line.startsWith(DE_LINE)) {
                String substring = line.substring(DE_LINE.length(), line.length());
                pfamDBEntry.setDescription(substring);
            } else if (line.startsWith(TP_LINE)) {
                String substring = line.substring(TP_LINE.length(), line.length());
                numOfTP++;
                pfamDBEntry.setType(substring);
            } else if (line.startsWith(CL_LINE)) {
                String substring = line.substring(CL_LINE.length(), line.length());
                pfamDBEntry.setClans(substring);
                numOfCL++;
            }
        }

//        if (numOfTP != 1){
//            log.error("Has pfam entry that do not have one TP.");
//        }
        if (numOfCL > 1){
            log.error("Has pfam entry that do not have one CL.");
        }


        return pfamDBEntry;
    }

    public Map<String, List<PfamDBEntry>> getSeqId2PfamDBEntriesMap(String domainFile) throws IOException {
        List<String> contents = Files.readAllLines(Path.of(domainFile));
        List<PfamDBEntry> pfamDBEntryList = getPfamEntries(contents);

        Map<String, List<PfamDBEntry>> seqId2PfamDBEntriesMap = new HashMap<>();

        for (PfamDBEntry pfamDBEntry : pfamDBEntryList){
            String seqId = pfamDBEntry.getId();
            List<PfamDBEntry> pfamDBEntries = seqId2PfamDBEntriesMap.computeIfAbsent(seqId, k -> new ArrayList<>());
            pfamDBEntries.add(pfamDBEntry);
        }

        return seqId2PfamDBEntriesMap;
    }
}