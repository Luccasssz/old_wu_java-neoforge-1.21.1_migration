# Release 发布清单

后续 Agent 发布新版本时，必须把编译完成的模组 JAR 上传到对应的 GitHub Release，不能只推送源码、提交或 Git tag。

## 发布要求

1. 从项目根目录执行正式构建：`./gradlew clean build`（Windows 使用 `gradlew.bat clean build`）。
2. 确认 `build/libs/` 中生成了最终 JAR，并检查文件名、版本号和文件大小。
3. 将该 JAR 作为 Release asset 上传到对应的 GitHub Release。
4. 如果 Release 已经创建但缺少 JAR，直接补传 asset，不要仅重新推送 tag。
5. 上传后打开 Release 页面确认 JAR 可以下载，并在交付说明中写明资产文件名。

## 当前项目的产物

- 模组版本来自 `gradle.properties` 的 `mod_version`。
- 当前构建产物命名为 `old_wu_java-<版本号>.jar`。
- `build/` 是本地构建输出目录，已被 `.gitignore` 忽略；它不等于 GitHub Release asset，必须单独上传。
