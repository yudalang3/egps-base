# eGPS-Base 包文档 - 第3部分：解析器与生物信息学工具

## 概述

本文档涵盖用于解析生物信息学文件格式以及与外部数据库和工具集成的包。

---

## 1. BLAST 工具

### 1.1 `blast.parse` - BLAST 解析器

**用途：** 解析 BLAST 输出文件

#### 关键类：

**`ZzzModuleSignature`** (类)
- **功能：** BLAST 解析工具的模块签名
- **关键特性：**
  - 支持多种 BLAST 输出格式
  - HSP（高分段对）解析
  - 比对得分和统计信息提取
- **描述：** BLAST 解析工具模块，用于从各种 BLAST 输出格式（包括表格和 XML 格式）中提取有意义的信息
- **重要性：** ⭐⭐⭐⭐ (BLAST 分析)

**`BlastHspRecord`** (类)
- **功能：** 表示 BLAST 高分段对
- **关键特性：**
  - 查询/主题序列
  - 比对得分
  - E 值
  - 同一性百分比
- **重要性：** ⭐⭐⭐⭐ (BLAST 分析)

---

## 2. 蛋白质结构域分析

### 2.1 `pfam.parse` - Pfam 解析器

**用途：** 解析 Pfam 数据库文件和结构域注释

#### 关键类：

**`PfamScanRecord`** (类)
- **功能：** 表示来自 hmmscan 输出的单个 Pfam 结构域注释
- **关键特性：**
  - 解析 hmmscan --domtblout 格式
  - 结构域边界（比对和包络坐标）
  - HMM 信息（Pfam ID、结构域名称）
  - 统计得分（bit score、E 值）
  - 家族分类
- **关键字段：**
  - `seqId`: 蛋白质标识符
  - `alignmentStart/End`: 序列上的比对坐标
  - `envelopeStart/End`: 结构域边界
  - `hmmAcc`: Pfam 登录号（例如 PF00001）
  - `hmmName`: 结构域名称
  - `bitScore`, `eValue`: 统计显著性
- **关键方法：**
  - `parseHmmerScanOut(String)`: 解析整个 hmmscan 输出文件
- **返回：** Map<String, List<PfamScanRecord>> - 蛋白质 ID 到结构域列表
- **用例：**
  - 蛋白质结构域架构分析
  - 功能注释
  - 基于结构域的系统发育学
- **重要性：** ⭐⭐⭐⭐⭐ (结构域分析必需)

**`HmmDatParser`** (类)
- **功能：** 解析 Pfam HMM .dat 文件
- **关键特性：**
  - 提取结构域元数据
  - 解析 HMM 描述
- **重要性：** ⭐⭐⭐ (Pfam 元数据)

**`PfamDBEntry`** (类)
- **功能：** 表示 Pfam 数据库条目
- **关键特性：**
  - 结构域描述
  - 家族分配
  - GO 术语关联
- **重要性：** ⭐⭐⭐ (Pfam 数据模型)

**`ZzzModuleSignature`** (类)
- **功能：** Pfam 解析工具的模块签名
- **描述：** Pfam 和 HMMER 解析模块，用于蛋白质结构域注释和分析
- **重要性：** ⭐⭐⭐ (模块元数据)

**重要性：** ⭐⭐⭐⭐⭐ (蛋白质结构域分析)

---

## 3. NCBI 集成

### 3.1 `ncbi.taxonomy` - NCBI 分类学工具

**用途：** NCBI 分类学数据处理工具

#### 关键类：

**`TaxonomyParser`** (类)
- **功能：** 解析 NCBI 分类学数据库文件
- **关键特性：**
  - 解析 nodes.dmp 和 names.dmp 文件
  - 构建完整的分类层次结构树
  - 处理 18 字段节点记录
  - 支持压缩文件（.gz）
- **输入文件：**
  - `nodes.dmp`: 分类节点关系
  - `names.dmp`: 分类名称和同义词
- **关键方法：**
  - `parseTree(String nodePath, String namePath)`: 构建分类树
- **输出：** 表示完整分类的 EvolNode 树
- **数据源：** NCBI FTP: ftp://ftp.ncbi.nlm.nih.gov/pub/taxonomy/
- **重要性：** ⭐⭐⭐⭐⭐ (分类分类必需)

**`TaxonomyNode`** (类)
- **功能：** 表示 NCBI 分类中的单个节点
- **关键字段：**
  - taxId、parentTaxId、rank（超界、界等）
  - 遗传密码 ID（核、线粒体、质体）
  - 分类和继承标志
- **重要性：** ⭐⭐⭐⭐ (分类数据模型)

