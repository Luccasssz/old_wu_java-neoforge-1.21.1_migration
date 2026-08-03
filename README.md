# Old Wu Java — NeoForge 1.21.1

这是 [FunctionHookTJU/old_wu_java-template-26.2](https://github.com/FunctionHookTJU/old_wu_java-template-26.2) v1.2.0 的 NeoForge 移植版。

## 运行环境

- Minecraft 1.21.1
- NeoForge 21.1.1（当前最低通过候选）
- Java 21
- 模组 ID：`old_wu_java`

## 构建

在 Windows PowerShell 中指定 Java 21 后，可使用本项目 wrapper 或参考工程的 wrapper：

```powershell
$env:JAVA_HOME = 'D:\Luccasssz\Documents\MDK-1.21.1-NeoGradle-main\.gradle-local\jdks\eclipse_adoptium-21-amd64-windows.2'
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
& 'D:\Luccasssz\Documents\Dayun_Truck\gradlew.bat' -p 'D:\Luccasssz\Documents\Cat_Growl' --no-daemon clean build
```

成品位于 `build/libs/old_wu_java-1.2.0-neoforge-1.21.1.jar`。开发客户端和服务端的运行目录位于 `run/client`、`run/server`。

## 状态机

- 未配对的猫在 16 格内发现另一只未配对猫后进入 angry，靠近后进入 pairing。
- 配对至少 100 tick 后，每 tick 有 5% 概率进入 battle；战斗猫会靠近、缠斗、跳跃并互相造成 0.5 点无击退伤害。
- 战斗中任意一方生命值不高于 1 时，双方进入 recovery，获得再生 I 与缓慢 III，并显示绿色发光轮廓；双方恢复到最大生命值 80% 以上后继续 battle。
- 生命值不高于 1 的未配对猫单独 recovery，恢复后回到 common。
- 16 格内的矿车会打断其他状态并吸引猫；接触矿车或使用铲子右键会进入 flat，持续 300 tick。
- 马、驴、骡或猪在 0.5 格内会随机触发 dance 或 flat；dance 持续 100 tick，每 5 tick 随机切换状态模型并跳跃。
- 名字精确为 `maodie` 或 `耄耋` 的猫使用专用模型/贴图，停止本模组 AI 与声音；改名后恢复原版 AI。

## 验收边界

构建通过只表示源码、资源和打包成功；服务端启动、客户端启动、状态机玩法、模型切换、动画、轮廓、粒子和音效分别检查。无法由日志确认的视觉/听觉结果必须进行人工游戏内验收。

## 许可

上游项目使用 CC0；本移植保留该许可。详见 [LICENSE](LICENSE)。
