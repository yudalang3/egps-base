# eGPS-Base Package Documentation - Part 3: Parsers & Bioinformatics Tools

## Overview

This document covers packages for parsing bioinformatics file formats and integrating with external databases and tools.

---

## 1. BLAST Tools

### 1.1 `blast.parse` - BLAST Parser

**Purpose:** Parse BLAST output files

#### Key Classes:

**`ZzzModuleSignature`** (Class)
- **Function:** Module signature for BLAST parsing tools
- **Key Features:**
  - Multiple BLAST output format support
  - HSP (High-scoring Segment Pair) parsing
  - Alignment score and statistics extraction
- **Description:** BLAST parsing tools module for extracting meaningful information from various BLAST output formats including tabulated and XML formats
- **Importance:** ⭐⭐⭐⭐ (BLAST analysis)

**`BlastHspRecord`** (Class)
- **Function:** Represents BLAST High-scoring Segment Pair
- **Key Features:**
  - Query/subject sequences
  - Alignment scores
  - E-values
  - Identity percentages
- **Importance:** ⭐⭐⭐⭐ (BLAST analysis)

---

## 2. Protein Domain Analysis

### 2.1 `pfam.parse` - Pfam Parser

**Purpose:** Parse Pfam database files and domain annotations

#### Key Classes:

**`PfamScanRecord`** (Class)
- **Function:** Represents a single Pfam domain annotation from hmmscan output
- **Key Features:**
  - Parses hmmscan --domtblout format
  - Domain boundaries (alignment and envelope coordinates)
  - HMM information (Pfam ID, domain name)
  - Statistical scores (bit score, E-value)
  - Clan classification
- **Key Fields:**
  - `seqId`: Protein identifier
  - `alignmentStart/End`: Alignment coordinates on sequence
  - `envelopeStart/End`: Domain boundary
  - `hmmAcc`: Pfam accession (e.g., PF00001)
  - `hmmName`: Domain name
  - `bitScore`, `eValue`: Statistical significance
- **Key Methods:**
  - `parseHmmerScanOut(String)`: Parse entire hmmscan output file
- **Returns:** Map<String, List<PfamScanRecord>> - protein ID to domain list
- **Use Cases:**
  - Protein domain architecture analysis
  - Functional annotation
  - Domain-based phylogenetics
- **Importance:** ⭐⭐⭐⭐⭐ (Essential for domain analysis)

**`HmmDatParser`** (Class)
- **Function:** Parse Pfam HMM .dat files
- **Key Features:**
  - Extract domain metadata
  - Parse HMM descriptions
- **Importance:** ⭐⭐⭐ (Pfam metadata)

**`PfamDBEntry`** (Class)
- **Function:** Represents a Pfam database entry
- **Key Features:**
  - Domain description
  - Clan assignment
  - GO term associations
- **Importance:** ⭐⭐⭐ (Pfam data model)

**`ZzzModuleSignature`** (Class)
- **Function:** Module signature for Pfam parsing tools
- **Description:** Pfam and HMMER parsing module for protein domain annotation and analysis
- **Importance:** ⭐⭐⭐ (Module metadata)

**Importance:** ⭐⭐⭐⭐⭐ (Protein domain analysis)

---

## 3. NCBI Integration

### 3.1 `ncbi.taxonomy` - NCBI Taxonomy Tools

**Purpose:** Tools for NCBI taxonomy data processing

#### Key Classes:

**`TaxonomyParser`** (Class)
- **Function:** Parse NCBI Taxonomy database files
- **Key Features:**
  - Parses nodes.dmp and names.dmp files
  - Constructs complete taxonomic hierarchy tree
  - Handles 18-field node records
  - Compressed file support (.gz)
- **Input Files:**
  - `nodes.dmp`: Taxonomic node relationships
  - `names.dmp`: Taxonomic names and synonyms
- **Key Methods:**
  - `parseTree(String nodePath, String namePath)`: Build taxonomy tree
- **Output:** EvolNode tree representing complete taxonomy
- **Data Source:** NCBI FTP: ftp://ftp.ncbi.nlm.nih.gov/pub/taxonomy/
- **Importance:** ⭐⭐⭐⭐⭐ (Essential for taxonomic classification)

**`TaxonomyNode`** (Class)
- **Function:** Represents a single node in NCBI taxonomy
- **Key Fields:**
  - taxId, parentTaxId, rank (superkingdom, kingdom, etc.)
  - Genetic code IDs (nuclear, mitochondrial, plastid)
  - Division and inheritance flags
- **Importance:** ⭐⭐⭐⭐ (Taxonomy data model)