**`TaxonomicRank`** (枚举)
- **功能：** 分类等级的枚举
- **值：** SUPERKINGDOM、KINGDOM、PHYLUM、CLASS、ORDER、FAMILY、GENUS、SPECIES
- **重要性：** ⭐⭐⭐⭐ (分类分类)

**`TaxonomyFullNameLineageParser`** (类)
- **功能：** 具有完整分类层次结构的 NCBI 分类等级谱系文件解析器
- **关键特性：**
  - 解析 rankedlineage.dmp 文件（简化的分类格式）
  - 按分类单元 ID 直接检索谱系
  - 单个记录中的完整分类层次结构
  - 支持所有主要分类等级
- **关键方法：**
  - `parseTree(String)`: 解析 rankedlineage.dmp 文件
  - `getLineage(int)`: 按分类单元 ID 获取谱系
  - `getLineage(String)`: 按物种名称获取谱系
  - `getAllTaxonomicRanks()`: 获取所有解析的分类记录
- **数据字段：**
  - taxId、taxName、species、genus、family、order、class、phylum、kingdom、superkingdom
- **用例：**
  - 快速谱系查找
  - 分类注释
  - 物种分类
- **重要性：** ⭐⭐⭐⭐⭐ (快速谱系检索)

**`API4R`** (类)
- **功能：** 用于 NCBI 分类谱系检索操作的 R 语言 API
- **关键特性：**
  - R 兼容的接口设计
  - 批量谱系检索
  - 制表符分隔的输出格式
  - 处理缺失数据的 NA
- **关键方法：**
  - `getRankedLineages(String, int[])`: 批量检索分类单元 ID 的谱系
  - 返回带有制表符分隔的谱系字段的 String[]
- **输出格式：**
  - `[taxName]\t[species]\t[genus]\t[family]\t[order]\t[class]\t[phylum]\t[kingdom]\t[superkingdom]`
  - 缺失值表示为 "NA"
- **用例：**
  - R 集成以进行分类分析
  - 批量物种注释
  - 系统发育上下文检索
- **重要性：** ⭐⭐⭐⭐ (R 集成)

**`ZzzModuleSignature`** (类)
- **功能：** NCBI 分类学工具的模块签名
- **描述：** NCBI 分类模块，提供分类分类和谱系检索的解析器和工具
- **重要性：** ⭐⭐⭐ (模块元数据)

**重要性：** ⭐⭐⭐⭐⭐ (NCBI 集成)

---

## 4. Web 服务

### 4.1 `rest` - REST 服务

**用途：** RESTful Web 服务集成

#### 关键类：

**`EnsemblTranslationRest`** (类)
- **功能：** 用于蛋白质翻译服务的 Ensembl REST API 客户端
- **关键特性：**
  - 蛋白质翻译 API 集成
  - 结构域注释查询支持
  - RESTful Web 服务通信
- **关键方法：**
  - `getTranslation(String)`: 检索蛋白质翻译
  - `getProteinDomains(String)`: 查询蛋白质结构域注释
- **用例：**
  - 自动蛋白质注释
  - 基因/蛋白质数据库集成
  - 生物信息学工作流自动化
- **API 端点：**
  - 用于翻译的 Ensembl REST API
  - 结构域注释服务
- **重要性：** ⭐⭐⭐⭐ (数据库集成)

**`EnsJsonTreeParserCustomized`** (类)
- **功能：** 具有增强名称格式化和分类提取的自定义 Ensembl JSON 树解析器
- **关键特性：**
  - 扩展 EnsJsonTreeParser
  - 人类可读名称优先（通用名称或科学名称）
  - 分类 ID 提取和输出
  - TimeTree MYA（百万年前）集成
- **关键方法：**
  - `iterate2transferTree(TreeBean)`: 将 JSON 树解析为 DefaultPhyNode
  - `getOutput()`: 获取分类 ID 到名称的映射
- **输出：**
  - 具有格式化节点名称的系统发育树
  - `[taxonomy_id]\t[human_name]` 映射列表
- **用例：**
  - Ensembl 物种树可视化
  - 系统发育分析的分类映射
  - 具有分化时间的物种树
- **重要性：** ⭐⭐⭐⭐ (Ensembl 系统发育集成)

**子包：**
- `client`: REST 客户端工具
- `model`: 数据模型
- `service`: 服务实现
- `ensembl`: Ensembl 特定的 REST 服务
- `ensembl.phylo`: Ensembl 系统发育树服务（EnsJsonTreeParser、EnsJsonTreeParserCustomized、TreeBean、TaxonomyBean、ConfidenceBean、EventsBean、JsonTreeBean）

**重要性：** ⭐⭐⭐⭐ (Web 集成)

---

## 5. 分析框架

### 5.1 `analysis` - 分析框架

