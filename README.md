# Old Wu Java — NeoForge 1.21.1

这是 [FunctionHookTJU/old_wu_java-template-26.2](https://github.com/FunctionHookTJU/old_wu_java-template-26.2) 1.4.1 标签的 NeoForge 移植版。

## 运行环境

- Minecraft 1.21.1
- NeoForge 21.1.1（当前最低通过候选）
- Java 21
- 模组 ID：`old_wu_java`

## 状态机

- 未配对的猫在 16 格内发现另一只未配对猫后进入 angry，靠近后进入 pairing。
- 配对至少 100 tick 后，每 tick 有 5% 概率进入 battle；战斗猫会靠近、缠斗、跳跃并互相造成 0.5 点无击退伤害。
- 战斗中任意一方生命值不高于 1 时，双方进入 recovery，获得再生 I 与缓慢 III，并显示绿色发光轮廓；双方恢复到最大生命值 80% 以上后继续 battle。
- 生命值不高于 1 的未配对猫单独 recovery，恢复后回到 common。
- 16 格内的矿车会打断其他状态并吸引猫；接触矿车或使用铲子右键会进入 flat，持续 300 tick。
- 马、驴、骡、骆驼或猪在 0.5 格内会随机触发 dance 或 flat；dance 持续 100 tick，每 5 tick 随机切换状态模型并跳跃。
- 名字精确为 `maodie` 或 `耄耋` 的猫进入耄耋 Boss 行为：325 点生命、1.5 倍尺寸、目标索敌、近战/纸卷攻击、狂暴阶段、粒子圆环、haqi 贴图与 Boss 血条；之后改成其他名字只移除 Boss 血条，仍保留 325 最大生命与 1.5 缩放基值。
- 纸卷可在创造标签中获取并蓄力投掷；命中会造成伤害并产生小型生物爆炸，击败耄耋会掉落纸卷。
- 水瓶溅射附近的猫会进入舔毛状态并获得 600 tick 的战斗和平期；使用望远镜分两次观察耄耋、或击败耄耋可完成对应进度。

## 许可

上游项目使用 CC0；本移植保留该许可。详见 [LICENSE](LICENSE)。
