# eGPS-Base 包文档 - 第5部分：可视化与图形

## 概述

本文档涵盖用于数据可视化、树渲染和 GUI 组件的包，包括科学绘图、颜色映射和树显示工具。

---

## 1. 图形引擎

### 1.1 `graphic.engine` - 图形引擎

**用途：** GUI 基础设施和可视化组件

**子包：**
- `colors`: 调色板和管理
- `drawer`: 绘图工具
- `guibean`: GUI 组件
- `legend`: 图例渲染

#### 关键类：

**`AxisTickCalculator`** (类)
- **功能：** 科学绘图的标准轴刻度计算
- **关键特性：**
  - Nice Numbers 算法实现
  - 自动格式选择（普通/科学计数法）
  - 空间自适应刻度密度
  - 标准刻度间隔：{1.0, 2.0, 5.0, 10.0}
- **关键方法：**
  - `determineAxisTick()`: 主计算方法
  - `setMinAndMaxPair()`: 设置数据范围
  - `setWorkingSpace()`: 设置可用像素空间
- **性能：** O(n) 复杂度，轻量级内存使用
- **重要性：** ⭐⭐⭐⭐ (核心可视化组件)

**`AxisTickCalculatorHeavy`** (类)
- **功能：** 具有智能优化的高级轴刻度计算器
- **关键特性：**
  - 具有多个密度选项的自适应步长选择
  - 扩展的 Nice Numbers 序列：{1.0, 1.2, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0, 6.0, 8.0, 10.0}
  - 用于最佳布局选择的质量评分系统
  - 智能边界覆盖
  - 多步提示评估：{32, 48, 64, 80, 96}
- **关键方法：**
  - `determineAxisTick()`: 具有质量评分的增强计算
  - `getCurrentStepHint()`: 获取选定的步长提示
  - `evaluateResult()`: 刻度布局的质量评估
- **性能：** 复杂度略高于标准版本，但提供更优越的美学效果
- **用例：** 当最佳刻度布局对演示至关重要时
- **重要性：** ⭐⭐⭐⭐ (增强可视化)

**`ColorScheme`** (类)
- **功能：** 颜色方案管理

**`ColorMapper`** (接口)
- **功能：** 用于将数值映射到颜色以进行数据可视化的接口
- **关键方法：**
  - `mapColor(double)`: 将单个值映射到颜色
  - `mapColors(List<Double>)`: 将值列表映射到颜色
- **用例：**
  - 热图生成
  - 渐变可视化
  - 数据驱动的着色
- **重要性：** ⭐⭐⭐⭐ (颜色映射)

**`DefaultLinerColorMapper`** (类)
- **功能：** 使用蓝-白-红渐变的默认线性颜色映射器
- **关键特性：**
  - 从蓝色（最小值）到白色（中值）到红色（最大值）的线性渐变
  - NA 值处理，带自定义颜色（默认灰色）
  - 最小/最大范围规范化
  - 渐变颜色方案自定义
- **关键方法：**
  - `mapColor(double)`: 将值映射到渐变颜色
  - `mapColors(List<Double>)`: 批量颜色映射
- **用例：**
  - 表达热图
  - 数值数据可视化
  - 基于分数的着色
- **重要性：** ⭐⭐⭐⭐ (默认颜色映射)

**`GradientColorHolder`** (类)
- **功能：** 管理用于平滑颜色过渡的渐变颜色方案
- **关键特性：**
  - 具有多个控制点的自定义渐变
  - 平滑的颜色插值
  - 基于位置的颜色检索（0.0 到 1.0）
- **关键方法：**
  - `setColorScheme(float[], Color[])`: 定义渐变停止点
  - `getColorFromPallet(double)`: 获取位置处的颜色
- **用例：**
  - 自定义颜色渐变
  - 多点颜色方案
- **重要性：** ⭐⭐⭐⭐ (渐变管理)

**`EGPSDrawUtil`** (类)
- **功能：** 常见绘图操作的工具类
- **关键特性：**
  - 可自定义大小的箭头绘制
  - 线条和形状基元
  - 图形的几何计算
- **关键方法：**
  - `drawArrow(Graphics2D, Line2D, int)`: 绘制带大小的箭头
- **用例：**
  - 系统发育树箭头
  - 有向图
  - 注释图形
- **重要性：** ⭐⭐⭐⭐ (绘图工具)

