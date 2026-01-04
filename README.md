# TNTUtils

A Minecraft mod that provides some config options and tags for controlling explosions and the damage dealt by them.

Downloads and more information about the available features can be found on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/tntutils) and [Modrinth](https://modrinth.com/mod/tntutils).

## Building and setting up a dev environment
This mod uses a very simple multiloader setup (Fabric and NeoForge), inspired by the MultiLoader Template and the setup of other multiloader mods such as Patchouli.

To build the mod, run `./gradlew build` (on Windows, `gradlew.bat build`) from the root folder. This will build the mod jars in {fabric|neoforge}/build/libs.

To set up a dev environment:
1. Import the root folder as Gradle project into your IDE of choice (should work with IDEA, VS Code and Eclipse)
2. Depending on your IDE, you might need to run the `fabric:genEclipseRuns` or `fabric:vscode` Gradle task to generate Fabric run configs
3. Optionally, you can run the `fabric:genSources` Gradle task to decompile Minecraft in the Fabric subproject as well (this is not needed for developing but might be useful for debugging)
