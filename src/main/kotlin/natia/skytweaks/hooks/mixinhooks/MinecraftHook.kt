package natia.skytweaks.hooks.mixinhooks

import natia.skytweaks.SecretUtils
import natia.skytweaks.SkyTweaks
import natia.skytweaks.utils.FileUtils
import natia.skytweaks.utils.FrameMaker
import natia.skytweaks.utils.ModAssistantHook
import natia.skytweaks.utils.WebUtils.fetch
import net.minecraft.client.main.GameConfiguration
import net.minecraftforge.fml.common.FMLCommonHandler
import org.lwjgl.opengl.Display
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.awt.Dimension
import java.awt.event.ActionListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.regex.Pattern
import javax.swing.WindowConstants

object MinecraftHook {
    private var updatePreperations = arrayOfNulls<Any>(5)

    fun shutdownTask() {
        SkyTweaks.LOGGER.info("Saving SkyTweaks Config Before Shutdown...")
        SkyTweaks.configHandler?.saveConfig()
    }

    fun checkUpdates(gameConfig: GameConfiguration, ci: CallbackInfo) {
        val viciousFolder = File(gameConfig.folderInfo.mcDataDir.absolutePath + "\\vicious\\")
        if (!viciousFolder.exists()) {
            viciousFolder.mkdir()
        }
        val viciousUpdateCycle = File(gameConfig.folderInfo.mcDataDir.absolutePath + "\\vicious\\updater.jar")
        /* if not downloaded, download vicious updater */
        fetch("https://api.github.com/repos/Nat3z/ModAssistant/releases/latest") { res ->
            val downloadURL = res.asJson().get("assets").getAsJsonArray().get(0).getAsJsonObject().get("browser_download_url").getAsString()
            if (!viciousUpdateCycle.exists()) {
                SkyTweaks.LOGGER.info("Mod Assistant is not installed. Now installing Mod Assistant.")
                try {
                    downloader(viciousFolder, downloadURL, "updater.jar")
                } catch (e: IOException) {
                    SkyTweaks.LOGGER.error("Failed to download Vicious updater.jar")
                    e.printStackTrace()
                }

            }
        }
        var modsFolder = File(gameConfig.folderInfo.mcDataDir.absolutePath + "\\mods\\")
        val subMods = File(gameConfig.folderInfo.mcDataDir.absolutePath + "\\mods\\1.8.9\\")

        if (subMods.exists()) {
            modsFolder = subMods
        }
        //ModAssistantHook.openLauncher(modsFolder);
        /* check for updates & auto update */
        val finalModsFolder = modsFolder
        fetch("https://api.github.com/repos/Nat3z/SkyTweaks/releases") { res ->
            if (SkyTweaks.VERSION != res.asJsonArray().get(0).getAsJsonObject().get("tag_name").getAsString()) {
                /* update that mod! */
                SkyTweaks.LOGGER.info("Applying update to SkyTweaks...")
                fetch("https://api.github.com/repos/Nat3z/SkyTweaks/releases") { res1 ->
                    val downloadURL = res1.asJsonArray().get(0).getAsJsonObject().get("assets").getAsJsonArray().get(0).getAsJsonObject().get("browser_download_url").getAsString()
                    val body = res1.asJsonArray().get(0).getAsJsonObject().get("body").getAsString()
                    var hashID: String = ""
                    if (body.contains("**SHA-256 Hash:** `")) {
                        SkyTweaks.LOGGER.info(body.split("**SHA-256 Hash:** `"))
                        hashID =
                            body.split("Hash:** `")[1].split("`")[0]
                    }
                    SkyTweaks.LOGGER.info(hashID)

                    SkyTweaks.LOGGER.info("Prepared update for SkyTweaks.")
                    var updateAs = ""
                    /* for glass mod implementation */
                    for (file in finalModsFolder.listFiles()!!) {
                        if (file.name.startsWith("Skyblock.Secret.Mod")) {
                            updateAs = "Skyblock.Secret.Mod.jar"
                            break
                        } else if (file.name.startsWith("SkyTweaks")) {
                            updateAs = "SkyTweaks.jar"
                            break
                        }

                    }
                    SecretUtils.preparedupate = true
                    updatePreperations = arrayOf<Any?>("https://api.github.com/repos/Nat3z/SkyTweaks/releases", downloadURL, finalModsFolder, updateAs, updateAs, hashID)
                }
            } else if (SkyTweaks.VERSION != res.asJsonArray().get(1).getAsJsonObject().get("tag_name").getAsString() && res.asJsonArray().get(1).getAsJsonObject().get("prerelease").asBoolean) {
                /* update that mod! */
                SkyTweaks.LOGGER.info("Applying update to SkyTweaks...")
                fetch("https://api.github.com/repos/Nat3z/SkyTweaks/releases") { res1 ->
                    val downloadURL = res1.asJsonArray().get(1).getAsJsonObject().get("assets").getAsJsonArray().get(0).getAsJsonObject().get("browser_download_url").getAsString()
                    SkyTweaks.LOGGER.info("Prepared update for SkyTweaks.")
                    var updateAs = ""
                    /* for glass mod implementation */
                    for (file in finalModsFolder.listFiles()!!) {
                        if (file.name.startsWith("Skyblock.Secret.Mod")) {
                            updateAs = "Skyblock.Secret.Mod.jar"
                            break
                        } else if (file.name.startsWith("SkyTweaks")) {
                            updateAs = "SkyTweaks.jar"
                            break
                        }

                    }
                    SecretUtils.preparedupate = true
                    updatePreperations = arrayOf<Any?>("https://api.github.com/repos/Nat3z/SkyTweaks/releases", downloadURL, finalModsFolder, updateAs, updateAs)
                }
            } else {
                val versionType = File(SecretUtils.generalFolder.getAbsolutePath() + "\\versionType.txt")
                if (SkyTweaks.IS_UNSTABLE) {
                    SkyTweaks.LOGGER.info("THIS USER IS CURRENTLY USING AN UNSTABLE RELEASE OF SkyTweaks. IF LOGS WERE SENT AND THIS WAS RECEIVED, PLEASE DO NOT GIVE ANY SUPPORT.")
                }
                if (versionType.exists()) {
                    FileUtils.writeToFile(versionType, "" + SkyTweaks.IS_UNSTABLE)
                } else if (SkyTweaks.IS_UNSTABLE) {
                    /* version is unstable ui ui */
                    val willAllow = AtomicBoolean(true)
                    val maker = FrameMaker("Version detected as UNSTABLE.", Dimension(350, 150), WindowConstants.DO_NOTHING_ON_CLOSE, false)
                    val suc = AtomicBoolean(false)

                    val frame = maker.pack()
                    maker.addText("You are currently using an UNSTABLE release.", 10, 10, 11, false)
                    maker.addText("<html>" +
                            "Since this is your first release, we highly<br/>recommend you download the latest stable release.<br/>" +
                            "Do you acknowledge that you will not receive any<br/>support and me (NatiaDev) is not<br/>" +
                            "liable for any lost items due to<br/>crashes related to the mod?</html>", 10, 25, 11, false)

                    val allowUse = maker.addButton("Yes", 180, 70, 60, ActionListener{ e ->
                        suc.set(true)
                        willAllow.set(true)
                        frame.dispose()
                    })
                    val disallowUse = maker.addButton("No", 270, 70, 60, ActionListener { e ->
                        suc.set(true)
                        willAllow.set(false)
                        frame.dispose()
                    })
                    maker.override()
                    /* keep it from continuing */
                    while (!suc.get()) {
                        try {
                            TimeUnit.SECONDS.sleep(1)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }

                    }

                    if (!willAllow.get())
                        FMLCommonHandler.instance().exitJava(0, false)
                    else {
                        try {
                            versionType.createNewFile()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                        FileUtils.writeToFile(versionType, "" + SkyTweaks.IS_UNSTABLE)
                    }
                }/* if downloaded is first ever download && version is unstable... */
                SkyTweaks.LOGGER.info("User is on the latest version of SkyTweaks.")
            }
        }

        if (SecretUtils.preparedupate) {
            Display.destroy()
            SkyTweaks.LOGGER.info("Starting SkyTweaks update...")
            ModAssistantHook.open(updatePreperations[0] as String, updatePreperations[1] as String,
                updatePreperations[2] as File, updatePreperations[3] as String, updatePreperations[4] as String, updatePreperations[5] as String)
        }
    }

    private fun isRedirected(header: Map<String, List<String>>): Boolean {
        return false
    }

    @Throws(IOException::class)
    private fun downloader(modsFolder: File, link: String, fileName: String) {
        var link = link
        var url = URL(link)
        var http = url.openConnection() as HttpURLConnection
        var header = http.headerFields
        while (isRedirected(header)) {
            link = header["Location"]?.get(0)!!
            url = URL(link)
            http = url.openConnection() as HttpURLConnection
            header = http.headerFields
        }
        val input = http.inputStream
        val buffer = ByteArray(4096)
        var n = -1
        if (modsFolder.isFile) {
            val output = FileOutputStream(modsFolder)
            while ({ n = input.read(buffer); n }() != -1) {
                output.write(buffer, 0, n)
            }
            output.close()
        } else {
            val output = FileOutputStream(File(modsFolder.absolutePath + "\\" + fileName))
            while ({ n = input.read(buffer); n }() != -1) {
                output.write(buffer, 0, n)
            }
            output.close()
        }
    }
}
