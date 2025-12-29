# Auto Axis Design Document

## 1. Problem Statement

### Observed Issue

When using `AxisTickCalculator` with small working spaces, the number of generated ticks is low:

| workingSpace | maxLengthOfRoot2Leaf | Result Tick Count | Max Tick | 覆盖问题 |
|--------------|---------------------|-------------------|----------|----------|
| 16           | 85.5                | 1                 | 0        | 0 < 85.5 |
| 122          | 85.5                | 2                 | 50       | 50 < 85.5 |

**问题1**: 16px 空间太小，1个刻度可以接受。

**问题2 (重要)**: 122px 空间只有2个刻度 [0, 50]，最大刻度50 < 数据最大值85.5，导致50-85.5区间没有刻度参考。

### Root Cause Analysis

The issue lies in the `DEFAULT_TICK_MARK_STEP_HINT = 64` constant and how grid step is calculated:

```java
double gridStepHint = length / tickSpace * DEFAULT_TICK_MARK_STEP_HINT;
```

**Case 1: workingSpace = 16**
```
tickSpace = 16 * 0.95 = 15.2 ≈ 15
gridStepHint = 85.5 / 15 * 64 = 364.8
mantissa = 3.648, exponent = 2
gridStep = 5 * 10^2 = 500  (since 3.648 > 3.5)
```
Result: Only 1 tick at 0 (500 > 85.5, no more ticks fit)

**Case 2: workingSpace = 122**
```
tickSpace = 122 * 0.95 = 115.9 ≈ 115
gridStepHint = 85.5 / 115 * 64 = 47.6
mantissa = 4.76, exponent = 1
gridStep = 5 * 10^1 = 50  (since 4.76 > 3.5)
```
Result: Only 2 ticks at 0 and 50

## 2. Algorithm Overview

### Nice Numbers Algorithm

The current implementation uses the **Nice Numbers** algorithm, which selects visually pleasing tick intervals from a predefined set: `{1, 2, 5, 10}` (scaled by powers of 10).

```
mantissa → gridStep
> 7.5  → 10
> 3.5  → 5
> 1.5  → 2
else   → 1
```

### Key Parameters

| Parameter | Value | Description |
|-----------|-------|-------------|
| `DEFAULT_TICK_MARK_STEP_HINT` | 64 | Target pixels between ticks |
| `workSpaceRatio` | 0.95 | Portion of working space used for ticks |

## 3. Problem Analysis

### Formula Breakdown

```
gridStepHint = dataRange / tickSpace * stepHint
expectedTickCount ≈ dataRange / gridStep
                  ≈ tickSpace / stepHint
```

When `tickSpace = 16`, expected ticks ≈ 16/64 = 0.25 → only ~1 tick
When `tickSpace = 122`, expected ticks ≈ 122/64 = 1.9 → only ~2 ticks

### The Core Issue

The algorithm assumes a **minimum working space** that provides enough room for multiple ticks at 64px spacing. When the space is small, the formula produces a grid step larger than the entire data range.

## 4. Comparison: Original vs Claude4 Version

### AxisTickCalculator (Original)
- Fixed step hint: 64
- Simple Nice Numbers: {1, 2, 5, 10}
- No adaptive behavior

### AxisTickCalculatorHeavy (Improved)
- Multiple step hints: {32, 48, 64, 80, 96}
- Extended Nice Numbers: {1.0, 1.2, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0, 6.0, 8.0, 10.0}
- Quality scoring system preferring 4-7 ticks
- Penalties for:
  - Tick count deviation from 5
  - Spacing < 30px (too dense)
  - Spacing > 120px (too sparse)
  - Uncovered boundaries

## 5. Proposed Improvements

### Option A: Dynamic Step Hint

Adjust `stepHint` based on working space:

```java
int getAdaptiveStepHint(int workingSpace) {
    if (workingSpace < 50) return 8;      // Very small spaces
    if (workingSpace < 100) return 16;    // Small spaces
    if (workingSpace < 200) return 32;    // Medium spaces
    return 64;                             // Standard
}
```

### Option B: Target Tick Count

Instead of pixel-based spacing, target a specific tick count:

```java
int targetTickCount = Math.max(3, Math.min(8, workingSpace / 30));
double gridStep = dataRange / (targetTickCount - 1);
// Round to nice number
```

### Option C: Minimum Tick Guarantee

Ensure at least 3 ticks regardless of space:

```java
int minTicks = 3;
double maxGridStep = dataRange / (minTicks - 1);
gridStep = Math.min(calculatedGridStep, maxGridStep);
```

## 6. Testing Strategy

### Test Cases

1. **Small Working Space Tests**
   - workingSpace = 16, range = [0, 85.5] -> 期望 1 个刻度（空间太小）
   - workingSpace = 50, range = [0, 100] -> 期望 1-2 个刻度
   - workingSpace = 100, range = [0, 85.5] -> 期望 2-3 个刻度

2. **Standard Working Space Tests**
   - workingSpace = 250, range = [0, 100] -> 期望 3-5 个刻度
   - workingSpace = 500, range = [0, 85.5] -> 期望 5-8 个刻度

3. **Edge Cases**
   - Zero range (min == max)
   - Very small range (0.001)
   - Very large range (1e6)
   - Negative values

4. **Tick Count Validation**
   - Verify tick positions are within bounds
   - Verify tick labels are properly formatted

## 7. Implementation Recommendations

1. **Short-term**: Use `AxisTickCalculatorHeavy` which already implements adaptive selection

2. **Medium-term**: Add minimum tick guarantee to ensure small spaces still get useful ticks

3. **Long-term**: Consider using established charting libraries' axis calculation (e.g., XChart's actual implementation)

## 8. Reference

- Paul Heckbert, "Nice Numbers for Graph Labels", Graphics Gems I, 1990
- D3.js tickIncrement implementation
- XChart AxisTickCalculator source

## Appendix: Debug Calculation

For your specific case (workingSpace=122, range=[0, 85.5]):

```
With adaptive stepHint = 20:
  tickSpace = 115
  gridStepHint = 85.5 / 115 * 20 = 14.87
  mantissa = 1.487, exponent = 1
  gridStep = 2 * 10 = 20

Expected ticks: 0, 20, 40, 60, 80 → 5 ticks ✓
```
