# eGPS-Base Package Documentation - Part 1: Core Structures & Data Models

## Overview

This document covers the foundational packages that provide core data structures, module framework, and tree infrastructure for eGPS-Base.

---

## 1. Core Packages

### 1.1 `top.signature` - Module Signature System

**Purpose:** Foundation for the modular architecture

#### Key Classes:

**`IModuleSignature`** (Interface)
- **Function:** Defines the contract for all modules in the system
- **Key Methods:**
  - `getShortDescription()`: Returns module functionality description
  - `getTabName()`: Returns display name for GUI integration
- **Usage:** Every module implements this interface via `ZzzModuleSignature` class
- **Importance:** ⭐⭐⭐⭐⭐ (Core architecture component)

---

## 2. Data Structure Packages

### 2.1 `evoltree.struct` - Tree Data Structures

**Purpose:** Generic tree data structures for phylogenetic analysis

#### Key Classes:

**`EvolNode`** (Interface)
- **Function:** Top-level interface defining all tree node operations
- **Key Features:**
  - Parent-child relationship management (up-links and down-links)
  - Branch length support
  - Node naming and identification
  - Tree traversal support
- **Methods:** 30+ methods for comprehensive tree operations
- **Importance:** ⭐⭐⭐⭐⭐ (Foundation for all tree operations)

**`ArrayBasedNode`** (Class)
- **Function:** Array-backed implementation of tree nodes
- **Key Features:**
  - O(1) random access to children
  - Dynamic resizing with ArrayList
  - Recommended for production use
- **Performance:** Fast child access, higher memory overhead
- **Importance:** ⭐⭐⭐⭐⭐ (Primary tree implementation)

**`LinkedBasedNode`** (Class)
- **Function:** Linked-list-based tree node implementation
- **Key Features:**
  - First-child/next-sibling representation
  - Lower memory per node
  - O(n) child access
- **Use Cases:** Educational purposes, memory-constrained environments
- **Importance:** ⭐⭐⭐ (Alternative implementation)

**`TreeCoder`** (Interface)
- **Function:** Interface for encoding tree structures to strings
- **Key Method:** `code(EvolNode node)` → String
- **Importance:** ⭐⭐⭐⭐ (Serialization)

**`TreeDecoder`** (Interface)
- **Function:** Interface for decoding strings to tree structures
- **Key Method:** `decode(String)` → EvolNode
- **Importance:** ⭐⭐⭐⭐ (Deserialization)

**`AbstractNodeCoderDecoder<S>`** (Abstract Class)
- **Function:** Abstract base class for tree node encoding/decoding operations
- **Key Features:**
  - Template method pattern for serialization framework
  - Unified number formatting (6 decimal places, HALF_UP rounding)
  - Support for both leaf and internal node processing
  - Efficient node creation without reflection
- **Key Methods:**
  - `codeNode(S node)`: Encode node to string
  - `parseNode(S node, String str)`: Parse string into node
  - `createNode()`: Create new node instance (abstract)
  - `convertRate2Decimal(double value)`: Format numeric values
- **Design Pattern:** Template Method - defines algorithm skeleton, subclasses implement specifics
- **Use Cases:**
  - Newick format leaf node processing
  - Internal node encoding/decoding
  - Custom tree format implementations
- **Performance:** Optimized for large trees, avoids reflection overhead
- **Importance:** ⭐⭐⭐⭐ (Foundation for tree serialization)

---

### 2.2 `evoltree.phylogeny` - Phylogenetic Trees

**Purpose:** Specialized phylogenetic tree operations and Newick format support

#### Key Classes:

**`ZzzModuleSignature`** (Class)
- **Function:** Module signature for phylogenetic tree operations
- **Key Features:**
  - Newick format parsing and generation
  - Phylogenetic tree serialization
  - Node and branch metadata handling
- **Description:** Phylogenetic tree parsing tools for processing Newick format files and performing tree-based analysis operations
- **Importance:** ⭐⭐⭐⭐⭐ (Essential for tree I/O)

**`DefaultPhyNode`** (Class)
- **Function:** Standard phylogenetic node implementation
- **Key Features:**
  - Full phylogenetic metadata support
  - Branch lengths
  - Bootstrap values
- **Importance:** ⭐⭐⭐⭐ (Standard phylogenetic operations)

**`PhyloTreeEncoderDecoder`** (Class)
- **Function:** Complete tree serialization/deserialization
- **Supported Formats:** Newick (NWK) format
- **Key Features:**
  - Encode trees to Newick strings
  - Parse Newick strings to trees
- **Key Methods:**
  - `encode(DefaultPhyNode)`: Tree to Newick string
  - `decode(String)`: Newick string to tree
