# eGPS-Base Package Guide 4: Utilities And Tools

[Docs Index](README.md) | [中文](understand_packages4_utilities_zh.md)

## Document Role

This guide is for maintainers working in the repository's cross-cutting support layer. Read it when your task involves file helpers, strings, collections, persistence, CLI wrappers, small GUI helpers, or the lightweight unified-output abstraction.

## Package Guide

### `utils`

Role:

- provides broad utility support reused across many packages

Representative source files:

- `EGPSFileUtil`
- `EGPSGeneralUtil`
- `EGPSListUtil`
- `EGPSUtil`
- `EGPSObjectCounter`
- `EGPSObjectsUtil`
- `EGPSGuiUtil`
- `EGPSFormatUtil`
- `ZzzModuleSignature`

Reading note:

- This package is intentionally broad, so documentation should point readers to specific classes rather than telling them only to "look in utils".

### `utils.string`

Role:

- provides text-processing helpers

Current source files:

- `EGPSStringUtil`
- `StringCounter`

Reading note:

- This is the first stop for tabular text splitting, bracket validation, or small high-frequency string operations.

### `utils.datetime`

Role:

- provides date and time formatting or conversion helpers

Current source files:

- `DateTimeOperator`
- `EGPSTimeUtil`

### `utils.collections`

Role:

- provides collection-level helpers that sit outside the broader `utils` catch-all classes

Current source files:

- `CollectionUtils`
- `CombinatoricsUtil`

### `utils.storage`

Role:

- provides small persistence-oriented helpers

Current source files:

- `FinalFields`
- `MapPersistence`
- `ObjectPersistence`

Reading note:

- Read this package when a task is more about storing or restoring in-memory structures than about parsing a domain format.

### `cli.tools`

Role:

- provides the curated, user-facing CLI registry and its directly maintained tools

Current source files:

- `ListTools`
- `ClipboardPath4Win2WSL`
- `ClipboardPathNormalized`
- `CountFilesWithSuffix`
- `ListFilesWithSuffix`
- `CheckNwkFormat`
- `RemoveInternalNodeNames`
- `NodeNames4Space4Underline`
- `SeeModulesWeHave`
- `ZzzModuleSignature`

Reading note:

- Treat this package as the maintained CLI surface, not as a census of every `main()` method in the repository.
- `check_before_gitpush.md` explicitly asks maintainers to review `ListTools` when user-facing CLI entry points change.

### `gui.simple.tools`

Role:

- provides small GUI wrappers around convenience functionality

Current source files:

- `FilePathNormalizedGUI`
- `ZzzModuleSignature`

Reading note:

- This area is intentionally narrow and should stay narrow unless the repository grows a broader maintained GUI toolkit.

### `unified.output`

Role:

- provides a lightweight abstraction for printing to different destinations

Current source files:

- `UnifiedPrinter`
- `UnifiedPrinterBuilder`
- `ZzzModuleSignature`

Implementation note:

- `ConsolePrinter` and `FilePrinter` currently live as package-private classes inside `UnifiedPrinterBuilder.java`.
- There is no additional nested `output` subpackage under `unified.output`.

## Reading Strategy

- Start with `utils` or `utils.string` for most cross-cutting helper work.
- Move into `utils.collections`, `utils.storage`, or `utils.datetime` when the task is clearly scoped to one of those narrower concerns.
- Read `cli.tools` only when the question is about the maintained command-line surface.

## Related Guides

- Pair this guide with [understand_packages2_file_io.md](understand_packages2_file_io.md) when utilities are being used around FASTA, TSV, or MSA workflows.
- Pair this guide with [understand_packages5_visualization.md](understand_packages5_visualization.md) when helper code is being used in Swing or graphics-adjacent paths.
