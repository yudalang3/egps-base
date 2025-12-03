# eGPS-Base 包文档 - 第1部分：核心结构与数据模型

## 概述

本文档涵盖了为 eGPS-Base 提供核心数据结构、模块框架和树基础设施的基础包。

---

## 1. 核心包

### 1.1 `top.signature` - 模块签名系统

**用途：** 模块化架构的基础

#### 关键类：

**`IModuleSignature`** (接口)
- **功能：** 定义系统中所有模块的契约
- **关键方法：**
  - `getShortDescription()`: 返回模块功能描述
  - `getTabName()`: 返回GUI集成的显示名称
- **用法：** 每个模块通过 `ZzzModuleSignature` 类实现此接口
- **重要性：** ⭐⭐⭐⭐⭐ (核心架构组件)

---

## 2. 数据结构包

### 2.1 `evoltree.struct` - 树数据结构

**用途：** 用于系统发育分析的通用树数据结构

#### 关键类：

**`EvolNode`** (接口)
- **功能：** 定义所有树节点操作的顶层接口
- **关键特性：**
  - 父子关系管理（上行链接和下行链接）
  - 分支长度支持
  - 节点命名和标识
  - 树遍历支持
- **方法：** 30+ 方法用于全面的树操作
- **重要性：** ⭐⭐⭐⭐⭐ (所有树操作的基础)

**`ArrayBasedNode`** (类)
- **功能：** 基于数组的树节点实现
- **关键特性：**
  - O(1) 随机访问子节点
  - 使用 ArrayList 动态调整大小
  - 推荐用于生产环境
- **性能：** 快速的子节点访问，较高的内存开销
- **重要性：** ⭐⭐⭐⭐⭐ (主要树实现)

**`LinkedBasedNode`** (类)
- **功能：** 基于链表的树节点实现
- **关键特性：**
  - 首子/下一兄弟表示法
  - 每个节点较低的内存占用
  - O(n) 子节点访问
- **用例：** 教育目的、内存受限环境
- **重要性：** ⭐⭐⭐ (替代实现)

**`TreeCoder`** (接口)
- **功能：** 将树结构编码为字符串的接口
- **关键方法：** `code(EvolNode node)` → String
- **重要性：** ⭐⭐⭐⭐ (序列化)

**`TreeDecoder`** (接口)
- **功能：** 将字符串解码为树结构的接口
- **关键方法：** `decode(String)` → EvolNode
- **重要性：** ⭐⭐⭐⭐ (反序列化)

**`AbstractNodeCoderDecoder<S>`** (抽象类)
- **功能：** 树节点编码/解码操作的抽象基类
- **关键特性：**
  - 序列化框架的模板方法模式
  - 统一的数字格式化（6位小数，HALF_UP 舍入）
  - 支持叶节点和内部节点处理
  - 无反射的高效节点创建
- **关键方法：**
  - `codeNode(S node)`: 将节点编码为字符串
  - `parseNode(S node, String str)`: 将字符串解析为节点
  - `createNode()`: 创建新节点实例（抽象）
  - `convertRate2Decimal(double value)`: 格式化数值
- **设计模式：** 模板方法 - 定义算法骨架，子类实现细节
- **用例：**
  - Newick 格式叶节点处理
  - 内部节点编码/解码
  - 自定义树格式实现
- **性能：** 针对大型树优化，避免反射开销
- **重要性：** ⭐⭐⭐⭐ (树序列化基础)

---

### 2.2 `evoltree.phylogeny` - 系统发育树

**用途：** 专门的系统发育树操作和 Newick 格式支持

#### 关键类：

**`ZzzModuleSignature`** (类)
- **功能：** 系统发育树操作的模块签名
- **关键特性：**
  - Newick 格式解析和生成
  - 系统发育树序列化
  - 节点和分支元数据处理
- **描述：** 系统发育树解析工具，用于处理 Newick 格式文件并执行基于树的分析操作
- **重要性：** ⭐⭐⭐⭐⭐ (树 I/O 必需)

**`DefaultPhyNode`** (类)
- **功能：** 标准系统发育节点实现
- **关键特性：**
  - 完整的系统发育元数据支持
  - 分支长度
  - Bootstrap 值
- **重要性：** ⭐⭐⭐⭐ (标准系统发育操作)

**`PhyloTreeEncoderDecoder`** (类)
- **功能：** 完整的树序列化/反序列化
- **支持格式：** Newick (NWK) 格式
- **关键特性：**
  - 将树编码为 Newick 字符串
  - 将 Newick 字符串解析为树
- **关键方法：**
  - `encode(DefaultPhyNode)`: 树到 Newick 字符串
  - `decode(String)`: Newick 字符串到树
