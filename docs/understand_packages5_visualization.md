# eGPS-Base Package Documentation - Part 5: Visualization & Graphics

## Overview

This document covers packages for data visualization, tree rendering, and GUI components including scientific plotting, color mapping, and tree display utilities.

---

## 1. Graphics Engine

### 1.1 `graphic.engine` - Graphics Engine

**Purpose:** GUI infrastructure and visualization components

**Sub-packages:**
- `colors`: Color palettes and management
- `drawer`: Drawing utilities
- `guibean`: GUI components
- `legend`: Legend rendering

#### Key Classes:

**`AxisTickCalculator`** (Class)
- **Function:** Standard axis tick calculation for scientific plots
- **Key Features:**
  - Nice Numbers algorithm implementation
  - Automatic format selection (normal/scientific notation)
  - Space-adaptive tick density
  - Standard tick intervals: {1.0, 2.0, 5.0, 10.0}
- **Key Methods:**
  - `determineAxisTick()`: Main calculation method
  - `setMinAndMaxPair()`: Set data range
  - `setWorkingSpace()`: Set available pixel space
- **Performance:** O(n) complexity, lightweight memory usage
- **Importance:** ⭐⭐⭐⭐ (Core visualization component)

**`AxisTickCalculatorByClaude4`** (Class)
- **Function:** Advanced axis tick calculator with intelligent optimization
- **Key Features:**
  - Adaptive step selection with multiple density options
  - Extended Nice Numbers sequence: {1.0, 1.2, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0, 6.0, 8.0, 10.0}
  - Quality scoring system for optimal layout selection
  - Intelligent boundary coverage
  - Multi-step hint evaluation: {32, 48, 64, 80, 96}
- **Key Methods:**
  - `determineAxisTick()`: Enhanced calculation with quality scoring
  - `getCurrentStepHint()`: Get selected step hint
  - `evaluateResult()`: Quality assessment of tick layouts
- **Performance:** Slightly higher complexity than standard version but provides superior aesthetics
- **Use Case:** When optimal tick layout is critical for presentation
- **Importance:** ⭐⭐⭐⭐ (Enhanced visualization)

**`ColorScheme`** (Class)
- **Function:** Color scheme management

**`ColorMapper`** (Interface)
- **Function:** Interface for mapping numeric values to colors for data visualization
- **Key Methods:**
  - `mapColor(double)`: Map single value to color
  - `mapColors(List<Double>)`: Map list of values to colors
- **Use Cases:**
  - Heatmap generation
  - Gradient visualization
  - Data-driven coloring
- **Importance:** ⭐⭐⭐⭐ (Color mapping)

**`DefaultLinerColorMapper`** (Class)
- **Function:** Default linear color mapper using blue-white-red gradient
- **Key Features:**
  - Linear gradient from blue (min) to white (mid) to red (max)
  - NA value handling with custom color (default gray)
  - Min/max range normalization
  - Gradient color scheme customization
- **Key Methods:**
  - `mapColor(double)`: Map value to gradient color
  - `mapColors(List<Double>)`: Batch color mapping
- **Use Cases:**
  - Expression heatmaps
  - Numerical data visualization
  - Score-based coloring
- **Importance:** ⭐⭐⭐⭐ (Default color mapping)

**`GradientColorHolder`** (Class)
- **Function:** Manages gradient color schemes for smooth color transitions
- **Key Features:**
  - Custom gradient with multiple control points
  - Smooth color interpolation
  - Position-based color retrieval (0.0 to 1.0)
- **Key Methods:**
  - `setColorScheme(float[], Color[])`: Define gradient stops
  - `getColorFromPallet(double)`: Get color at position
- **Use Cases:**
  - Custom color gradients
  - Multi-point color schemes
- **Importance:** ⭐⭐⭐⭐ (Gradient management)

**`EGPSDrawUtil`** (Class)
- **Function:** Utility class for common drawing operations
- **Key Features:**
  - Arrow drawing with customizable size
  - Line and shape primitives
  - Geometric calculations for graphics
- **Key Methods:**
  - `drawArrow(Graphics2D, Line2D, int)`: Draw arrow with size
- **Use Cases:**
  - Phylogenetic tree arrows
  - Directed graphs
  - Annotation graphics
- **Importance:** ⭐⭐⭐⭐ (Drawing utilities)

