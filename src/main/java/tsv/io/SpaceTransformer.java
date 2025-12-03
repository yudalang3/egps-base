package tsv.io;

import com.google.common.base.Joiner;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * Command-line utility for converting space-delimited files to tab-delimited format.
 */
public class SpaceTransformer {

    public static void main(String[] args) throws IOException {
        // Must input a file path, else it will throw an error
        if (args.length < 1) {
            System.out.println("Usage: java SpaceTransformer /input/file/path");
            return;
        }

        LineIterator lineIterator = FileUtils.lineIterator(new File(args[0]), "UTF-8");

        Joiner joiner = Joiner.on('\t');
        while (lineIterator.hasNext()){
            String next = lineIterator.next();
            if (next.startsWith("#")){
                continue;
            }
            String[] strings = StringUtils.split(next, " ");
            String joined = joiner.join(strings);
            System.out.println(joined);
        }

        lineIterator.close();
    }
}