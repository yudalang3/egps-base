# eGPS-Base 项目总览

[文档索引](README_zh.md) | [English](global_understand.md)

## 文档定位

本文用于记录仓库层面的稳定事实，并为整套 `docs/` 提供阅读地图。它刻意保持简洁；更细的包说明分别放在各分册中维护。

## 仓库的稳定事实

- 项目：`egps-base`
- 语言：Java 25
- 构建工具：Maven
- 打包形式：`jar`
- 主源码目录：`src/main/java`
- 测试目录：`src/test/java`
- `src/main/java` 下的非 Java 文件会作为资源一起打包

从功能上看，这个仓库是 eGPS 2.1 生态中可复用的基础库，覆盖序列处理、系统发育树、表格数据、图形绘制、REST 集成与通用工具等领域。

## 代码布局

### 主要源码域

- `top.signature` - 模块元数据与发现约定
- `evoltree.*` - 树结构、系统发育、树显示与 tanglegram
- `fasta.*` - FASTA 输入输出、比较与统计
- `msaoperator.*` - 多序列比对的解析与写出
- `tsv.io` - TSV 与表格辅助类
- `blast.parse`、`pfam.parse`、`ncbi.taxonomy` - 领域解析器
- `rest.*` - Ensembl 与 InterPro 集成
- `graphic.engine` - 图表与图形基础设施
- `cli.tools`、`gui.simple.tools` - 可直接运行的小型工具
- `utils.*`、`unified.output` - 通用辅助包

### 测试布局

- `src/test/java/evoltree/phylogeny` - 树编解码测试
- `src/test/java/evoltree/txtdisplay` - 文本树渲染测试
- `src/test/java/graphic/engine` - 轴刻度计算测试
- `src/test/java/script/run/once` - 一次性脚本，不应默认视为稳定自动化测试
- `src/test/java/os` 与 `src/test/java/tab` - 范围较小的环境或特性检查

## 构建与测试入口

### 构建

```bash
mvn compile
mvn clean package source:jar
mvn dependency:copy-dependencies -DoutputDirectory=/absolute/output/dir
```

### 测试

```bash
mvn test
mvn -Dtest=PhyloTreeEncoderDecoderTest test
mvn -Dtest=evoltree.phylogeny.PhyloTreeEncoderDecoderTest test
mvn -Dtest=PhyloTreeEncoderDecoderTest#testEncodeSimpleTree test
```

### 仓库内 JAR 脚本

```bash
./build_jar.sh /absolute/output_dir
./build_jar_and_remove.sh
```

`build_jar.sh` 直接打包当前 `target/classes`，因此在类文件可能过期时，应先执行一次新的编译或打包。

## 组织约定

### 模块签名约定

许多功能包都会提供一个实现 `top.signature.IModuleSignature` 的 `ZzzModuleSignature` 类，用于向发现工具或 GUI 元数据系统提供简短说明与标签名。

### 可运行入口的边界

仓库中存在大量 `main()` 方法。其中一部分是长期维护的、面向用户的 CLI 工具；另一部分则是一过性的脚本、调试入口或临时执行辅助。因此，`cli.tools.ListTools` 应被理解为“精选清单”，而不是“全部可运行类总表”。

### 以包为中心的结构

本仓库主要按领域分包，而不是按经典分层应用架构组织。对实际维护而言，包级导读通常比逐类罗列 API 更有帮助。

## 文档地图

- [understand_packages1_core_structure_zh.md](understand_packages1_core_structure_zh.md) - 模块元数据、树结构、系统发育与遗传密码
- [understand_packages2_file_io_zh.md](understand_packages2_file_io_zh.md) - FASTA、TSV 与 MSA 输入输出
- [understand_packages3_parsers_zh.md](understand_packages3_parsers_zh.md) - 外部数据解析、REST、分析与系统发育相关包
- [understand_packages4_utilities_zh.md](understand_packages4_utilities_zh.md) - 通用工具、CLI 辅助、GUI 小工具与统一输出
- [understand_packages5_visualization_zh.md](understand_packages5_visualization_zh.md) - 图形引擎、树显示与 tanglegram 代码
- [design/axis_tick_calculation_zh.md](design/axis_tick_calculation_zh.md) - 轴刻度行为的专题说明

## 维护约定

- 记录当前真实存在的包与代表性入口，不要把文档继续扩展成历史类名大全。
- 除非依赖版本会直接影响仓库行为，否则不要在此嵌入大段版本表。
- 面向用户的 CLI 工具发生变化时，应同时检查 `cli.tools.ListTools` 与工具类导读。
