# eGPS-Base Project Overview

## Project Information

**Project Name:** eGPS-Base  
**Group ID:** ydl.lab.utils  
**Artifact ID:** egps-base  
**Version:** 1.0-SNAPSHOT  
**Build Tool:** Maven  
**Java Version:** 25  
**Encoding:** UTF-8

## Project Description

eGPS-Base is a comprehensive bioinformatics utility library designed to support evolutionary genomics and phylogenetic analysis. It provides a modular architecture with specialized tools for processing biological sequence data, phylogenetic trees, genetic codes, and various data formats commonly used in computational biology research.

## Architecture Overview

The project follows a modular architecture with each package serving a specific domain in bioinformatics. All modules implement the `IModuleSignature` interface, which provides metadata about each module's purpose and tab name for GUI integration.

### Core Design Pattern

Each module contains a `ZzzModuleSignature` class that implements `IModuleSignature`, providing:
- **getShortDescription()**: Module functionality description
- **getTabName()**: Display name for GUI tabs

## Module Structure

---

### **Top Signature Module** (`top.signature`)
**Purpose:** Module signature framework  
**Features:** `IModuleSignature` interface for all modules, GUI integration support

All code collections implementing this interface are considered a `module`. Please refer to `understand_packages1_core_structure*` for detailed documentation of all class functionalities.

---

## Technology Stack

### Core Dependencies
- **Java 25**: Latest Java version for modern features
- **Maven**: Build and dependency management

### Major Libraries

#### Data Processing
- **FastJSON 1.2.83**: JSON parsing (Swing-compatible version)
- **Apache Commons Lang3 3.18.0**: Common utilities
- **Apache Commons CLI 1.9.0**: Command-line parsing
- **Apache Commons IO 2.18.0**: File I/O utilities
- **Apache Commons Compress 1.27.1**: Compression support
- **XZ 1.9**: XZ compression
- **Guava 33.4.8-jre**: Google core libraries

#### GUI Frameworks
- **FlatLaf 3.6**: Modern Swing Look & Feel
- **SwingX 1.6.1**: Extended Swing components
- **JIDE-OSS 3.7.15**: Professional Swing components
- **MigLayout 4.2**: Layout manager
- **Timing Framework 1.0**: Animation support

#### Visualization
- **XChart 3.8.8**: Chart library (excludes animated-gif-lib)
- **JSVG 1.2.0**: SVG rendering

#### Bioinformatics
- **HTSJDK 4.3.0**: High-throughput sequencing data (excludes Nashorn)
- **DOM4J 2.1.4**: XML parsing

#### Document Processing
- **iTextPDF 5.1.3**: PDF generation (last standalone version)
- **Apache POI 5.4.1**: Excel/Office file handling
  - poi
  - poi-ooxml

#### Logging
- **Log4j2 2.24.3**: Logging framework
  - log4j-core
  - log4j-api
  - log4j-slf4j2-impl

#### Other
- **javax.mail 1.6.2**: Email support
- **Reflections 0.10.2**: Runtime reflection
- **Java-sizeof 0.0.5**: Memory usage measurement

#### Testing
- **JUnit Jupiter 5.11.1**: Unit testing framework

### Repository Configuration
- Maven Central
- Aliyun Maven Mirror (Chinese acceleration)
- JOSM Repository (for specific dependencies)

---

## Test Structure

The project includes several test categories:

1. **evoltree.txtdisplay**: Text tree display tests
   - `TextTreeDescriberTest`

2. **module.ihave**: Module discovery
   - `SeeMyModule`

3. **os.test**: OS and memory tests
   - `MemoryTest100G`: Large memory testing

4. **script.run.once**: One-time analysis scripts
   - Bioinformatics workflow scripts
   - BLAST result processing
   - Sequence extraction
   - FASTA preprocessing

5. **tab.index**: Tabix testing
   - `TestTbx`

---

## Build Configuration

### Resource Handling
- Includes all non-Java files from `src/main/java`
- Excludes `.java` source files from resources
- UTF-8 encoding throughout

### Maven Properties
```xml
maven.compiler.source: 25
maven.compiler.target: 25
project.build.sourceEncoding: UTF-8
```

---

## Key Design Principles

### 1. **Modular Architecture**
Each functional area is isolated into its own package with clear responsibilities.

### 2. **Interface-Based Design**
Core abstractions use interfaces (`IModuleSignature`, `IGeneticCode`, `EvolNode`) to allow multiple implementations.

### 3. **Efficient I/O**
Large buffer sizes (10-100MB) for bioinformatics file processing, supporting compressed formats.

### 4. **GUI Integration**
Built-in support for Swing-based interfaces with module signatures for tab-based navigation.

### 5. **Performance Monitoring**
Integrated memory and timing utilities for performance analysis.

### 6. **CLI Support**
Comprehensive command-line tools with standardized help generation.

---

## Project Purpose

eGPS-Base serves as a foundation library for:
- **Evolutionary genomics research**: Phylogenetic tree analysis and manipulation
- **Sequence analysis**: FASTA/BLAST/Pfam processing
- **Data visualization**: Scientific plotting and tree rendering
- **Bioinformatics pipelines**: Workflow automation and data processing
- **GUI applications**: Swing-based bioinformatics tools

---

## Development Notes

### Code Organization
- Each module has a `ZzzModuleSignature` marker class
- Consistent package structure across domains
- Separation of I/O, algorithms, and visualization

### Dependency Strategy
- Uses specific library versions for stability
- Excludes conflicting dependencies (e.g., Nashorn, animated-gif-lib)
- Maintains backward compatibility with Swing components
- Comments indicate version lock reasons

### File Handling
- Supports both compressed and uncompressed files
- Automatic format detection
- Large file optimization (100MB+ buffers)
- Stream processing for memory efficiency

---

## Future Extensibility

The modular design allows easy extension by:
1. Implementing `IModuleSignature` for new modules
2. Adding new parsers to existing packages
3. Extending tree data structures with `EvolNode`
4. Adding new genetic code tables
5. Implementing new visualizations

---

## Author Information
- **Author**: yudalang
- **Development Team**: eGPS Development Team
- **Contact**: ydl.lab.utils

---

## Version History
- **Current Version**: 1.0-SNAPSHOT
- **Status**: Active development
- **Last Updated**: 2025

---

*This document provides a comprehensive overview of the eGPS-Base project structure, architecture, and capabilities.*