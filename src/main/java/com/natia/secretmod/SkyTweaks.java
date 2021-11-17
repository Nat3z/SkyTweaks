package com.natia.secretmod;

import com.natia.secretmod.commands.RepartyCommand;
import com.natia.secretmod.config.SecretModCommand;
import com.natia.secretmod.config.SecretModHUD;
import com.natia.secretmod.config.CoreExtension;
import com.natia.secretmod.commands.SavePickupLog;
import com.natia.secretmod.core.TickedEvent;
import com.natia.secretmod.extensions.ExtensionList;
import com.natia.secretmod.core.BlockRenderingHook;
import com.natia.secretmod.features.AlertPickups;
import com.natia.secretmod.features.MinionAnalyzer;
import com.natia.secretmod.features.RepartyHook;
import com.natia.secretmod.features.dungeons.BonzoSpiritHook;
import com.natia.secretmod.features.dungeons.CopyFails;
import com.natia.secretmod.features.dungeons.TerminalHighlight;
import com.natia.secretmod.features.slayers.VoidGloom;
import com.natia.secretmod.networking.ColorText;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = SkyTweaks.MODID,
        name = "SkyTweaks",
        version = SkyTweaks.VERSION,
        clientSideOnly = true
)
public class SkyTweaks {
    public static final String MODID = "skytweaks-mod";
    public static final String VERSION = "v1.0.3.1";
    public static final boolean IS_UNSTABLE = false;

    public static CoreExtension configHandler;
    @Mod.Instance(MODID)
    public static SkyTweaks INSTANCE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        configHandler = new CoreExtension();
        ExtensionList.addExtension(configHandler);
        configHandler.updateConfigVariables();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        /* Minecraft - Hooks */
        MinecraftForge.EVENT_BUS.register(new TickedEvent());
        MinecraftForge.EVENT_BUS.register(new BlockRenderingHook());

        /* Dungeons*/
        MinecraftForge.EVENT_BUS.register(new BonzoSpiritHook());
        MinecraftForge.EVENT_BUS.register(new CopyFails());
        MinecraftForge.EVENT_BUS.register(new TerminalHighlight());
        MinecraftForge.EVENT_BUS.register(RepartyHook.getInstance());

        /* Quality of Life */
        MinecraftForge.EVENT_BUS.register(new MinionAnalyzer());
        MinecraftForge.EVENT_BUS.register(new AlertPickups());

        /* Cosmetics */
        MinecraftForge.EVENT_BUS.register(new ColorText());

        /* Slayers - Voidgloom */
        MinecraftForge.EVENT_BUS.register(VoidGloom.getInstance());

        System.out.println("SkyTweaks Mod Initialized");
    }

    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new SavePickupLog());
        ClientCommandHandler.instance.registerCommand(new SecretModCommand());
        ClientCommandHandler.instance.registerCommand(new SecretModHUD());
        ClientCommandHandler.instance.registerCommand(new RepartyCommand());

        configHandler.saveConfig();

        new Thread(SecretUtils::updateBazaarCache).start();

    }
}
