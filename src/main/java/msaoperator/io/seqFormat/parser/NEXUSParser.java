package msaoperator.io.seqFormat.parser;

import msaoperator.alignment.sequence.BasicSequenceData;
import msaoperator.alignment.sequence.Sequence;
import msaoperator.alignment.sequence.SequenceI;
import msaoperator.io.seqFormat.AbstractParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Parser for NEXUS format sequence alignment files.
 */
public class NEXUSParser extends AbstractParser {

    private ArrayList<SequenceI> seqElements = new ArrayList<>(30);

    final String beginLine = "begin data;";
    final String endLine = "end;";

    public NEXUSParser(File inputFile) {
        super(inputFile);
    }

    @Override
    public void parse() throws Exception {
        StringBuilder sBuilder = new StringBuilder(16384);

        FileInputStream inputStream = new FileInputStream(inputFile);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String str = null;

        boolean shouldReadDATABlock = false;

        while ((str = bufferedReader.readLine()) != null) {

            str = str.trim();
            if (str.startsWith(beginLine)) {
                sBuilder.setLength(0);
                shouldReadDATABlock = true;
                continue;
            }

            if (shouldReadDATABlock) {
                sBuilder.append(str).append("\n");
            }

            if (str.startsWith(endLine)) {
                shouldReadDATABlock = false;

                parseDATABlock(sBuilder.toString());
            }
        }

        inputStream.close();
        bufferedReader.close();

    }

    private void parseDATABlock(String string) throws IllegalAccessException {
        String[] splits = string.split(";");
        for (String str : splits) {
            if (str.contains("format")) {
                // DATA TYPE not DNA is not permitted!
                if (str.indexOf("datatype=dna") == -1) {
                    String s = "Sorry currently we don't support non DNA sequences!";
                    throw new IllegalAccessException(s);
                }
            } else if (str.contains("matrix")) {
                String[] alignmentLines = str.split("\n");
                int end = alignmentLines.length;
                for (int i = 2; i < end; i++) {
                    String line = alignmentLines[i];
                    String operateAVStr = line.trim();
                    if (operateAVStr.indexOf("[") == -1) {
                        String[] split = operateAVStr.split("\\s+");
                        seqElements.add(new Sequence(split[0], split[1]));
                    }
                }

            }
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public BasicSequenceData getSeqElements() {
        return new BasicSequenceData(seqElements);
    }


}
