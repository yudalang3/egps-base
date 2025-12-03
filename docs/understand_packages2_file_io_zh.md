# eGPS-Base 包文档 - 第2部分：文件 I/O 与数据格式

## 概述

本文档涵盖负责读写生物学数据格式的包，包括 FASTA、TSV 和多序列比对格式。

---

## 1. 序列文件 I/O

### 1.1 `fasta.io` - FASTA 文件操作

**用途：** 读写 FASTA 格式文件（序列）

#### 关键类：

**`ZzzModuleSignature`** (类)
- **功能：** FASTA 文件操作的模块签名
- **关键特性：**
  - 高性能序列文件 I/O
  - 支持多种格式（压缩/未压缩）
  - 流式处理能力
- **描述：** FASTA 文件 I/O 操作模块，提供高效的序列文件读写，支持各种压缩格式
- **重要性：** ⭐⭐⭐⭐⭐ (序列数据必需)

**`Script_*` 处理脚本**
- **功能：** 序列数据的专门批处理工具
- **关键特性：**
  - 自动文件组织和预处理
  - Ensembl 数据的最长 CDS 提取
  - 序列唯一性过滤和分析
  - 同源性检测集成
- **脚本包括：**
  - `Script_allocate_raw_pep_to_dir_by_fileName`: 按命名模式组织蛋白质文件
  - `Script_get_longest_CDS_forEnsembl_pepFa`: 提取最长编码序列
  - `Script_get_unique_forEnsembl_pepFa`: 过滤唯一蛋白质序列
  - `Script_homoGene_finder_blastpWithHmmer`: 同源基因检测
  - `Script_prepare_longestUnique_forEnsembl_pepFa`: 准备非冗余数据集
- **用法：** 用于大规模序列处理的命令行执行
- **重要性：** ⭐⭐⭐⭐ (自动化和预处理)

**`FastaReader`** (类)
- **功能：** 高性能 FASTA 文件读取器
- **关键特性：**
  - 处理大文件（100MB+ 缓冲区）
  - 自动压缩检测（.gz, .bz2, .xz）
  - 流式处理以提高内存效率
  - 大写转换
  - 多种输入源（File、String、InputStream）
- **关键方法：**
  - `readFastaSequence(String)`: 读取整个文件
  - `readAndProcessFastaPerEntry(BiPredicate)`: 流式处理
- **性能：** 针对生物信息学大文件优化
- **重要性：** ⭐⭐⭐⭐⭐ (序列数据必需)

---

### 1.2 `fasta.stat` - FASTA 统计

**用途：** FASTA 序列文件的统计分析

#### 关键类：

**`UniqueStat`** (类)
- **功能：** 分析序列唯一性并检测重复
- **关键特性：**
  - 识别条目间的相同序列
  - 计算重复率（0.0 到 1.0）
  - 频率排序（按重复计数排序）
  - 条目名称跟踪（列出共享相同序列的所有名称）
- **关键方法：**
  - `getRatio(String)`: 计算重复率
  - main(String[]): CLI 接口
- **输出格式：**
  - `[count]\t[name1;name2;...]` 用于重复项
  - 重复率：duplicate_count / total_count
- **算法：**
  - 构建序列到计数和序列到名称的映射
  - 按重复计数排序（降序）
  - 打印计数 > 1 的条目
- **用例：**
  - 序列数据集的质量控制
  - 检测 PCR 重复或伪影
  - 评估数据集多样性
  - 发现污染或错误标注
- **重要性：** ⭐⭐⭐⭐ (质量控制)

**`BatchUniqueStat`** (类)
- **功能：** 跨多个 FASTA 文件计算唯一序列统计的批处理器
- **关键特性：**
  - 目录遍历以进行批处理
  - 文件后缀过滤
  - 自动统计生成
  - 表格输出格式
- **关键方法：**
  - `main(String[])`: CLI 入口点（参数：目录路径、文件后缀）
  - `doStats(Path)`: 处理单个文件
- **输出格式：**
  - `[filename]\t[repeat_ratio]` 用于每个文件
- **用例：**
  - 多个数据集的批量质量控制
  - 数据集比较和选择
  - 大规模序列整理
- **重要性：** ⭐⭐⭐⭐ (批处理)

**`ZzzModuleSignature`** (类)
- **功能：** FASTA 统计工具的模块签名
- **描述：** FASTA 文件统计分析模块，提供唯一性分析和质量控制
- **重要性：** ⭐⭐⭐ (模块元数据)

