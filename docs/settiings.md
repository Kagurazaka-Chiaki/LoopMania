## VS Code 配置

创建 .vscode 文件夹

launch.json

```json
{
  "version": "0.2.0",
  "cwd": "${workspaceRoot}",
  "configurations": [
    // %PATH_TO_JAVA_FX_LIB% 是 JavaFX 的安装路径
    {
      "type": "java",
      "name": "Local (Launch) - LoopManiaApplication",
      "request": "launch",
      "vmArgs": "--module-path %PATH_TO_JAVA_FX_LIB% --add-modules javafx.controls,javafx.fxml -enableassertions",
      "mainClass": "unsw.LoopManiaApplication"
    }
    // 要在 VLab 上跑的保留以下配置, 但是现在 symlink_javafx 文件丢失
    // {
    //     "type": "java",
    //     "name": "CodeLens (Launch) - LoopManiaApplication",
    //     "request": "launch",
    //     "vmArgs": "--module-path ./lib/symlink_javafx --add-modules javafx.controls,javafx.fxml -enableassertions",
    //     "mainClass": "unsw.LoopManiaApplication"
    // }
  ]
}
```

现在是 gradle 的默认结构， 可以不用 settings.json

```json
{
  // 打开 import gradle
  "java.import.gradle.enabled": true,
  "java.import.gradle.wrapper.enabled": true,
  "java.project.sourcePaths": ["src"],
  // 导入本地路径 "path/to/javafx/lib" <- 全部导入
  "java.project.referencedLibraries": ["lib/**/*.jar"]
}
```

因为 .vscode 已经不在远程分支中， 倾向于用 VS Code 的要删去这两个文件, 它们的优先级高于 .vscode

```powershell
rm .classpath
rm .project
```

## IDEA 配置

正常配置即可运行

可能要添加 vmArgs 选项， 然后不要保存运行配置到项目中

## Eclipse 配置

应该没人用就不做说明了

## 其他情况

可以先跑一遍

```shell]
./gradlew build
./gradlew run
```

万一以上都不行， ./gradlew 是一定可以的。

Windows 下使用 ./gradlew 运行
