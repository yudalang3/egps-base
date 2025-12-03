# eGPS-Base 项目概览

## 项目信息

**项目名称:** eGPS-Base  
**Group ID:** ydl.lab.utils  
**Artifact ID:** egps-base  
**版本:** 1.0-SNAPSHOT  
**构建工具:** Maven  
**Java 版本:** 25  
**编码:** UTF-8

## 项目描述

eGPS-Base 是一个综合性的生物信息学工具库,专为支持进化基因组学和系统发育分析而设计。它提供了模块化架构,包含用于处理生物序列数据、系统发育树、遗传密码表以及计算生物学研究中常用的各种数据格式的专业工具。

## 架构概览

项目采用模块化架构,每个包都服务于生物信息学中的特定领域。所有模块都实现了 `IModuleSignature` 接口,该接口提供了每个模块用途和 GUI 集成所需的元数据。

### 核心设计模式

每个模块都包含一个实现 `IModuleSignature` 的 `ZzzModuleSignature` 类,提供:
- **getShortDescription()**: 模块功能描述
- **getTabName()**: GUI 标签页显示名称

## 模块结构

---

### **顶层签名模块** (`top.signature`)
**用途:** 模块签名框架  
**功能:** 所有模块的 `IModuleSignature` 接口、GUI 集成支持

所有实现了这个接口的代码集合都是一个`模块`，请见`understand_packages1_core_structure*`获取所有的类的功能说明文档。
---

## 技术栈

### 核心依赖
- **Java 25**: 最新 Java 版本,支持现代特性
- **Maven**: 构建和依赖管理

### 主要库

#### 数据处理
- **FastJSON 1.2.83**: JSON 解析(Swing 兼容版本)
- **Apache Commons Lang3 3.18.0**: 常用工具
- **Apache Commons CLI 1.9.0**: 命令行解析
- **Apache Commons IO 2.18.0**: 文件 I/O 工具
- **Apache Commons Compress 1.27.1**: 压缩支持
- **XZ 1.9**: XZ 压缩
- **Guava 33.4.8-jre**: Google 核心库

#### GUI 框架
- **FlatLaf 3.6**: 现代 Swing 外观
- **SwingX 1.6.1**: 扩展 Swing 组件
- **JIDE-OSS 3.7.15**: 专业 Swing 组件
- **MigLayout 4.2**: 布局管理器
- **Timing Framework 1.0**: 动画支持

#### 可视化
- **XChart 3.8.8**: 图表库(排除 animated-gif-lib)
- **JSVG 1.2.0**: SVG 渲染

#### 生物信息学
- **HTSJDK 4.3.0**: 高通量测序数据(排除 Nashorn)
- **DOM4J 2.1.4**: XML 解析

#### 文档处理
- **iTextPDF 5.1.3**: PDF 生成(最后一个独立版本)
- **Apache POI 5.4.1**: Excel/Office 文件处理
  - poi
  - poi-ooxml

#### 日志
- **Log4j2 2.24.3**: 日志框架
  - log4j-core
  - log4j-api
  - log4j-slf4j2-impl

#### 其他
- **javax.mail 1.6.2**: 电子邮件支持
- **Reflections 0.10.2**: 运行时反射
- **Java-sizeof 0.0.5**: 内存使用测量

#### 测试
- **JUnit Jupiter 5.11.1**: 单元测试框架

### 仓库配置
- Maven 中央仓库
- 阿里云 Maven 镜像(中国加速)
- JOSM 仓库(用于特定依赖)

---

## 测试结构

项目包含几个测试类别:

1. **evoltree.txtdisplay**: 文本树显示测试
   - `TextTreeDescriberTest`

2. **module.ihave**: 模块发现
   - `SeeMyModule`

3. **os.test**: 操作系统和内存测试
   - `MemoryTest100G`: 大内存测试

4. **script.run.once**: 一次性分析脚本
   - 生物信息学工作流脚本
   - BLAST 结果处理
   - 序列提取
   - FASTA 预处理

5. **tab.index**: Tabix 测试
   - `TestTbx`

---

## 构建配置

### 资源处理
- 包含 `src/main/java` 中的所有非 Java 文件
- 从资源中排除 `.java` 源文件
- 全程使用 UTF-8 编码

### Maven 属性
```
maven.compiler.source: 25
maven.compiler.target: 25
project.build.sourceEncoding: UTF-8
```

---

## 关键设计原则

### 1. **模块化架构**
每个功能区域都被隔离到自己的包中,具有清晰的职责。

### 2. **基于接口的设计**
核心抽象使用接口(`IModuleSignature`、`IGeneticCode`、`EvolNode`)以允许多种实现。

### 3. **高效 I/O**
生物信息学文件处理使用大缓冲区(10-100MB),支持压缩格式。

### 4. **GUI 集成**
内置对基于 Swing 的界面的支持,具有用于基于选项卡导航的模块签名。

### 5. **性能监控**
集成的内存和计时工具用于性能分析。

### 6. **CLI 支持**
具有标准化帮助生成的全面命令行工具。

---

## 项目用途

eGPS-Base 作为以下领域的基础库:
- **进化基因组学研究**: 系统发育树分析和操作
- **序列分析**: FASTA/BLAST/Pfam 处理
- **数据可视化**: 科学绘图和树渲染
- **生物信息学流程**: 工作流自动化和数据处理
- **GUI 应用程序**: 基于 Swing 的生物信息学工具

---

## 开发注意事项

### 代码组织
- 每个模块都有一个 `ZzzModuleSignature` 标记类
- 跨域的一致包结构
- I/O、算法和可视化的分离

### 依赖策略
- 使用特定库版本以保持稳定性
- 排除冲突的依赖项(例如 Nashorn、animated-gif-lib)
- 保持与 Swing 组件的向后兼容性
- 注释指示版本锁定原因

### 文件处理
- 支持压缩和未压缩文件
- 自动格式检测
- 大文件优化(100MB+ 缓冲区)
- 流式处理以提高内存效率

---

## 未来可扩展性

模块化设计允许通过以下方式轻松扩展:
1. 为新模块实现 `IModuleSignature`
2. 向现有包添加新解析器
3. 使用 `EvolNode` 扩展树数据结构
4. 添加新的遗传密码表
5. 实现新的可视化

---

## 作者信息
- **作者**: yudalang
- **开发团队**: eGPS 开发团队
- **联系方式**: ydl.lab.utils

---

## 版本历史
- **当前版本**: 1.0-SNAPSHOT
- **状态**: 积极开发中
- **最后更新**: 2025 年

---

*本文档全面概述了 eGPS-Base 项目的结构、架构和功能。*