**`EGPSStrokeUtil`** (类)
- **功能：** 线条样式的笔画管理
- **关键特性：**
  - 预定义的笔画样式
  - 线宽自定义
- **重要性：** ⭐⭐⭐ (线条样式)

**`ZzzModuleSignature`** (类)
- **功能：** 图形引擎的模块签名
- **描述：** 图形引擎模块，提供可视化组件、颜色映射和绘图工具
- **重要性：** ⭐⭐⭐ (模块元数据)

**子包：**
- `colors`: 调色板和管理（EGPSColors）
- `drawer`: 绘图工具（SectorRingDrawer）
- `guibean`: GUI 组件（EGPSInsets、ColorIcon）
- `guicalculator`: GUI 计算（GuiCalculator、BlankArea）
- `guirelated`: GUI 工具（EGPSVectorExporter、SwingDebugger、JTextAreaPrintStream、SwingDisplayInterface、GradientColorHolder）
- `legend`: 图例渲染（LinerColorLegendPainter、CategloryLegendPainter）

**重要性：** ⭐⭐⭐⭐ (可视化)

---

## 2. 树可视化

### 2.1 `evoltree.swingvis` - 树可视化

**用途：** 基于 Swing 的树可视化

**关键类：**
- `OneNodeDrawer`: 渲染单个节点

**重要性：** ⭐⭐⭐ (树显示)

---

### 2.2 `evoltree.txtdisplay` - 基于文本的树显示

**用途：** 具有用于文本和 GUI 可视化的图形显示属性的树节点

#### 关键类：

**`BaseGraphicNode`** (类)
- **功能：** 具有图形显示属性的系统发育树节点的基类
- **关键特性：**
  - 使用显示坐标扩展 ArrayBasedNode
  - X/Y 坐标存储（父位置和自身位置）
  - 分支长度属性（真实和显示长度）
  - 支持旋转显示（螺旋树）
  - 节点折叠状态管理
  - 树遍历的深度跟踪
- **关键字段：**
  - `x1, y1`: 父节点坐标
  - `x2, y2`: 自身节点坐标
  - `angle`: 螺旋布局的旋转角度
  - `depth`: 节点深度（叶节点为 0）
  - `realBranchLength`: 实际分支长度
  - `hideNodeFlag`: 节点可见性控制
- **用例：**
  - 基于 GUI 的树可视化
  - 基于文本的树渲染
  - 交互式树显示
  - 多种布局样式（矩形、圆形、螺旋）
- **重要性：** ⭐⭐⭐⭐ (树可视化基础)

**`ZzzModuleSignature`** (类)
- **功能：** 基于文本的树显示工具的模块签名
- **描述：** 树文本显示模块，为系统发育树提供文本渲染和可视化功能
- **重要性：** ⭐⭐⭐ (模块元数据)

**重要性：** ⭐⭐⭐⭐ (树显示工具)

---

### 2.3 `evoltree.tanglegram` - Tanglegram

**用途：** 并排比较两个系统发育树

#### 关键类：

**`LeftRectangularLayoutQuickCalculator`** (类)
- **功能：** tanglegram 可视化中左侧矩形树布局的计算器
- **关键特性：**
  - 计算左树面板的节点位置
  - 处理高度和宽度分布
  - 自动深度计算
  - 叶间距优化
- **关键方法：**
  - `calculateTree()`: 主布局计算方法
  - `iterate2getMaxDepth()`: 计算最大树深度
  - `iterateTree2assignLocation()`: 为节点分配 X/Y 坐标
- **用例：**
  - tanglegram 中的左面板树定位
  - 双树对齐可视化
- **重要性：** ⭐⭐⭐ (Tanglegram 布局)

**`RightRectangularLayoutQuickCalculator`** (类)
- **功能：** tanglegram 可视化中右侧矩形树布局的计算器
- **关键特性：**
  - 左计算器的镜像布局
  - 从右到左的树定位
  - 与左树的叶对齐同步
- **用例：**
  - tanglegram 中的右面板树定位
  - 与左树保持对应关系
- **重要性：** ⭐⭐⭐ (Tanglegram 布局)

**`PairwisePaintingPanel`** (类)
- **功能：** 用于渲染带有对应线的 tanglegram 的面板
- **关键特性：**
  - 双树渲染
  - 在匹配的叶之间绘制对应线
  - 交互式可视化
  - 可自定义的树布局