**用途：** 运行分析脚本和操作的框架

#### 关键类：

**`AbstractAnalysisAction`** (抽象类)
- **功能：** 分析操作的基类
- **关键特性：**
  - 分析的模板模式
  - 标准生命周期方法
- **重要性：** ⭐⭐⭐ (分析框架)

---

### 5.2 `analysis.math` - 数学工具

**用途：** 生物信息学的数学操作

#### 关键类：

**`DoubleListUtils`** (类)
- **功能：** 对双精度列表的操作
- **关键特性：**
  - 查找最小值和最大值
  - 基于流的处理
  - 支持 List<Double> 和 double[] 数组
- **关键方法：**
  - `getMinMax(List<Double>)`: 从列表获取最小/最大值
  - `getMinMax(double[])`: 从数组获取最小/最大值
- **重要性：** ⭐⭐⭐ (统计操作)

**`RandomArrayGenerator`** (类)
- **功能：** 为模拟生成随机数组
- **关键特性：**
  - 多个随机数生成器（MersenneTwister、Java Random）
  - 使用种子的可重现生成
  - [min, max] 范围内的均匀分布
  - 高质量伪随机数生成
- **关键方法：**
  - `generateRandomDoublesByColt()`: 带种子的 MersenneTwister
  - `generateRandomDoubles()`: 简单随机生成
  - `generateRandomDoublesByMath3()`: Apache Commons Math3
- **用例：**
  - 蒙特卡罗模拟
  - Bootstrap 重采样
  - 排列测试
- **重要性：** ⭐⭐⭐⭐ (模拟与统计测试)

---

## 6. 系统发育分析

### 6.1 `phylo.algorithm` - 系统发育算法

**用途：** 系统发育分析的算法工具

#### 关键类：

**`ZzzModuleSignature`** (类)
- **功能：** 系统发育算法的模块签名
- **描述：** 系统发育算法模块，提供树分析和比较的计算方法
- **重要性：** ⭐⭐⭐ (模块元数据)

**重要性：** ⭐⭐⭐⭐ (系统发育计算)

---

### 6.2 `phylo.gsefea` - 进化分析的基因集富集

**用途：** 进化背景下的基因集富集分析

#### 关键类：

**`ZzzModuleSignature`** (类)
- **功能：** GSEFEA 工具的模块签名
- **描述：** 进化分析的基因集富集模块，提供系统发育背景下的功能富集工具
- **重要性：** ⭐⭐⭐ (模块元数据)

**重要性：** ⭐⭐⭐⭐ (进化富集分析)

---

### 6.3 `phylo.msa.util` - 系统发育学的 MSA 工具

**用途：** 用于系统发育分析的多序列比对工具

#### 关键类：

**`ZzzModuleSignature`** (类)
- **功能：** 系统发育 MSA 工具的模块签名
- **描述：** 用于系统发育分析的 MSA 工具，在树推断中提供比对处理的专门工具
- **重要性：** ⭐⭐⭐ (模块元数据)

**重要性：** ⭐⭐⭐⭐ (系统发育 MSA 处理)

---

## 包重要性总结

### ⭐⭐⭐⭐⭐ 关键（必用）：
1. `ncbi.taxonomy` (TaxonomyParser、TaxonomyFullNameLineageParser) - NCBI 集成
2. `pfam.parse` (PfamScanRecord) - 蛋白质结构域分析

### ⭐⭐⭐⭐ 重要（常用）：
1. `blast.parse` - BLAST 结果
2. `rest` - Web 服务集成
3. `analysis.math` - 数学操作
4. `phylo.*` - 系统发育算法

---

## 典型使用模式

### 模式 1：BLAST 分析
```
1. 解析 BLAST 输出：BlastHspRecord
2. 提取比对：HSP 记录
3. 按 E 值/得分过滤
4. 导出到 TSV
```

### 模式 2：结构域注释
```
1. 运行 hmmscan（外部工具）
2. 解析结果：PfamScanRecord.parseHmmerScanOut()
3. 提取每个蛋白质的结构域
4. 可视化结构域架构
```

### 模式 3：分类查找
```
1. 加载分类：TaxonomyParser.parseTree()
2. 查询谱系：TaxonomyFullNameLineageParser.getLineage()
3. 提取等级：getAllTaxonomicRanks()
4. 导出到 R：API4R.getRankedLineages()
```

### 模式 4：Ensembl 集成
```
1. 查询 Ensembl REST：EnsemblTranslationRest
2. 解析 JSON 树：EnsJsonTreeParserCustomized
3. 提取分类映射
4. 可视化物种树
```

---

*本文档涵盖解析器和生物信息学工具包。其他部分见核心结构、文件 I/O、工具类和可视化。*