- **Importance:** ⭐⭐⭐⭐⭐ (Essential for tree I/O)

**`NWKInternalCoderDecoder`** (Class)
- **Function:** Handle internal nodes in Newick format
- **Key Features:**
  - Parse node name, bootstrap, branch length
  - Support format: `name:bootstrap:length`
  - Handle nodes with/without bootstrap values
- **Importance:** ⭐⭐⭐⭐ (Newick parsing)

**`NWKLeafCoderDecoder`** (Class)
- **Function:** Handle leaf nodes in Newick format
- **Key Features:**
  - Parse species/gene names
  - Extract branch lengths
  - Format: `species_name:length`
  - Creates DefaultPhyNode instances
- **Usage:** Used internally by PhyloTreeEncoderDecoder
- **Importance:** ⭐⭐⭐⭐ (Newick parsing)

---

## 3. Genetic Code Packages

### 3.1 `geneticcodes` - Genetic Code Translation

**Purpose:** Translate nucleotide sequences to amino acid sequences

#### Key Classes:

**`IGeneticCode`** (Interface)
- **Function:** Defines genetic code translation contract
- **Key Features:**
  - Support for NCBI genetic code tables
  - Codon to amino acid translation
  - Multiple genetic code variants
- **Key Methods:**
  - `translateCodonToAminoAcidOneLetter(char[])`: Translate codon
  - `getGeneticCodeName()`: Get code table name
- **Importance:** ⭐⭐⭐⭐⭐ (Core biological function)

**`GeneticCode`** (Abstract Class)
- **Function:** Base implementation for genetic code tables
- **Key Features:**
  - Codon degeneracy analysis
  - Start/stop codon identification
  - Reverse complement support
  - Factory pattern for different genetic codes
- **Key Methods:**
  - `translateCodonToAminoAcid()`: Core translation
  - `degenerateAttr()`: Analyze codon degeneracy
  - `getStartIndex()`: Find start codons
  - `nthStopLocation()`: Find stop codons
- **Importance:** ⭐⭐⭐⭐⭐ (Translation engine)

**`AminoAcid`** (Interface)
- **Function:** Standard amino acid representations
- **Key Features:**
  - 20 standard amino acids
  - Three notation systems:
    - Single-letter codes (A, C, D, E, ...)
    - Three-letter codes (ALA, CYS, ASP, ...)
    - Full names (Alanine, Cysteine, ...)
  - Special symbols (*, -, X)
- **Importance:** ⭐⭐⭐⭐⭐ (Fundamental constants)

**`OpenReadingFrame`** (Class)
- **Function:** Define and manage Open Reading Frames (ORFs)
- **Key Features:**
  - Section definition ([start, end] pairs)
  - 1-based to 0-based conversion
  - Multiple section support
- **Key Methods:**
  - `setSection(Vector<Integer>)`: Define ORF regions
  - `getSection()`: Retrieve defined sections
- **Use Cases:** Gene finding, translation region specification
- **Importance:** ⭐⭐⭐⭐ (ORF analysis)

---

### 3.2 `geneticcodes.codeTables` - Genetic Code Tables

**Purpose:** Concrete implementations of various genetic code tables used across different organisms

#### Key Classes:

**`TheStandardCode`** (Class)
- **Function:** Standard genetic code table (NCBI translation table 1) for nuclear translation
- **NCBI Table ID:** 1
- **Use Cases:** Most nuclear genes in eukaryotes and prokaryotes
- **Importance:** ⭐⭐⭐⭐⭐ (Universal genetic code)

**`ECOLI_GeneticCodes`** (Class)
- **Function:** E. coli specific genetic code table
- **Use Cases:** Bacterial translation
- **Importance:** ⭐⭐⭐⭐ (Bacterial genetics)

**`TheVertebrateMitochondrialCode`** (Class)
- **Function:** Vertebrate mitochondrial genetic code (NCBI table 2)
- **Key Differences:** AGA/AGG code for stop instead of Arg
- **Use Cases:** Vertebrate mitochondrial genome annotation
- **Importance:** ⭐⭐⭐⭐ (Mitochondrial genetics)

**`TheInvertebrateMitochondrialCode`** (Class)
- **Function:** Invertebrate mitochondrial genetic code (NCBI table 5)
- **Use Cases:** Invertebrate mitochondrial genomes
- **Importance:** ⭐⭐⭐⭐ (Invertebrate mitochondrial genetics)

**`YEASTM_GeneticCodes`** (Class)
- **Function:** Yeast mitochondrial genetic code
- **Use Cases:** Yeast mitochondrial genome analysis
- **Importance:** ⭐⭐⭐ (Fungal mitochondrial genetics)

