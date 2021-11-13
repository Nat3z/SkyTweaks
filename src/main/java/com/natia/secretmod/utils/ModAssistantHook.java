package com.natia.secretmod.utils;

import com.natia.secretmod.SecretMod;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.yaml.snakeyaml.Yaml;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.natia.secretmod.utils.WebUtils.fetch;

public class ModAssistantHook {

    public static void openLauncher(File modsFolder) {
        File viciousFolder = new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "\\vicious\\");
        if (!viciousFolder.exists()) {
            viciousFolder.mkdir();
        }

        File launcherSettings = new File(viciousFolder.getAbsolutePath() + "\\launcherSettings.txt");
        File viciousLauncher = new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "\\vicious\\launcher.jar");

        if (launcherSettings.exists()) {
            if (FileUtils.readFile(launcherSettings).equals("disabled"))
                return;
        }
        if (!viciousLauncher.exists()) {
            FrameMaker maker = new FrameMaker("Mod Assistant", new Dimension(350, 150), WindowConstants.DO_NOTHING_ON_CLOSE, false);
            AtomicBoolean suc = new AtomicBoolean(false);
            AtomicBoolean downloadingFull = new AtomicBoolean(false);

            JFrame frame = maker.pack();
            maker.addText("Would you like to install Mod Assistant Lite or Mod Assistant?", 10, 10, 11, false);
            maker.addText("<html>Lite: Auto-Update system<br/>" +
                    "Full: Mod Manager, Mods profile system, Minecraft pre-launch ui</html>", 10, 30, 11, false);

            JButton skipUpdate = maker.addButton("Lite", 50, 70, 100, e -> {
                suc.set(true);
                frame.dispose();
            });
            JButton downloadUpdate = maker.addButton("Full", 180, 70, 100, e -> {
                suc.set(true);
                downloadingFull.set(true);
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

            if (!downloadingFull.get()) {
                if (!launcherSettings.exists()) {
                    try {
                        launcherSettings.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    FileUtils.writeToFile(launcherSettings, "disabled");
                    return;
                }
            }
        }
        /* check if version is update */
        fetch("https://api.github.com/repos/Nat3z/ModAssistant-Launcher/releases/latest", res -> {
            String viciousCycle = res.asJson().get("assets").getAsJsonArray().get(0).getAsJsonObject().get("browser_download_url").getAsString();
            try {
                String version = res.asJson().get("tag_name").getAsString();
                if (!launcherSettings.exists()) {
                    launcherSettings.createNewFile();
                    FileUtils.writeToFile(launcherSettings, "no version found.");
                }

                if (!launcherSettings.exists() || !FileUtils.readFile(launcherSettings).equals(version)) {
                    System.out.println("Mod Assistant Launcher is not installed/not up to date. Now installing Mod Assistant.");
                    downloader(viciousFolder, viciousCycle, "launcher.jar");
                    /* create version system */
                    FileUtils.writeToFile(launcherSettings, version);
                }
            } catch (IOException e) {
                System.out.println("Failed to download Mod Assistant launcher.jar");
                e.printStackTrace();
            }
        });

        List<Class> classes = null;
        try {
            classes = JarFileReader.getClassesFromJarFile(viciousLauncher);

            for (Class c : classes) {
                System.out.println(c.getName());
                if (c.getSimpleName().toLowerCase().equals("modassistant")) {
                    Method replaceMethod = c.getDeclaredMethod("open", File.class, File.class);

                    System.out.println("Attempting to open Mod Assistant Launcher.");
                    boolean result = (boolean) replaceMethod.invoke(c.newInstance(), modsFolder, Minecraft.getMinecraft().mcDataDir);
                    if (!result) FMLCommonHandler.instance().exitJava(0, false);
                }
            }
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void open(String apiURL, String downloadURL, File modsFolder, String filename, String replacement) {
        File viciousFolder = new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "\\vicious\\");
        if (!viciousFolder.exists()) {
            viciousFolder.mkdir();
        }
        File viciousUpdateCycle = new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "\\vicious\\updater.jar");
        File updaterVersion = new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "\\vicious\\update-version.txt");
        File optOutPreRelease = new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "\\vicious\\opt-out-pre.txt");

        /* if not downloaded, download vicious updater */
        fetch("https://api.github.com/repos/Nat3z/ModAssistant/releases/latest", res -> {
            String viciousCycle = res.asJson().get("assets").getAsJsonArray().get(0).getAsJsonObject().get("browser_download_url").getAsString();
            String version = res.asJson().get("tag_name").getAsString();

            try {

                if (!updaterVersion.exists()) {
                    updaterVersion.createNewFile();
                    FileUtils.writeToFile(updaterVersion, "no version found.");
                }
                if (!viciousUpdateCycle.exists() || !FileUtils.readFile(updaterVersion).equals(version)) {
                    System.out.println("Mod Assistant is not installed/not up to date. Now installing Mod Assistant.");
                    downloader(viciousFolder, viciousCycle, "updater.jar");
                    /* create version system */
                    FileUtils.writeToFile(updaterVersion, version);
                }
            } catch (IOException e) {
                System.out.println("Failed to download Vicious updater.jar");
                e.printStackTrace();
            }
        });
        String[] disabledPreRelease = new String[]{""};
        if (optOutPreRelease.exists())
            disabledPreRelease = FileUtils.readFile(optOutPreRelease).split("/n");

        AtomicBoolean willDownload = new AtomicBoolean(true);
        AtomicReference<String> assistVersionDownload = new AtomicReference<>("");
        String[] finalDisabledPreRelease = disabledPreRelease;
        fetch(apiURL, res -> {
            assistVersionDownload.set(res.asJson().get("assets").getAsJsonArray().get(0).getAsJsonObject().get("browser_download_url").getAsString());

            for (String uri : finalDisabledPreRelease) {
                if (uri.equals(assistVersionDownload.get())) {
                    willDownload.set(false);
                }
            }
            /* not in list of no pre release */
            if (willDownload.get())
                if (res.asJson().get("prerelease").getAsBoolean()) {
                    /* pre-release ui */
                    FrameMaker maker = new FrameMaker("Pre-Release update found.", new Dimension(350, 150), WindowConstants.DO_NOTHING_ON_CLOSE, false);
                    AtomicBoolean suc = new AtomicBoolean(false);

                    JFrame frame = maker.pack();
                    maker.addText("A pre-release update for " + filename + " is available.", 10, 10, 11, false);
                    maker.addText("Would you like to download this update?", 40, 30, 15, false);

                    JButton skipUpdate = maker.addButton("Skip", 50, 70, 100, e -> {
                        suc.set(true);
                        willDownload.set(false);
                        frame.dispose();
                    });
                    JButton downloadUpdate = maker.addButton("Download", 180, 70, 100, e -> {
                        suc.set(true);
                        willDownload.set(true);
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
                }
        });
        if (!willDownload.get()) {
            if (!optOutPreRelease.exists()) {
                try {
                    optOutPreRelease.createNewFile();
                    FileUtils.writeToFile(optOutPreRelease, assistVersionDownload.get() + "/n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                boolean alreadyDisabled = false;
                for (String uri : finalDisabledPreRelease) {
                    if (uri.equals(assistVersionDownload.get())) {
                        alreadyDisabled = true;
                        break;
                    }
                }
                if (!alreadyDisabled)
                    FileUtils.writeToFile(optOutPreRelease, FileUtils.readFile(optOutPreRelease) + assistVersionDownload.get() + "/n");
            }
            return;
        }

        try {
            List<Class> classes = JarFileReader.getClassesFromJarFile(viciousUpdateCycle);
            for (Class c : classes) {
                System.out.println(c.getName());
                if (c.getName().toLowerCase().contains("downloadreplace")) {
                    Method replaceMethod = c.getDeclaredMethod("downloadReplaceWindow", String.class, File.class, String.class, String.class);

                    System.out.println("Attempting to update using Mod Assistant.");
                    replaceMethod.invoke(c.newInstance(), downloadURL, modsFolder, filename, replacement);
                    FMLCommonHandler.instance().exitJava(0, false);
                }
            }
        } catch (Exception ex) {
            System.out.println("Failed to auto-update using Mod Assistant.");
            ex.printStackTrace();
        }
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