**重要性：** ⭐⭐⭐⭐ (序列质量控制)

---

### 1.3 `fasta.comparison` - FASTA 比较工具

**用途：** 比较和可视化序列差异

#### 关键类：

**`FastaComparer`** (类)
- **功能：** 使用 BLAST/Diamond 比对结果比较两个 FASTA 文件
- **关键特性：**
  - 基于比对的比较（fmt6 格式）
  - 查询和主题的覆盖率分析
  - 未映射序列检测
  - 名称解析（处理空格）
- **输入文件：**
  - 查询 FASTA 文件
  - 主题 FASTA 文件
  - BLAST/Diamond fmt6 比对文件
- **Fmt6 格式：** `query_id\tsubject_id\t[columns...]`
- **关键方法：**
  - run(String, String, String): 使用比对比较两个 FASTA
  - readFasta(String, List): 提取序列名称
- **输出：**
  - 两个文件的匹配计数和总计
  - 覆盖率（匹配/总数）
  - 未映射序列（如果比率 > 1.0）
- **用例：**
  - 验证 BLAST 完整性
  - 比较直系同源覆盖率
  - 评估数据库覆盖率
  - 同源性搜索的质量控制
- **重要性：** ⭐⭐⭐⭐ (比对验证)

**`PairwiseSeqDiffPrinter`** (类)
- **功能：** 可视化成对序列比对差异
- **关键特性：**
  - 双标记模式：标记匹配 (|) 或不匹配 (^, *)
  - 换行显示（可配置列宽）
  - 间隙可视化（· 用于显示，∅ 用于逻辑）
  - 位置跟踪（基于1的索引）
  - 同一性百分比计算
- **关键方法：**
  - `printDiff(String, String, int)`: 用 '|' 标记匹配
  - `printDiffMarkingMismatches(String, String, int, char)`: 标记不匹配
- **输出格式：**
  ```
           [start]-[end]
  A:        [sequence_A]
            [markers]
  B:        [sequence_B]
  ```
- **不匹配格式：** `position:char_A→char_B`
- **用例：**
  - 可视化蛋白质比对差异
  - 比较预测与参考序列
  - 识别突变位置
  - 序列编辑的质量控制
- **重要性：** ⭐⭐⭐⭐ (序列比较)

**`ZzzModuleSignature`** (类)
- **功能：** FASTA 比较工具的模块签名
- **描述：** FASTA 比较模块，提供序列比对验证和差异可视化
- **重要性：** ⭐⭐⭐ (模块元数据)

**重要性：** ⭐⭐⭐⭐ (序列比较和质量控制)

---

## 2. 表格数据 I/O

### 2.1 `tsv.io` - TSV 文件操作

**用途：** 制表符分隔值文件的读写

#### 关键类：

**`TSVReader`** (类)
- **功能：** 灵活的 TSV 文件解析器
- **关键特性：**
  - 头部支持（可选）
  - 注释行过滤（# 符号）
  - 按列数据访问
  - 多种输出格式
  - 100MB 缓冲区用于大文件
- **关键方法：**
  - `readTsvTextFile(String)`: 读取为 KitTable
  - `readAsKey2ListMap(String)`: 读取为列映射
  - `organizeMap(String, String, String)`: 提取两列
- **重要性：** ⭐⭐⭐⭐ (常见数据格式)

**`TSVWriter`** (类)
- **功能：** 将表格数据写入 TSV 格式
- **关键特性：**
  - 列到行的转换
  - 自动头部生成
  - 制表符分隔符
  - 缓冲写入以提高效率
- **输入格式：** Map<String, List<String>>（面向列）
- **输出格式：** 带头部的标准 TSV
- **关键方法：**
  - `write(Map<String, List<String>>, String)`: 将数据写入文件
- **用例：**
  - 导出分析结果
  - 生成报告
  - 数据交换
- **重要性：** ⭐⭐⭐⭐ (数据导出)

**`KitTable`** (类)
- **功能：** 表格数据的容器
- **关键特性：**
  - 头部名称存储
  - 内容存储（List<List<String>>）
  - 原始行保留
  - 维度查询（行/列）
  - 保存到文件能力
- **数据结构：**
  - headerNames: List<String>
  - contents: List<List<String>>
  - originalLines: List<String>
  - path: String（可选）
