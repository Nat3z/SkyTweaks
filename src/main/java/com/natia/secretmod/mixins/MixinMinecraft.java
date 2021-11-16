package com.natia.secretmod.mixins;

import com.natia.secretmod.SkyTweaks;
import com.natia.secretmod.SecretUtils;
import com.natia.secretmod.utils.FileUtils;
import com.natia.secretmod.utils.FrameMaker;
import com.natia.secretmod.utils.ModAssistantHook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfiguration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.natia.secretmod.utils.WebUtils.fetch;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    private Method replaceMethod;
    private Object instance;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(GameConfiguration gameConfig, CallbackInfo ci) {
        File viciousFolder = new File(gameConfig.folderInfo.mcDataDir.getAbsolutePath() + "\\vicious\\");
        if (!viciousFolder.exists()) {
            viciousFolder.mkdir();
        }
        File viciousUpdateCycle = new File(gameConfig.folderInfo.mcDataDir.getAbsolutePath() + "\\vicious\\updater.jar");
        /* if not downloaded, download vicious updater */
        fetch("https://api.github.com/repos/Nat3z/ModAssistant/releases/latest", res -> {
            String downloadURL = res.asJson().get("assets").getAsJsonArray().get(0).getAsJsonObject().get("browser_download_url").getAsString();
            if (!viciousUpdateCycle.exists()) {
                System.out.println("Mod Assistant is not installed. Now installing Mod Assistant.");
                try {
                    downloader(viciousFolder, downloadURL, "updater.jar");
                } catch (IOException e) {
                    System.out.println("Failed to download Vicious updater.jar");
                    e.printStackTrace();
                }
            }
        });
        File modsFolder = new File(gameConfig.folderInfo.mcDataDir.getAbsolutePath() + "\\mods\\");
        File subMods = new File(gameConfig.folderInfo.mcDataDir.getAbsolutePath() + "\\mods\\1.8.9\\");

        if (subMods.exists()) {
            modsFolder = subMods;
        }
        ModAssistantHook.openLauncher(modsFolder);
        /* check for updates & auto update */
        File finalModsFolder = modsFolder;
        fetch("https://api.github.com/repos/Nat3z/SkyTweaks/releases/latest", res -> {
            if (!SkyTweaks.VERSION.equals(res.asJson().get("tag_name").getAsString())) {
                /* update that mod! */
                System.out.println("Applying update to SkyTweaks...");
                fetch("https://api.github.com/repos/Nat3z/SkyTweaks/releases/latest", res1 -> {
                    String downloadURL = res1.asJson().get("assets").getAsJsonArray().get(0).getAsJsonObject().get("browser_download_url").getAsString();
                    System.out.println("Prepared update for SkyTweaks.");
                    String updateAs = "";
                    /* for glass mod implementation */
                    for (File file : finalModsFolder.listFiles()) {
                        if (file.getName().startsWith("Skyblock.Secret.Mod")) {
                            updateAs = "Skyblock.Secret.Mod.jar";
                            break;
                        } else if (file.getName().startsWith("SkyTweaks")) {
                            updateAs = "SkyTweaks.jar";
                            break;
                        }

                    }

                    ModAssistantHook.open("https://api.github.com/repos/Nat3z/SkyTweaks/releases/latest", downloadURL, finalModsFolder, updateAs, updateAs);
                });
            } else {
                File versionType = new File(SecretUtils.getGeneralFolder().getAbsolutePath() + "\\versionType.txt");
                if (SkyTweaks.IS_UNSTABLE) {
                    System.out.println("THIS USER IS CURRENTLY USING AN UNSTABLE RELEASE OF SkyTweaks. IF LOGS WERE SENT AND THIS WAS RECEIVED, PLEASE DO NOT GIVE ANY SUPPORT.");
                }
                if (versionType.exists()) {
                    if (Boolean.getBoolean(FileUtils.readFile(versionType)) != SkyTweaks.IS_UNSTABLE) {
                        FileUtils.writeToFile(versionType, "" + SkyTweaks.IS_UNSTABLE);
                    }

                }
                /* if downloaded is first ever download && version is unstable... */
                else if (SkyTweaks.IS_UNSTABLE) {
                    /* version is unstable ui ui */
                    AtomicBoolean willAllow = new AtomicBoolean(true);
                    FrameMaker maker = new FrameMaker("Version detected as UNSTABLE.", new Dimension(350, 150), WindowConstants.DO_NOTHING_ON_CLOSE, false);
                    AtomicBoolean suc = new AtomicBoolean(false);

                    JFrame frame = maker.pack();
                    maker.addText("You are currently using an UNSTABLE release.", 10, 10, 11, false);
                    maker.addText("<html>" +
                            "Since this is your first release, we highly<br/>recommend you download the latest stable release.<br/>" +
                            "Do you acknowledge that you will not receive any<br/>support and me (NatiaDev) is not<br/>" +
                            "liable for any lost items due to<br/>crashes related to the mod?</html>", 10, 25, 11, false);

                    JButton allowUse = maker.addButton("Yes", 180, 70, 60, e -> {
                        suc.set(true);
                        willAllow.set(true);
                        frame.dispose();
                    });
                    JButton disallowUse = maker.addButton("No", 270, 70, 60, e -> {
                        suc.set(true);
                        willAllow.set(false);
                        frame.dispose();
                    });
                    maker.override();
                    /* keep it from continuing */
                    while (!suc.get()) {
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    if (!willAllow.get())
                        FMLCommonHandler.instance().exitJava(0, false);
                    else {
                        try {
                            versionType.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        FileUtils.writeToFile(versionType, "" + SkyTweaks.IS_UNSTABLE);
                    }
                }
                System.out.println("User is on the latest version of SkyTweaks.");
            }
        });
    }
    private static boolean isRedirected(Map<String, List<String>> header) {
        for( String hv : header.get( null )) {
            if(   hv.contains( " 301 " )
                    || hv.contains( " 302 " )) return true;
        }
        return false;
    }
    private static void downloader(File modsFolder, String link, String fileName) throws IOException {
        URL url  = new URL( link );
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        Map< String, List< String >> header = http.getHeaderFields();
        while( isRedirected( header )) {
            link = header.get( "Location" ).get( 0 );
            url    = new URL( link );
            http   = (HttpURLConnection)url.openConnection();
            header = http.getHeaderFields();
        }
        InputStream input  = http.getInputStream();
        byte[]       buffer = new byte[4096];
        int          n      = -1;
        if (modsFolder.isFile()) {
            OutputStream output = new FileOutputStream(modsFolder);
            while ((n = input.read(buffer)) != -1) {
                output.write( buffer, 0, n );
            }
            output.close();
        } else {
            OutputStream output = new FileOutputStream( new File( modsFolder.getAbsolutePath() + "\\" + fileName ));
            while ((n = input.read(buffer)) != -1) {
                output.write( buffer, 0, n );
            }
            output.close();
        }
    }
}