- **用例：**
  - 视觉树比较
  - 基因-物种树协调
  - 共系统发育分析
- **重要性：** ⭐⭐⭐⭐ (Tanglegram 可视化)

**`QuickPairwiseTreeComparator`** (类)
- **功能：** 两个系统发育树的快速比较工具
- **关键特性：**
  - 并排树比较
  - 自动布局计算
  - 快速可视化设置
- **用例：**
  - 快速树比较
  - 树推断的质量控制
- **重要性：** ⭐⭐⭐ (树比较)

**关键特性：**
- 双树可视化
- 对应线
- 树比较

**重要性：** ⭐⭐⭐ (树比较)

---

## 包重要性总结

### ⭐⭐⭐⭐ 重要（常用）：
1. `graphic.engine` - 可视化组件
2. `evoltree.txtdisplay` (BaseGraphicNode) - 树可视化基础
3. `evoltree.tanglegram` (PairwisePaintingPanel) - Tanglegram 可视化

### ⭐⭐⭐ 有用（专门）：
1. `evoltree.swingvis` - 树显示
2. `evoltree.tanglegram` (计算器) - Tanglegram 布局

---

## 典型使用模式

### 模式 1：带刻度的科学绘图
```
1. 创建刻度计算器：new AxisTickCalculator()
2. 设置数据范围：setMinAndMaxPair(min, max)
3. 设置像素空间：setWorkingSpace(pixels)
4. 计算刻度：determineAxisTick()
5. 使用计算的刻度渲染绘图
```

### 模式 2：热图生成
```
1. 创建颜色映射器：new DefaultLinerColorMapper()
2. 设置数据范围（min, max）
3. 映射值：mapColors(dataList)
4. 使用映射的颜色渲染热图
```

### 模式 3：树可视化
```
1. 创建树节点：BaseGraphicNode
2. 计算布局：分配 x1, y1, x2, y2
3. 设置显示属性（depth, angle）
4. 使用 OneNodeDrawer 渲染树
```

### 模式 4：Tanglegram 可视化
```
1. 加载两棵树
2. 计算左布局：LeftRectangularLayoutQuickCalculator
3. 计算右布局：RightRectangularLayoutQuickCalculator
4. 使用 PairwisePaintingPanel 渲染
5. 绘制对应线
```

### 模式 5：自定义渐变
```
1. 创建渐变：new GradientColorHolder()
2. 定义颜色停止点：setColorScheme(positions, colors)
3. 获取颜色：getColorFromPallet(position)
4. 应用于可视化
```

---

## 颜色映射示例

### 示例 1：表达热图
```java
DefaultLinerColorMapper mapper = new DefaultLinerColorMapper();
mapper.setMin(-2.0);
mapper.setMax(2.0);
List<Color> colors = mapper.mapColors(expressionValues);
```

### 示例 2：自定义渐变
```java
GradientColorHolder gradient = new GradientColorHolder();
float[] positions = {0.0f, 0.5f, 1.0f};
Color[] colors = {Color.BLUE, Color.WHITE, Color.RED};
gradient.setColorScheme(positions, colors);
Color midColor = gradient.getColorFromPallet(0.5);
```

---

## 轴刻度计算示例

### 示例 1：标准刻度
```java
AxisTickCalculator calc = new AxisTickCalculator();
calc.setMinAndMaxPair(0.0, 100.0);
calc.setWorkingSpace(500); // 500 像素
calc.determineAxisTick();
// 返回最佳刻度位置
```

### 示例 2：增强刻度
```java
AxisTickCalculatorHeavy calc = new AxisTickCalculatorHeavy();
calc.setMinAndMaxPair(0.0, 100.0);
calc.setWorkingSpace(500);
calc.determineAxisTick();
// 返回美学优化的刻度位置
```

---

## 最佳实践

1. **使用 AxisTickCalculatorHeavy** 用于出版质量的绘图
2. **使用 DefaultLinerColorMapper** 用于标准热图
3. **使用 GradientColorHolder 创建自定义渐变** 用于专门的可视化
4. **使用 BaseGraphicNode** 作为树可视化节点的基类
5. **使用 tanglegram 工具** 用于比较系统发育学

---

*本文档涵盖可视化和图形包。其他部分见核心结构、文件 I/O、解析器和工具类。*
