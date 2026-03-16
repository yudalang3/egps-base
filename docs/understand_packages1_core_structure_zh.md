# eGPS-Base 分册 1：核心结构

[文档索引](README_zh.md) | [English](understand_packages1_core_structure.md)

## 文档定位

本分册面向阅读源码的维护者，用于建立对仓库结构层的稳定认识。凡是涉及模块元数据、通用树对象、系统发育树编解码，或遗传密码实现的工作，都应优先从这里进入。

## 包级导读

### `top.signature`

用途：

- 定义贯穿全仓库的轻量级模块签名约定

当前源码文件：

- `IModuleSignature`
- `Version`
- `ZzzModuleSignature`
- `Readme.txt`

阅读要点：

- 如果你想理解为什么很多包都会自带一个 `ZzzModuleSignature` 类，这里就是起点。
- `SeeModulesWeHave` 之类的发现工具依赖的正是这一套约定。

### `evoltree.struct`

用途：

- 定义更高层树代码所依赖的通用树模型

当前源码文件：

- `EvolNode`
- `ArrayBasedNode`
- `LinkedBasedNode`
- `TreeCoder`
- `TreeDecoder`
- `ZzzModuleSignature`

阅读要点：

- `ArrayBasedNode` 与 `LinkedBasedNode` 是理解节点存储方式的两个核心入口。
- `TreeCoder` 与 `TreeDecoder` 则标出了“树对象”和“具体格式编解码器”之间的边界。

### `evoltree.struct.io`

用途：

- 抽离并复用节点级别的解析与序列化逻辑

当前源码文件：

- `AbstractNodeCoderDecoder`
- `BasicInternalCoderDecoder`
- `BasicLeafCoderDecoder`
- `PrimaryNodeTreeDecoder`

阅读要点：

- 当你想理解“节点如何被解析，而不直接绑定某一种树格式”时，应先读这个包。
- 它是通用树模型与上层具体编解码器之间的衔接层。

### `evoltree.struct.util`

用途：

- 在通用树模型之上提供操作型辅助能力

当前源码文件：

- `EvolNodeUtil`
- `EvolTreeComparator`
- `EvolTreeOperator`
- `PhyloTreeGenerator`

阅读要点：

- 如果你的任务是检查、转换、比较或生成树，这个包通常比底层接口更直接。

### `evoltree.phylogeny`

用途：

- 实现面向 Newick 的系统发育树读写逻辑

当前源码文件：

- `DefaultPhyNode`
- `PhyloTreeEncoderDecoder`
- `NWKInternalCoderDecoder`
- `NWKLeafCoderDecoder`
- `ZzzModuleSignature`

阅读要点：

- 当树问题已经不再是泛型结构问题，而是明确落在系统发育树或 Newick 文本上时，应转入这个包。
- 该区域最直接的回归测试参考是 `src/test/java/evoltree/phylogeny/PhyloTreeEncoderDecoderTest.java`。

### `geneticcodes`

用途：

- 提供翻译相关抽象与共享的遗传密码行为

当前源码文件：

- `IGeneticCode`
- `GeneticCode`
- `AminoAcid`
- `ZzzModuleSignature`

阅读要点：

- 这一包与树结构层相邻，但并不属于树编解码栈本身；它更像生物学基础设施的一部分。

### `geneticcodes.codeTables`

用途：

- 保存仓库当前真正提供的具体遗传密码表实现

当前源码文件：

- `TheStandardCode`
- `TheVertebrateMitochondrialCode`
- `TheInvertebrateMitochondrialCode`
- `TheYeastMitochondrialCode`
- `TheBacterialPlantPlastidCode`
- `TheCiliateDHexamitaNuclearCode`
- `TheEuplotidNuclearCode`
- `TheEFMitochondrialCode`
- `TheMPCMCMSCode`

阅读要点：

- 这一部分必须始终以仓库当前真实存在的源码文件为准，不应继续沿用外部复制来的旧密码表清单。

## 阅读建议

- 想理解模块约定时，从 `top.signature` 开始。
- 想追踪树对象的表示方式与序列化边界时，从 `evoltree.struct` 与 `evoltree.struct.io` 开始。
- 想定位 Newick 相关问题时，直接进入 `evoltree.phylogeny`。
- 只有当任务与翻译或密码表实现直接相关时，才优先进入 `geneticcodes`。

## 相关文档

- 当你需要同时理解结构层与绘制层时，请结合 [understand_packages5_visualization_zh.md](understand_packages5_visualization_zh.md) 阅读。
- 当 taxonomy 或 REST 数据最终要落到树对象上时，请结合 [understand_packages3_parsers_zh.md](understand_packages3_parsers_zh.md) 阅读。
