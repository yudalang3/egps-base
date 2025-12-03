# eGPS-Base Package Documentation - Part 2: File I/O & Data Formats

## Overview

This document covers packages responsible for reading and writing biological data formats including FASTA, TSV, and multiple sequence alignment formats.

---

## 1. Sequence File I/O

### 1.1 `fasta.io` - FASTA File Operations

**Purpose:** Read and write FASTA format files (sequences)

#### Key Classes:

**`ZzzModuleSignature`** (Class)
- **Function:** Module signature for FASTA file operations
- **Key Features:**
  - High-performance sequence file I/O
  - Multiple format support (compressed/uncompressed)
  - Stream processing capabilities
- **Description:** FASTA file I/O operations module providing efficient reading and writing of sequence files with support for various compression formats
- **Importance:** ⭐⭐⭐⭐⭐ (Essential for sequence data)

**`Script_*` Processing Scripts**
- **Function:** Specialized batch processing utilities for sequence data
- **Key Features:**
  - Automated file organization and preprocessing
  - Longest CDS extraction for Ensembl data
  - Sequence uniqueness filtering and analysis
  - Homology detection integration
- **Scripts Include:**
  - `Script_allocate_raw_pep_to_dir_by_fileName`: Organize protein files by naming patterns
  - `Script_get_longest_CDS_forEnsembl_pepFa`: Extract longest coding sequences
  - `Script_get_unique_forEnsembl_pepFa`: Filter unique protein sequences
  - `Script_homoGene_finder_blastpWithHmmer`: Homology gene detection
  - `Script_prepare_longestUnique_forEnsembl_pepFa`: Prepare non-redundant datasets
- **Usage:** Command-line execution for large-scale sequence processing
- **Importance:** ⭐⭐⭐⭐ (Automation and preprocessing)

**`FastaReader`** (Class)
- **Function:** High-performance FASTA file reader
- **Key Features:**
  - Handles large files (100MB+ buffers)
  - Automatic compression detection (.gz, .bz2, .xz)
  - Stream processing for memory efficiency
  - Uppercase conversion
  - Multiple input sources (File, String, InputStream)
- **Key Methods:**
  - `readFastaSequence(String)`: Read entire file
  - `readAndProcessFastaPerEntry(BiPredicate)`: Stream processing
- **Performance:** Optimized for bioinformatics large files
- **Importance:** ⭐⭐⭐⭐⭐ (Essential for sequence data)

---

### 1.2 `fasta.stat` - FASTA Statistics

**Purpose:** Statistical analysis of FASTA sequence files

#### Key Classes:

**`UniqueStat`** (Class)
- **Function:** Analyze sequence uniqueness and detect duplicates
- **Key Features:**
  - Identify identical sequences across entries
  - Calculate repeat ratio (0.0 to 1.0)
  - Frequency ranking (sort by duplicate count)
  - Entry name tracking (list all names sharing same sequence)
- **Key Methods:**
  - `getRatio(String)`: Calculate repeat ratio
  - main(String[]): CLI interface
- **Output Format:**
  - `[count]\t[name1;name2;...]` for duplicates
  - Repeat ratio: duplicate_count / total_count
- **Algorithm:**
  - Build sequence-to-count and sequence-to-names mappings
  - Sort by duplicate count (descending)
  - Print entries with count > 1
- **Use Cases:**
  - Quality control for sequence datasets
  - Detect PCR duplicates or artifacts
  - Assess dataset diversity
  - Find contamination or mislabeling
- **Importance:** ⭐⭐⭐⭐ (Quality control)

**`BatchUniqueStat`** (Class)
- **Function:** Batch processor for calculating unique sequence statistics across multiple FASTA files
- **Key Features:**
  - Directory traversal for batch processing
  - File suffix filtering
  - Automated statistics generation
  - Tabular output format
- **Key Methods:**
  - `main(String[])`: CLI entry point (args: directory path, file suffix)
  - `doStats(Path)`: Process single file
- **Output Format:**
  - `[filename]\t[repeat_ratio]` for each file
- **Use Cases:**
  - Batch quality control for multiple datasets
  - Dataset comparison and selection
  - Large-scale sequence curation
- **Importance:** ⭐⭐⭐⭐ (Batch processing)

**`ZzzModuleSignature`** (Class)
- **Function:** Module signature for FASTA statistics tools
- **Description:** Statistical analysis module for FASTA files providing uniqueness analysis and quality control
- **Importance:** ⭐⭐⭐ (Module metadata)

