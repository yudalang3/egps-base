# eGPS-Base 包文档 - 第4部分：工具类与辅助函数

## 概述

本文档涵盖工具类包，为文件操作、字符串处理、集合、日期/时间处理等提供通用辅助函数。

---

## 1. 通用工具

### 1.1 `utils` - 通用工具

**用途：** 整个项目中使用的常见工具函数

#### 关键类：

**`EGPSFileUtil`** (类)
- **功能：** 全面的文件操作
- **关键特性：**
  - 文件复制（基于流、基于通道）
  - 压缩/解压缩（GZ、BZ2、XZ）
  - 递归文件列表
  - 网络文件下载
  - 自动压缩检测
- **关键方法：**
  - `copyFileUsingStream()`: 复制文件
  - `getInputStreamFromOneFileMaybeCompressed()`: 打开压缩文件
  - `downloadFromUrl()`: 从 URL 下载
  - `getListFiles()`: 递归文件列表
- **重要性：** ⭐⭐⭐⭐⭐ (文件处理必需)

**`EGPSGeneralUtil`** (类)
- **功能：** 杂项辅助方法
- **关键特性：**
  - 可视化的颜色插值
  - 数字格式化（3、6、9 位小数）
  - 集合到文件的转换
  - 系统属性访问
  - 生物学数据库 URL 常量
- **关键常量：**
  - `NCBI_PROTEIN`、`NCBI_NUCCORE`: NCBI 数据库 URL
  - `UNIPROT_KB`: UniProt URL
  - `ZERO_DIFF`: 浮点比较容差 (1.0E-9)
  - `FORMATTER_6`、`FORMATTER_9`: 小数格式化器
- **关键方法：**
  - `calcColor()`: 颜色插值
  - `collection2file()`: 将集合写入文件
  - `collapseWhiteSpace()`: 字符串规范化
- **重要性：** ⭐⭐⭐⭐ (广泛使用的工具)

**`EGPSListUtil`** (类)
- **功能：** 列表操作和分析
- **关键特性：**
  - 元素频率计数
  - 列表分区/分块
  - 集合操作（交集、差集）
  - 包含百分比计算
- **关键方法：**
  - `countString()`: 优化的频率计数（1000万+ 元素）
  - `countSameComponents()`: 简单频率计数
  - `partitionList()`: 拆分为块
  - `intersection()`、`difference()`、`subtract()`: 集合操作
- **性能：** 针对大列表优化
- **重要性：** ⭐⭐⭐⭐ (集合操作)

**`EGPSUtil`** (类)
- **功能：** 通用 eGPS 系统工具
- **关键特性：**
  - JVM 内存监控
  - 性能计时（Guava Stopwatch）
  - 浏览器集成
  - 剪贴板回退
- **关键方法：**
  - `getAlreadyUsedJVMMemory()`: 获取内存使用（MB）
  - `printUsedJVMMemory()`: 打印内存到控制台
  - `obtainRunningTimeNewThread()`: 计时执行（新线程）
  - `obtainRunningTimeSecondBlocked()`: 计时执行（阻塞）
  - `openUrlInBrowser()`: 在浏览器中打开 URL
- **重要性：** ⭐⭐⭐⭐ (性能与监控)

**`EGPSObjectCounter<T>`** (类)
- **功能：** 用于跟踪对象出现次数的通用计数器
- **关键特性：**
  - 支持任何对象的泛型类型
  - 保持插入顺序（LinkedHashMap）
  - 使用 MutableInt 高效计数（无装箱/拆箱）
  - 按计数排序（升序）
- **关键方法：**
  - `addOneEntry(T)`: 添加/增加计数
  - `printWithOriginalOrder()`: 按插入顺序打印
  - `printSortedResults()`: 按计数排序打印
  - `getCounterMap()`: 获取 Map<T, MutableInt>
- **用例：**
  - 统计物种出现次数
  - 氨基酸频率统计
  - 结构域类型计数
- **重要性：** ⭐⭐⭐⭐ (频率计数)

