# eGPS-Base Package Guide 5: Visualization

[Docs Index](README.md) | [中文](understand_packages5_visualization_zh.md)

## Document Role

This guide is for maintainers working on charting, graphics, tree display, or paired-tree visualization. Read it when your task is no longer about raw tree data structures and has moved into layout, rendering, legends, axes, or tree-comparison views.

## Package Guide

### `graphic.engine`

Role:

- provides shared charting and graphics infrastructure

Representative source files:

- `AxisTickCalculator`
- `AxisTickCalculatorHeavy`
- `ColorMapper`
- `DefaultLinerColorMapper`
- `GradientColorHolder`
- `EGPSDrawUtil`
- `EGPSStrokeUtil`
- `ZzzModuleSignature`

Important subpackages:

- `colors`
- `drawer`
- `guibean`
- `guicalculator`
- `guirelated`
- `legend`

Reading note:

- Start here for axis behavior, legends, color mapping, or general charting helpers.
- The focused companion note is [design/axis_tick_calculation.md](design/axis_tick_calculation.md).
- The dedicated regression-style tests are the `AxisTickCalculatorTest*.java` files under `src/test/java/graphic/engine`.

### `evoltree.txtdisplay`

Role:

- provides layout-aware tree display primitives for text-oriented or Swing-oriented rendering paths

Current source files:

- `BaseGraphicNode`
- `ReflectGraphicNode`
- `TextTreeDescriber`
- `TreeDrawUnit`
- `ZzzModuleSignature`

Reading note:

- Read this package when the question is about tree layout state, text rendering, or display-oriented node data rather than about the raw phylogenetic tree object itself.
- The most direct test reference is `src/test/java/evoltree/txtdisplay/TextTreeDescriberTest.java`.

### `evoltree.swingvis`

Role:

- provides small Swing-specific tree rendering helpers

Current source files:

- `OneNodeDrawer`
- `ZzzModuleSignature`

Reading note:

- This package is small, but it is still the clearest starting point for node-level tree painting inside Swing code.

### `evoltree.tanglegram`

Role:

- provides paired-tree layout and side-by-side comparison views

Current source files:

- `BaseRectangularLayoutCalculator`
- `LeftRectangularLayoutQuickCalculator`
- `RightRectangularLayoutQuickCalculator`
- `PairwisePaintingPanel`
- `QuickPairwiseTreeComparator`
- `ZzzModuleSignature`

Reading note:

- This area matters when two trees need to be displayed together, aligned visually, or inspected side by side rather than compared only by a numeric metric.

## Reading Strategy

- Start with `graphic.engine` for axes, legends, color systems, or general charting behavior.
- Start with `evoltree.txtdisplay` when the problem is specifically about tree-layout state or textual tree output.
- Start with `evoltree.tanglegram` when the task is paired-tree comparison or dual-tree presentation.
- Read `evoltree.swingvis` only when you are already in a Swing rendering path.

## Related Guides

- Pair this guide with [understand_packages1_core_structure.md](understand_packages1_core_structure.md) when rendering and tree structures need to be understood together.
- Pair this guide with [understand_packages4_utilities.md](understand_packages4_utilities.md) when Swing or helper-layer code is involved in the same task.
