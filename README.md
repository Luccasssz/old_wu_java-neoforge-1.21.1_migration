# Old Wu Java — NeoForge 1.21.1

这是 [FunctionHookTJU/old_wu_java-template-26.2](https://github.com/FunctionHookTJU/old_wu_java-template-26.2) v1.2.0 的 NeoForge 移植版。

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
- 马、驴、骡或猪在 0.5 格内会随机触发 dance 或 flat；dance 持续 100 tick，每 5 tick 随机切换状态模型并跳跃。
- 名字精确为 `maodie` 或 `耄耋` 的猫使用专用模型/贴图，停止本模组 AI 与声音；改名后恢复原版 AI。

## 许可

上游项目使用 CC0；本移植保留该许可。详见 [LICENSE](LICENSE)。