**`EGPSObjectsUtil`** (类)
- **功能：** Java 对象序列化和持久化
- **关键特性：**
  - Java 序列化（ObjectOutputStream/ObjectInputStream）
  - JSON 序列化（FastJSON）
  - 无需 serialVersionUID 的快速保存/加载
  - 类型安全的 JSON 反序列化
- **关键方法：**
  - `quickSaveAnObject2file()`: 保存对象
  - `quickObtainAnObjectFromFile()`: 加载对象
  - `persistentSaveJavaBeanByFastaJSON()`: 保存为 JSON
  - `obtainJavaBeanByFastaJSON()`: 从 JSON 加载
- **用例：**
  - 保存/加载配置
  - 缓存计算结果
- **重要性：** ⭐⭐⭐⭐ (对象持久化)

**`EGPSGuiUtil`** (类)
- **功能：** 用于开发的简单 GUI/CLI 接口
- **关键特性：**
  - 带按钮的快速 GUI IDE
  - 带命令的 CLI IDE（t/q）
  - 窗口位置/大小持久化
  - 线程安全操作
- **关键方法：**
  - `universalSimplestIDE(Runnable)`: GUI 接口
  - `universalSimplestCLIIDE(Runnable)`: CLI 接口
- **用例：**
  - 迭代开发
  - 算法测试
- **重要性：** ⭐⭐⭐ (开发工具)

**`EGPSFormatUtil`** (类)
- **功能：** 数字和 HTML 内容的格式化工具
- **关键特性：**
  - 数字的千位分隔符格式化
  - HTML 字体大小标签生成
  - 线程安全的格式化操作
  - 本地化数字格式支持
- **关键方法：**
  - `addThousandSeparatorForInteger(int)`: 添加千位分隔符
  - `html32Concat(int, String)`: 创建 HTML 字体标签
- **用法示例：**
  ```java
  // 格式化大数字
  String formatted = EGPSFormatUtil.addThousandSeparatorForInteger(1234567);
  // 结果："1,234,567"
  
  // 创建 HTML 内容
  String html = EGPSFormatUtil.html32Concat(5, "Hello");
  // 结果："<html><font size="5">Hello</font></html>"
  ```
- **用例：**
  - GUI 组件中的数字显示
  - HTML 报告生成
  - 数据可视化格式化
- **重要性：** ⭐⭐⭐ (格式化工具)

---

## 2. 字符串工具

### 2.1 `utils.string` - 字符串工具

**用途：** 专门用于生物信息学的字符串操作

#### 关键类：

**`EGPSStringUtil`** (类)
- **功能：** 针对生物学数据优化的全面字符串操作
- **关键特性：**
  - 高性能字符串拆分（单字符分隔符优化）
  - 矩阵转置（2D 字符串列表）
  - 括号验证（圆括号、花括号、方括号）
  - 查找共同前缀/后缀
  - 从混合字符串中提取数字
  - 字节数组行解析（US-ASCII）
  - Log4j 风格的 {} 字符串格式化
- **关键方法：**
  - `split(String, char)`: 快速单字符分隔符拆分
  - `split(String, char, int)`: 固定大小拆分
  - `splitByTab(String)`: 制表符分隔拆分
  - `transpose(List<List<String>>)`: 转置列到行
  - `validateStringAccording2bracks(String)`: 括号匹配
  - `getCommonPrefix(String, String)`: 查找共同前缀
  - `getCommonTail(String, String)`: 查找共同后缀
  - `getNumInString(String)`: 提取数字
  - `getStringAfterEqualChar(String)`: 解析 key=value
  - `fillString(char, int)`: 重复字符
  - `getLinesFromByteArray(byte[])`: 从字节解析行
  - `format(String, Object...)`: 参数化格式化
- **性能优化：**
  - 单字符分隔符：比正则表达式快
  - 基于栈的括号匹配：O(n)
  - 基于消费者的流式拆分
- **用例：**
  - 解析 TSV/CSV 文件
  - 验证 Newick 树格式
  - 提取样本 ID
  - 构建格式化路径
- **重要性：** ⭐⭐⭐⭐⭐ (文本处理必需)

