# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**eGPS-Base** is a comprehensive bioinformatics utility library for evolutionary genomics and phylogenetic analysis. It serves as the foundational layer in the eGPS 2.1 ecosystem, providing core infrastructure without dependencies. The project is written in Java 25 and uses Maven for build management.

Full documentation available at: https://www.yuque.com/u21499046/egpsdoc

## Build & Development Commands

### Building the Project

```bash
# Build and package (creates JAR in target/)
mvn clean package source:jar

# Using the build script (alternative)
./build_jar.sh [output_directory]  # defaults to /tmp
```

This generates:
- `target/egps-base-0.0.1-SNAPSHOT.jar` - Main JAR
- `target/egps-base-0.0.1-SNAPSHOT-sources.jar` - Sources JAR

### Deploying Dependencies

```bash
# Export all dependency JARs to a directory
mvn dependency:copy-dependencies -DoutputDirectory=/Users/dalang/Documents/software/egps-collection/deployed
```

### Testing

```bash
# Run all tests
mvn test

# Run a specific test class
mvn test -Dtest=ClassName

# Run tests in a specific package
mvn test -Dtest="package.name.*"
```

### Running Command-Line Tools

```bash
# On macOS - GUI example
java -cp "dependency-egps/*" gui.simple.tools.FilePathNormalizedGUI

# Running CLI tools
java -cp "target/classes:dependency-egps/*" cli.tools.ListTools
```

To discover all available CLI tools with main() methods, run:
```bash
java -cp "target/classes:dependency-egps/*" cli.tools.ListTools
```

## Architecture

### Module System

The project uses a **module signature pattern** where each functional area is organized as a "module":

- Every module has a `ZzzModuleSignature` class implementing `IModuleSignature`
- This interface provides:
  - `getShortDescription()` - Module functionality description
  - `getTabName()` - Display name for GUI integration
- Modules are discovered via reflection and integrated into the GUI framework

### Package Structure

Top-level packages organized by domain:

- **`top.signature`** - Module signature framework (IModuleSignature interface)
- **`evoltree`** - Phylogenetic tree structures, algorithms, and visualization
  - `evoltree.struct` - Core tree data structures (EvolNode, ArrayBasedNode)
  - `evoltree.phylogeny` - Phylogenetic tree formats (Newick, Nexus)
  - `evoltree.tanglegram` - Tree comparison and tanglegram visualization
  - `evoltree.txtdisplay` - Text-based tree rendering
- **`msaoperator`** - Multiple sequence alignment operations
  - `msaoperator.io` - MSA file I/O (FASTA, PHYLIP, NEXUS, PAML)
  - `msaoperator.alignment` - Gap handling strategies (pairwise/complete deletion)
- **`fasta`** - FASTA file operations
  - `fasta.io` - Reading/writing FASTA files
  - `fasta.stat` - Sequence statistics
  - `fasta.comparison` - Sequence comparison tools
- **`geneticcodes`** - Genetic code tables and codon translation
  - `geneticcodes.codeTables` - Standard and variant genetic codes
- **`graphic.engine`** - Swing/GUI infrastructure and visualization
  - `graphic.engine.drawer` - Custom drawing components
  - `graphic.engine.legend` - Legend rendering
  - `graphic.engine.guirelated` - Swing utilities and vector export
- **`blast.parse`** - BLAST output parsing
- **`pfam.parse`** - Pfam domain annotation parsing
- **`ncbi.taxonomy`** - NCBI taxonomy data parsing
- **`phylo`** - Phylogenetic analysis algorithms
  - `phylo.gsefea` - Gene set enrichment for evolutionary analysis
  - `phylo.algorithm` - Tree metrics (Robinson-Foulds distance)
- **`rest`** - REST API clients (Ensembl, InterPro)
- **`cli.tools`** - Command-line utilities
- **`gui.simple.tools`** - Simple GUI utilities
- **`tsv.io`** - TSV/table data structures
- **`utils`** - General utilities (datetime, storage, string, collections)
- **`unified.output`** - Standardized output formatting
- **`analysis.math`** - Mathematical analysis utilities

### Core Data Structures

**Tree Nodes:**
- `EvolNode` interface - Foundation for all tree operations (30+ methods)
- `ArrayBasedNode` - Array-backed implementation (recommended, fast child access)
- `LinkedBasedNode` - Linked-list implementation (educational, memory-efficient)

**Tree Serialization:**
- `TreeCoder` interface - Encode trees to strings
- `TreeDecoder` interface - Parse strings to trees
- `AbstractNodeCoderDecoder` - Base class with template pattern

**Sequence Alignments:**
- Support for FASTA, PHYLIP, NEXUS, PAML formats
- Deletion strategies: Complete, Pairwise, Partial

### Technology Stack

- **Java 25** (requires Java 25 or higher)
- **GUI**: FlatLaf, SwingX, JIDE-OSS, MigLayout
- **Visualization**: XChart, JSVG
- **Bioinformatics**: HTSJDK (high-throughput sequencing)
- **Data**: FastJSON 1.2.83, Apache Commons (Lang3, IO, CLI, Compress)
- **Documents**: iTextPDF 5.1.3, Apache POI 5.4.1
- **Logging**: Log4j2 2.24.3
- **Testing**: JUnit Jupiter

**Important:** FastJSON is pinned at 1.2.83 (version 2 has Swing compatibility issues). iTextPDF is pinned at 5.1.3 (last standalone version).

## Key Implementation Patterns

### Adding New CLI Tools

When creating new command-line tools with `main()` methods, register them in `cli.tools.ListTools`:

```java
addEntry(YourNewTool.class.getName(), "Description of what the tool does");
```

### Module Registration

All new functional modules should:
1. Create a `ZzzModuleSignature` class implementing `IModuleSignature`
2. Provide meaningful `getShortDescription()` and `getTabName()` implementations
3. Follow the naming convention for automatic module discovery

### Tree Node Implementation

The preferred tree implementation is `ArrayBasedNode`:
- Fast O(1) child access via ArrayList
- Production-ready and well-tested
- Use for all new phylogenetic tree code

### Number Formatting in Trees

All branch lengths and numeric values in trees use:
- 6 decimal places precision
- HALF_UP rounding mode
- Defined in `AbstractNodeCoderDecoder`

## Development Notes

### Before Git Push

See `check_before_gitpush.md` for pre-push checklist:
1. Update `cli.tools.ListTools` with any new CLI tools
2. Sync README.md and README_zh.md if either changed

### Resources in Build

Maven is configured to include all resources from `src/main/java` except `.java` files, allowing co-location of data files with code.

### Test Structure

Tests are organized under `src/test/java/`:
- `evoltree/` - Tree structure tests
- `module/` - Module system tests
- `os/` - OS-specific tests
- `script/` - Script execution tests
- `tab/` - Tab/GUI component tests

### Documentation Location

Comprehensive documentation is in the `docs/` directory:
- `global_understand.md` - Project overview
- `understand_packages1_core_structure.md` - Core structures
- `understand_packages2_file_io.md` - File I/O systems
- `understand_packages3_parsers.md` - Parser implementations
- `understand_packages4_utilities.md` - Utility packages
- `understand_packages5_visualization.md` - Visualization components

(Chinese versions available with `_zh.md` suffix)

## IDE Setup

Project is designed for **IntelliJ IDEA**:
- Import as Maven project
- Set Java SDK to Java 25
- Maven auto-import will handle dependencies
- Run configurations can reference `target/classes` and `dependency-egps/*` classpath
