# eGPS-Base 分册 3：解析器与外部数据

[文档索引](README_zh.md) | [English](understand_packages3_parsers.md)

## 文档定位

本分册面向那些需要追踪“外部数据如何进入仓库”的维护者。它涵盖外部格式解析、外部数据库与远程服务访问，以及紧接在这些输入之后的少量分析与系统发育相关包。

## 包级导读

### `blast.parse`

用途：

- 解析 BLAST 结果结构

当前源码文件：

- `BlastHspRecord`
- `ZzzModuleSignature`

阅读要点：

- 这里是一个聚焦的解析区域，而不是完整的 BLAST 工作流框架。

### `pfam.parse`

用途：

- 解析 Pfam 与 HMMER 相关输出及元数据

当前源码文件：

- `PfamScanRecord`
- `PfamDBEntry`
- `HmmDatParser`
- `TxtVisualizeSeqDomains`
- `ZzzModuleSignature`

阅读要点：

- 当输入来自 `hmmscan --domtblout`、Pfam `.dat`，或需要快速整理结构域文本概览时，应先看这个包。

### `ncbi.taxonomy`

用途：

- 解析 taxonomy 数据并支持 lineage 检索

当前源码文件：

- `TaxonomyParser`
- `TaxonomyFullNameLineageParser`
- `TaxonomyNode`
- `TaxonomyName`
- `TaxonomicRank`
- `API4R`
- `ZzzModuleSignature`

阅读要点：

- 这个包同时提供完整树解析路径，以及更轻量的谱系检索路径。

### `rest`

用途：

- 为 Ensembl 与 InterPro 相关工作流提供远程数据访问能力

当前源码目录结构：

- `rest.ensembl`
- `rest.ensembl.compara`
- `rest.ensembl.ensembrest`
- `rest.ensembl.phylo`
- `rest.ensembl.proteins`
- `rest.interpro.entrytaxon`

代表性类：

- `EnsemblTranslationRest`
- `ConvertGeneIDsByHgncTable`
- `EnsGeneTreeInfoRest`
- `EnsHomologyInforRest`
- `OverlapTransBeanParser`
- `EnsJsonTreeParser`
- `EnsJsonTreeParserCustomized`
- `RestGetProteinDomains`
- `InterProEntryTaxonInfoParser`

阅读要点：

- 这一部分的文档刻意以真实源码目录结构为准，不再使用 `client`、`model`、`service` 之类的抽象占位层名。

### `analysis`

用途：

- 提供轻量级分析骨架与数值辅助能力

当前源码文件与子包：

- `AbstractAnalysisAction`
- `analysis.math.DoubleListUtils`
- `analysis.math.RandomArrayGenerator`
- `analysis.math.ZzzModuleSignature`
- `analysis.ZzzModuleSignature`

阅读要点：

- 虽然这个包不大，但当解析后流程需要复用数值辅助或 action 风格骨架时，它是合适的查看入口。

### `phylo`

用途：

- 提供建立在树结构与比对基础设施之上的下游系统发育算法

当前子包：

- `phylo.algorithm` - 包含 `RobinsonFouldsMetricCalculator`
- `phylo.gsefea` - 包含 `DoGSEFEA`
- `phylo.msa.util` - 包含 `EvolutionaryProperties` 与 `MsaCommonUtil`

阅读要点：

- 与低层解析器或树结构层相比，这些包更靠近应用逻辑。

## 阅读建议

- 如果问题起点是某种明确的外部格式，优先从 `blast.parse`、`pfam.parse` 或 `ncbi.taxonomy` 开始。
- 如果流程依赖 Ensembl 或 InterPro 数据获取，优先进入 `rest.*`。
- 只有在上游数据路径已经清楚之后，再进一步阅读 `analysis` 与 `phylo.*`。

## 相关文档

- 如果解析结果只是更大导入流程的一部分，请结合 [understand_packages2_file_io_zh.md](understand_packages2_file_io_zh.md) 阅读。
- 如果解析后的数据最终要落到树对象上，请结合 [understand_packages1_core_structure_zh.md](understand_packages1_core_structure_zh.md) 阅读。
