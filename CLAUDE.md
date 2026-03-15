# CLAUDE.md

This file provides guidance to coding agents working in this repository.

## Scope

- Repository: `egps-base`, the eGPS 2.1 base library.
- Language: Java 25.
- Build tool: Maven.
- Packaging: `jar`.
- Test framework: JUnit Jupiter.
- Main sources: `src/main/java`.
- Tests: `src/test/java`.
- Non-Java files under `src/main/java` are packaged as resources.

## Additional Instruction Files

- `check_before_gitpush.md` is part of the repository workflow and should be followed.
- No `.cursor/rules/` directory was found.
- No `.cursorrules` file was found.
- No `.github/copilot-instructions.md` file was found.

## Project Overview

**eGPS-Base** is a foundational bioinformatics utility library for evolutionary genomics and phylogenetic analysis within the eGPS 2.1 ecosystem.

- Full documentation: https://www.yuque.com/u21499046/egpsdoc
- The codebase is package-oriented and spans FASTA, MSA, TSV, REST, graphics, phylogeny, and general utility modules.
- Functional modules are typically represented by `ZzzModuleSignature` classes implementing `top.signature.IModuleSignature`.
- Many classes expose `main()` methods for CLI utilities or ad hoc script execution.

## Build And Development Commands

### Build

```bash
# Full package build
mvn clean package source:jar

# Compile only
mvn compile

# Clean outputs
mvn clean

# Copy runtime dependencies to a directory
mvn dependency:copy-dependencies -DoutputDirectory=/absolute/output/dir
```

### Tests

```bash
# Run all tests
mvn test

# Run one test class
mvn -Dtest=PhyloTreeEncoderDecoderTest test

# Run one fully-qualified test class
mvn -Dtest=evoltree.phylogeny.PhyloTreeEncoderDecoderTest test

# Run one test method
mvn -Dtest=PhyloTreeEncoderDecoderTest#testEncodeSimpleTree test

# Run one package pattern
mvn -Dtest="evoltree.phylogeny.*" test
```

Notes:

- Prefer Surefire `-Dtest=...` syntax for targeted test runs.
- Not every file under `src/test/java` is a stable automated test.
- Files under `src/test/java/script/run/once` are often one-off scripts.
- JUnit tests are the files that actually use `@Test`.

### JAR Scripts

```bash
# Package the existing target/classes tree into a jar and copy it out
./build_jar.sh /absolute/output_dir

# Copy the built jar to multiple local deployment targets
./build_jar_and_remove.sh
```

Important:

- `build_jar.sh` packages the current `target/classes` directory; it does not compile sources first.
- Run Maven compile/package before using the jar script if `target/classes` may be stale.

### Running CLI Or GUI Utilities

```bash
# List registered CLI tools
java -cp "target/classes:dependency-egps/*" cli.tools.ListTools

# Example GUI utility
java -cp "dependency-egps/*" gui.simple.tools.FilePathNormalizedGUI
```

When adding a real CLI tool with `main()`, update `src/main/java/cli/tools/ListTools.java`.

## Maven Caveat

- Maven may require network access to resolve plugins and dependencies.
- In this environment, a Maven test run hit an SSL/plugin-resolution failure for `maven-resources-plugin`.
- Treat that as an environment or network issue, not proof that the project itself is broken.
- Do not change dependency versions or repository definitions just to work around transient resolution failures.

## Linting And Static Checks

- There is no configured lint command in `pom.xml`.
- No Checkstyle, Spotless, PMD, SpotBugs, or formatter plugin is configured.
- Do not invent a lint step in commits, docs, or automation.
- Match the style already used by the surrounding code instead.

## Architecture

### Module System

The project uses a module-signature pattern:

- Modules commonly expose a `ZzzModuleSignature` class.
- Those classes implement `IModuleSignature`.
- `IModuleSignature` provides `getShortDescription()` and `getTabName()`.
- These metadata classes are used for discovery and GUI integration.

### Package Structure

Top-level packages are organized by domain, for example:

- `evoltree.*` - tree structures, encoding/decoding, visualization, comparison.
- `fasta.*` - FASTA I/O, statistics, and comparison utilities.
- `msaoperator.*` - multiple sequence alignment formats and operations.
- `tsv.io` - TSV and table helpers.
- `rest.*` - REST clients and parsers.
- `cli.tools` - command-line utilities.
- `gui.simple.tools` - small GUI tools.
- `utils.*` - general-purpose helpers.
- `analysis.*`, `blast.parse`, `pfam.parse`, `ncbi.taxonomy`, `phylo.*` - domain-specific utilities.

### Tree Code

For phylogenetic tree work:

