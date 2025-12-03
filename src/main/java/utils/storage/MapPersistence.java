package utils.storage;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Utility class for Map persistence with support for both serialization and JSON formats.
 * 
 * <p>
 * This class provides convenient methods for storing and retrieving Map objects
 * with different data types and storage formats. It supports compressed serialization
 * for space efficiency and JSON format for human-readable configuration files.
 * </p>
 * 
 * <h2>Supported Map Types:</h2>
 * <ul>
 *   <li><strong>String to Integer:</strong> {@code Map<String, Integer>}</li>
 *   <li><strong>String to String:</strong> {@code Map<String, String>}</li>
 * </ul>
 * 
 * <h2>Storage Formats:</h2>
 * <ul>
 *   <li><strong>GZIP Serialization:</strong> Compressed binary format for space efficiency</li>
 *   <li><strong>JSON:</strong> Human-readable text format for configuration files</li>
 * </ul>
 * 
 * <h2>Usage Examples:</h2>
 * 
 * <h3>String to Integer Map:</h3>
 * <pre>
 * // Create and store a String->Integer map
 * Map&lt;String, Integer&gt; counts = new HashMap&lt;&gt;();
 * counts.put("species_a", 150);
 * counts.put("species_b", 75);
 * counts.put("species_c", 200);
 * 
 * // Store with compression
 * MapPersistence.storeStr2numberMap(counts, "/path/to/species_counts.ser.gz");
 * 
 * // Load from file
 * Map&lt;String, Integer&gt; loaded = MapPersistence.getStr2numberMap("/path/to/species_counts.ser.gz");
 * </pre>
 * 
 * <h3>String to String Map:</h3>
 * <pre>
 * // Create and store a String->String map
 * Map&lt;String, String&gt; config = new HashMap&lt;&gt;();
 * config.put("database_url", "jdbc:mysql://localhost:3306/bio");
 * config.put("username", "researcher");
 * config.put("output_format", "fasta");
 * 
 * // Store as JSON (human-readable)
 * MapPersistence.saveStr2StrMapToJSON(config, "/path/to/config.json");
 * 
 * // Load from JSON
 * Map&lt;String, String&gt; loadedConfig = MapPersistence.getStr2StrMapFromJSON("/path/to/config.json");
 * </pre>
 * 
 * <h2>Use Cases:</h2>
 * <ul>
 *   <li><strong>Species Counting:</strong> Store species occurrence counts from phylogenetic analysis</li>
 *   <li><strong>Configuration Management:</strong> Save application settings in JSON format</li>
 *   <li><strong>Caching:</strong> Cache computed results for faster subsequent processing</li>
 *   <li><strong>Data Persistence:</strong> Store intermediate results between processing steps</li>
 * </ul>
 * 
 * <h2>Performance Characteristics:</h2>
 * <ul>
 *   <li><strong>Serialization:</strong> Faster I/O, compressed storage, binary format</li>
 *   <li><strong>JSON:</strong> Slower I/O, larger files, human-readable and editable</li>
 *   <li><strong>Auto-creation:</strong> Empty maps returned when files don't exist</li>
 * </ul>
 * 
 * <h2>Error Handling:</h2>
 * <p>
 * All methods handle errors gracefully:
 * </p>
 * <ul>
 *   <li>Missing files return empty HashMap instances</li>
 *   <li>Deserialization errors are logged and return empty maps</li>
 *   <li>JSON operations throw IOException for explicit error handling</li>
 * </ul>
 * 
 * <h2>File Naming Recommendations:</h2>
 * <ul>
 *   <li><strong>Serialized:</strong> {@code .ser.gz} extension (e.g., {@code counts.ser.gz})</li>
 *   <li><strong>JSON:</strong> {@code .json} extension (e.g., {@code config.json})</li>
 * </ul>
 * 
 * @author yudal
 * @version 1.0
 * @since 1.0
 * @see ObjectPersistence For underlying serialization operations
 * @see JSONObject For JSON serialization operations
 * @see FileUtils For file I/O operations
 */
public class MapPersistence {

    private static final Logger log = LoggerFactory.getLogger(MapPersistence.class);

    /**
     * 将一个 String 到 Integer 的 Map 持久化存储到指定路径。
     * 使用 GZIP 压缩和对象序列化的方式保存数据。
     *
     * @param map   要存储的 Map 数据
     * @param gpath 存储文件的路径
     */
    public static void storeStr2numberMap(Map<String, Integer> map, String gpath) {
        ObjectPersistence.saveObjectByObjectOutputWithGZIP(map, gpath);
    }

    /**
     * 从指定路径加载一个 String 到 Integer 的 Map。
     * 如果文件不存在，则返回一个空的 HashMap。
     *
     * @param path 存储文件的路径
     * @return 加载的 Map 数据，如果文件不存在或读取失败，则返回空的 HashMap
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Integer> getStr2numberMap(String path) {
        File storePathFile = new File(path);
        if (!storePathFile.exists()) {
            return new HashMap<>();
        }

        try {
            GZIPInputStream gis = new GZIPInputStream(new FileInputStream(path));
            ObjectInputStream ois = new ObjectInputStream(gis);
            Object o = ois.readObject();
            ois.close();
            return (Map<String, Integer>) o;
        } catch (Exception ex) {
            log.error("Map persistence", ex);
        }

        return new HashMap<>();
    }

    /**
     * 将一个 String 到 String 的 Map 持久化存储到指定路径。
     * 使用 GZIP 压缩和对象序列化的方式保存数据。
     *
     * @param map   要存储的 Map 数据
     * @param gpath 存储文件的路径
     */
    public static void storeStr2strMap(Map<String, String> map, String gpath) {
        ObjectPersistence.saveObjectByObjectOutputWithGZIP(map, gpath);
    }

    /**
     * 从指定路径加载一个 String 到 String 的 Map。
     * 如果文件不存在，则返回一个空的 HashMap。
     *
     * @param path 存储文件的路径
     * @return 加载的 Map 数据，如果文件不存在或读取失败，则返回空的 HashMap
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getStr2strMap(String path) {
        File storePathFile = new File(path);
        if (!storePathFile.exists()) {
            return new HashMap<>();
        }

        try {
            GZIPInputStream gis = new GZIPInputStream(new FileInputStream(path));
            ObjectInputStream ois = new ObjectInputStream(gis);
            Object o = ois.readObject();
            ois.close();
            return (Map<String, String>) o;
        } catch (Exception ex) {
            log.error("Map persistence", ex);
        }

        return new HashMap<>();
    }

    /**
     * 将一个 String 到 String 的 Map 以 JSON 格式保存到指定路径。
     *
     * @param map   要存储的 Map 数据
     * @param path  存储文件的路径
     * @throws IOException 如果写入文件时发生 I/O 错误
     */
    public static void saveStr2StrMapToJSON(Map<String, String> map, String path) throws IOException {
        String jsonString = JSONObject.toJSONString(map, false);
        FileUtils.writeStringToFile(new File(path), jsonString, StandardCharsets.UTF_8);
    }

    /**
     * 从指定路径加载一个以 JSON 格式存储的 String 到 String 的 Map。
     *
     * @param path 存储文件的路径
     * @return 加载的 Map 数据
     * @throws IOException 如果读取文件时发生 I/O 错误
     */
    public static HashMap<String, String> getStr2StrMapFromJSON(String path) throws IOException {
        String fileToString = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8);
        @SuppressWarnings("unchecked")
        HashMap<String, String> object = JSONObject.parseObject(fileToString, HashMap.class);
        return object;
    }

}