- **重要性：** ⭐⭐⭐⭐⭐ (树 I/O 必需)

**`NWKInternalCoderDecoder`** (类)
- **功能：** 处理 Newick 格式的内部节点
- **关键特性：**
  - 解析节点名称、bootstrap、分支长度
  - 支持格式：`name:bootstrap:length`
  - 处理有/无 bootstrap 值的节点
- **重要性：** ⭐⭐⭐⭐ (Newick 解析)

**`NWKLeafCoderDecoder`** (类)
- **功能：** 处理 Newick 格式的叶节点
- **关键特性：**
  - 解析物种/基因名称
  - 提取分支长度
  - 格式：`species_name:length`
  - 创建 DefaultPhyNode 实例
- **用法：** 由 PhyloTreeEncoderDecoder 内部使用
- **重要性：** ⭐⭐⭐⭐ (Newick 解析)

---

## 3. 遗传密码包

### 3.1 `geneticcodes` - 遗传密码翻译

**用途：** 将核苷酸序列翻译为氨基酸序列

#### 关键类：

**`IGeneticCode`** (接口)
- **功能：** 定义遗传密码翻译契约
- **关键特性：**
  - 支持 NCBI 遗传密码表
  - 密码子到氨基酸的翻译
  - 多种遗传密码变体
- **关键方法：**
  - `translateCodonToAminoAcidOneLetter(char[])`: 翻译密码子
  - `getGeneticCodeName()`: 获取密码表名称
- **重要性：** ⭐⭐⭐⭐⭐ (核心生物学功能)

**`GeneticCode`** (抽象类)
- **功能：** 遗传密码表的基础实现
- **关键特性：**
  - 密码子简并性分析
  - 起始/终止密码子识别
  - 反向互补支持
  - 不同遗传密码的工厂模式
- **关键方法：**
  - `translateCodonToAminoAcid()`: 核心翻译
  - `degenerateAttr()`: 分析密码子简并性
  - `getStartIndex()`: 查找起始密码子
  - `nthStopLocation()`: 查找终止密码子
- **重要性：** ⭐⭐⭐⭐⭐ (翻译引擎)

**`AminoAcid`** (接口)
- **功能：** 标准氨基酸表示
- **关键特性：**
  - 20种标准氨基酸
  - 三种表示系统：
    - 单字母代码 (A, C, D, E, ...)
    - 三字母代码 (ALA, CYS, ASP, ...)
    - 全名 (Alanine, Cysteine, ...)
  - 特殊符号 (*, -, X)
- **重要性：** ⭐⭐⭐⭐⭐ (基本常量)

**`OpenReadingFrame`** (类)
- **功能：** 定义和管理开放阅读框 (ORF)
- **关键特性：**
  - 区段定义（[起始, 结束] 对）
  - 基于1的到基于0的转换
  - 多区段支持
- **关键方法：**
  - `setSection(Vector<Integer>)`: 定义 ORF 区域
  - `getSection()`: 检索定义的区段
- **用例：** 基因发现、翻译区域规范
- **重要性：** ⭐⭐⭐⭐ (ORF 分析)

---

### 3.2 `geneticcodes.codeTables` - 遗传密码表

**用途：** 不同生物体使用的各种遗传密码表的具体实现

#### 关键类：

**`TheStandardCode`** (类)
- **功能：** 标准遗传密码表（NCBI 翻译表 1）用于核翻译
- **NCBI 表 ID：** 1
- **用例：** 真核生物和原核生物的大多数核基因
- **重要性：** ⭐⭐⭐⭐⭐ (通用遗传密码)

**`ECOLI_GeneticCodes`** (类)
- **功能：** 大肠杆菌特异性遗传密码表
- **用例：** 细菌翻译
- **重要性：** ⭐⭐⭐⭐ (细菌遗传学)

**`TheVertebrateMitochondrialCode`** (类)
- **功能：** 脊椎动物线粒体遗传密码（NCBI 表 2）
- **关键差异：** AGA/AGG 编码终止而不是 Arg
- **用例：** 脊椎动物线粒体基因组注释
- **重要性：** ⭐⭐⭐⭐ (线粒体遗传学)

**`TheInvertebrateMitochondrialCode`** (类)
- **功能：** 无脊椎动物线粒体遗传密码（NCBI 表 5）
- **用例：** 无脊椎动物线粒体基因组
- **重要性：** ⭐⭐⭐⭐ (无脊椎动物线粒体遗传学)

**`YEASTM_GeneticCodes`** (类)
- **功能：** 酵母线粒体遗传密码
- **用例：** 酵母线粒体基因组分析
- **重要性：** ⭐⭐⭐ (真菌线粒体遗传学)

