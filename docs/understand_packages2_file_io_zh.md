# eGPS-Base 分册 2：文件输入输出

[文档索引](README_zh.md) | [English](understand_packages2_file_io.md)

## 文档定位

本分册面向维护序列与表格数据流的开发者。凡是涉及 FASTA 文件、TSV 类表格，或多序列比对格式的读写与转换，都建议先从这里进入。

## 包级导读

### `fasta.io`

用途：

- 提供 FASTA 读取能力，以及一组面向工作流的序列处理脚本

当前源码文件：

- `FastaReader`
- `Script_allocate_raw_pep_to_dir_by_fileName`
- `Script_get_longest_CDS_forEnsembl_pepFa`
- `Script_get_unique_forEnsembl_pepFa`
- `Script_homoGene_finder_blastpWithHmmer`
- `Script_prepare_longestUnique_forEnsembl_pepFa`
- `ZzzModuleSignature`

阅读要点：

- `FastaReader` 才是这里最主要的可复用入口。
- `Script_*` 更适合作为围绕特定数据整理任务的工作流脚本，而不是通用库接口。

### `fasta.stat`

用途：

- 提供序列层面的质量控制统计能力

当前源码文件：

- `UniqueStat`
- `BatchUniqueStat`
- `ZzzModuleSignature`

阅读要点：

- 如果问题与重复序列、重复率或批量唯一性检查有关，这个包就是第一入口。

### `fasta.comparison`

用途：

- 基于比对结果对序列集合进行比较与汇总展示

当前源码文件：

- `FastaComparer`
- `PairwiseSeqDiffPrinter`
- `ZzzModuleSignature`

阅读要点：

- 只有在你已经拿到 BLAST 或 DIAMOND 一类的比对输出之后，这个包才会成为后续分析入口。

### `tsv.io`

用途：

- 提供表格容器与面向 TSV 的辅助类

当前源码文件：

- `TSVReader`
- `TSVWriter`
- `KitTable`
- `IntTable`
- `ExcelReaderTemplate`
- `BeanCreator`
- `TsvNameTransversionInfo`
- `SpaceTransformer`
- `ZzzModuleSignature`

阅读要点：

- 这个包不只是简单的 TSV 解析器，也包含导入流程里会复用的小型转换与表构建辅助类。

### `msaoperator`

用途：

- 处理多序列比对的元数据、解析、预处理与写出

顶层源码文件：

- `DataforamtInfo`
- `DataForamtPrivateInfor`
- `DefaultDataFormatPrivateInfor`
- `ZzzModuleSignature`

重要子包与入口：

- `msaoperator.alignment` - `AlignmentPreprocesser`、`CompeleDeletion`、`PairwiseDeletion`、`PartialDeletion`、`DeletionHandler`、`GlobalAlignmentSettings`
- `msaoperator.alignment.sequence` - 序列对象模型：`SequenceI`（核心接口）、`Sequence`（基础实现）、`BasicSequenceData`（数据存储）、`SequenceComponentRatio`（组成比例统计）
- `msaoperator.io` - `MSAFileParser`
- `msaoperator.io.seqFormat.parser` - 各格式解析器
- `msaoperator.io.seqFormat.writer` - 各格式写出器
- `msaoperator.io.seqFormat.model` - 比对数据模型

阅读要点：

- 若问题聚焦格式导入，`MSAFileParser` 是最重要的读取入口。
- 只有在你已经确认需要缺失位点处理或预处理策略时，才有必要深入 `alignment` 子包。
- `GeneBankWriter` 与 `NexmlWriter` 当前仍是被整体注释的脚手架文件，文档中应按“未启用实现”对待，而不是写成正式支持能力。

## 阅读建议

- 按输入格式选入口：FASTA 先看 `FastaReader`，TSV 先看 `TSVReader`，MSA 先看 `MSAFileParser`。
- 只有在基础读入路径已经明确之后，再进入 `fasta.stat` 或 `fasta.comparison`。
- 在文档与评审中，应始终区分“可复用库入口”和“脚本式工作流辅助类”。

## 相关文档

- 如果导入流程还依赖 BLAST、Pfam、分类学或远程服务，请结合 [understand_packages3_parsers_zh.md](understand_packages3_parsers_zh.md) 阅读。
- 如果文件处理还需要更广泛的路径、字符串或 CLI 辅助代码，请结合 [understand_packages4_utilities_zh.md](understand_packages4_utilities_zh.md) 阅读。
