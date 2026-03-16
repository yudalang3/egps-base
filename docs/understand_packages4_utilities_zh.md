# eGPS-Base 分册 4：工具与辅助层

[文档索引](README_zh.md) | [English](understand_packages4_utilities.md)

## 文档定位

本分册面向处理仓库横切性支撑代码的维护者。凡是涉及文件辅助、字符串处理、集合操作、持久化、CLI 包装、小型 GUI 工具，或轻量级统一输出抽象的任务，都建议先从这里阅读。

## 包级导读

### `utils`

用途：

- 提供被多个包反复复用的广义通用工具能力

代表性源码文件：

- `EGPSFileUtil`
- `EGPSGeneralUtil`
- `EGPSListUtil`
- `EGPSUtil`
- `EGPSObjectCounter`
- `EGPSObjectsUtil`
- `EGPSGuiUtil`
- `EGPSFormatUtil`
- `ZzzModuleSignature`

阅读要点：

- 这个包本身范围很宽，因此文档与评审都应尽量指向具体类，而不是只说“去 utils 看”。

### `utils.string`

用途：

- 提供文本处理辅助能力

当前源码文件：

- `EGPSStringUtil`
- `StringCounter`

阅读要点：

- 当任务涉及表格文本切分、括号校验或高频小型字符串操作时，这里通常是第一入口。

### `utils.datetime`

用途：

- 提供时间格式化与时间对象转换辅助

当前源码文件：

- `DateTimeOperator`
- `EGPSTimeUtil`

### `utils.collections`

用途：

- 提供集合层面的辅助能力，职责比大而全的 `utils` 更聚焦

当前源码文件：

- `CollectionUtils`
- `CombinatoricsUtil`

### `utils.storage`

用途：

- 提供小型、偏持久化方向的辅助类

当前源码文件：

- `FinalFields`
- `MapPersistence`
- `ObjectPersistence`

阅读要点：

- 当问题更像是在保存或恢复内存结构，而不是解析某种领域格式时，应优先看这个包。

### `cli.tools`

用途：

- 维护面向用户的 CLI 清单及其直接维护的工具类

当前源码文件：

- `ListTools`
- `ClipboardPath4Win2WSL`
- `ClipboardPathNormalized`
- `CountFilesWithSuffix`
- `ListFilesWithSuffix`
- `CheckNwkFormat`
- `RemoveInternalNodeNames`
- `NodeNames4Space4Underline`
- `SeeModulesWeHave`
- `ZzzModuleSignature`

阅读要点：

- 这里应被理解为“维护中的 CLI 工具清单”，而不是“仓库全部 `main()` 入口总表”。
- `check_before_gitpush.md` 已明确要求：当面向用户的 CLI 发生变化时，要同步检查 `ListTools`。

### `gui.simple.tools`

用途：

- 为便捷功能提供小型 GUI 封装

当前源码文件：

- `FilePathNormalizedGUI`
- `ZzzModuleSignature`

阅读要点：

- 这一块的职责应保持克制，除非仓库未来真的发展出更完整的 GUI 工具层，否则不宜继续泛化。

### `unified.output`

用途：

- 提供面向不同输出目标的轻量级打印抽象

当前源码文件：

- `UnifiedPrinter`
- `UnifiedPrinterBuilder`
- `ZzzModuleSignature`

实现说明：

- `ConsolePrinter` 与 `FilePrinter` 当前以包级实现的方式定义在 `UnifiedPrinterBuilder.java` 内部。
- `unified.output` 下目前并不存在额外的嵌套 `output` 子包。

## 阅读建议

- 大多数横切性辅助问题，都可以先从 `utils` 或 `utils.string` 开始排查。
- 当问题已经明显收束到集合、持久化或时间处理时，再进入更窄的子包。
- 只有当问题明确落在“维护中的命令行工具表面”上时，才优先阅读 `cli.tools`。

## 相关文档

- 如果工具类服务于 FASTA、TSV 或 MSA 流程，请结合 [understand_packages2_file_io_zh.md](understand_packages2_file_io_zh.md) 阅读。
- 如果这些辅助代码出现在 Swing 或图形相关路径中，请结合 [understand_packages5_visualization_zh.md](understand_packages5_visualization_zh.md) 阅读。
