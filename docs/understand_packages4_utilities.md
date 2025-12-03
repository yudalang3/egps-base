# eGPS-Base Package Documentation - Part 4: Utilities & Helpers

## Overview

This document covers utility packages providing general-purpose helper functions for file operations, string manipulation, collections, date/time handling, and more.

---

## 1. General Utilities

### 1.1 `utils` - General Utilities

**Purpose:** Common utility functions used throughout the project

#### Key Classes:

**`EGPSFileUtil`** (Class)
- **Function:** Comprehensive file operations
- **Key Features:**
  - File copying (stream-based, channel-based)
  - Compression/decompression (GZ, BZ2, XZ)
  - Recursive file listing
  - Network file downloading
  - Automatic compression detection
- **Key Methods:**
  - `copyFileUsingStream()`: Copy files
  - `getInputStreamFromOneFileMaybeCompressed()`: Open compressed files
  - `downloadFromUrl()`: Download from URLs
  - `getListFiles()`: Recursive file listing
- **Importance:** ⭐⭐⭐⭐⭐ (Essential for file handling)

**`EGPSGeneralUtil`** (Class)
- **Function:** Miscellaneous helper methods
- **Key Features:**
  - Color interpolation for visualizations
  - Number formatting (3, 6, 9 decimal places)
  - Collection to file conversion
  - System property access
  - Biological database URL constants
- **Key Constants:**
  - `NCBI_PROTEIN`, `NCBI_NUCCORE`: NCBI database URLs
  - `UNIPROT_KB`: UniProt URL
  - `ZERO_DIFF`: Floating-point comparison tolerance (1.0E-9)
  - `FORMATTER_6`, `FORMATTER_9`: Decimal formatters
- **Key Methods:**
  - `calcColor()`: Color interpolation
  - `collection2file()`: Write collections to files
  - `collapseWhiteSpace()`: String normalization
- **Importance:** ⭐⭐⭐⭐ (Widely used utilities)

**`EGPSListUtil`** (Class)
- **Function:** List manipulation and analysis
- **Key Features:**
  - Element frequency counting
  - List partitioning/chunking
  - Set operations (intersection, difference)
  - Containment percentage calculations
- **Key Methods:**
  - `countString()`: Optimized frequency count (10M+ elements)
  - `countSameComponents()`: Simple frequency count
  - `partitionList()`: Split into chunks
  - `intersection()`, `difference()`, `subtract()`: Set operations
- **Performance:** Optimized for large lists
- **Importance:** ⭐⭐⭐⭐ (Collection operations)

**`EGPSUtil`** (Class)
- **Function:** General eGPS system utilities
- **Key Features:**
  - JVM memory monitoring
  - Performance timing (Guava Stopwatch)
  - Browser integration
  - Clipboard fallback
- **Key Methods:**
  - `getAlreadyUsedJVMMemory()`: Get memory usage in MB
  - `printUsedJVMMemory()`: Print memory to console
  - `obtainRunningTimeNewThread()`: Time execution (new thread)
  - `obtainRunningTimeSecondBlocked()`: Time execution (blocking)
  - `openUrlInBrowser()`: Open URLs in browser
- **Importance:** ⭐⭐⭐⭐ (Performance & monitoring)

**`EGPSObjectCounter<T>`** (Class)
- **Function:** Generic counter for tracking object occurrences
- **Key Features:**
  - Generic type support for any object
  - Maintains insertion order (LinkedHashMap)
  - Efficient counting with MutableInt (no boxing/unboxing)
  - Sort by count (ascending)
- **Key Methods:**
  - `addOneEntry(T)`: Add/increment count
  - `printWithOriginalOrder()`: Print in insertion order
  - `printSortedResults()`: Print sorted by count
  - `getCounterMap()`: Get Map<T, MutableInt>
- **Use Cases:**
  - Count species occurrences
  - Amino acid frequency tallies
  - Domain type counting
- **Importance:** ⭐⭐⭐⭐ (Frequency counting)