**`StringCounter`** (类)
- **功能：** 字符串频率计数器
- **关键特性：**
  - 使用 MutableInt 高效计数
  - 基于 HashMap 的存储
  - 按键排序输出
- **关键方法：**
  - `addOneEntry(String)`: 添加/增加计数
  - `printWithOriginalOrder()`: 打印未排序
  - `printSortedResults()`: 按字母顺序排序打印
  - `getCounterMap()`: 获取 Map<String, MutableInt>
  - `clear()`: 重置计数器
- **用例：**
  - 统计物种名称
  - 统计密码子频率
  - 跟踪分类等级
- **重要性：** ⭐⭐⭐ (字符串计数)

---

## 3. 日期/时间工具

### 3.1 `utils.datetime` - 日期/时间工具

**用途：** 带格式验证的日期和时间操作

#### 关键类：

**`DateTimeOperator`** (类)
- **功能：** 日期/时间格式化和验证
- **关键特性：**
  - 固定格式："yyyy-MM-dd HH:mm:ss"
  - 当前时间戳生成
  - 格式验证（检测用户修改）
- **关键方法：**
  - `getCurrentTime()`: 获取格式化的当前时间
  - `isStillUseProgramGeneratedString(String)`: 验证格式
- **用例：**
  - 为日志生成时间戳
  - 检测手动编辑的时间戳
  - 跟踪分析执行时间
- **重要性：** ⭐⭐⭐ (时间处理)

**`EGPSTimeUtil`** (类)
- **功能：** 在 Date 和 LocalDate 对象之间转换的工具类
- **关键特性：**
  - Date 到 LocalDate 转换
  - LocalDate 到 Date 转换
  - 批量列表转换
  - 系统默认时区处理
- **关键方法：**
  - `date2LocalDate(Date)`: 将 Date 转换为 LocalDate
  - `localDate2Date(LocalDate)`: 将 LocalDate 转换为 Date
  - `dateList2localDateList(List<Date>)`: 批量 Date 到 LocalDate
  - `localDateList2dateList(List<LocalDate>)`: 批量 LocalDate 到 Date
- **用例：**
  - 现代 Java 8 时间 API 集成
  - 旧版 Date 对象兼容性
  - 时区转换
- **重要性：** ⭐⭐⭐⭐ (日期/时间转换)

**重要性：** ⭐⭐⭐⭐ (日期/时间工具)

---

## 4. CLI 工具

### 4.1 `cli.tools` - 命令行工具

**用途：** 常见任务的命令行工具

#### 关键类：

**`ZzzModuleSignature`** (类)
- **功能：** 命令行工具的模块签名
- **关键特性：**
  - CLI 工具框架集成
  - 命令行界面管理
  - 工具发现和组织
- **描述：** 命令行工具模块，为系统操作、数据处理和生物信息学工作流提供全面的工具
- **重要性：** ⭐⭐⭐ (CLI 框架)

**`CheckNwkFormat`** (工具)
- **功能：** 验证 Newick 树格式
- **用例：** 验证树文件完整性
- **用法：** `java CheckNwkFormat /path/to/tree.nwk`
- **重要性：** ⭐⭐⭐ (格式验证)

**`ClipboardPathNormalized`** (工具)
- **功能：** 将剪贴板中的 Windows 路径转换为 Unix 格式
- **关键特性：**
  - 从系统剪贴板读取
  - 将反斜杠 (\) 替换为正斜杠 (/)
  - 将规范化路径写回剪贴板
- **用例：** 跨平台路径处理
- **用法：** `java ClipboardPathNormalized`（无参数）
- **示例：** `C:\data\file.txt` → `C:/data/file.txt`
- **重要性：** ⭐⭐⭐⭐ (开发者生产力)

**`CountFilesWithSuffix`** (工具)
- **功能：** 按扩展名统计文件
- **用例：** 文件系统统计
- **重要性：** ⭐⭐⭐ (批处理操作)

**`ListFilesWithSuffix`** (工具)
- **功能：** 生成具有特定扩展名的文件的 TSV
- **用例：** 批量文件处理