**`TheYeastMitochondrialCode`** (Class)
- **Function:** Yeast mitochondrial genetic code (NCBI table 3)
- **Importance:** ⭐⭐⭐ (Fungal mitochondrial genetics)

**`PLANTM_GeneticCodes`** (Class)
- **Function:** Plant mitochondrial genetic code
- **Use Cases:** Plant mitochondrial genome annotation
- **Importance:** ⭐⭐⭐ (Plant mitochondrial genetics)

**`MOLDMITO_GeneticCodes`** (Class)
- **Function:** Mold, protozoan, and coelenterate mitochondrial code
- **Importance:** ⭐⭐⭐ (Diverse organisms)

**`TheCiliateDHexamitaNuclearCode`** (Class)
- **Function:** Ciliate, Dasycladacean and Hexamita nuclear code (NCBI table 6)
- **Use Cases:** Ciliate nuclear genome translation
- **Importance:** ⭐⭐⭐ (Ciliate genetics)

**`CILIATE_GeneticCodes`** (Class)
- **Function:** Ciliate nuclear genetic code
- **Importance:** ⭐⭐⭐ (Ciliate genetics)

**`TheEuplotidNuclearCode`** (Class)
- **Function:** Euplotid nuclear genetic code (NCBI table 10)
- **Use Cases:** Euplotid ciliate genomes
- **Importance:** ⭐⭐⭐ (Specialized protozoa)

**`TheBacterialPlantPlastidCode`** (Class)
- **Function:** Bacterial, archaeal and plant plastid code (NCBI table 11)
- **Use Cases:** Chloroplast genome translation
- **Importance:** ⭐⭐⭐⭐ (Plastid genetics)

**`ECHINOM_GeneticCodes`** (Class)
- **Function:** Echinoderm and flatworm mitochondrial code
- **Use Cases:** Echinoderm mitochondrial genomes
- **Importance:** ⭐⭐⭐ (Invertebrate mitochondrial genetics)

**`TheEFMitochondrialCode`** (Class)
- **Function:** Echinoderm and flatworm mitochondrial code (NCBI table 9)
- **Importance:** ⭐⭐⭐ (Invertebrate mitochondrial genetics)

**`INVERTM_GeneticCodes`** (Class)
- **Function:** Invertebrate mitochondrial genetic code
- **Importance:** ⭐⭐⭐⭐ (Invertebrate mitochondrial genetics)

**`MAMMALRV_GeneticCodes`** (Class)
- **Function:** Mammalian retroviral genetic code
- **Use Cases:** Retrovirus sequence analysis
- **Importance:** ⭐⭐⭐ (Viral genetics)

**`YEASTRV_GeneticCodes`** (Class)
- **Function:** Yeast retroviral genetic code
- **Importance:** ⭐⭐⭐ (Yeast viral genetics)

**`VERTM_GeneticCodes`** (Class)
- **Function:** Vertebrate mitochondrial genetic code
- **Importance:** ⭐⭐⭐⭐ (Vertebrate mitochondrial genetics)

**`WORSCOLI_GeneticCodes`** (Class)
- **Function:** Bacterial genetic code for specific bacteria
- **Importance:** ⭐⭐⭐ (Bacterial genetics)

**`TheMPCMCMSCode`** (Class)
- **Function:** Mold, Protozoan, and Coelenterate Mitochondrial Code plus Mycoplasma/Spiroplasma (NCBI table 4)
- **Use Cases:** Diverse mitochondrial and bacterial systems
- **Importance:** ⭐⭐⭐ (Diverse organisms)

**Importance:** ⭐⭐⭐⭐⭐ (Essential for accurate translation across organisms)

---

## Package Importance Summary

### ⭐⭐⭐⭐⭐ Critical (Must-use):
1. `top.signature` - Module system foundation
2. `evoltree.struct` - Core tree structures
3. `evoltree.phylogeny` - Phylogenetic operations
4. `geneticcodes` - Translation system

### ⭐⭐⭐⭐ Important (Frequently-used):
1. `geneticcodes.codeTables` - Organism-specific codes

---

## Typical Usage Patterns

### Pattern 1: Building a Phylogenetic Tree
```
1. Create nodes: new ArrayBasedNode()
2. Build tree structure: EvolNode methods
3. Serialize: PhyloTreeEncoderDecoder.encode()
4. Parse: PhyloTreeEncoderDecoder.decode()
```

### Pattern 2: Genetic Translation
```
1. Select genetic code: TheStandardCode or variant
2. Translate codons: translateCodonToAminoAcid()
3. Find ORFs: OpenReadingFrame.setSection()
4. Analyze degeneracy: degenerateAttr()
```

---

*This document covers the foundational core structures of eGPS-Base. See other parts for File I/O, Utilities, Parsers, and Visualization.*