**`EGPSObjectsUtil`** (Class)
- **Function:** Java object serialization and persistence
- **Key Features:**
  - Java serialization (ObjectOutputStream/ObjectInputStream)
  - JSON serialization (FastJSON)
  - Quick save/load without serialVersionUID
  - Type-safe JSON deserialization
- **Key Methods:**
  - `quickSaveAnObject2file()`: Save object
  - `quickObtainAnObjectFromFile()`: Load object
  - `persistentSaveJavaBeanByFastaJSON()`: Save as JSON
  - `obtainJavaBeanByFastaJSON()`: Load from JSON
- **Use Cases:**
  - Save/load configuration
  - Cache computed results
- **Importance:** ⭐⭐⭐⭐ (Object persistence)

**`EGPSGuiUtil`** (Class)
- **Function:** Simple GUI/CLI interfaces for development
- **Key Features:**
  - Quick GUI IDE with buttons
  - CLI IDE with commands (t/q)
  - Window position/size persistence
  - Thread-safe operations
- **Key Methods:**
  - `universalSimplestIDE(Runnable)`: GUI interface
  - `universalSimplestCLIIDE(Runnable)`: CLI interface
- **Use Cases:**
  - Iterative development
  - Algorithm testing
- **Importance:** ⭐⭐⭐ (Development tools)

**`EGPSFormatUtil`** (Class)
- **Function:** Formatting utilities for numbers and HTML content
- **Key Features:**
  - Thousand separator formatting for numbers
  - HTML font size tag generation
  - Thread-safe formatting operations
  - Localized number format support
- **Key Methods:**
  - `addThousandSeparatorForInteger(int)`: Add thousand separators
  - `html32Concat(int, String)`: Create HTML font tags
- **Usage Examples:**
  ```java
  // Format large numbers
  String formatted = EGPSFormatUtil.addThousandSeparatorForInteger(1234567);
  // Result: "1,234,567"
  
  // Create HTML content
  String html = EGPSFormatUtil.html32Concat(5, "Hello");
  // Result: "<html><font size="5">Hello</font></html>"
  ```
- **Use Cases:**
  - Number display in GUI components
  - HTML report generation
  - Data visualization formatting
- **Importance:** ⭐⭐⭐ (Formatting utilities)

---

## 2. String Utilities

### 2.1 `utils.string` - String Utilities

**Purpose:** String manipulation specialized for bioinformatics

#### Key Classes:

**`EGPSStringUtil`** (Class)
- **Function:** Comprehensive string operations optimized for biological data
- **Key Features:**
  - High-performance string splitting (single-char delimiter optimization)
  - Matrix transpose (2D string lists)
  - Bracket validation (parentheses, braces, square brackets)
  - Common prefix/suffix finding
  - Number extraction from mixed strings
  - Byte array line parsing (US-ASCII)
  - Log4j-style string formatting with {}
- **Key Methods:**
  - `split(String, char)`: Fast single-char delimiter splitting
  - `split(String, char, int)`: Fixed-size splitting
  - `splitByTab(String)`: Tab-separated splitting
  - `transpose(List<List<String>>)`: Transpose columns to rows
  - `validateStringAccording2bracks(String)`: Bracket matching
  - `getCommonPrefix(String, String)`: Find common prefix
  - `getCommonTail(String, String)`: Find common suffix
  - `getNumInString(String)`: Extract digits
  - `getStringAfterEqualChar(String)`: Parse key=value
  - `fillString(char, int)`: Repeat characters
  - `getLinesFromByteArray(byte[])`: Parse lines from bytes
  - `format(String, Object...)`: Parameterized formatting
- **Performance Optimizations:**
  - Single-char delimiter: faster than regex
  - Stack-based bracket matching: O(n)
  - Consumer-based streaming split
- **Use Cases:**
  - Parse TSV/CSV files
  - Validate Newick tree format
  - Extract sample IDs
  - Build formatted paths
- **Importance:** ⭐⭐⭐⭐⭐ (Essential text processing)