- Prefer `evoltree.struct.ArrayBasedNode` for new production code.
- `LinkedBasedNode` exists, but `ArrayBasedNode` is the default choice.
- Serialization centers on `TreeCoder`, `TreeDecoder`, and `AbstractNodeCoderDecoder`.
- Tree numeric formatting uses 6 decimal places and `RoundingMode.HALF_UP`.
- Reuse the existing template-method approach in tree codecs instead of introducing reflection-heavy factories.

## Repository-Specific Workflow Rules

- Keep `README.md` and `README_zh.md` synchronized when either one changes.
- This is explicitly required by `check_before_gitpush.md`.
- Register newly added CLI entrypoints in `cli.tools.ListTools`.
- Preserve unrelated user changes in a dirty worktree.
- Prefer focused edits over repository-wide reformatting.

## Code Style

### General

- Match the style of the file you are editing.
- Do not normalize the whole repository to one indentation or formatting style.
- Older core files often use tabs.
- Newer or recently revised files often use 4 spaces.
- Preserve existing indentation in touched files.
- Keep lines readable and avoid wrapping churn.
- Prefer small, local diffs over broad cleanup.

### Imports

- Keep one import per line.
- Preserve the local import grouping and ordering already used by the file.
- The repository does not use one universal import sorter.
- Many files group project or third-party imports separately from Java imports.
- Tests often keep static assertion imports last.
- Avoid new wildcard imports in production code unless the file already uses them for a good reason.

### Naming

- Packages are lowercase and domain-oriented, for example `evoltree.phylogeny` and `fasta.stat`.
- Public classes use PascalCase.
- Utility classes tend to use explicit names like `EGPSFileUtil`, `EvolNodeUtil`, and `CollectionUtils`.
- Module metadata classes are conventionally named `ZzzModuleSignature`.
- CLI classes should use descriptive names.
- Script-style operational classes may use the `Script_*` prefix; preserve that convention when extending similar code.
- Test classes usually end with `Test`.
- Test methods use descriptive camelCase names.

### Types And APIs

- Java 25 is available; modern language features are fine when they improve clarity.
- Records are already used in places like `ListTools.FileEntry`.
- Local `var` appears in the codebase, but use it only when the type is still obvious.
- Prefer concrete, readable types in public APIs.
- Use generics precisely; avoid raw collections.
- Use `Optional<T>` where the repository already models results as optional.
- Prefer direct instantiation over reflection in performance-sensitive paths.

### Formatting And Structure

- Keep the package declaration first.
- Group class members logically: constants/fields, constructors, public methods, helper methods.
- Preserve the comment density already present in the file.
- Many public classes include substantial Javadoc.
- Add Javadoc for new public APIs when behavior is non-obvious.
- Do not add noisy comments for straightforward code.
- Prefer expressive method names over explanatory comments.
- Keep helper methods `private` unless a wider visibility is needed.

## Error Handling

- Favor clear, actionable error messages.
- For CLI tools, print usage/help text when arguments are invalid.
- Existing CLI tools often write user-facing problems to `System.err` and normal results to `System.out`.
- Preserve that stdout/stderr split for new command-line tools.
- Prefer try-with-resources for streams, readers, writers, and channels.
- Do not silently swallow exceptions.
- Propagate checked exceptions when that keeps the code simpler and matches nearby code.
- If converting to a runtime exception, include useful context.
- Legacy code sometimes uses `printStackTrace()`; new code should prefer structured messages or rethrow with detail.
- Use `Objects.requireNonNull` for required inputs when it improves failure clarity.

## Testing Style

- JUnit 5 (`org.junit.jupiter.api.Test`) is the standard test framework.
- Common assertions include `assertEquals`, `assertTrue`, `assertNotNull`, `assertFalse`, and `assertThrows`.
- Keep tests small and structural.
- Prefer deterministic in-memory fixtures over external files when possible.
- When file-based tests are necessary, use `java.nio.file.Path` and `Files`.
- Avoid tests that depend on private machine-specific paths.
- Some test classes also contain manual `main()` methods; do not assume those are CI-style tests.

## CLI Implementation Guidance

- Use Apache Commons CLI for non-trivial argument parsing.
- Include `-h` or `--help` support for new CLI tools.
- Print a short purpose and output description in the help text.
- Keep stdout machine-friendly when output is likely to be piped.
- Keep status messages and errors on stderr.
- If the tool is user-facing, add it to `ListTools` with a clear one-line description.

## Documentation And IDE Notes

- The `docs/` directory contains broader package and architecture notes.
- Chinese companion documentation often exists with `_zh.md` suffixes.
- The project is commonly used from IntelliJ IDEA as a Maven project with Java 25 configured.

## Before Finishing A Change

- Run the narrowest relevant test command first.
- If you add a CLI entrypoint, update `cli.tools.ListTools`.
- If you touch a README, check whether the paired language version also needs updating.
- Do not change build tooling unless the task explicitly requires it.
- Do not add new lint or formatting infrastructure unless requested.

