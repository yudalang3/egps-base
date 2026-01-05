package tab.index;

import htsjdk.samtools.util.BlockCompressedInputStream;
import htsjdk.samtools.util.BlockCompressedOutputStream;
import htsjdk.tribble.SimpleFeature;
import htsjdk.tribble.index.tabix.TabixFormat;
import htsjdk.tribble.index.tabix.TabixIndex;
import htsjdk.tribble.index.tabix.TabixIndexCreator;
import htsjdk.tribble.readers.AsciiLineReader;
import htsjdk.tribble.readers.TabixReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 还没跑通，主要是htsjdk这个库教程很少
 */
public class TestTbx {


    private static void query() throws IOException {
        TabixReader tr = new TabixReader("genes.tsv.gz");
        TabixReader.Iterator it = tr.query("chr1:100000-200000");
        for (String rec; (rec = it.next()) != null; ) {
            System.out.println(rec);
        }
        tr.close();
    }

    /** 把普通 TSV 压缩成 bgzip */
    private static Path bgzipCompress(Path tsv) throws IOException {

        Path bgz = tsv.resolveSibling(tsv.getFileName() + ".bgz");
        try (var in = Files.newInputStream(tsv); var out = new BlockCompressedOutputStream(bgz.toFile())) {
            byte[] buf = new byte[1 << 16];
            int n;
            while ((n = in.read(buf)) >= 0) out.write(buf, 0, n);
        }
        return bgz;
    }

    public static void generateTbx(String path) throws IOException {
        // 1) 定义文件格式：第一列 chr，第二列 start，没有 end 列
        TabixFormat fmt = new TabixFormat(
                TabixFormat.GENERIC_FLAGS,  // 通用 TSV
                /*sequenceCol=*/0,
                /*startCol=*/3,
                /*endCol=*/4,               // 0=无 end 列
                '#',                        // 注释前缀
                /*skipHeaderLines=*/0
        );

// 2) 准备 IndexCreator
        TabixIndexCreator tic = new TabixIndexCreator(TabixFormat.GFF);

// 3) 逐行读取 bgzip 文件，构造 Feature 并送给 tic
        try (BlockCompressedInputStream bcis = new BlockCompressedInputStream(
                Files.newInputStream(Path.of(path)));
             AsciiLineReader lr = new AsciiLineReader(bcis)) {

            String line;
            while ((line = lr.readLine()) != null) {
                if (line.charAt(0) == '#') continue;        // 跳过注释
                long filePos = bcis.getFilePointer();       // 记录此行的偏移
                long bcisPosition = bcis.getPosition();
                long lrPositon = lr.getPosition() ;
                System.out.println("bcis.getFilePointer() is : " + filePos);
                System.out.println("bcisPosition is : " + bcisPosition);
                System.out.println("lrPositon is : " + lrPositon);
                System.out.println("filePos is : " + filePos);

                String[] t = line.split("\t");
                SimpleFeature f = new SimpleFeature(
                        t[0],                           // contig
                        Integer.parseInt(t[3]),         // start
                        Integer.parseInt(t[4]));        // 没有 end 就用 start

                if (Integer.parseInt(t[3]) >= Integer.parseInt(t[4])){
                    System.out.println();
                }
                try {
                    tic.addFeature(f, filePos);             // 加入索引
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }

// 4) 写出 .tbi
        long size = Files.size(Path.of(path));
        TabixIndex index = (TabixIndex) tic.finalizeIndex(
                size);
        index.writeBasedOnFeatureFile(new File(path.concat(".idx")));

    }

    public static void main(String[] args) throws IOException {
        //bgzipCompress(Path.of("C:\\Users\\yudal\\PycharmProjects\\WntEvolPy\\corelation4\\eggNOG\\process_digest\\gencode.v48.basic.annotation.gff3"));
//        generateTbx("C:\\Users\\yudal\\PycharmProjects\\WntEvolPy\\corelation4\\eggNOG\\process_digest\\gencode.v48.basic.annotation.gff3.bgz");
        generateTbx("C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\带学生\\韩珊珊\\GenomeData\\hs1.ncbiRefSeq.sorted.gtf.gz");
    }
}