**`StringCounter`** (Class)
- **Function:** String frequency counter
- **Key Features:**
  - Efficient counting with MutableInt
  - HashMap-based storage
  - Sorted output by key
- **Key Methods:**
  - `addOneEntry(String)`: Add/increment count
  - `printWithOriginalOrder()`: Print unsorted
  - `printSortedResults()`: Print sorted alphabetically
  - `getCounterMap()`: Get Map<String, MutableInt>
  - `clear()`: Reset counter
- **Use Cases:**
  - Count species names
  - Tally codon frequencies
  - Track taxonomy ranks
- **Importance:** ⭐⭐⭐ (String counting)

---

## 3. Date/Time Utilities

### 3.1 `utils.datetime` - Date/Time Utilities

**Purpose:** Date and time operations with format validation

#### Key Classes:

**`DateTimeOperator`** (Class)
- **Function:** Date/time formatting and validation
- **Key Features:**
  - Fixed format: "yyyy-MM-dd HH:mm:ss"
  - Current timestamp generation
  - Format validation (detect user modifications)
- **Key Methods:**
  - `getCurrentTime()`: Get formatted current time
  - `isStillUseProgramGeneratedString(String)`: Validate format
- **Use Cases:**
  - Generate timestamps for logs
  - Detect manually edited timestamps
  - Track analysis execution times
- **Importance:** ⭐⭐⭐ (Time handling)

**`EGPSTimeUtil`** (Class)
- **Function:** Utility class for converting between Date and LocalDate objects
- **Key Features:**
  - Date to LocalDate conversion
  - LocalDate to Date conversion
  - Batch list conversions
  - System default time zone handling
- **Key Methods:**
  - `date2LocalDate(Date)`: Convert Date to LocalDate
  - `localDate2Date(LocalDate)`: Convert LocalDate to Date
  - `dateList2localDateList(List<Date>)`: Batch Date to LocalDate
  - `localDateList2dateList(List<LocalDate>)`: Batch LocalDate to Date
- **Use Cases:**
  - Modern Java 8 time API integration
  - Legacy Date object compatibility
  - Time zone conversions
- **Importance:** ⭐⭐⭐⭐ (Date/time conversion)

**Importance:** ⭐⭐⭐⭐ (Date/time utilities)

---

## 4. CLI Tools

### 4.1 `cli.tools` - Command Line Tools

**Purpose:** Command-line utilities for common tasks

#### Key Classes:

**`ZzzModuleSignature`** (Class)
- **Function:** Module signature for command-line tools
- **Key Features:**
  - CLI tool framework integration
  - Command-line interface management
  - Tool discovery and organization
- **Description:** Command-line utilities module providing comprehensive tools for system operations, data processing, and bioinformatics workflows
- **Importance:** ⭐⭐⭐ (CLI framework)

**`CheckNwkFormat`** (Tool)
- **Function:** Validate Newick tree format
- **Use Case:** Verify tree file integrity
- **Usage:** `java CheckNwkFormat /path/to/tree.nwk`
- **Importance:** ⭐⭐⭐ (Format validation)

**`ClipboardPathNormalized`** (Tool)
- **Function:** Convert Windows paths to Unix format in clipboard
- **Key Features:**
  - Reads from system clipboard
  - Replaces backslashes (\) with forward slashes (/)
  - Writes normalized path back to clipboard
- **Use Case:** Cross-platform path handling
- **Usage:** `java ClipboardPathNormalized` (no arguments)
- **Example:** `C:\data\file.txt` → `C:/data/file.txt`
- **Importance:** ⭐⭐⭐⭐ (Developer productivity)

**`CountFilesWithSuffix`** (Tool)
- **Function:** Count files by extension
- **Use Case:** File system statistics
- **Importance:** ⭐⭐⭐ (Batch operations)

**`ListFilesWithSuffix`** (Tool)
- **Function:** Generate TSV of files with specific extension
- **Use Case:** Batch file processing

