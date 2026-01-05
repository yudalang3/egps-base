package script.run.once;

import fasta.io.FastaReader;

import java.io.IOException;
import java.util.regex.Matcher;

public class Fasta_ExtractSequenceAccording2rex {
    public static void main(String[] args) throws IOException {
        String fastaFile = args[0];
        String regexStr = args[1];
        // compile the regexStr
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regexStr);


        FastaReader.readAndProcessFastaPerEntry(fastaFile, (name, seq) -> {
            int indexOfSpace = name.indexOf(' ');
            // judge the pattern match the name
            Matcher matcher = pattern.matcher(name);
            if (matcher.find()) {
                System.out.println(name);
                System.out.println(seq);
            }
            return false;
        });

    }
}