**`TaxonomicRank`** (Enum)
- **Function:** Enumeration of taxonomic ranks
- **Values:** SUPERKINGDOM, KINGDOM, PHYLUM, CLASS, ORDER, FAMILY, GENUS, SPECIES
- **Importance:** ⭐⭐⭐⭐ (Taxonomy classification)

**`TaxonomyFullNameLineageParser`** (Class)
- **Function:** Parser for NCBI taxonomy ranked lineage files with full taxonomic hierarchy
- **Key Features:**
  - Parses rankedlineage.dmp file (simplified taxonomy format)
  - Direct lineage retrieval by taxon ID
  - Full taxonomic hierarchy in single record
  - Supports all major taxonomic ranks
- **Key Methods:**
  - `parseTree(String)`: Parse rankedlineage.dmp file
  - `getLineage(int)`: Get lineage by taxon ID
  - `getLineage(String)`: Get lineage by species name
  - `getAllTaxonomicRanks()`: Get all parsed taxonomic records
- **Data Fields:**
  - taxId, taxName, species, genus, family, order, class, phylum, kingdom, superkingdom
- **Use Cases:**
  - Quick lineage lookup
  - Taxonomic annotation
  - Species classification
- **Importance:** ⭐⭐⭐⭐⭐ (Fast lineage retrieval)

**`API4R`** (Class)
- **Function:** R language API for NCBI taxonomy lineage retrieval operations
- **Key Features:**
  - R-compatible interface design
  - Batch lineage retrieval
  - Tab-separated output format
  - NA handling for missing data
- **Key Methods:**
  - `getRankedLineages(String, int[])`: Batch retrieve lineages for taxon IDs
  - Returns String[] with tab-separated lineage fields
- **Output Format:**
  - `[taxName]\t[species]\t[genus]\t[family]\t[order]\t[class]\t[phylum]\t[kingdom]\t[superkingdom]`
  - Missing values represented as "NA"
- **Use Cases:**
  - R integration for taxonomic analysis
  - Batch species annotation
  - Phylogenetic context retrieval
- **Importance:** ⭐⭐⭐⭐ (R integration)

**`ZzzModuleSignature`** (Class)
- **Function:** Module signature for NCBI taxonomy tools
- **Description:** NCBI taxonomy module providing parsers and utilities for taxonomic classification and lineage retrieval
- **Importance:** ⭐⭐⭐ (Module metadata)

**Importance:** ⭐⭐⭐⭐⭐ (NCBI integration)

---

## 4. Web Services

### 4.1 `rest` - REST Services

**Purpose:** RESTful web services integration

#### Key Classes:

**`EnsemblTranslationRest`** (Class)
- **Function:** Ensembl REST API client for protein translation services
- **Key Features:**
  - Protein translation API integration
  - Domain annotation query support
  - RESTful web service communication
- **Key Methods:**
  - `getTranslation(String)`: Retrieve protein translation
  - `getProteinDomains(String)`: Query protein domain annotations
- **Use Cases:**
  - Automated protein annotation
  - Gene/protein database integration
  - Bioinformatics workflow automation
- **API Endpoints:**
  - Ensembl REST API for translations
  - Domain annotation services
- **Importance:** ⭐⭐⭐⭐ (Database integration)

**`EnsJsonTreeParserCustomized`** (Class)
- **Function:** Customized Ensembl JSON tree parser with enhanced name formatting and taxonomy extraction
- **Key Features:**
  - Extends EnsJsonTreeParser
  - Human-readable name preference (common name or scientific name)
  - Taxonomy ID extraction and output
  - TimeTree MYA (Million Years Ago) integration
- **Key Methods:**
  - `iterate2transferTree(TreeBean)`: Parse JSON tree to DefaultPhyNode
  - `getOutput()`: Get taxonomy ID to name mappings
- **Output:**
  - Phylogenetic tree with formatted node names
  - List of `[taxonomy_id]\t[human_name]` mappings
- **Use Cases:**
  - Ensembl species tree visualization
  - Taxonomy mapping for phylogenetic analysis
  - Species tree with divergence times
- **Importance:** ⭐⭐⭐⭐ (Ensembl phylogeny integration)

**Sub-packages:**
- `client`: REST client utilities
- `model`: Data models
- `service`: Service implementations
- `ensembl`: Ensembl-specific REST services
- `ensembl.phylo`: Ensembl phylogenetic tree services (EnsJsonTreeParser, EnsJsonTreeParserCustomized, TreeBean, TaxonomyBean, ConfidenceBean, EventsBean, JsonTreeBean)

**Importance:** ⭐⭐⭐⭐ (Web integration)

---

## 5. Analysis Framework

### 5.1 `analysis` - Analysis Framework

**Purpose:** Framework for running analysis scripts and actions

#### Key Classes:

**`AbstractAnalysisAction`** (Abstract Class)
- **Function:** Base class for analysis operations
- **Key Features:**
  - Template pattern for analyses
  - Standard lifecycle methods