**`EGPSStrokeUtil`** (Class)
- **Function:** Stroke management for line styles
- **Key Features:**
  - Predefined stroke styles
  - Line width customization
- **Importance:** ⭐⭐⭐ (Line styling)

**`ZzzModuleSignature`** (Class)
- **Function:** Module signature for graphics engine
- **Description:** Graphics engine module providing visualization components, color mapping, and drawing utilities
- **Importance:** ⭐⭐⭐ (Module metadata)

**Sub-packages:**
- `colors`: Color palettes and management (EGPSColors)
- `drawer`: Drawing utilities (SectorRingDrawer)
- `guibean`: GUI components (EGPSInsets, ColorIcon)
- `guicalculator`: GUI calculations (GuiCalculator, BlankArea)
- `guirelated`: GUI utilities (EGPSVectorExporter, SwingDebugger, JTextAreaPrintStream, SwingDisplayInterface, GradientColorHolder)
- `legend`: Legend rendering (LinerColorLegendPainter, CategloryLegendPainter)

**Importance:** ⭐⭐⭐⭐ (Visualization)

---

## 2. Tree Visualization

### 2.1 `evoltree.swingvis` - Tree Visualization

**Purpose:** Swing-based tree visualization

**Key Classes:**
- `OneNodeDrawer`: Render individual nodes

**Importance:** ⭐⭐⭐ (Tree display)

---

### 2.2 `evoltree.txtdisplay` - Text-based Tree Display

**Purpose:** Tree nodes with graphical display properties for text and GUI visualization

#### Key Classes:

**`BaseGraphicNode`** (Class)
- **Function:** Base class for phylogenetic tree nodes with graphical display properties
- **Key Features:**
  - Extends ArrayBasedNode with display coordinates
  - X/Y coordinate storage (parent and self positions)
  - Branch length properties (real and display lengths)
  - Support for rotational display (spiral trees)
  - Node collapse state management
  - Depth tracking for tree traversal
- **Key Fields:**
  - `x1, y1`: Parent node coordinates
  - `x2, y2`: Self node coordinates
  - `angle`: Rotation angle for spiral layouts
  - `depth`: Node depth (0 for leaves)
  - `realBranchLength`: Actual branch length
  - `hideNodeFlag`: Node visibility control
- **Use Cases:**
  - GUI-based tree visualization
  - Text-based tree rendering
  - Interactive tree displays
  - Multiple layout styles (rectangular, circular, spiral)
- **Importance:** ⭐⭐⭐⭐ (Tree visualization foundation)

**`ZzzModuleSignature`** (Class)
- **Function:** Module signature for text-based tree display tools
- **Description:** Tree text display module providing text rendering and visualization capabilities for phylogenetic trees
- **Importance:** ⭐⭐⭐ (Module metadata)

**Importance:** ⭐⭐⭐⭐ (Tree display utilities)

---

### 2.3 `evoltree.tanglegram` - Tanglegram

**Purpose:** Compare two phylogenetic trees side-by-side

#### Key Classes:

**`LeftRectangularLayoutQuickCalculator`** (Class)
- **Function:** Calculator for left-side rectangular tree layout in tanglegram visualization
- **Key Features:**
  - Calculates node positions for left tree panel
  - Handles height and width distribution
  - Automatic depth calculation
  - Leaf spacing optimization
- **Key Methods:**
  - `calculateTree()`: Main layout calculation method
  - `iterate2getMaxDepth()`: Calculate maximum tree depth
  - `iterateTree2assignLocation()`: Assign X/Y coordinates to nodes
- **Use Cases:**
  - Left panel tree positioning in tanglegram
  - Dual tree alignment visualization
- **Importance:** ⭐⭐⭐ (Tanglegram layout)

**`RightRectangularLayoutQuickCalculator`** (Class)
- **Function:** Calculator for right-side rectangular tree layout in tanglegram visualization
- **Key Features:**
  - Mirror layout of left calculator
  - Right-to-left tree positioning
  - Synchronized leaf alignment with left tree
- **Use Cases:**
  - Right panel tree positioning in tanglegram
  - Maintains correspondence with left tree
- **Importance:** ⭐⭐⭐ (Tanglegram layout)

**`PairwisePaintingPanel`** (Class)
- **Function:** Panel for rendering tanglegram with correspondence lines
- **Key Features:**
  - Dual tree rendering
  - Correspondence line drawing between matching leaves
  - Interactive visualization
  - Customizable tree layouts
