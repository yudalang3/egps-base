# Axis Tick Calculation Note

[Docs Index](../README.md) | [中文](axis_tick_calculation_zh.md)

## Document Role

This note documents the current axis tick calculation code under `graphic.engine`. It is a focused design note and a more detailed companion to the visualization implementation guide.

## Problem It Solves

When drawing a 2D figure, one of the most important steps is choosing suitable tick marks for the current horizontal and vertical data ranges. These calculator classes are meant to compute visually reasonable ticks automatically, including both tick count and spacing.

## Current Implementation

### 1. `AxisTickCalculator`

- uses a fixed step hint of `64` pixels between ticks
- uses a compact Nice Numbers sequence based on `1`, `2`, `5`, and `10`
- switches between normal and scientific formatting for labels
- works well for ordinary chart widths, but may produce sparse ticks when the drawing space becomes small

### 2. `AxisTickCalculatorHeavy`

- evaluates multiple step hints: `32`, `48`, `64`, `80`, `96`
- uses an expanded mantissa set: `1.0`, `1.2`, `1.5`, `2.0`, `2.5`, `3.0`, `4.0`, `5.0`, `6.0`, `8.0`, `10.0`
- scores candidate layouts by tick count, spacing density, and boundary coverage
- aims to produce more practical layouts for constrained or presentation-sensitive cases

## When To Read This Note

Read this note when you need to:

- explain why a chart axis generates too few ticks
- compare the lightweight and heavy calculators
- tune tick density without changing the surrounding rendering pipeline
- interpret the dedicated axis-tick tests in `src/test/java/graphic/engine`

## Related Tests

- `src/test/java/graphic/engine/AxisTickCalculatorTest.java`
- `src/test/java/graphic/engine/AxisTickCalculatorTest1.java`
- `src/test/java/graphic/engine/AxisTickCalculatorTest2.java`
- `src/test/java/graphic/engine/AxisTickCalculatorTest3.java`
- `src/test/java/graphic/engine/AxisTickCalculatorTest4.java`
- `src/test/java/graphic/engine/AxisTickCalculatorTest5.java`

## Maintenance Notes

- Keep this note tied to current code rather than to the history of a particular discussion or assistant session.
- If the heavy calculator becomes the default path in callers, update this note and the visualization guide together.
- If new tick-selection heuristics are added, document the selection criteria before documenting the exact constants.
