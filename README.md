# SkyTweaks
A Hypixel Skyblock Quality of Life mod with features that you may or may not be able to find in other mods.

**SkyTweaks** is a mod created by Natia/Nat3zDev that implements QOL features for Hypixel Skyblock. Like the description says, "it's a mod that has features you may or may not find in other mods."

# Extensions API
**SkyTweaks** has an extensive Extensions API that allows you to access information in the mod and get a cool looking UI with it!

By having your mod implemented into the Extensions API, your mod will soon^tm have auto-updating if you link your github page!

## How to implement into your mod
First, install SkyTweaks and make sure it's the latest version. 
Then, you create a `libs` or `deps` folder in your gradle project and put the mod's jar in it.
Next, you make sure the following lines are in your `build.gradle` file:
```gradle
dependencies {
    compile fileTree(dir: 'deps', include: '*.jar')
}
// optional
shadowJar {
  exclude 'Skyblock.Secret.Mod.jar'
}
```

Then, you create a new class that extends to the `Extension` class.

Almost done! Once you have done that, in your `preInit(FMLPreInitializationEvent)` method, add the following lines:
```java
ExtensionList.addExtension(extension);
extension.updateConfigVariables();
```

Then, you add these lines in your `postInit(FMLPostInitializationEvent)` method:
```java
extension.saveConfig();
```

Congratulations! You have successfully added the SkyTweaks ExtensionsAPI into your mod!


# Features
- ExtensionsAPI
- Automatic Updates using the Vicious API framework. (made by me!)
- Stop Rendering Players in Hub - Stops rendering players in Hub. The only exception is NPCS.
- Item Pickup Logs - Creates a text file where it shows item drops/pickups throughout your session.
- Minion Analyzer - Analyzes minion contents and calculates the amount of coins in it.
- Readied Players - Adds a Ready Players HUD which shows the users in your party which are ready.
- Spirit/Bonzo Timers - Displays a timer which shows how long the cooldown for the spirit/bonzo mask is.
- Copy Fails/Deaths - Automatically copies dungeon fails AND dungeon deaths.
- Reparty Command (/rp) - Automatically reparties all party members.
- Voidgloom Seraph Assistance - Assists you in the fight with the Voidgloom Seraph.
# Commands
- /sm - Command to access config gui.
- /smhud - Command to change positions of hud elements.
- /rp - Command to start the reparty hook.

# Open Sourced
SkyTweaks is open source and because of this it uses code from open source programs! Below is a list of open source programs used to develop **SkyTweaks**!

[List of Open Source Programs](https://github.com/Nat3z/SkyTweaks/blob/main/OPEN_SOURCE_SOFTWARE.md)

This mod is protected by the [**MIT License**](https://github.com/Nat3z/SkyTweaks/blob/main/LICENSE). All rights reserved.