- **关键方法：**
  - `getNumOfRows()`: 获取行数
  - `getNumOfColum()`: 获取列数
  - `save2file(String)`: 写入 TSV
- **重要性：** ⭐⭐⭐⭐ (数据容器)

**`IntTable`** (类)
- **功能：** 基于整数的表数据结构
- **关键特性：**
  - 针对整数数据优化
  - 高效的存储和检索
- **用例：**
  - 计数矩阵
  - 基于整数的数据表
- **重要性：** ⭐⭐⭐ (整数表)

**`SpaceTransformer`** (类)
- **功能：** 转换字符串中空白的工具
- **关键特性：**
  - 空格规范化
  - 制表符/空格转换
  - 空白清理
- **用例：**
  - TSV 文件预处理
  - 字符串格式化
- **重要性：** ⭐⭐⭐ (字符串转换)

**`ZzzModuleSignature`** (类)
- **功能：** TSV I/O 工具的模块签名
- **描述：** TSV 文件操作模块，用于读写表格数据
- **重要性：** ⭐⭐⭐ (模块元数据)

**重要性：** ⭐⭐⭐⭐ (表格数据处理)

---

## 3. 多序列比对 I/O

### 3.1 `msaoperator` - 多序列比对

**用途：** 对多序列比对的操作

#### 关键类：

**`DataforamtInfo`** (类)
- **功能：** 数据格式验证结果容器
- **关键组件：**
  - `isSuccess`: 格式验证是否成功
  - `dataFormatCode`: 格式标识符或错误代码
  - `dataForamtPrivateInfor`: 可选的格式特定元数据
- **用例：**
  - MSA 文件格式检测
  - 格式验证报告
- **重要性：** ⭐⭐⭐ (格式验证)

**`DataForamtPrivateInfor`** (接口)
- **功能：** 定义 MSA 格式私有信息的契约
- **关键特性：**
  - 格式识别和兼容性检查
  - 错误报告和详细描述
  - 格式特定元数据支持
- **关键方法：**
  - `isCompatiable(DataForamtPrivateInfor)`: 兼容性检查
  - `getFormatName()`: 获取格式名称
- **用例：**
  - 合并前的 MSA 格式验证
  - 格式特定元数据处理
  - 动态格式检测和验证
- **重要性：** ⭐⭐⭐⭐ (格式兼容性基础)

**`DefaultDataFormatPrivateInfor`** (类)
- **功能：** DataForamtPrivateInfor 的默认实现
- **关键特性：**
  - 通用兼容性（与所有格式兼容）
  - 无格式约束的默认行为
  - 回退选项和占位符使用
- **兼容性策略：**
  - 始终返回兼容 (true)
  - 无错误消息 (null)
  - 零约束限制
- **用例：**
  - 未初始化 MSA 对象的默认格式
  - 测试场景中的格式兼容性
  - 通用格式适配器
- **重要性：** ⭐⭐⭐ (默认格式处理)

**`AlignmentPreprocesser`** (类)
- **功能：** 预处理比对数据
- **关键特性：**
  - 间隙处理
  - 序列修剪
- **重要性：** ⭐⭐⭐ (比对预处理)

**`CompeleDeletion`** (类)
- **功能：** 完全删除处理器，删除包含间隙或缺失数据的任何比对列
- **关键特性：**
  - 删除任何间隙字符的列
  - 仅保留完全解析的列
  - 扩展 DeletionHandler 基类
- **关键方法：**
  - `dealWithDeletion(String[])`: 使用完全删除处理比对
- **用例：**
  - 保守的比对清理
  - 需要完整数据的系统发育分析
  - 距离矩阵计算
- **重要性：** ⭐⭐⭐⭐ (间隙处理策略)

**`PairwiseDeletion`** (类)
- **功能：** 比对处理的成对删除处理器
- **关键特性：**
  - 仅在成对比较中删除间隙
  - 保留最大序列信息
- **用例：**
  - 成对距离计算
  - 最大数据保留场景
- **重要性：** ⭐⭐⭐ (间隙处理策略)

**`PartialDeletion`** (类)
- **功能：** 基于阈值的列删除的部分删除处理器
- **关键特性：**
  - 基于间隙阈值删除列
  - 可配置的间隙容差
  - 数据保留和质量之间的平衡
- **用例：**
  - 灵活的比对清理
  - 用户定义的质量阈值
- **重要性：** ⭐⭐⭐ (间隙处理策略)