**`RemoveInternalNodeNames`** (工具)
- **功能：** 从树中删除内部节点名称
- **用例：** 清理树文件

**重要性：** ⭐⭐⭐ (自动化工具)

---

## 5. GUI 工具

### 5.1 `gui.simple.tools` - 简单 GUI 工具

**用途：** 用于常见操作的简单图形用户界面工具

#### 关键类：

**`FilePathNormalizedGUI`** (类)
- **功能：** 用于从剪贴板规范化文件路径的 GUI 工具
- **关键特性：**
  - 基于 Swing 的 GUI 界面
  - 一键路径规范化
  - 剪贴板集成
  - 通过文本区域提供用户反馈
- **关键组件：**
  - 用于规范化操作的 JButton
  - 用于状态显示的 JTextArea
  - 与 ClipboardPathNormalized 集成
- **用例：**
  - 跨平台开发
  - 路径格式转换
  - 基于剪贴板的工作流
- **重要性：** ⭐⭐⭐ (GUI 便利工具)

**`ZzzModuleSignature`** (类)
- **功能：** 简单 GUI 工具的模块签名
- **描述：** 简单 GUI 工具模块，为常见操作提供用户友好的界面
- **重要性：** ⭐⭐⭐ (模块元数据)

**重要性：** ⭐⭐⭐ (GUI 工具)

---

## 6. 统一输出

### 6.1 `unified.output` - 统一接口

**用途：** 跨模块操作的统一接口

#### 关键类：

**`ZzzModuleSignature`** (类)
- **功能：** 统一输出接口的模块签名
- **描述：** 统一输出模块，为跨模块数据交换和报告提供标准化接口
- **重要性：** ⭐⭐⭐ (模块元数据)

**子包：**
- `output`: 统一输出接口和实现

**关键特性：**
- 数据交换的标准接口
- 模块互操作性
- 一致的输出格式化

**重要性：** ⭐⭐⭐ (集成)

---

## 包重要性总结

### ⭐⭐⭐⭐⭐ 关键（必用）：
1. `utils` (EGPSFileUtil) - 文件工具必需
2. `utils.string` (EGPSStringUtil) - 文本处理必需

### ⭐⭐⭐⭐ 重要（常用）：
1. `utils` (EGPSListUtil、EGPSUtil、EGPSObjectCounter) - 集合操作
2. `utils` (EGPSGeneralUtil、EGPSObjectsUtil) - 通用工具
3. `utils.datetime` (EGPSTimeUtil) - 日期/时间转换
4. `cli.tools` (ClipboardPathNormalized) - 开发者生产力

### ⭐⭐⭐ 有用（专门）：
1. `utils` (EGPSGuiUtil、EGPSFormatUtil) - GUI 和格式化
2. `utils.string` (StringCounter) - 字符串计数
3. `utils.datetime` (DateTimeOperator) - 时间处理
4. `cli.tools` (其他工具) - 自动化
5. `gui.simple.tools` - GUI 工具
6. `unified.output` - 集成

---

## 典型使用模式

### 模式 1：文件处理
```
1. 列出文件：EGPSFileUtil.getListFiles()
2. 打开压缩文件：getInputStreamFromOneFileMaybeCompressed()
3. 处理数据
4. 保存结果：collection2file()
```

### 模式 2：字符串操作
```
1. 拆分数据：EGPSStringUtil.split()
2. 提取数字：getNumInString()
3. 验证格式：validateStringAccording2bracks()
4. 统计出现次数：StringCounter.addOneEntry()
```

### 模式 3：性能监控
```
1. 启动计时器：EGPSUtil.obtainRunningTimeSecondBlocked()
2. 运行分析
3. 检查内存：getAlreadyUsedJVMMemory()
4. 记录性能
```

### 模式 4：对象持久化
```
1. 计算结果
2. 保存到文件：EGPSObjectsUtil.quickSaveAnObject2file()
3. 稍后：从文件加载：quickObtainAnObjectFromFile()
```

---

*本文档涵盖工具类和辅助函数包。其他部分见核心结构、文件 I/O、解析器和可视化。*