**`TheYeastMitochondrialCode`** (类)
- **功能：** 酵母线粒体遗传密码（NCBI 表 3）
- **重要性：** ⭐⭐⭐ (真菌线粒体遗传学)

**`PLANTM_GeneticCodes`** (类)
- **功能：** 植物线粒体遗传密码
- **用例：** 植物线粒体基因组注释
- **重要性：** ⭐⭐⭐ (植物线粒体遗传学)

**`MOLDMITO_GeneticCodes`** (类)
- **功能：** 霉菌、原生动物和腔肠动物线粒体密码
- **重要性：** ⭐⭐⭐ (多样化生物)

**`TheCiliateDHexamitaNuclearCode`** (类)
- **功能：** 纤毛虫、伞藻和六鞭毛虫核密码（NCBI 表 6）
- **用例：** 纤毛虫核基因组翻译
- **重要性：** ⭐⭐⭐ (纤毛虫遗传学)

**`CILIATE_GeneticCodes`** (类)
- **功能：** 纤毛虫核遗传密码
- **重要性：** ⭐⭐⭐ (纤毛虫遗传学)

**`TheEuplotidNuclearCode`** (类)
- **功能：** 游仆虫核遗传密码（NCBI 表 10）
- **用例：** 游仆虫纤毛虫基因组
- **重要性：** ⭐⭐⭐ (专门的原生动物)

**`TheBacterialPlantPlastidCode`** (类)
- **功能：** 细菌、古菌和植物质体密码（NCBI 表 11）
- **用例：** 叶绿体基因组翻译
- **重要性：** ⭐⭐⭐⭐ (质体遗传学)

**`ECHINOM_GeneticCodes`** (类)
- **功能：** 棘皮动物和扁形动物线粒体密码
- **用例：** 棘皮动物线粒体基因组
- **重要性：** ⭐⭐⭐ (无脊椎动物线粒体遗传学)

**`TheEFMitochondrialCode`** (类)
- **功能：** 棘皮动物和扁形动物线粒体密码（NCBI 表 9）
- **重要性：** ⭐⭐⭐ (无脊椎动物线粒体遗传学)

**`INVERTM_GeneticCodes`** (类)
- **功能：** 无脊椎动物线粒体遗传密码
- **重要性：** ⭐⭐⭐⭐ (无脊椎动物线粒体遗传学)

**`MAMMALRV_GeneticCodes`** (类)
- **功能：** 哺乳动物逆转录病毒遗传密码
- **用例：** 逆转录病毒序列分析
- **重要性：** ⭐⭐⭐ (病毒遗传学)

**`YEASTRV_GeneticCodes`** (类)
- **功能：** 酵母逆转录病毒遗传密码
- **重要性：** ⭐⭐⭐ (酵母病毒遗传学)

**`VERTM_GeneticCodes`** (类)
- **功能：** 脊椎动物线粒体遗传密码
- **重要性：** ⭐⭐⭐⭐ (脊椎动物线粒体遗传学)

**`WORSCOLI_GeneticCodes`** (类)
- **功能：** 特定细菌的细菌遗传密码
- **重要性：** ⭐⭐⭐ (细菌遗传学)

**`TheMPCMCMSCode`** (类)
- **功能：** 霉菌、原生动物和腔肠动物线粒体密码加支原体/螺原体（NCBI 表 4）
- **用例：** 多样化的线粒体和细菌系统
- **重要性：** ⭐⭐⭐ (多样化生物)

**重要性：** ⭐⭐⭐⭐⭐ (跨生物体准确翻译的基础)

---

## 包重要性总结

### ⭐⭐⭐⭐⭐ 关键（必用）：
1. `top.signature` - 模块系统基础
2. `evoltree.struct` - 核心树结构
3. `evoltree.phylogeny` - 系统发育操作
4. `geneticcodes` - 翻译系统

### ⭐⭐⭐⭐ 重要（常用）：
1. `geneticcodes.codeTables` - 特定生物体密码

---

## 典型使用模式

### 模式 1：构建系统发育树
```
1. 创建节点：new ArrayBasedNode()
2. 构建树结构：EvolNode 方法
3. 序列化：PhyloTreeEncoderDecoder.encode()
4. 解析：PhyloTreeEncoderDecoder.decode()
```

### 模式 2：遗传翻译
```
1. 选择遗传密码：TheStandardCode 或变体
2. 翻译密码子：translateCodonToAminoAcid()
3. 查找 ORF：OpenReadingFrame.setSection()
4. 分析简并性：degenerateAttr()
```

---

*本文档涵盖 eGPS-Base 的基础核心结构。其他部分见文件 I/O、工具类、解析器和可视化。*
