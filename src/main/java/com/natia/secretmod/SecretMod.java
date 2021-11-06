package com.natia.secretmod;

import com.natia.secretmod.commands.RepartyCommand;
import com.natia.secretmod.config.SecretModCommand;
import com.natia.secretmod.config.SecretModHUD;
import com.natia.secretmod.config.CoreExtension;
import com.natia.secretmod.core.SavePickupLog;
import com.natia.secretmod.core.TickedEvent;
import com.natia.secretmod.extensions.ExtensionList;
import com.natia.secretmod.features.AreYouReady;
import com.natia.secretmod.features.MinionAnalyzer;
import com.natia.secretmod.features.RepartyHook;
import com.natia.secretmod.features.bazaar.BazaarHook;
import com.natia.secretmod.features.bazaar.Notifier;
import com.natia.secretmod.features.dungeons.BonzoSpiritHook;
import com.natia.secretmod.features.dungeons.CopyFails;
import com.natia.secretmod.networking.ColorText;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = SecretMod.MODID,
        name = "Secret Mod",
        version = SecretMod.VERSION,
        clientSideOnly = true
)
public class SecretMod {
    public static final String MODID = "secret-mod-v2";
    public static final String VERSION = "1.0.1";

    public static CoreExtension configHandler;
    @Mod.Instance(MODID)
    public static SecretMod INSTANCE;

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
        MinecraftForge.EVENT_BUS.register(new TickedEvent());
        /* Bazaar Notifier */
        //MinecraftForge.EVENT_BUS.register(new BazaarHook());
        //MinecraftForge.EVENT_BUS.register(new Notifier());
        /* Mask Timers */
        MinecraftForge.EVENT_BUS.register(new BonzoSpiritHook());
        MinecraftForge.EVENT_BUS.register(new CopyFails());

        /* Quality of Life */
        MinecraftForge.EVENT_BUS.register(new MinionAnalyzer());
        /* Cosmetics */
        MinecraftForge.EVENT_BUS.register(new ColorText());

        MinecraftForge.EVENT_BUS.register(AreYouReady.getInstance());
        MinecraftForge.EVENT_BUS.register(RepartyHook.getInstance());

        System.out.println("Secret Mod V2 Initialized");
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