- **Use Cases:**
  - Visual tree comparison
  - Gene-species tree reconciliation
  - Co-phylogeny analysis
- **Importance:** ⭐⭐⭐⭐ (Tanglegram visualization)

**`QuickPairwiseTreeComparator`** (Class)
- **Function:** Quick comparison tool for two phylogenetic trees
- **Key Features:**
  - Side-by-side tree comparison
  - Automatic layout calculation
  - Rapid visualization setup
- **Use Cases:**
  - Fast tree comparison
  - Quality control for tree inference
- **Importance:** ⭐⭐⭐ (Tree comparison)

**Key Features:**
- Dual tree visualization
- Correspondence lines
- Tree comparison

**Importance:** ⭐⭐⭐ (Tree comparison)

---

## Package Importance Summary

### ⭐⭐⭐⭐ Important (Frequently-used):
1. `graphic.engine` - Visualization components
2. `evoltree.txtdisplay` (BaseGraphicNode) - Tree visualization foundation
3. `evoltree.tanglegram` (PairwisePaintingPanel) - Tanglegram visualization

### ⭐⭐⭐ Useful (Specialized):
1. `evoltree.swingvis` - Tree display
2. `evoltree.tanglegram` (calculators) - Tanglegram layout

---

## Typical Usage Patterns

### Pattern 1: Scientific Plot with Ticks
```
1. Create tick calculator: new AxisTickCalculator()
2. Set data range: setMinAndMaxPair(min, max)
3. Set pixel space: setWorkingSpace(pixels)
4. Calculate ticks: determineAxisTick()
5. Render plot with calculated ticks
```

### Pattern 2: Heatmap Generation
```
1. Create color mapper: new DefaultLinerColorMapper()
2. Set data range (min, max)
3. Map values: mapColors(dataList)
4. Render heatmap with mapped colors
```

### Pattern 3: Tree Visualization
```
1. Create tree nodes: BaseGraphicNode
2. Calculate layout: assign x1, y1, x2, y2
3. Set display properties (depth, angle)
4. Render tree with OneNodeDrawer
```

### Pattern 4: Tanglegram Visualization
```
1. Load two trees
2. Calculate left layout: LeftRectangularLayoutQuickCalculator
3. Calculate right layout: RightRectangularLayoutQuickCalculator
4. Render with PairwisePaintingPanel
5. Draw correspondence lines
```

### Pattern 5: Custom Gradient
```
1. Create gradient: new GradientColorHolder()
2. Define color stops: setColorScheme(positions, colors)
3. Get colors: getColorFromPallet(position)
4. Apply to visualization
```

---

## Color Mapping Examples

### Example 1: Expression Heatmap
```java
DefaultLinerColorMapper mapper = new DefaultLinerColorMapper();
mapper.setMin(-2.0);
mapper.setMax(2.0);
List<Color> colors = mapper.mapColors(expressionValues);
```

### Example 2: Custom Gradient
```java
GradientColorHolder gradient = new GradientColorHolder();
float[] positions = {0.0f, 0.5f, 1.0f};
Color[] colors = {Color.BLUE, Color.WHITE, Color.RED};
gradient.setColorScheme(positions, colors);
Color midColor = gradient.getColorFromPallet(0.5);
```

---

## Axis Tick Calculation Examples

### Example 1: Standard Ticks
```java
AxisTickCalculator calc = new AxisTickCalculator();
calc.setMinAndMaxPair(0.0, 100.0);
calc.setWorkingSpace(500); // 500 pixels
calc.determineAxisTick();
// Returns optimal tick positions
```

### Example 2: Enhanced Ticks
```java
AxisTickCalculatorByClaude4 calc = new AxisTickCalculatorByClaude4();
calc.setMinAndMaxPair(0.0, 100.0);
calc.setWorkingSpace(500);
calc.determineAxisTick();
// Returns aesthetically optimized tick positions
```

---

## Best Practices

1. **Use AxisTickCalculatorByClaude4** for publication-quality plots
2. **Use DefaultLinerColorMapper** for standard heatmaps
3. **Create custom gradients** with GradientColorHolder for specialized visualizations
4. **Use BaseGraphicNode** as base class for tree visualization nodes
5. **Use tanglegram tools** for comparative phylogenetics

---

*This document covers Visualization and Graphics packages. See other parts for Core Structures, File I/O, Parsers, and Utilities.*
