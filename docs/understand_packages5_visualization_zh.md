# eGPS-Base 分册 5：可视化

[文档索引](README_zh.md) | [English](understand_packages5_visualization.md)

## 文档定位

本分册面向图表、图形、树显示与双树可视化相关的维护工作。凡是问题已经从“原始树结构”进入布局、渲染、图例、坐标轴或树比较视图层面，都建议从这里开始阅读。

## 包级导读

### `graphic.engine`

用途：

- 提供共享的图表与图形基础设施

代表性源码文件：

- `AxisTickCalculator`
- `AxisTickCalculatorHeavy`
- `ColorMapper`
- `DefaultLinerColorMapper`
- `GradientColorHolder`
- `EGPSDrawUtil`
- `EGPSStrokeUtil`
- `ZzzModuleSignature`

重要子包：

- `colors`
- `drawer`
- `guibean`
- `guicalculator`
- `guirelated`
- `legend`

阅读要点：

- 坐标轴行为、图例、颜色映射以及通用图表辅助问题，都应从这里开始。
- 相关专题设计说明见 [design/axis_tick_calculation_zh.md](design/axis_tick_calculation_zh.md)。
- 最直接的回归测试参考，是 `src/test/java/graphic/engine` 下那组 `AxisTickCalculatorTest*.java`。

### `evoltree.txtdisplay`

用途：

- 为文本型或 Swing 型树显示路径提供带布局语义的显示基础类型

当前源码文件：

- `BaseGraphicNode`
- `ReflectGraphicNode`
- `TextTreeDescriber`
- `TreeDrawUnit`
- `ZzzModuleSignature`

阅读要点：

- 当问题与树布局状态、文本树渲染，或面向显示的节点数据有关，而不再只是原始树对象本身时，应优先进入这个包。
- 最直接的测试参考是 `src/test/java/evoltree/txtdisplay/TextTreeDescriberTest.java`。

### `evoltree.swingvis`

用途：

- 提供小型的 Swing 树绘制辅助类

当前源码文件：

- `OneNodeDrawer`
- `ZzzModuleSignature`

阅读要点：

- 虽然这个包不大，但在 Swing 路径里定位节点级树绘制行为时，它依然是最清晰的入口。

### `evoltree.tanglegram`

用途：

- 提供双树布局与并排比较视图

当前源码文件：

- `BaseRectangularLayoutCalculator`
- `LeftRectangularLayoutQuickCalculator`
- `RightRectangularLayoutQuickCalculator`
- `PairwisePaintingPanel`
- `QuickPairwiseTreeComparator`
- `ZzzModuleSignature`

阅读要点：

- 当任务是把两棵树放在一起展示、做视觉对齐或并排比较，而不是只计算一个数值距离时，应优先阅读这里。

## 阅读建议

- 坐标轴、图例、颜色系统与通用图表行为，先看 `graphic.engine`。
- 树布局状态或文本树输出问题，先看 `evoltree.txtdisplay`。
- 双树并排比较或成对展示问题，先看 `evoltree.tanglegram`。
- 只有当你已经进入 Swing 绘制路径时，再进一步阅读 `evoltree.swingvis`。

## 相关文档

- 若要同时理解树结构本身与其渲染方式，请结合 [understand_packages1_core_structure_zh.md](understand_packages1_core_structure_zh.md) 阅读。
- 若任务同时涉及 Swing 或辅助层代码，请结合 [understand_packages4_utilities_zh.md](understand_packages4_utilities_zh.md) 阅读。
