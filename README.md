# egps-base

`egps-base` is the open-source base library of the eGPS 2.1 ecosystem. It provides reusable Java components for evolutionary genomics, phylogenetic analysis, sequence processing, tabular data handling, graphics, and small utility tools.

[中文版 README](README_zh.md)

## Highlights

- generic and phylogeny-specific tree structures, codecs, and utilities
- FASTA, TSV, and multiple-sequence-alignment readers, writers, and helpers
- BLAST, Pfam, and NCBI taxonomy parsers, plus Ensembl and InterPro integration
- graphics, tree display, tanglegram, and axis-tick utilities
- command-line and small GUI convenience tools

## Requirements

- Java 25
- Maven

## Quick Start

```bash
mvn compile
mvn test
mvn clean package source:jar
```

If you want a local runtime dependency directory for CLI usage:

```bash
mvn dependency:copy-dependencies -DoutputDirectory=dependency-egps
java -cp "target/classes:dependency-egps/*" cli.tools.ListTools
```

Repository helper scripts:

```bash
./build_jar.sh /absolute/output_dir
./build_jar_and_remove.sh
```

`build_jar.sh` packages the current `target/classes` tree, so compile or package first when needed.

## Documentation

- repository-local guides: [docs/README.md](docs/README.md)
- broader project notes: https://www.yuque.com/u21499046/egpsdoc

## Project Position

- This repository is the reusable low-level layer of eGPS 2.1.
- The main GUI application layer is not part of this repository.
- Source-oriented package guides live under `docs/`.
