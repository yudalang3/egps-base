package tsv.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Assign the key-value pairs to the corresponding fields of a bean object.
 * This is a quick way to create a bean object from a map of key-value pairs.
 * @author yudalang:
 *
 *
 */
public class BeanCreator {

	private static final Logger log = LoggerFactory.getLogger(BeanCreator.class);

	public static void main(String[] args) {
		// 假设我们有一些键值对
		Map<String, Object> keyValuePairs = new HashMap<>();
		keyValuePairs.put("name", "John Doe");
		keyValuePairs.put("age", 30);
		keyValuePairs.put("email", "johndoe@example.com");

		// 使用键值对创建一个 User 对象
		User user = createUserBean(keyValuePairs, User.class);

		// 打印 User 对象的属性
		System.out.println("User Name: " + user.getName());
		System.out.println("User Age: " + user.getAge());
		System.out.println("User Email: " + user.getEmail());
	}

	@SuppressWarnings("unchecked")
	public static <T> T createUserBean(Map<String, Object> keyValuePairs, Class<T> clazz) {
		try {
			T instance = clazz.getDeclaredConstructor().newInstance(); // 创建类的实例

			// 为每个键值对创建一个属性和 setter 方法
			for (Map.Entry<String, Object> entry : keyValuePairs.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();

				// 动态创建 setter 方法
				String setterName = "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
				String methodName = "set" + key; // 方法名
				Class<?>[] parameterTypes = new Class<?>[] { String.class }; // 参数类型
				Object[] arguments = new Object[] { value.toString() }; // 参数值

				// 通过反射调用 setter 方法
				Method setterMethod = clazz.getMethod(methodName, parameterTypes[0]);
				setterMethod.invoke(instance, arguments);
			}

			return instance;
		} catch (Exception e) {
			log.error("Please check your bean information.",e);
			return null;
		}
	}
}

// 这里定义了一个简单的 User 类
class User {
	private String name;
	private int age;
	private String email;

	// 省略其他属性的 getter 和 setter 方法

	public void setName(String name) {
		this.name = name;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public String getEmail() {
		return email;
	}

}