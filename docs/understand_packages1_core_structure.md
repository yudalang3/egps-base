# eGPS-Base Package Guide 1: Core Structure

[Docs Index](README.md) | [中文](understand_packages1_core_structure_zh.md)

## Document Role

This guide is for maintainers who need a reliable starting point for the repository's structural core. Read it first when your work touches module metadata, generic tree objects, phylogenetic tree codecs, or genetic code implementations.

## Package Guide

### `top.signature`

Role:

- defines the lightweight module-signature convention used across the repository

Current source files:

- `IModuleSignature`
- `Version`
- `ZzzModuleSignature`
- `Readme.txt`

Reading note:

- Start here if you need to understand why so many packages carry a `ZzzModuleSignature` class.
- Discovery-oriented tools such as `SeeModulesWeHave` depend on this convention.

### `evoltree.struct`

Role:

- defines the generic tree model used by higher-level tree code

Current source files:

- `EvolNode`
- `ArrayBasedNode`
- `LinkedBasedNode`
- `TreeCoder`
- `TreeDecoder`
- `ZzzModuleSignature`

Reading note:

- `ArrayBasedNode` and `LinkedBasedNode` are the two concrete starting points for understanding how nodes are stored.
- `TreeCoder` and `TreeDecoder` mark the seam between raw tree objects and format-specific codecs.

### `evoltree.struct.io`

Role:

- factors out reusable node-level parsing and serialization behavior

Current source files:

- `AbstractNodeCoderDecoder`
- `BasicInternalCoderDecoder`
- `BasicLeafCoderDecoder`
- `PrimaryNodeTreeDecoder`

Reading note:

- Read this package when you need to understand how node parsing is abstracted away from a concrete tree format.
- It is the bridge between the generic tree model and higher-level codecs.

### `evoltree.struct.util`

Role:

- provides operational helpers built on top of the generic tree model

Current source files:

- `EvolNodeUtil`
- `EvolTreeComparator`
- `EvolTreeOperator`
- `PhyloTreeGenerator`

Reading note:

- This package is more useful than the base interfaces when your task is to inspect, transform, compare, or generate trees.

### `evoltree.phylogeny`

Role:

- implements Newick-oriented phylogenetic tree reading and writing

Current source files:

- `DefaultPhyNode`
- `PhyloTreeEncoderDecoder`
- `NWKInternalCoderDecoder`
- `NWKLeafCoderDecoder`
- `ZzzModuleSignature`

Reading note:

- This is the package to read when a tree problem is no longer generic and is specifically about phylogenetic data or Newick text.
- The main regression-style reference here is `src/test/java/evoltree/phylogeny/PhyloTreeEncoderDecoderTest.java`.

### `geneticcodes`

Role:

- provides translation abstractions and shared genetic-code behavior

Current source files:

- `IGeneticCode`
- `GeneticCode`
- `AminoAcid`
- `ZzzModuleSignature`

Reading note:

- This package is adjacent to the tree stack rather than inside it, but it is part of the same biological foundation layer.

### `geneticcodes.codeTables`

Role:

- contains the concrete genetic-code tables that currently ship with the repository

Current source files:

- `TheStandardCode`
- `TheVertebrateMitochondrialCode`
- `TheInvertebrateMitochondrialCode`
- `TheYeastMitochondrialCode`
- `TheBacterialPlantPlastidCode`
- `TheCiliateDHexamitaNuclearCode`
- `TheEuplotidNuclearCode`
- `TheEFMitochondrialCode`
- `TheMPCMCMSCode`

Reading note:

- This section should always be documented from the source files that actually exist in the repository, not from historical translation-table lists copied from elsewhere.

## Reading Strategy

- Start with `top.signature` if you are trying to understand the repository's module convention.
- Start with `evoltree.struct` and `evoltree.struct.io` if you are tracing how tree objects are represented and serialized.
- Start with `evoltree.phylogeny` if you are debugging Newick-specific behavior.
- Start with `geneticcodes` only when your task is translation or code-table related rather than tree-structure related.

## Related Guides

- Pair this guide with [understand_packages5_visualization.md](understand_packages5_visualization.md) when data structures and rendering need to be understood together.
- Pair this guide with [understand_packages3_parsers.md](understand_packages3_parsers.md) when parsed taxonomy or REST data ultimately becomes tree content.
