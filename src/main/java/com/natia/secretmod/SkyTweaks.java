package com.natia.secretmod;

import com.natia.secretmod.commands.RepartyCommand;
import com.natia.secretmod.config.SkyTweaksCommand;
import com.natia.secretmod.config.SkyTweaksHUD;
import com.natia.secretmod.config.CoreExtension;
import com.natia.secretmod.commands.SavePickupLog;
import com.natia.secretmod.features.altC.QuickTab;
import com.natia.secretmod.hooks.*;
import com.natia.secretmod.extensions.ExtensionList;
import com.natia.secretmod.core.BlockRenderingHook;
import com.natia.secretmod.features.*;
import com.natia.secretmod.gui.GuiHook;
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
    public static final String VERSION = "v1.0.4";
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
        SecretUtils.addEnchantAdditives();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        /* Minecraft - Hooks */
        MinecraftForge.EVENT_BUS.register(new RenderHook());
        MinecraftForge.EVENT_BUS.register(new TickHook());
        MinecraftForge.EVENT_BUS.register(new BlockRenderingHook());
        MinecraftForge.EVENT_BUS.register(new GuiHook());
        MinecraftForge.EVENT_BUS.register(new GuiScreenHook());
        MinecraftForge.EVENT_BUS.register(new KeyboardInputHook());
        MinecraftForge.EVENT_BUS.register(new ItemPickupHook());
        MinecraftForge.EVENT_BUS.register(new MessageHook());
        MinecraftForge.EVENT_BUS.register(new WorldHook());

        /* Only do this for things that only have 1 event. */
        MinecraftForge.EVENT_BUS.register(DamagePerSecond.getInstance());
        MinecraftForge.EVENT_BUS.register(new QuickTab());

        /* Cosmetics */
        MinecraftForge.EVENT_BUS.register(new ColorText());

        System.out.println("SkyTweaks Mod Initialized");
    }

    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new SavePickupLog());
        ClientCommandHandler.instance.registerCommand(new SkyTweaksCommand());
        ClientCommandHandler.instance.registerCommand(new SkyTweaksHUD());
        ClientCommandHandler.instance.registerCommand(new RepartyCommand());

        configHandler.saveConfig();

        new Thread(SecretUtils::updateBazaarCache).start();

    }
}
