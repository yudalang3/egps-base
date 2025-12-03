package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSON;

/**
 * Utility class for Java object serialization and persistence.
 * 
 * <p>
 * This class provides methods for saving and loading Java objects using two approaches:
 * <ul>
 *   <li><strong>Java Serialization:</strong> Standard ObjectOutputStream/ObjectInputStream</li>
 *   <li><strong>JSON Serialization:</strong> FastJSON library for human-readable format</li>
 * </ul>
 * </p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Quick Object Persistence:</strong> Save/load objects without serialVersionUID management</li>
 *   <li><strong>JSON Support:</strong> Human-readable configuration files</li>
 *   <li><strong>Simple API:</strong> One-line save and load operations</li>
 *   <li><strong>Generic Type Support:</strong> Type-safe JSON deserialization</li>
 * </ul>
 * 
 * <h2>Usage Examples:</h2>
 * 
 * <h3>Java Serialization:</h3>
 * <pre>
 * // Save object
 * MyData data = new MyData();
 * EGPSObjectsUtil.quickSaveAnObject2file(data, new File("data.ser"));
 * 
 * // Load object
 * MyData loaded = (MyData) EGPSObjectsUtil.quickObtainAnObjectFromFile(new File("data.ser"));
 * </pre>
 * 
 * <h3>JSON Serialization:</h3>
 * <pre>
 * // Save as JSON
 * Configuration config = new Configuration();
 * EGPSObjectsUtil.persistentSaveJavaBeanByFastaJSON(config, new File("config.json"));
 * 
 * // Load from JSON
 * Configuration loaded = EGPSObjectsUtil.obtainJavaBeanByFastaJSON(
 *     Configuration.class, 
 *     new File("config.json")
 * );
 * </pre>
 * 
 * <h2>Use Cases:</h2>
 * <ul>
 *   <li>Save/load application configuration</li>
 *   <li>Cache computed results</li>
 *   <li>Persist analysis parameters</li>
 *   <li>Quick prototyping without database</li>
 * </ul>
 * 
 * <h2>Important Notes:</h2>
 * <ul>
 *   <li>Java serialization: Objects must implement Serializable</li>
 *   <li>JSON serialization: Works best with JavaBeans (getters/setters)</li>
 *   <li>Not recommended for production-critical data (no version control)</li>
 *   <li>Use for temporary storage and quick prototyping</li>
 * </ul>
 * 
 * @author eGPS Development Team
 * @version 1.0
 * @since 1.7
 * @see ObjectOutputStream
 * @see ObjectInputStream
 * @see JSON
 */
public class EGPSObjectsUtil {

	private EGPSObjectsUtil() {

	}

	/**
	 * Quickly saves a Java object to a file using Java serialization.
	 * 
	 * <p>
	 * <strong>Warning:</strong> This method does not manage serialVersionUID.
	 * Use only for temporary storage, not for long-term persistence.
	 * </p>
	 * 
	 * @param obj the object to save (must be Serializable)
	 * @param file the target file
	 * @throws IOException if an I/O error occurs
	 */
	public static void quickSaveAnObject2file(Object obj, File file) throws IOException {
		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));
		os.writeObject(obj);
		os.close();
	}

	/**
	 * Quickly loads a Java object from a file using Java deserialization.
	 * 
	 * <p>
	 * <strong>Warning:</strong> This method does not manage serialVersionUID.
	 * The class definition must match the one used during serialization.
	 * </p>
	 * 
	 * @param file the source file
	 * @return the deserialized object
	 * @throws IOException if an I/O error occurs
	 * @throws ClassNotFoundException if the class of the serialized object cannot be found
	 */
	public static Object quickObtainAnObjectFromFile(File file) throws IOException, ClassNotFoundException {
		ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
		Object readObject = input.readObject();
		input.close();
		return readObject;
	}

	/**
	 * Saves a JavaBean to a JSON file using FastJSON.
	 * 
	 * <p>
	 * The resulting JSON file is human-readable and can be edited manually.
	 * Ideal for configuration files.
	 * </p>
	 * 
	 * @param object the JavaBean object to save
	 * @param outFile the target JSON file
	 * @throws IOException if an I/O error occurs
	 */
	public static void persistentSaveJavaBeanByFastaJSON(Object object, File outFile) throws IOException {
		String jsonString = JSON.toJSONString(object);
		FileUtils.writeStringToFile(outFile, jsonString);
		
	}

	/**
	 * Loads a JavaBean from a JSON file using FastJSON.
	 * 
	 * <p>
	 * Type-safe deserialization that returns the object as the specified class type.
	 * </p>
	 * 
	 * @param <T> the type of the JavaBean
	 * @param clz the class of the JavaBean to deserialize
	 * @param outFile the source JSON file
	 * @return the deserialized JavaBean instance
	 * @throws IOException if an I/O error occurs
	 */
	public static <T> T obtainJavaBeanByFastaJSON(Class<T> clz, File outFile) throws IOException {
		String readFileToString = FileUtils.readFileToString(outFile);
		return JSON.parseObject(readFileToString, clz);
	}
}