**`RemoveInternalNodeNames`** (Tool)
- **Function:** Strip internal node names from trees
- **Use Case:** Clean tree files

**Importance:** ⭐⭐⭐ (Automation tools)

---

## 5. GUI Utilities

### 5.1 `gui.simple.tools` - Simple GUI Tools

**Purpose:** Simple graphical user interface tools for common operations

#### Key Classes:

**`FilePathNormalizedGUI`** (Class)
- **Function:** GUI tool for normalizing file paths from clipboard
- **Key Features:**
  - Swing-based GUI interface
  - One-click path normalization
  - Clipboard integration
  - User feedback via text area
- **Key Components:**
  - JButton for normalization action
  - JTextArea for status display
  - Integration with ClipboardPathNormalized
- **Use Cases:**
  - Cross-platform development
  - Path format conversion
  - Clipboard-based workflows
- **Importance:** ⭐⭐⭐ (GUI convenience tool)

**`ZzzModuleSignature`** (Class)
- **Function:** Module signature for simple GUI tools
- **Description:** Simple GUI tools module providing user-friendly interfaces for common operations
- **Importance:** ⭐⭐⭐ (Module metadata)

**Importance:** ⭐⭐⭐ (GUI utilities)

---

## 6. Unified Output

### 6.1 `unified.output` - Unified Interfaces

**Purpose:** Unified interfaces for cross-module operations

#### Key Classes:

**`ZzzModuleSignature`** (Class)
- **Function:** Module signature for unified output interfaces
- **Description:** Unified output module providing standardized interfaces for cross-module data exchange and reporting
- **Importance:** ⭐⭐⭐ (Module metadata)

**Sub-packages:**
- `output`: Unified output interfaces and implementations

**Key Features:**
- Standard interfaces for data exchange
- Module interoperability
- Consistent output formatting

**Importance:** ⭐⭐⭐ (Integration)

---

## Package Importance Summary

### ⭐⭐⭐⭐⭐ Critical (Must-use):
1. `utils` (EGPSFileUtil) - Essential file utilities
2. `utils.string` (EGPSStringUtil) - Essential text processing

### ⭐⭐⭐⭐ Important (Frequently-used):
1. `utils` (EGPSListUtil, EGPSUtil, EGPSObjectCounter) - Collection operations
2. `utils` (EGPSGeneralUtil, EGPSObjectsUtil) - General utilities
3. `utils.datetime` (EGPSTimeUtil) - Date/time conversion
4. `cli.tools` (ClipboardPathNormalized) - Developer productivity

### ⭐⭐⭐ Useful (Specialized):
1. `utils` (EGPSGuiUtil, EGPSFormatUtil) - GUI and formatting
2. `utils.string` (StringCounter) - String counting
3. `utils.datetime` (DateTimeOperator) - Time handling
4. `cli.tools` (other tools) - Automation
5. `gui.simple.tools` - GUI utilities
6. `unified.output` - Integration

---

## Typical Usage Patterns

### Pattern 1: File Processing
```
1. List files: EGPSFileUtil.getListFiles()
2. Open compressed: getInputStreamFromOneFileMaybeCompressed()
3. Process data
4. Save results: collection2file()
```

### Pattern 2: String Manipulation
```
1. Split data: EGPSStringUtil.split()
2. Extract numbers: getNumInString()
3. Validate format: validateStringAccording2bracks()
4. Count occurrences: StringCounter.addOneEntry()
```

### Pattern 3: Performance Monitoring
```
1. Start timer: EGPSUtil.obtainRunningTimeSecondBlocked()
2. Run analysis
3. Check memory: getAlreadyUsedJVMMemory()
4. Log performance
```

### Pattern 4: Object Persistence
```
1. Compute results
2. Save to file: EGPSObjectsUtil.quickSaveAnObject2file()
3. Later: load from file: quickObtainAnObjectFromFile()
```

---

*This document covers Utilities and Helper packages. See other parts for Core Structures, File I/O, Parsers, and Visualization.*
