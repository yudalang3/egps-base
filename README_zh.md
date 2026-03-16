# egps-base

`egps-base` 是 eGPS 2.1 生态中已经开源的基础库，提供一组可复用的 Java 组件，用于演化基因组学、系统发育分析、序列处理、表格数据处理、图形绘制以及若干便捷工具。

[English README](README.md)

## 主要内容

- 通用树结构、系统发育树结构及其编解码与辅助工具
- FASTA、TSV 与多序列比对格式的读写与处理能力
- BLAST、Pfam、NCBI taxonomy 解析，以及 Ensembl、InterPro 相关集成
- 图形绘制、树显示、tanglegram 与轴刻度计算工具
- 命令行工具与小型 GUI 便捷工具

## 环境要求

- Java 25
- Maven

## 快速开始

```bash
mvn compile
mvn test
mvn clean package source:jar
```

如果你希望为 CLI 使用准备本地依赖目录，可以执行：

```bash
mvn dependency:copy-dependencies -DoutputDirectory=dependency-egps
java -cp "target/classes:dependency-egps/*" cli.tools.ListTools
```

仓库内还提供了两个辅助脚本：

```bash
./build_jar.sh /absolute/output_dir
./build_jar_and_remove.sh
```

`build_jar.sh` 会直接打包当前的 `target/classes`，因此在需要时应先完成编译或打包。

## 文档入口

- 仓库内导读： [docs/README_zh.md](docs/README_zh.md)
- 更完整的项目说明： https://www.yuque.com/u21499046/egpsdoc

## 项目定位

- 本仓库是 eGPS 2.1 中可复用的底层基础层。
- 主 GUI 应用层不在本仓库中维护。
- 面向源码的分册导读位于 `docs/` 目录。