- **Importance:** ⭐⭐⭐ (Analysis framework)

---

### 5.2 `analysis.math` - Mathematical Utilities

**Purpose:** Mathematical operations for bioinformatics

#### Key Classes:

**`DoubleListUtils`** (Class)
- **Function:** Operations on lists of doubles
- **Key Features:**
  - Find minimum and maximum values
  - Stream-based processing
  - Support for both List<Double> and double[] arrays
- **Key Methods:**
  - `getMinMax(List<Double>)`: Get min/max from list
  - `getMinMax(double[])`: Get min/max from array
- **Importance:** ⭐⭐⭐ (Statistical operations)

**`RandomArrayGenerator`** (Class)
- **Function:** Generate random arrays for simulations
- **Key Features:**
  - Multiple random number generators (MersenneTwister, Java Random)
  - Reproducible generation with seeds
  - Uniform distribution over [min, max] range
  - High-quality pseudo-random number generation
- **Key Methods:**
  - `generateRandomDoublesByColt()`: MersenneTwister with seed
  - `generateRandomDoubles()`: Simple random generation
  - `generateRandomDoublesByMath3()`: Apache Commons Math3
- **Use Cases:**
  - Monte Carlo simulations
  - Bootstrap resampling
  - Permutation tests
- **Importance:** ⭐⭐⭐⭐ (Simulations & statistical testing)

---

## 6. Phylogenetic Analysis

### 6.1 `phylo.algorithm` - Phylogenetic Algorithms

**Purpose:** Algorithmic tools for phylogenetic analysis

#### Key Classes:

**`ZzzModuleSignature`** (Class)
- **Function:** Module signature for phylogenetic algorithms
- **Description:** Phylogenetic algorithms module providing computational methods for tree analysis and comparison
- **Importance:** ⭐⭐⭐ (Module metadata)

**Importance:** ⭐⭐⭐⭐ (Phylogenetic computation)

---

### 6.2 `phylo.gsefea` - Gene Set Enrichment for Evolutionary Analysis

**Purpose:** Gene set enrichment analysis in evolutionary context

#### Key Classes:

**`ZzzModuleSignature`** (Class)
- **Function:** Module signature for GSEFEA tools
- **Description:** Gene Set Enrichment for Evolutionary Analysis module providing tools for functional enrichment in phylogenetic context
- **Importance:** ⭐⭐⭐ (Module metadata)

**Importance:** ⭐⭐⭐⭐ (Evolutionary enrichment analysis)

---

### 6.3 `phylo.msa.util` - MSA Utilities for Phylogenetics

**Purpose:** Multiple sequence alignment utilities for phylogenetic analysis

#### Key Classes:

**`ZzzModuleSignature`** (Class)
- **Function:** Module signature for phylogenetic MSA utilities
- **Description:** MSA utilities for phylogenetic analysis providing specialized tools for alignment processing in tree inference
- **Importance:** ⭐⭐⭐ (Module metadata)

**Importance:** ⭐⭐⭐⭐ (Phylogenetic MSA processing)

---

## Package Importance Summary

### ⭐⭐⭐⭐⭐ Critical (Must-use):
1. `ncbi.taxonomy` (TaxonomyParser, TaxonomyFullNameLineageParser) - NCBI integration
2. `pfam.parse` (PfamScanRecord) - Protein domain analysis

### ⭐⭐⭐⭐ Important (Frequently-used):
1. `blast.parse` - BLAST results
2. `rest` - Web services integration
3. `analysis.math` - Mathematical operations
4. `phylo.*` - Phylogenetic algorithms

---

## Typical Usage Patterns

### Pattern 1: BLAST Analysis
```
1. Parse BLAST output: BlastHspRecord
2. Extract alignments: HSP records
3. Filter by E-value/score
4. Export to TSV
```

### Pattern 2: Domain Annotation
```
1. Run hmmscan (external tool)
2. Parse results: PfamScanRecord.parseHmmerScanOut()
3. Extract domains per protein
4. Visualize domain architecture
```

### Pattern 3: Taxonomy Lookup
```
1. Load taxonomy: TaxonomyParser.parseTree()
2. Query lineage: TaxonomyFullNameLineageParser.getLineage()
3. Extract ranks: getAllTaxonomicRanks()
4. Export to R: API4R.getRankedLineages()
```

### Pattern 4: Ensembl Integration
```
1. Query Ensembl REST: EnsemblTranslationRest
2. Parse JSON tree: EnsJsonTreeParserCustomized
3. Extract taxonomy mappings
4. Visualize species tree
```

---

*This document covers Parsers and Bioinformatics Tools packages. See other parts for Core Structures, File I/O, Utilities, and Visualization.*