**Importance:** ⭐⭐⭐⭐ (Sequence quality control)

---

### 1.3 `fasta.comparison` - FASTA Comparison Tools

**Purpose:** Compare and visualize sequence differences

#### Key Classes:

**`FastaComparer`** (Class)
- **Function:** Compare two FASTA files using BLAST/Diamond alignment results
- **Key Features:**
  - Alignment-based comparison (fmt6 format)
  - Coverage analysis for query and subject
  - Unmapped sequence detection
  - Name parsing (handles spaces)
- **Input Files:**
  - Query FASTA file
  - Subject FASTA file
  - BLAST/Diamond fmt6 alignment file
- **Fmt6 Format:** `query_id\tsubject_id\t[columns...]`
- **Key Methods:**
  - run(String, String, String): Compare two FASTAs with alignment
  - readFasta(String, List): Extract sequence names
- **Output:**
  - Match counts and totals for both files
  - Coverage ratios (matched/total)
  - Unmapped sequences (if ratio > 1.0)
- **Use Cases:**
  - Validate BLAST completeness
  - Compare ortholog coverage
  - Assess database coverage
  - QC for homology searches
- **Importance:** ⭐⭐⭐⭐ (Alignment validation)

**`PairwiseSeqDiffPrinter`** (Class)
- **Function:** Visualize pairwise sequence alignment differences
- **Key Features:**
  - Dual marking modes: mark matches (|) or mismatches (^, *)
  - Wrapped display (configurable column width)
  - Gap visualization (· for display, ∅ for logic)
  - Position tracking (1-based indexing)
  - Identity percentage calculation
- **Key Methods:**
  - `printDiff(String, String, int)`: Mark matches with '|'
  - `printDiffMarkingMismatches(String, String, int, char)`: Mark mismatches
- **Output Format:**
  ```
           [start]-[end]
  A:        [sequence_A]
            [markers]
  B:        [sequence_B]
  ```
- **Mismatch Format:** `position:char_A→char_B`
- **Use Cases:**
  - Visualize protein alignment differences
  - Compare predicted vs. reference sequences
  - Identify mutation positions
  - Quality control for sequence editing
- **Importance:** ⭐⭐⭐⭐ (Sequence comparison)

**`ZzzModuleSignature`** (Class)
- **Function:** Module signature for FASTA comparison tools
- **Description:** FASTA comparison module providing sequence alignment validation and difference visualization
- **Importance:** ⭐⭐⭐ (Module metadata)

**Importance:** ⭐⭐⭐⭐ (Sequence comparison and QC)

---

## 2. Tabular Data I/O

### 2.1 `tsv.io` - TSV File Operations

**Purpose:** Tab-Separated Values file reading and writing

#### Key Classes:

