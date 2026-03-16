# 轴刻度计算说明

[文档索引](../README_zh.md) | [English](axis_tick_calculation.md)

## 文档定位

本文档专门说明 `graphic.engine` 中当前与轴刻度计算直接相关的实现。它是一份专题设计说明，也是可视化分册的详细补充。

## 解决的问题

在二维图形绘制过程中，最重要的一步是计算当前横轴和纵轴数据最合适的刻度线。这个类的主要目的就是实现如何自动计算美观的刻度线，包括数量和间距。

## 当前实现

### 1. `AxisTickCalculator`

- 使用固定的 `64` 像素步长提示
- 采用较紧凑的 Nice Numbers 序列，核心取值为 `1`、`2`、`5`、`10`
- 会在普通数字格式与科学计数法之间切换标签格式
- 在常规图宽下通常足够，但在可用绘图空间较小时可能出现刻度偏稀的情况

### 2. `AxisTickCalculatorHeavy`

- 会评估多个步长提示：`32`、`48`、`64`、`80`、`96`
- 使用更丰富的尾数集合：`1.0`、`1.2`、`1.5`、`2.0`、`2.5`、`3.0`、`4.0`、`5.0`、`6.0`、`8.0`、`10.0`
- 会对候选布局按刻度数量、间距密度与边界覆盖进行评分
- 更适合空间受限或展示要求较高的场景

## 什么时候读这份说明

在以下场景中，应优先阅读本文：

- 解释为什么某个坐标轴只生成了很少的刻度
- 对比轻量版本与 heavy 版本的设计差异
- 在不改动整体绘图流程的前提下调整刻度密度
- 理解 `src/test/java/graphic/engine` 下那组轴刻度专项测试的意图

## 相关测试

- `src/test/java/graphic/engine/AxisTickCalculatorTest.java`
- `src/test/java/graphic/engine/AxisTickCalculatorTest1.java`
- `src/test/java/graphic/engine/AxisTickCalculatorTest2.java`
- `src/test/java/graphic/engine/AxisTickCalculatorTest3.java`
- `src/test/java/graphic/engine/AxisTickCalculatorTest4.java`
- `src/test/java/graphic/engine/AxisTickCalculatorTest5.java`

## 维护约定

- 这份说明应紧贴当前代码，而不是保留某次讨论或某个助手会话的历史痕迹。
- 如果后续调用方默认切换到 heavy 版本，应同步更新本文与可视化分册。
- 如果新增刻度选择策略，先写清选择原则，再写常量细节。