**`DeletionHandler`** (抽象类)
- **功能：** 比对间隙删除策略的抽象基类
- **关键特性：**
  - 删除算法的模板模式
  - 核苷酸验证支持
  - 通用间隙处理逻辑
- **关键方法：**
  - `dealWithDeletion(String[])`: 抽象删除方法
- **用例：**
  - 实现自定义删除策略
  - 标准化间隙处理
- **重要性：** ⭐⭐⭐⭐ (间隙处理框架)

**`GlobalAlignmentSettings`** (类)
- **功能：** 比对操作的全局设置
- **关键特性：**
  - 集中式配置管理
  - 比对参数存储
- **重要性：** ⭐⭐⭐ (配置管理)

**`MSAFileParser`** (类)
- **功能：** 支持各种 MSA 格式的多序列比对文件解析器
- **关键特性：**
  - 多格式支持（ClustalW、FASTA、PHYLIP、NEXUS、PAML、MEGA、GCG MSF）
  - 格式特定解析器选择
  - 统一的解析接口
- **关键方法：**
  - `parserMSAFile(File, MSA_DATA_FORMAT)`: 使用指定格式解析 MSA 文件
- **支持的格式：**
  - ALIGNED_CLUSTALW: ClustalW 格式
  - ALIGNED_FASTA: FASTA 比对
  - ALIGNED_PHYLIP: PHYLIP 格式
  - ALIGNED_PAML: PAML 格式
  - ALIGNED_MEGA: MEGA 格式
  - ALIGNED_GCGMSF: GCG MSF 格式
  - NEXUS_SEQ: NEXUS 格式
- **用例：**
  - 从不同工具读取比对文件
  - 格式转换管道
  - 比对数据导入
- **重要性：** ⭐⭐⭐⭐⭐ (MSA I/O 必需)

**子包：**
- `io`: MSA 文件 I/O（MSAFileParser、格式解析器/写入器）
- `io.seqFormat`: 格式特定实现（AbstractParser、AbstractWriter、MSA_DATA_FORMAT）
- `io.seqFormat.parser`: 格式解析器（ClustalWParser、FastaParser、NEXUSParser、PAMLParser、PHYParser、GCGMSFParser、MEGAParser）
- `io.seqFormat.writer`: 格式写入器（ClustalWriter、FastaWriter、PAMLWriter、PHYLIPWriter）
- `io.seqFormat.model`: 数据模型（CLUElementBase、ClustalWSequenceData、EMBLSeqElement、SeqElementBase）
- `alignment`: MSA 操作和间隙处理（CompeleDeletion、PairwiseDeletion、PartialDeletion、DeletionHandler、GlobalAlignmentSettings）
- `alignment.sequence`: 序列级操作（Sequence、SequenceComponentRatio）

**关键特性：**
- 读写比对格式（FASTA、PHYLIP、NEXUS、ClustalW、PAML、MEGA、GCG MSF）
- 列操作
- 一致性序列
- 间隙删除策略（完全、成对、部分）

**重要性：** ⭐⭐⭐⭐ (MSA 操作)

---

## 包重要性总结

### ⭐⭐⭐⭐⭐ 关键（必用）：
1. `fasta.io` - 序列文件 I/O
2. `msaoperator` (MSAFileParser) - MSA I/O 必需

### ⭐⭐⭐⭐ 重要（常用）：
1. `tsv.io` - 表格数据
2. `fasta.stat` - 序列质量控制
3. `fasta.comparison` - 序列比较
4. `msaoperator` (间隙处理器) - 比对预处理

---

## 典型使用模式

### 模式 1：读取 FASTA 文件
```
1. 创建读取器：new FastaReader()
2. 读取序列：readFastaSequence(path)
3. 处理：遍历序列
4. 分析：使用 fasta.stat 进行质量控制
```

### 模式 2：处理 MSA 文件
```
1. 解析比对：MSAFileParser.parserMSAFile()
2. 验证格式：DataforamtInfo
3. 处理间隙：CompeleDeletion/PairwiseDeletion
4. 导出：使用格式写入器
```

### 模式 3：TSV 数据处理
```
1. 读取 TSV：TSVReader.readTsvTextFile()
2. 处理数据：KitTable 操作
3. 写入结果：TSVWriter.write()
```

---

*本文档涵盖文件 I/O 和数据格式包。其他部分见核心结构、工具类、解析器和可视化。*
