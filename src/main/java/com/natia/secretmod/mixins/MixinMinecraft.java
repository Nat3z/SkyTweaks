package com.natia.secretmod.mixins;

import com.natia.secretmod.SecretMod;
import com.natia.secretmod.utils.JarFileReader;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static com.natia.secretmod.utils.WebUtils.fetch;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Shadow @Final public File mcDataDir;

    private Method replaceMethod;
    private boolean updateMod = false;

    private Object[] args;
    private Object instance;
    private String downloadURL;
    private File finalModsFolder;
    private String version;

    @Inject(method = "startGame", at = @At("HEAD"))
    public void startGame(CallbackInfo ci) {
        File viciousFolder = new File(mcDataDir.getAbsolutePath() + "\\vicious\\");
        if (!viciousFolder.exists()) {
            viciousFolder.mkdir();
        }
        File viciousUpdateCycle = new File(mcDataDir.getAbsolutePath() + "\\vicious\\updater.jar");
        /* if not downloaded, download vicious updater */
        fetch("https://api.github.com/repos/Nat3z/ModAssistant/releases/latest", res -> {
            String downloadURL = res.asJson().get("assets").getAsJsonArray().get(0).getAsJsonObject().get("browser_download_url").getAsString();
            if (!viciousUpdateCycle.exists()) {
                try {
                    downloader(viciousFolder, downloadURL, "updater.jar");
                } catch (IOException e) {
                    System.out.println("Failed to download Vicious updater.jar");
                    e.printStackTrace();
                }
            }
        });
        /* check for updates & auto update */

        fetch("https://secretmod-hypixel.herokuapp.com/latest", res -> {
            if (!SecretMod.VERSION.equals(res.asString())) {
                /* update that mod! */
                System.out.println("Adding update to Skyblock Secret Mod...");
                File modsFolder = new File(mcDataDir.getAbsolutePath() + "\\mods\\");
                File subMods = new File(mcDataDir.getAbsolutePath() + "\\mods\\1.8.9\\");

                if (subMods.exists()) {
                    modsFolder = subMods;
                }

                File finalModsFolder = modsFolder;
                fetch("https://api.github.com/repos/Nat3z/SkyblockMod/releases/latest", res1 -> {
                    String downloadURL = res1.asJson().get("assets").getAsJsonArray().get(0).getAsJsonObject().get("browser_download_url").getAsString();
                    try {
                        List<Class> classes = JarFileReader.getClassesFromJarFile(viciousUpdateCycle);
                        for (Class c : classes) {
                            System.out.println(c.getName());
                            if (c.getName().toLowerCase().contains("downloadreplace")) {
                                replaceMethod = c.getDeclaredMethod("downloadReplaceWindow", String.class, File.class, String.class, String.class);
                                System.out.println(replaceMethod.getName());
                                replaceMethod.setAccessible(true);
                                updateMod = true;
                                instance = c.newInstance();
                                this.downloadURL = downloadURL;
                                this.finalModsFolder = finalModsFolder;
                                this.version = res.asString();

                                System.out.println("Prepared update for Skyblock Secret Mod. Now waiting for Minecraft applet to close...");
                                System.out.println("Attempting to update Vicious Mod.");
                                replaceMethod.invoke(instance, downloadURL, finalModsFolder, "Skyblock.Secret.Mod-" + res.asString() + ".jar", "Skyblock.Secret.Mod");
                                break;
                            }
                        }
                    } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                System.out.println("User is on the latest version of Skyblock Secret Mod.");
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
