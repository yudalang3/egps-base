# eGPS-Base Project Overview

[Docs Index](README.md) | [中文](global_understand_zh.md)

## Document Role

This page records stable repository facts and provides a reading map for the rest of the documentation set. It is intentionally concise: detailed package descriptions live in the package guides.

## Stable Repository Facts

- project: `egps-base`
- language: Java 25
- build tool: Maven
- packaging: `jar`
- main sources: `src/main/java`
- tests: `src/test/java`
- non-Java files under `src/main/java` are packaged as resources

At a functional level, the repository acts as the reusable base library of the eGPS 2.1 ecosystem. Its code spans sequence processing, phylogenetic trees, tabular data, graphics, REST integration, and general utilities.

## Code Layout

### Main source domains

- `top.signature` - module metadata and discovery conventions
- `evoltree.*` - tree structures, phylogeny, display, and tanglegrams
- `fasta.*` - FASTA I/O, comparison, and statistics
- `msaoperator.*` - multiple-sequence-alignment parsing and writing
- `tsv.io` - TSV and table helpers
- `blast.parse`, `pfam.parse`, `ncbi.taxonomy` - domain parsers
- `rest.*` - Ensembl and InterPro integration
- `graphic.engine` - charting and graphics infrastructure
- `cli.tools`, `gui.simple.tools` - small runnable utilities
- `utils.*`, `unified.output` - general helper packages

### Test layout

- `src/test/java/evoltree/phylogeny` - tree codec tests
- `src/test/java/evoltree/txtdisplay` - text tree rendering tests
- `src/test/java/graphic/engine` - axis tick calculation tests
- `src/test/java/script/run/once` - one-off scripts rather than stable automated tests
- `src/test/java/os` and `src/test/java/tab` - smaller environment- or feature-specific checks

## Build And Test Entry Points

### Build

```bash
mvn compile
mvn clean package source:jar
mvn dependency:copy-dependencies -DoutputDirectory=/absolute/output/dir
```

### Tests

```bash
mvn test
mvn -Dtest=PhyloTreeEncoderDecoderTest test
mvn -Dtest=evoltree.phylogeny.PhyloTreeEncoderDecoderTest test
mvn -Dtest=PhyloTreeEncoderDecoderTest#testEncodeSimpleTree test
```

### Repository JAR scripts

```bash
./build_jar.sh /absolute/output_dir
./build_jar_and_remove.sh
```

`build_jar.sh` packages the current `target/classes` directory, so it should be used after a fresh compile or package step when class files may be stale.

## Organizing Conventions

### Module signature convention

Many functional packages expose a `ZzzModuleSignature` class that implements `top.signature.IModuleSignature`. These classes provide short descriptions and GUI-facing tab names for discovery-oriented tooling.

### Runnable entry points

The repository contains many `main()` methods. Some are maintained, user-facing CLI tools; others are one-off scripts, debugging helpers, or ad hoc execution entry points. `cli.tools.ListTools` is therefore a curated registry, not a full inventory of every runnable class.

### Package-oriented structure

The codebase is organized primarily by domain package rather than by a layered application architecture. In practice, package guides are more useful than exhaustive class-by-class summaries.

## Document Map

- [understand_packages1_core_structure.md](understand_packages1_core_structure.md) - module metadata, trees, phylogeny, and genetic codes
- [understand_packages2_file_io.md](understand_packages2_file_io.md) - FASTA, TSV, and MSA I/O
- [understand_packages3_parsers.md](understand_packages3_parsers.md) - external data parsers, REST, analysis, and phylogeny-facing packages
- [understand_packages4_utilities.md](understand_packages4_utilities.md) - general utilities, CLI helpers, GUI helpers, unified output
- [understand_packages5_visualization.md](understand_packages5_visualization.md) - graphics engine, tree display, and tanglegram code
- [design/axis_tick_calculation.md](design/axis_tick_calculation.md) - focused note for axis tick behavior

## Maintenance Notes

- Document current packages and representative entry points rather than attempting an exhaustive historical class list.
- Avoid embedding dependency version tables here unless a version directly affects repository behavior.
- When user-facing CLI tools change, review both `cli.tools.ListTools` and the utility guide.
