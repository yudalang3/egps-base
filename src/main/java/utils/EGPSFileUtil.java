/**
 * <p>Title: FileUtil.java</p>
 * <p>Description: The quick file utils</p>
 * @author yudalang
 * @date 2018年10月2日
 * @version 1.0

 */
package utils;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.mutable.MutableInt;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.zip.*;

/**
 * Comprehensive file utility class providing operations for file I/O, compression, and manipulation.
 * 
 * <p>
 * This utility class offers a wide range of file operations including:
 * <ul>
 *   <li>File copying (stream-based and channel-based)</li>
 *   <li>File creation with automatic directory creation</li>
 *   <li>Recursive file listing</li>
 *   <li>Compression/decompression (GZ, BZ2, XZ formats)</li>
 *   <li>Network file downloading</li>
 *   <li>Stream content reading</li>
 *   <li>File filtering and searching</li>
 * </ul>
 * </p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Compression Support:</strong> GZIP, BZIP2, XZ compression algorithms</li>
 *   <li><strong>Auto-detection:</strong> Automatically detects and handles compressed files</li>
 *   <li><strong>Large File Support:</strong> Optimized for bioinformatics large file processing</li>
 *   <li><strong>Network I/O:</strong> Download files from URLs</li>
 *   <li><strong>Recursive Operations:</strong> Directory traversal and batch operations</li>
 * </ul>
 * 
 * <h2>Supported Compression Formats:</h2>
 * <ul>
 *   <li>.gz - GZIP compression</li>
 *   <li>.bz2 - BZIP2 compression</li>
 *   <li>.xz - XZ compression</li>
 *   <li>Automatic format detection based on file extension</li>
 * </ul>
 * 
 * <h2>Usage Examples:</h2>
 * <pre>
 * // Copy file
 * EGPSFileUtil.copyFileUsingStream(srcFile, destFile);
 * 
 * // Read compressed file
 * InputStream input = EGPSFileUtil.getInputStreamFromOneFileMaybeCompressed("data.txt.gz");
 * 
 * // Download from URL
 * Optional&lt;File&gt; downloaded = EGPSFileUtil.downloadFromUrl(url, targetDir);
 * 
 * // Get all files recursively
 * ArrayList&lt;File&gt; files = EGPSFileUtil.getListFiles(directory);
 * </pre>
 * 
 * <h2>Thread Safety:</h2>
 * <p>
 * Most methods are thread-safe for read operations. File modification operations
 * (create, delete, copy) should be externally synchronized if used concurrently.
 * </p>
 * 
 * @author yudalang
 * @version 1.0
 * @since 2018-10-02
 * @see FileUtils
 * @see FilenameUtils
 */
public class EGPSFileUtil {

    public static void copyFileUsingStream(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void copyFileUsingFileChannels(String source, String dest) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(source);
        FileChannel inputChannel = fileInputStream.getChannel();
        FileOutputStream fileOutputStream = new FileOutputStream(dest);
        FileChannel outputChannel = fileOutputStream.getChannel();
        outputChannel.transferFrom(inputChannel, 0, inputChannel.size());

        fileInputStream.close();
        fileOutputStream.close();
    }

