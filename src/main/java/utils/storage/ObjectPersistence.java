package utils.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for Java object serialization and deserialization with optional compression.
 * 
 * <p>
 * This class provides methods to persist Java objects to disk and retrieve them later.
 * It supports both standard serialization and GZIP-compressed serialization for
 * space-efficient storage of large objects.
 * </p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Standard Serialization:</strong> Basic ObjectOutputStream/ObjectInputStream</li>
 *   <li><strong>GZIP Compression:</strong> Compressed serialization for space efficiency</li>
 *   <li><strong>Generic Type Safety:</strong> Type-safe deserialization with generic methods</li>
 *   <li><strong>Error Handling:</strong> Comprehensive logging and graceful error handling</li>
 *   <li><strong>Auto Directory Creation:</strong> Parent directories created automatically for GZIP saves</li>
 * </ul>
 * 
 * <h2>Usage Examples:</h2>
 * 
 * <h3>Standard Serialization:</h3>
 * <pre>
 * // Save object
 * MyData data = new MyData();
 * boolean success = ObjectPersistence.saveObjectByObjectOutput(data, "data.ser");
 * 
 * // Load object
 * MyData loaded = ObjectPersistence.getObjectByObjectInput("data.ser");
 * </pre>
 * 
 * <h3>Compressed Serialization:</h3>
 * <pre>
 * // Save with compression
 * LargeObject largeObj = new LargeObject();
 * boolean success = ObjectPersistence.saveObjectByObjectOutputWithGZIP(largeObj, "data.ser.gz");
 * 
 * // Load compressed object
 * LargeObject loaded = ObjectPersistence.getObjectByObjectInputWithGZIP("data.ser.gz");
 * </pre>
 * 
 * <h2>File Naming Conventions:</h2>
 * <ul>
 *   <li><strong>Standard:</strong> {@code .ser} extension (e.g., {@code config.ser})</li>
 *   <li><strong>Compressed:</strong> {@code .ser.gz} extension (e.g., {@code data.ser.gz})</li>
 * </ul>
 * 
 * <h2>Performance Considerations:</h2>
 * <ul>
 *   <li><strong>Standard:</strong> Faster I/O, larger file size</li>
 *   <li><strong>Compressed:</strong> Slower I/O due to compression, smaller file size</li>
 *   <li><strong>Best Use:</strong> Use compression for objects > 1MB or when disk space is limited</li>
 * </ul>
 * 
 * <h2>Important Notes:</h2>
 * <ul>
 *   <li>Objects must implement {@code Serializable} interface</li>
 *   <li>Class definitions must match between save and load operations</li>
 *   <li>No serialVersionUID management - use for temporary storage only</li>
 *   <li>Method returns null on deserialization failure (check for null results)</li>
 * </ul>
 * 
 * <h2>Error Handling:</h2>
 * <p>
 * All methods use try-with-resources for proper resource management and log
 * exceptions with SLF4J. Failed operations return {@code false} (save) or {@code null} (load).
 * </p>
 * 
 * @author eGPS Development Team
 * @version 1.0
 * @since 1.0
 * @see ObjectOutputStream
 * @see ObjectInputStream
 * @see GZIPOutputStream
 * @see GZIPInputStream
 */
public class ObjectPersistence {

	private static final Logger log = LoggerFactory.getLogger(ObjectPersistence.class);

	/**
	 * File usually end with .ser
	 * @param o
	 * @param file
	 * @return
	 */
	public static boolean saveObjectByObjectOutput(Object o, String file) {
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
			objectOutputStream.writeObject(o);
			objectOutputStream.close();
		} catch (IOException e) {
			log.error("Map persistence", e);
			return false;
		}

		return true;
	}

	public static boolean saveObjectByObjectOutputWithGZIP(Object o, String file) {
		try {

			File file2 = new File(file);
			if (!file2.exists()) {
				FileUtils.createParentDirectories(file2);
			}

			FileOutputStream fos = new FileOutputStream(file2);
			GZIPOutputStream gz = new GZIPOutputStream(fos);

			ObjectOutputStream oos = new ObjectOutputStream(gz);

			oos.writeObject(o);
			oos.close();
		} catch (IOException ex) {
			log.error("Map persistence", ex);
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getObjectByObjectInput(String file) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			Object o = ois.readObject();
			return (T) o; // 使用强制类型转换
		} catch (Exception ex) {
			log.error("Map persistence", ex);
		}

		return null;

	}

	@SuppressWarnings("unchecked")
	public static <T> T getObjectByObjectInputWithGZIP(String file) {
		try (GZIPInputStream gis = new GZIPInputStream(new FileInputStream(file));
				ObjectInputStream ois = new ObjectInputStream(gis)) {
			Object o = ois.readObject();
			return (T) o; // 使用强制类型转换
		} catch (Exception ex) {
			log.error("Map persistence", ex);
		}

		return null;
	}

}
