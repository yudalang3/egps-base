# eGPS-Base Package Guide 3: Parsers And External Data

[Docs Index](README.md) | [中文](understand_packages3_parsers_zh.md)

## Document Role

This guide is for maintainers who are tracing data that enters the repository from external formats, external databases, or remote services. It also covers the smaller analysis and phylogeny-facing packages that sit immediately downstream of that imported data.

## Package Guide

### `blast.parse`

Role:

- parses BLAST result structures

Current source files:

- `BlastHspRecord`
- `ZzzModuleSignature`

Reading note:

- This is a focused parser area rather than a complete BLAST workflow framework.

### `pfam.parse`

Role:

- parses Pfam and HMMER-related outputs and metadata

Current source files:

- `PfamScanRecord`
- `PfamDBEntry`
- `HmmDatParser`
- `TxtVisualizeSeqDomains`
- `ZzzModuleSignature`

Reading note:

- Start here when the input is `hmmscan --domtblout`, Pfam `.dat`, or a quick domain-summary task.

### `ncbi.taxonomy`

Role:

- parses taxonomy data and supports lineage-oriented lookup

Current source files:

- `TaxonomyParser`
- `TaxonomyFullNameLineageParser`
- `TaxonomyNode`
- `TaxonomyName`
- `TaxonomicRank`
- `API4R`
- `ZzzModuleSignature`

Reading note:

- This package offers both full-tree parsing and lighter lineage retrieval paths.

### `rest`

Role:

- provides remote-data access for Ensembl- and InterPro-related workflows

Current source tree:

- `rest.ensembl`
- `rest.ensembl.compara`
- `rest.ensembl.ensembrest`
- `rest.ensembl.phylo`
- `rest.ensembl.proteins`
- `rest.interpro.entrytaxon`

Representative classes:

- `EnsemblTranslationRest`
- `ConvertGeneIDsByHgncTable`
- `EnsGeneTreeInfoRest`
- `EnsHomologyInforRest`
- `OverlapTransBeanParser`
- `EnsJsonTreeParser`
- `EnsJsonTreeParserCustomized`
- `RestGetProteinDomains`
- `InterProEntryTaxonInfoParser`

Reading note:

- This part of the documentation is deliberately based on the real source tree, not on abstract placeholder layers such as `client`, `model`, or `service`.

### `analysis`

Role:

- provides lightweight analysis scaffolding and numerical helpers

Current source files and subpackages:

- `AbstractAnalysisAction`
- `analysis.math.DoubleListUtils`
- `analysis.math.RandomArrayGenerator`
- `analysis.math.ZzzModuleSignature`
- `analysis.ZzzModuleSignature`

Reading note:

- This package is small, but it is the right place to inspect when a parser-adjacent workflow needs reusable numerical or action-style support.

### `phylo`

Role:

- provides downstream phylogeny-facing algorithms built on the repository's tree and alignment primitives

Current subpackages:

- `phylo.algorithm` - includes `RobinsonFouldsMetricCalculator`
- `phylo.gsefea` - includes `DoGSEFEA`
- `phylo.msa.util` - includes `EvolutionaryProperties` and `MsaCommonUtil`

Reading note:

- These packages sit closer to application logic than the lower-level parser or tree-structure layers.

## Reading Strategy

- Start with `blast.parse`, `pfam.parse`, or `ncbi.taxonomy` when the problem begins with a concrete external format.
- Start with `rest.*` when your workflow depends on Ensembl or InterPro data acquisition.
- Move into `analysis` or `phylo.*` only after the upstream data path is already clear.

## Related Guides

- Pair this guide with [understand_packages2_file_io.md](understand_packages2_file_io.md) when parser output is part of a larger import pipeline.
- Pair this guide with [understand_packages1_core_structure.md](understand_packages1_core_structure.md) when parsed data eventually becomes tree objects.