    /**
     * @Title: createFile
     */
    public static boolean createFile(String destFileName) {
        File file = new File(destFileName);
        // Determine whether the file is a file.
        if (destFileName.endsWith(File.separator)) {
            return false;
        }
        // Determine if the directory exists.
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                return false;
            }
        }
        try {
            // Determine if the file exists.
            if (!file.exists()) {
                if (file.createNewFile()) {
                    return true;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * Gets all the files in the project.
     *
     * @Title: getListFiles
     *
     * @param obj
     *
     * &#064;return:  ArrayList<File> list of files
     *
     */
    public static ArrayList<File> getListFiles(Object obj) {
        File directory;
        if (obj instanceof File) {
            directory = (File) obj;
        } else {
            directory = new File(obj.toString());
        }
        ArrayList<File> files = new ArrayList<>();
        if (directory.isFile()) {
            files.add(directory);
        } else if (directory.isDirectory()) {
            File[] fileArr = directory.listFiles();
            if (fileArr != null) {
                for (File fileOne : fileArr) {
                    files.addAll(getListFiles(fileOne));
                }
            }
        }
        return files;
    }


    public static synchronized boolean forceDelete(File file) {
        boolean result = file.delete();
        while (!result) {
            result = file.delete();
            System.out.println("forece Delete " + file);
        }
        return result;
    }


    private static String getFileNameFromUrl(String url) {
        String name = System.currentTimeMillis() + ".X";
        int index = url.lastIndexOf("/");
        if (index > 0) {
            name = url.substring(index + 1);
            if (!name.trim().isEmpty()) {
                return name;
            }
        }
        return name;

    }

    /**
     * 从网上下载数据
     *
     * @param url        网上的 url，例如 <a href="http://112.124.15.168/asserts/egps2_hias.jar">...</a>
     * @param parentFile 下载到本地的位置，例如 C:/temp
     * @return
     */
    public static Optional<File> downloadFromUrl(String url, File parentFile) {
        File ret;
        try {
            URI uri = new URI(url);
            URL httpurl = uri.toURL();
            String fileName = getFileNameFromUrl(url);
            ret = new File(parentFile, fileName);
            FileUtils.copyURLToFile(httpurl, ret);
        } catch (Exception e) {
            e.printStackTrace();
            ret = null;
        }

        return Optional.ofNullable(ret);
    }

    public static String getContentFromInputStreamAsString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[10240];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        result.close();

        return result.toString("UTF-8");
    }


    public static List<String> getContentFromInputStreamAsLines(InputStream inputStream) throws IOException {
        List<String> ret = new ArrayList<>();

        int sz = 100 * 1024 * 1024;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream), sz);
        String line;
        while ((line = reader.readLine()) != null) {
            ret.add(line);
        }

        reader.close();

        return ret;
    }

    public static Collection<File> getAllFilesInOneDir(File dir) {
        IOFileFilter fileFileFilter = FileFileFilter.INSTANCE;
        return FileUtils.listFiles(dir, fileFileFilter, DirectoryFileFilter.DIRECTORY);
    }

    /**
     * 只读取第一个文件。 特别要注意，如果是苹果电脑压缩的话，不能用，因为苹果电脑里面有 macOS这种文件。
     *
     * @title readZipFile
     * @createdDate 2021-04-02 11:27
     * @lastModifiedDate 2021-04-02 11:27
     * @author yudalang
     * @since 1.7
     *
     * @return List<String> :
     */
    public static List<String> readOneZipFile(File file) throws IOException {
        ZipFile zipFile = new ZipFile(file);

        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        // 只读取第一个文件
        ZipEntry entry = entries.nextElement();
        InputStream stream = zipFile.getInputStream(entry);
        List<String> readLines = IOUtils.readLines(stream, StandardCharsets.UTF_8);

        stream.close();

        zipFile.close();

        return readLines;
    }

    /**
     * 例子：输入 inputPath = a/b/c/d/qwe.prot.human.fas ; additionalStr = "renamed"
     * 调用方式为 appendAdditionalStr2path(); 那么输出的结果为：
     * a/b/c/d/qwe.prot.human.renamed.fas
     *
     */
    public static String appendAdditionalStr2path(String inputPath, String additionalStr) {
        String baseName = FilenameUtils.getBaseName(inputPath);

        StringBuilder sb = new StringBuilder();
        String path = FilenameUtils.separatorsToUnix(FilenameUtils.getFullPath(inputPath));
        sb.append(path);
        sb.append(baseName);
        sb.append(".");
        sb.append(additionalStr);
        sb.append(".");
        sb.append(FilenameUtils.getExtension(inputPath));

        return sb.toString();
    }

    /**
     * 例子：输入 inputPath = a/b/c/d/qwe.prot.human.fas ; additionalStr = "renamed.tsv"
     * 调用方式为 appendAdditionalStr2endOfPathWithoutSuffix(); 那么输出的结果为：
     * a/b/c/d/qwe.prot.human.renamed.tsv
     *
     */
    public static String appendAdditionalStr2endOfPathWithoutSuffix(String inputPath, String additionalStr) {
        String baseName = FilenameUtils.getBaseName(inputPath);

        StringBuilder sb = new StringBuilder();
        String path = FilenameUtils.separatorsToUnix(FilenameUtils.getFullPath(inputPath));
        sb.append(path);
        sb.append(baseName);
        sb.append(".");
        sb.append(additionalStr);

        return sb.toString();
    }

    /**
     * 这绝对是最快的Hash值计算方式。 缺点，这个是不能用的，实际接给出的结果是不稳定的。
     *
     */
    public static String getTheFastestHashCode(String fileUri) throws IOException {
        byte[] allBytes = Files.readAllBytes(Paths.get(fileUri));
        return Long.toHexString(allBytes.hashCode()).toUpperCase();
    }

    /**
     * 计算输入文件的crc32值
     *
     */
    public static String getCRC32(String fileUri) {
        CRC32 crc32 = new CRC32();
        FileInputStream fileinputstream = null;
        CheckedInputStream checkedinputstream = null;
        String crc = null;
        try {
            fileinputstream = new FileInputStream(fileUri);
            checkedinputstream = new CheckedInputStream(fileinputstream, crc32);
            while (checkedinputstream.read() != -1) {
            }
            crc = Long.toHexString(crc32.getValue()).toUpperCase();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileinputstream != null) {
                try {
                    fileinputstream.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            if (checkedinputstream != null) {
                try {
                    checkedinputstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return crc;
    }

    /**
     * 超级好用的一句话提取
     *
     * @param inputFilePath
     */
    public static List<String> getContentsFromOneFileMaybeCompressed(String inputFilePath) throws IOException {
        InputStream inputStream = getInputStreamFromOneFileMaybeCompressed(inputFilePath);
        List<String> ret = new LinkedList<>();

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        try (BufferedReader br = new BufferedReader(inputStreamReader)) {
            String readLine;
            while ((readLine = br.readLine()) != null) {
                ret.add(readLine);
            }
        }

        return ret;
    }

    /**
     * Quick for loop for a file, the file will auto-detect format
     *
     */
    public static void forLoopToFileMaybeCompressed(String filePath, Predicate<String> processor) throws IOException {
        InputStream inputStream = EGPSFileUtil.getInputStreamFromOneFileMaybeCompressed(filePath);
        int sz = 500 * 1024 * 1024;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream), sz)) {
            String readLine;
            while ((readLine = br.readLine()) != null) {
                boolean test = processor.test(readLine);
                if (test) {
                    break;
                }
            }
        }

    }

    public static byte[] getContentByteArrayWithMemMapping(String filePath) throws IOException {
        byte[] ret;
        Path path = Paths.get(filePath);
        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ)) {
            long fileSize = fileChannel.size();
            if (fileSize > Integer.MAX_VALUE) {
                throw new IOException("File size exceeds maximum supported size.");
            }
            long chunkSize = fileSize;

            MappedByteBuffer buffer = fileChannel.map(MapMode.READ_ONLY, 0, chunkSize);
            ret = buffer.array();
        }

        return ret;
    }

    public static void writeListToFile(List<String> dataList, String outputPath) throws IOException {
        BufferedWriter writer = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputPath);
            if (outputPath.toLowerCase().endsWith(".gz")) {
                GZIPOutputStream gzipOutputStream = new GZIPOutputStream(fileOutputStream);
                writer = new BufferedWriter(new OutputStreamWriter(gzipOutputStream, StandardCharsets.UTF_8));
            } else if (outputPath.toLowerCase().endsWith(".xz")) {
                XZCompressorOutputStream xzOutputStream = new XZCompressorOutputStream(fileOutputStream);
                writer = new BufferedWriter(new OutputStreamWriter(xzOutputStream, StandardCharsets.UTF_8));
            } else {
                writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8));
            }

            for (String line : dataList) {
                writer.write(line);
                writer.newLine();
            }
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static InputStream getInputStreamFromOneFileMaybeCompressed(String inputFilePath)
            throws IOException {
        InputStream inputStream;
        String extension = inputFilePath.substring(inputFilePath.lastIndexOf('.') + 1);
        FileInputStream in = new FileInputStream(inputFilePath);
        switch (extension) {
            case "gz":
                inputStream = new GZIPInputStream(in);
                break;
            case "xz":
                inputStream = new XZCompressorInputStream(in);
                break;
            case "bz2":
                inputStream = new BZip2CompressorInputStream(in);
                break;
            case "zip":
                ZipInputStream zipInputStream = new ZipInputStream(in);
                ZipEntry entry = zipInputStream.getNextEntry();
                // Assuming there's only one file in the zip, and it's a text file.
                inputStream = zipInputStream;
                break;
            default:
                inputStream = in;
                break;
        }
        return inputStream;
    }

    public static BufferedWriter getBufferedWriterFromFileMaybeCompressed(String outputFilePath) throws IOException {
        OutputStream outputStream = getOutputStreamFromFileMaybeCompressed(outputFilePath);
        return new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
    }

    private static OutputStream getOutputStreamFromFileMaybeCompressed(String outputFilePath) throws IOException {
        OutputStream outputStream;
        String extension = outputFilePath.substring(outputFilePath.lastIndexOf('.') + 1);
        FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath);

        switch (extension) {
            case "gz":
                outputStream = new GZIPOutputStream(fileOutputStream);
                break;
            case "xz":
                outputStream = new XZCompressorOutputStream(fileOutputStream);
                break;
            case "bz2":
                outputStream = new BZip2CompressorOutputStream(fileOutputStream);
                break;
            case "zip":
                ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
                zipOutputStream.putNextEntry(new ZipEntry("output.txt")); // Assuming the output file name inside the zip
                outputStream = zipOutputStream;
                break;
            default:
                outputStream = fileOutputStream;
                break;
        }

        return outputStream;
    }

    public static void main(String[] args) throws IOException {

        EGPSUtil.obtainRunningTimeNewThread(() -> {
//            Optional<File> downloadFromUrl = downloadFromUrl("http://yudalang.top:8080/asserts/egps_version.json",
//                    new File("C:/temp"));

            try {
                writeListToFile(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"),
                        "C:/tmp/test.txt.xz");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }


}

/**
 * https://blog.csdn.net/wtwcsdn123/article/details/141426833
 */
class HighPerformanceReading {

    static String path = "C:\\Users\\yudal\\Documents\\project\\WntEvolution\\Archieve\\main_stream_web_resource\\NCBI_resources\\NCBI_taxonomy\\data\\taxid_changelog\\taxid-changelog.csv";
//    static String path = "C:\\Users\\yudal\\Documents\\project\\WntEvolution\\phmmer_search_324Animal\\before20250106_run\\3.targetGeneEstimation\\running_FIMO\\humanGenome_test\\upStream.sequence.human.genes.fasta";
//    static String path = "C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\申请项目\\国自然青基\\网上花钱下载的\\青年基金申请书.doc";

    public static void inputStream(Path filename) {
        try (InputStream is = Files.newInputStream(filename)) {
            int c;
            while ((c = is.read()) != -1) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void bufferedInputStream(Path filename) {
        try (BufferedInputStream br = new BufferedInputStream(Files.newInputStream(filename))) {
            int bufferSize = 100 * 1024 * 1024;
            byte[] buffer = new byte[bufferSize]; // 使用4KB的缓冲区
            int bytesRead;
            while ((bytesRead = br.read(buffer)) != -1) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void randomAccessFile(Path filename) {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(filename.toFile(), "r")) {
            for (long i = 0; i < randomAccessFile.length(); i++) {
                randomAccessFile.seek(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mappedFile(Path filename) {
        try (FileChannel fileChannel = FileChannel.open(filename)) {
            long size = fileChannel.size();
            long position = 0;
            long chunkSize = Math.min(Integer.MAX_VALUE, size); // 每次映射的最大大小为 Integer.MAX_VALUE

            while (position < size) {
                long remaining = size - position;
                long mapSize = Math.min(chunkSize, remaining);

                MappedByteBuffer mappedByteBuffer = fileChannel.map(MapMode.READ_ONLY, position, mapSize);
                for (long i = 0; i < mapSize; i++) {
                    mappedByteBuffer.get((int) i);
                }

                position += mapSize;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        EGPSUtil.obtainRunningTimeSecondBlocked(() -> {
            System.out.println("mappedFile");
            mappedFile(Paths.get(path));
        });
        System.out.println("-------------------------------------------------------");
        EGPSUtil.obtainRunningTimeSecondBlocked(() -> {
            System.out.println("bufferedInputStream");
            bufferedInputStream(Paths.get(path));
        });
        System.out.println("-------------------------------------------------------");

        EGPSUtil.obtainRunningTimeSecondBlocked(() -> {
            System.out.println("For loop one line with the Buffered file reader:");
            MutableInt count = new MutableInt();
            try {
                EGPSFileUtil.forLoopToFileMaybeCompressed(path, line -> {
                    count.increment();
                    return false;
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("count:" + count);
        });
        System.out.println("-------------------------------------------------------");

//		EGPSUtil.obtainRunningTimeSecondBlocked(() -> {
//			System.out.println("inputStream");
//			inputStream(Paths.get(path));
//		});

    }
}
