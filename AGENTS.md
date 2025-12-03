# AGENTS.md

This file provides guidance to Qoder (qoder.com) when working with code in this repository.

## Build System

This is a Maven-based Java 25 project.

### Common Commands

**Build the project:**
```bash
mvn clean install
```

**Compile only:**
```bash
mvn compile
```

**Run tests:**
```bash
mvn test
```

**Package:**
```bash
mvn package
```

## Architecture Overview

### Module System

eGPS-Base is a modular bioinformatics library with a signature-based architecture. Every module implements the `IModuleSignature` interface (in `top.signature` package) which provides:
- `getShortDescription()`: Module functionality description (HTML supported)
- `getTabName()`: Display name for GUI integration

Each module contains a `ZzzModuleSignature` class implementing this interface, making modules self-documenting and GUI-compatible.

### Core Data Structures

**Tree/Graph Nodes (`evoltree.struct` package):**
- `EvolNode`: Top-level interface for phylogenetic tree nodes
- Supports both single and multiple parent relationships (most implementations use single parent only)
- Two main implementations:
  - `ArrayBasedNode`: Array-backed, single parent only
  - `LinkedBasedNode`: Linked-list backed
- All methods managing children must maintain consistency between up-links (child→parent) and down-links (parent→child)

**Tree I/O (`evoltree.phylogeny` package):**
- Uses encoder/decoder pattern
- `NWKLeafCoderDecoder`: Handles leaf nodes in Newick format
- `NWKInternalCoderDecoder`: Handles internal nodes
- `PhyloTreeEncoderDecoder`: Complete tree serialization

**Genetic Codes (`geneticcodes` package):**
- `IGeneticCode`: Interface for genetic code tables
- `AbstractGeneticCodes`: Base implementation
- Various genetic code tables in `geneticcodes.codeTables`

### Key Modules

**Analysis (`analysis`):** Script execution helpers and mathematical utilities
**BLAST Parser (`blast.parse`):** Parse BLAST output files and HSP records
**CLI Tools (`cli.tools`):** Command-line utilities (tree validation, path normalization, file counting)
**FASTA I/O (`fasta.io`):** 
- Uses large buffers (10-100MB) for efficient processing
- `FastaReader` supports compressed files and stream processing
- Methods: `readFastaSequence()` for full file, `readAndProcessFastaPerEntry()` for streaming

**MSA Operator (`msaoperator`):** Multiple sequence alignment operations
**NCBI Taxonomy (`ncbi.taxonomy`):** Parse NCBI taxonomy database with `TaxonomyParser`
**Pfam Parse (`pfam.parse`):** Parse HMMER and Pfam domain results
**Phylo (`phylo`):** Phylogenetic algorithms including Robinson-Foulds metric
**REST (`rest`):** Ensembl and InterPro REST API clients
**TSV I/O (`tsv.io`):** TSV/Excel file parsing with bean mapping
**Graphics Engine (`graphic.engine`):** Swing-based visualization components, color mapping, axis calculation
**Utils (`utils`):** General utilities including file I/O, collections, string operations, memory monitoring

## Technology Stack

**Core:**
- Java 25
- Maven build system
- UTF-8 encoding

**Major Libraries:**
- Guava 33.4.8-jre (Google core libraries)
- Apache Commons (Lang3, CLI, IO, Compress)
- FastJSON 1.2.83 (Swing-compatible version)

**GUI:**
- FlatLaf 3.6 (modern Look & Feel)
- SwingX 1.6.1, JIDE-OSS 3.7.15
- MigLayout 4.2
- XChart 3.8.8 (charting)

**Bioinformatics:**
- HTSJDK 4.3.0 (high-throughput sequencing data)

**Document Processing:**
- iTextPDF 5.1.3 (last standalone version - do not upgrade)
- Apache POI 5.4.1 (Excel/Office files)

**Testing:**
- JUnit Jupiter 5.11.1

**Logging:**
- Log4j2 2.24.3 (core, api, slf4j2-impl)

## Code Conventions

- Interface-based design pattern throughout the codebase
- Builder pattern for complex object creation (e.g., `UnifiedPrinterBuilder`)
- Encoder/Decoder pattern for serialization
- Module signature pattern for metadata and GUI integration

## Development Notes

- Tests include one-time analysis scripts in `src/test/java/script/run/once/`
- Large file processing uses 10-100MB buffers for performance
- Dependency versions are intentionally locked for stability (see comments in pom.xml)
- Resources in `src/main/java` are included in build (excludes .java files)
