package cli.tools;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import top.signature.IModuleSignature;
import org.reflections.Reflections;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import top.signature.IModuleSignature;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Comparator;

public class SeeModulesWeHave {
        static final String formatStr = "%-40s, %-50s, %-200s\n";
        public static void main(String[] args) {
            // 使用 ClasspathHelper 获取整个类路径
            Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forJavaClassPath())
                    .addScanners(Scanners.SubTypes));

            Set<Class<? extends IModuleSignature>> subTypes = reflections.getSubTypesOf(IModuleSignature.class);
            List<Class<? extends IModuleSignature>> list = new ArrayList<>(subTypes);
            // Sort the list according to the class name
            list.sort(Comparator.comparing(Class::getName));

            System.out.printf("Dalang, You have the following %d modules:\n----------------------------------------------------------------\n", subTypes.size());
            System.out.printf(formatStr, "Path", "Name", "Desc");
            for (Class<? extends IModuleSignature> clazz : list) {
                // In fact, it is not needed to check if it is an interface or abstract class, for we get the subtypes of IModuleSignature
                if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())){
                    System.out.println("Abstract class: " + clazz.getName());
                    continue;
                }
                printInfo(clazz);
            }


        }

        private static void printInfo(Class<? extends IModuleSignature> clazz) {
            try {
                // 检查是否是接口或抽象类，避免无法实例化的情况

                if (!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
                    // 创建实例
                    IModuleSignature instance = clazz.getDeclaredConstructor().newInstance();
                    System.out.printf(formatStr, clazz.getName(), instance.getTabName(), instance.getShortDescription());
                    // 调用方法
                }
            } catch (Exception e) {
                System.err.println("Failed to instantiate or use class " + clazz.getName() + ": " + e.getMessage());
            }
        }
    }