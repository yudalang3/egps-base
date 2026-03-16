# eGPS-Base Package Guide 2: File I/O

[Docs Index](README.md) | [中文](understand_packages2_file_io_zh.md)

## Document Role

This guide is for maintainers working on sequence and table data flowing into or out of the repository. Start here when your task involves FASTA files, TSV-like tables, or multiple-sequence-alignment formats.

## Package Guide

### `fasta.io`

Role:

- provides FASTA reading and a small set of workflow-style sequence-processing scripts

Current source files:

- `FastaReader`
- `Script_allocate_raw_pep_to_dir_by_fileName`
- `Script_get_longest_CDS_forEnsembl_pepFa`
- `Script_get_unique_forEnsembl_pepFa`
- `Script_homoGene_finder_blastpWithHmmer`
- `Script_prepare_longestUnique_forEnsembl_pepFa`
- `ZzzModuleSignature`

Reading note:

- Treat `FastaReader` as the main reusable entry point.
- Treat the `Script_*` classes as workflow helpers around specific datasets rather than as general library APIs.

### `fasta.stat`

Role:

- provides sequence-level quality-control statistics

Current source files:

- `UniqueStat`
- `BatchUniqueStat`
- `ZzzModuleSignature`

Reading note:

- This package is the right place to start when the question is about duplicate sequences, repeat ratios, or batch uniqueness checks.

### `fasta.comparison`

Role:

- compares sequence sets using alignment-informed summaries and reports

Current source files:

- `FastaComparer`
- `PairwiseSeqDiffPrinter`
- `ZzzModuleSignature`

Reading note:

- This package becomes relevant after you already have BLAST or DIAMOND-style alignment output and want downstream comparison summaries.

### `tsv.io`

Role:

- provides table containers and TSV-oriented helpers

Current source files:

- `TSVReader`
- `TSVWriter`
- `KitTable`
- `IntTable`
- `ExcelReaderTemplate`
- `BeanCreator`
- `TsvNameTransversionInfo`
- `SpaceTransformer`
- `ZzzModuleSignature`

Reading note:

- This package is broader than plain TSV parsing: it also contains small conversion and table-construction helpers used in import workflows.

### `msaoperator`

Role:

- handles multiple-sequence-alignment metadata, parsing, preprocessing, and writing

Top-level source files:

- `DataforamtInfo`
- `DataForamtPrivateInfor`
- `DefaultDataFormatPrivateInfor`
- `ZzzModuleSignature`

Important subpackages and entry points:

- `msaoperator.alignment` - `AlignmentPreprocesser`, `CompeleDeletion`, `PairwiseDeletion`, `PartialDeletion`, `DeletionHandler`, `GlobalAlignmentSettings`
- `msaoperator.alignment.sequence` - the sequence object model: `SequenceI` (core interface), `Sequence` (base implementation), `BasicSequenceData` (data storage), `SequenceComponentRatio` (composition-ratio statistics)
- `msaoperator.io` - `MSAFileParser`
- `msaoperator.io.seqFormat.parser` - concrete format parsers
- `msaoperator.io.seqFormat.writer` - concrete format writers
- `msaoperator.io.seqFormat.model` - alignment data models

Reading note:

- `MSAFileParser` is the main reading entry point when the problem is format import.
- The `alignment` package matters only after you already know you need gap handling or preprocessing policy.
- Two writer files, `GeneBankWriter` and `NexmlWriter`, currently exist as commented scaffolds rather than active implementations and should be documented accordingly.

## Reading Strategy

- Start with `FastaReader`, `TSVReader`, or `MSAFileParser` depending on the input format you care about.
- Move into `fasta.stat` or `fasta.comparison` only after basic reading is already clear.
- Keep reusable library entry points separate from script-style workflow helpers when documenting or reviewing this area.

## Related Guides

- Pair this guide with [understand_packages3_parsers.md](understand_packages3_parsers.md) when your import workflow also depends on BLAST, Pfam, taxonomy, or remote services.
- Pair this guide with [understand_packages4_utilities.md](understand_packages4_utilities.md) when file handling needs broader helper code around paths, strings, or CLI wrappers.
