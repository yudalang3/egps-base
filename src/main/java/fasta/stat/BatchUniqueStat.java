package fasta.stat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Batch processor for calculating unique sequence statistics across multiple FASTA files.
 */
public class BatchUniqueStat {
    public static void main(String[] args) throws IOException {
        String dirPath = args[0];
        String suffix = args[1];

        Path pathOfInput = Path.of(dirPath);

        UniqueStat.output = false;
        Files.walk(pathOfInput).forEach(path -> {
            if (path.toString().endsWith(suffix)) {
                doStats(path);
            }
        });

    }

    private static void doStats(Path path) {

        try {
            double ratio = UniqueStat.getRatio(path.toString());
            System.out.println(path.getFileName().toString() + "\t" + ratio);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}