**`TSVReader`** (Class)
- **Function:** Flexible TSV file parser
- **Key Features:**
  - Header support (optional)
  - Comment line filtering (# symbol)
  - Column-wise data access
  - Multiple output formats
  - 100MB buffer for large files
- **Key Methods:**
  - `readTsvTextFile(String)`: Read as KitTable
  - `readAsKey2ListMap(String)`: Read as column map
  - `organizeMap(String, String, String)`: Extract two columns
- **Importance:** ⭐⭐⭐⭐ (Common data format)

**`TSVWriter`** (Class)
- **Function:** Write tabular data to TSV format
- **Key Features:**
  - Column-to-row conversion
  - Automatic header generation
  - Tab delimiter
  - Buffered writing for efficiency
- **Input Format:** Map<String, List<String>> (column-oriented)
- **Output Format:** Standard TSV with headers
- **Key Method:**
  - `write(Map<String, List<String>>, String)`: Write data to file
- **Use Cases:**
  - Export analysis results
  - Generate reports
  - Data interchange
- **Importance:** ⭐⭐⭐⭐ (Data export)

**`KitTable`** (Class)
- **Function:** Container for tabular data
- **Key Features:**
  - Header names storage
  - Content storage (List<List<String>>)
  - Original line preservation
  - Dimension queries (rows/columns)
  - Save to file capability
- **Data Structure:**
  - headerNames: List<String>
  - contents: List<List<String>>
  - originalLines: List<String>
  - path: String (optional)
- **Key Methods:**
  - `getNumOfRows()`: Get row count
  - `getNumOfColum()`: Get column count
  - `save2file(String)`: Write to TSV
- **Importance:** ⭐⭐⭐⭐ (Data container)

**`IntTable`** (Class)
- **Function:** Integer-based table data structure
- **Key Features:**
  - Optimized for integer data
  - Efficient storage and retrieval
- **Use Cases:**
  - Count matrices
  - Integer-based data tables
- **Importance:** ⭐⭐⭐ (Integer tables)

**`SpaceTransformer`** (Class)
- **Function:** Utility for transforming whitespace in strings
- **Key Features:**
  - Space normalization
  - Tab/space conversion
  - Whitespace cleanup
- **Use Cases:**
  - TSV file preprocessing
  - String formatting
- **Importance:** ⭐⭐⭐ (String transformation)

**`ZzzModuleSignature`** (Class)
- **Function:** Module signature for TSV I/O tools
- **Description:** TSV file operations module for reading and writing tabular data
- **Importance:** ⭐⭐⭐ (Module metadata)

**Importance:** ⭐⭐⭐⭐ (Tabular data handling)

---

## 3. Multiple Sequence Alignment I/O

### 3.1 `msaoperator` - Multiple Sequence Alignment

**Purpose:** Operations on multiple sequence alignments

#### Key Classes:

**`DataforamtInfo`** (Class)
- **Function:** Data format validation result container
- **Key Components:**
  - `isSuccess`: Whether format validation succeeded
  - `dataFormatCode`: Format identifier or error code
  - `dataForamtPrivateInfor`: Optional format-specific metadata
- **Use Cases:**
  - MSA file format detection
  - Format validation reporting
- **Importance:** ⭐⭐⭐ (Format validation)

**`DataForamtPrivateInfor`** (Interface)
- **Function:** Contract defining MSA format private information
- **Key Features:**
  - Format identification and compatibility checking
  - Error reporting and detailed descriptions
  - Format-specific metadata support
- **Key Methods:**
  - `isCompatiable(DataForamtPrivateInfor)`: Compatibility checking
  - `getFormatName()`: Get format name
- **Use Cases:**
  - Pre-merge MSA format validation
  - Format-specific metadata processing
  - Dynamic format detection and validation
- **Importance:** ⭐⭐⭐⭐ (Format compatibility foundation)

**`DefaultDataFormatPrivateInfor`** (Class)
- **Function:** Default implementation of DataForamtPrivateInfor
- **Key Features:**
  - Universal compatibility (compatible with all formats)
  - No format constraints default behavior
  - Fallback option and placeholder usage
- **Compatibility Strategy:**
  - Always returns compatible (true)
  - No error messages (null)
  - Zero constraint restrictions
- **Use Cases:**
  - Default format for uninitialized MSA objects
  - Format compatibility in testing scenarios
  - Universal format adapter
- **Importance:** ⭐⭐⭐ (Default format handling)

**`AlignmentPreprocesser`** (Class)
- **Function:** Preprocess alignment data
- **Key Features:**
  - Gap handling
  - Sequence trimming
- **Importance:** ⭐⭐⭐ (Alignment preprocessing)

**`CompeleDeletion`** (Class)
- **Function:** Complete deletion handler that removes any alignment column containing gaps or missing data
- **Key Features:**
  - Removes columns with any gap character
  - Preserves only fully-resolved columns
  - Extends DeletionHandler base class
- **Key Methods:**
  - `dealWithDeletion(String[])`: Process alignment with complete deletion
- **Use Cases:**
  - Conservative alignment cleaning
  - Phylogenetic analysis requiring complete data
  - Distance matrix calculations
- **Importance:** ⭐⭐⭐⭐ (Gap handling strategy)

**`PairwiseDeletion`** (Class)
- **Function:** Pairwise deletion handler for alignment processing
- **Key Features:**
  - Removes gaps only for pairwise comparisons
  - Retains maximum sequence information
- **Use Cases:**
  - Pairwise distance calculations
  - Maximum data retention scenarios
- **Importance:** ⭐⭐⭐ (Gap handling strategy)

**`PartialDeletion`** (Class)
- **Function:** Partial deletion handler with threshold-based column removal
- **Key Features:**
  - Removes columns based on gap threshold
  - Configurable gap tolerance
  - Balance between data retention and quality
- **Use Cases:**
  - Flexible alignment cleaning
  - User-defined quality thresholds
- **Importance:** ⭐⭐⭐ (Gap handling strategy)

**`DeletionHandler`** (Abstract Class)
- **Function:** Abstract base class for alignment gap deletion strategies
- **Key Features:**
  - Template pattern for deletion algorithms
  - Nucleotide validation support
  - Common gap handling logic
- **Key Methods:**
  - `dealWithDeletion(String[])`: Abstract deletion method
- **Use Cases:**
  - Implementing custom deletion strategies
  - Standardizing gap handling
- **Importance:** ⭐⭐⭐⭐ (Gap handling framework)

**`GlobalAlignmentSettings`** (Class)
- **Function:** Global settings for alignment operations
- **Key Features:**
  - Centralized configuration management
  - Alignment parameter storage
- **Importance:** ⭐⭐⭐ (Configuration management)

**`MSAFileParser`** (Class)
- **Function:** Parser for multiple sequence alignment files supporting various MSA formats
- **Key Features:**
  - Multi-format support (ClustalW, FASTA, PHYLIP, NEXUS, PAML, MEGA, GCG MSF)
  - Format-specific parsers selection
  - Unified parsing interface
- **Key Methods:**
  - `parserMSAFile(File, MSA_DATA_FORMAT)`: Parse MSA file with specified format
- **Supported Formats:**
  - ALIGNED_CLUSTALW: ClustalW format
  - ALIGNED_FASTA: FASTA alignment
  - ALIGNED_PHYLIP: PHYLIP format
  - ALIGNED_PAML: PAML format
  - ALIGNED_MEGA: MEGA format
  - ALIGNED_GCGMSF: GCG MSF format
  - NEXUS_SEQ: NEXUS format
- **Use Cases:**
  - Reading alignment files from different tools
  - Format conversion pipelines
  - Alignment data import
- **Importance:** ⭐⭐⭐⭐⭐ (Essential MSA I/O)

**Sub-packages:**
- `io`: MSA file I/O (MSAFileParser, format parsers/writers)
- `io.seqFormat`: Format-specific implementations (AbstractParser, AbstractWriter, MSA_DATA_FORMAT)
- `io.seqFormat.parser`: Format parsers (ClustalWParser, FastaParser, NEXUSParser, PAMLParser, PHYParser, GCGMSFParser, MEGAParser)
- `io.seqFormat.writer`: Format writers (ClustalWriter, FastaWriter, PAMLWriter, PHYLIPWriter)
- `io.seqFormat.model`: Data models (CLUElementBase, ClustalWSequenceData, EMBLSeqElement, SeqElementBase)
- `alignment`: MSA manipulations and gap handling (CompeleDeletion, PairwiseDeletion, PartialDeletion, DeletionHandler, GlobalAlignmentSettings)
- `alignment.sequence`: Sequence-level operations (Sequence, SequenceComponentRatio)

**Key Features:**
- Read/write alignment formats (FASTA, PHYLIP, NEXUS, ClustalW, PAML, MEGA, GCG MSF)
- Column operations
- Consensus sequences
- Gap deletion strategies (complete, pairwise, partial)

**Importance:** ⭐⭐⭐⭐ (MSA operations)

---

## Package Importance Summary

### ⭐⭐⭐⭐⭐ Critical (Must-use):
1. `fasta.io` - Sequence file I/O
2. `msaoperator` (MSAFileParser) - Essential MSA I/O

### ⭐⭐⭐⭐ Important (Frequently-used):
1. `tsv.io` - Tabular data
2. `fasta.stat` - Sequence quality control
3. `fasta.comparison` - Sequence comparison
4. `msaoperator` (gap handlers) - Alignment preprocessing

---

## Typical Usage Patterns

### Pattern 1: Reading FASTA Files
```
1. Create reader: new FastaReader()
2. Read sequences: readFastaSequence(path)
3. Process: iterate over sequences
4. Analyze: use fasta.stat for QC
```

### Pattern 2: Processing MSA Files
```
1. Parse alignment: MSAFileParser.parserMSAFile()
2. Validate format: DataforamtInfo
3. Handle gaps: CompeleDeletion/PairwiseDeletion
4. Export: Use format writers
```

### Pattern 3: TSV Data Processing
```
1. Read TSV: TSVReader.readTsvTextFile()
2. Process data: KitTable operations
3. Write results: TSVWriter.write()
```

---

*This document covers File I/O and Data Format packages. See other parts for Core Structures, Utilities, Parsers, and Visualization.*
