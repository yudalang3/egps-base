package script.run.once;

import com.google.common.collect.Lists;
import fasta.io.FastaReader;
import org.apache.commons.lang3.mutable.MutableInt;
import utils.EGPSFileUtil;

import java.io.IOException;
import java.util.List;

public class Preprocess_pep_fasta {
    public static void main(String[] args) throws IOException {
        String fasta = "C:\\Users\\yudal\\Documents\\project\\WntEvolution\\Archieve\\SpeciesDataSet1_oneSpeciesOnePhylum\\see_alternative_chromos\\Vertebrata\\Homo_sapiens.GRCh38.pep.all_longestCDS.fa.gz";
        boolean isEnsemblPep = false;
        if (fasta.endsWith("pep.all_longestCDS.fa.gz")){
            isEnsemblPep = true;
        }

        if (isEnsemblPep){
            String output = fasta.replace("pep.all_longestCDS.fa.gz", "pep.all_longestCDS.rmAlt.fa.gz");

            List<String> outputs = Lists.newLinkedList();
            MutableInt totalCount = new MutableInt();
            MutableInt passCount = new MutableInt();
            FastaReader.readAndProcessFastaPerEntry(fasta, (speciesName, sequence) -> {
                if (!speciesName.contains("scaffold:GRCh38:HSCHR")){
                    outputs.add(">" + speciesName);
                    outputs.add(sequence);
                    passCount.increment();
                }
                totalCount.increment();
                return  false;
            });

            EGPSFileUtil.writeListToFile(outputs, output);
            System.out.println("totalCount is: " + totalCount.intValue() + ", passCount is: " + passCount.intValue());
        }else {
            System.err.println("Not Ensembl pep fasta");
        }
    }
}
