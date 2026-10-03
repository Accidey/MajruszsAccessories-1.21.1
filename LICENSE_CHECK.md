# 移植版发布许可核查报告

> 核查对象：**Majrusz's Accessories**（原版）
> 原项目地址：https://github.com/Majrusz/MajruszsAccessories
> 原作者：**Majrusz**
> 核查时间：2026-10-03

---

## 结论

**可以发布移植版本，但必须严格履行 MIT 许可证规定的义务。**

原作者 Majrusz 已在 **Modrinth 官方发布页** 明确标注该模组采用 **MIT License** 许可。MIT 许可证明确允许任何人复制、修改、合并、出版、分发、再许可和/或出售软件的副本，因此**移植（修改代码并适配新版本）后发布是许可证明确允许的行为**。

### 必须履行的义务（MIT 许可证要求）

发布时必须满足以下条件：

1. **保留原始版权声明** —— 在发布页面/仓库中注明 `Copyright (c) Majrusz`。
2. **附带 MIT 许可证全文** —— 随模组分发完整的 MIT 许可证文本（或提供许可证链接）。
3. **不得删改原作者署名** —— 在模组元数据（如 `neoforge.mods.toml` 的 authors 字段）和发布页面中保留原作者 Majrusz 的署名。

### 建议做法（行业惯例，非强制）

- 在模组介绍中明确注明"本模组为 Majrusz's Accessories 的移植版本，原模组由 Majrusz 创作"。
- 在发布页面附上原项目链接（GitHub / Modrinth）。
- 尽量在原作者发布的平台上同步告知（可选，非强制）。

---

## 核查依据

### 1. Modrinth 官方发布页（最权威）

- 页面地址：https://modrinth.com/mod/majruszs-accessories
- 许可证标注：**Licensed MIT**
- 作者：**Majrusz**（Owner）
- 支持平台：Fabric / Forge / NeoForge / Quilt
- 支持版本：1.18.2、1.19.2–1.19.4、1.20–1.20.2

### 2. 原项目 gradle.properties

- 原项目（1.20.X 分支）`gradle.properties` 中声明：
  - `mod_license=MIT License`
  - `mod_authors=Majrusz`

### 3. 移植版工作区信息

- 本移植版 `gradle.properties` 中同样声明：
  - `mod_license=MIT`
  - `mod_authors=Majrusz, xulai`（已保留原作者署名）

### 4. GitHub 仓库说明

- GitHub API 中仓库 `license` 字段为 `null`，且仓库根目录**没有 LICENSE 文件**。
- 但结合 Modrinth 页面明确标注 **MIT** 及项目配置中声明的 `MIT License`，可以认定作者以 MIT 许可证发布该模组。Modrinth 是作者官方发布渠道，其许可证标注具有直接效力。

---

## MIT 许可证核心条款摘要

MIT 许可证（SPDX: MIT）的核心条款如下：

> Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
>
> The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
>
> THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

即：任何人可以自由使用、复制、修改、合并、出版、分发、再许可或出售本软件，但**必须**在所有副本或实质性部分中**保留上述版权声明和许可声明**；软件按"原样"提供，作者不对任何损害承担责任。

---

## 发布检查清单

发布移植版前，请确认：

- [x] `neoforge.mods.toml` 的 authors 字段包含原作者（当前为 `Majrusz, xulai`）
- [x] `gradle.properties` 的 mod_license 字段为 MIT
- [x] 模组介绍（MOD_INTRO.md）中包含对原作者的致谢
- [ ] 发布页面附上原项目链接（https://github.com/Majrusz/MajruszsAccessories 或 Modrinth 页面）
- [ ] 发布包内附带 MIT 许可证文本（建议将 LICENSE 文件加入模组 JAR 或发布仓库）