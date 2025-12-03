package os.test;

import java.util.ArrayList;
import java.util.List;

public class MemoryTest100G {
    
    public static void main(String[] args) {
        System.out.println("开始内存测试 - 目标: ~100GB");
        System.out.println("当前最大内存: " + (Runtime.getRuntime().maxMemory() / 1024 / 1024 / 1024) + "GB");
        
        try {
            // 方法1: 创建大型字节数组列表
//            testWithByteArrays();
            
            // 方法2: 创建大量字符串对象
//             testWithStrings();
            
            // 方法3: 创建大量自定义对象
             testWithCustomObjects();
            
        } catch (OutOfMemoryError e) {
            System.err.println("内存不足异常: " + e.getMessage());
            printMemoryInfo();
        }
    }
    
    /**
     * 方法1: 使用字节数组消耗内存
     */
    public static void testWithByteArrays() {
        List<byte[]> memoryConsumer = new ArrayList<>();
        
        try {
            // 每个数组100MB，创建1000个数组 = 约100GB
            int arraySize = 100 * 1024 * 1024; // 100MB
            int arrayCount = 1000; // 1000个数组
            
            System.out.println("创建 " + arrayCount + " 个 " + (arraySize/1024/1024) + "MB 的字节数组...");
            
            for (int i = 0; i < arrayCount; i++) {
                byte[] largeArray = new byte[arraySize];
                
                // 填充一些数据，防止JVM优化
                for (int j = 0; j < arraySize; j += 1000) {
                    largeArray[j] = (byte) (i % 256);
                }
                
                memoryConsumer.add(largeArray);
                
                if (i % 10 == 0) {
                    System.out.println("已创建 " + (i + 1) + " 个数组, 约消耗内存: " + 
                        ((long)(i + 1) * arraySize / 1024 / 1024 / 1024) + "GB");
                    printMemoryInfo();
                }
                
                // 短暂休眠，避免系统卡死
                Thread.sleep(100);
            }
            
            System.out.println("成功创建所有数组！总计约100GB内存");
            
            // 保持内存占用，等待用户输入
            System.out.println("按回车键释放内存并退出...");
            System.in.read();
            
        } catch (Exception e) {
            System.err.println("测试过程中发生异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 方法2: 使用字符串消耗内存
     */
    public static void testWithStrings() {
        List<String> stringList = new ArrayList<>();
        
        try {
            // 创建大量大字符串
            String baseString = "A".repeat(1024 * 1024); // 1MB字符串
            
            System.out.println("创建大量字符串对象...");
            
            for (int i = 0; i < 100000; i++) { // 约100GB
                String largeString = baseString + i;
                stringList.add(largeString);
                
                if (i % 1000 == 0) {
                    System.out.println("已创建 " + i + " 个字符串对象");
                    printMemoryInfo();
                }
            }
            
        } catch (Exception e) {
            System.err.println("字符串测试异常: " + e.getMessage());
        }
    }
    
    /**
     * 方法3: 使用自定义对象消耗内存
     */
    public static void testWithCustomObjects() {
        List<LargeObject> objectList = new ArrayList<>();
        
        try {
            System.out.println("创建大量自定义对象...");
            
            for (int i = 0; i < 1000000; i++) { // 每个对象约100KB
                LargeObject obj = new LargeObject(i);
                objectList.add(obj);
                
                if (i % 10000 == 0) {
                    System.out.println("已创建 " + i + " 个对象");
                    printMemoryInfo();
                }
            }
            
        } catch (Exception e) {
            System.err.println("对象测试异常: " + e.getMessage());
        }
    }
    
    /**
     * 打印内存使用信息
     */
    public static void printMemoryInfo() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        System.out.println("=== 内存信息 ===");
        System.out.println("最大内存: " + formatBytes(maxMemory));
        System.out.println("已分配内存: " + formatBytes(totalMemory));
        System.out.println("空闲内存: " + formatBytes(freeMemory));
        System.out.println("已使用内存: " + formatBytes(usedMemory));
        System.out.println("内存使用率: " + (usedMemory * 100 / maxMemory) + "%");
        System.out.println("================");
    }
    
    /**
     * 格式化字节数显示
     */
    public static String formatBytes(long bytes) {
        long gb = bytes / (1024 * 1024 * 1024);
        long mb = (bytes % (1024 * 1024 * 1024)) / (1024 * 1024);
        return gb + "GB " + mb + "MB";
    }
    
    /**
     * 自定义大对象类
     */
    static class LargeObject {
        private int id;
        private byte[] data;
        private String description;
        private List<Integer> numbers;
        
        public LargeObject(int id) {
            this.id = id;
            this.data = new byte[100 * 1024]; // 100KB数据
            this.description = "Large object " + id + " - " + "X".repeat(1000);
            this.numbers = new ArrayList<>();
            
            // 填充数据
            for (int i = 0; i < data.length; i++) {
                data[i] = (byte) (id % 256);
            }
            
            for (int i = 0; i < 100; i++) {
                numbers.add(id + i);
            }
        }
    }
}