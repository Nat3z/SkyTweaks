package natia.skytweaks

import cc.blendingMC.commands.BlendingCommandHandler
import gg.blendingMC.BlendingMC
import gg.blendingMC.settings.BlendingMCSettings
import natia.skytweaks.commands.RepartyCommand
import natia.skytweaks.commands.SavePickupLog
import natia.skytweaks.commands.waypoints.WaypointsCommand
import natia.skytweaks.config.SkyTweaksBlend
import natia.skytweaks.config.SkyTweaksCommand
import natia.skytweaks.config.SkyTweaksHUDCommand
import natia.skytweaks.core.BlockRenderingHook
import natia.skytweaks.features.BatDied
import natia.skytweaks.features.DamagePerSecond
import natia.skytweaks.features.altC.QuickTab
import natia.skytweaks.hooks.*
import natia.skytweaks.features.griffin.GriffinBurrowWaypoints
import natia.skytweaks.gui.GuiHook
import natia.skytweaks.gui.trade.ItemLoreHook
import natia.skytweaks.networking.ColorText
import net.minecraft.client.Minecraft
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.lwjgl.input.Keyboard

@Mod(modid = SkyTweaks.MODID, name = "SkyTweaks", version = SkyTweaks.VERSION, clientSideOnly = true)
class SkyTweaks {

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        BlendingMCSettings.welcomed = true
        BlendingMC.getInstance().fmlinitialize()
        configHandler = SkyTweaksBlend()
        configHandler!!.updateConfigVariables()
        SecretUtils.addEnchantAdditives()
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        /* Minecraft - Hooks */
        MinecraftForge.EVENT_BUS.register(RenderHook())
        MinecraftForge.EVENT_BUS.register(TickHook())
        MinecraftForge.EVENT_BUS.register(BlockRenderingHook())
        MinecraftForge.EVENT_BUS.register(GuiHook())

        MinecraftForge.EVENT_BUS.register(GuiScreenHook())
        MinecraftForge.EVENT_BUS.register(KeyboardInputHook())
        MinecraftForge.EVENT_BUS.register(ItemPickupHook())
        MinecraftForge.EVENT_BUS.register(MessageHook())
        MinecraftForge.EVENT_BUS.register(WorldHook())

        /* Only do this for things that only have 1 event. */
        MinecraftForge.EVENT_BUS.register(DamagePerSecond.instance)
        MinecraftForge.EVENT_BUS.register(QuickTab())
        MinecraftForge.EVENT_BUS.register(GriffinBurrowWaypoints.instance)
        MinecraftForge.EVENT_BUS.register(BatDied.instance)
//        MinecraftForge.EVENT_BUS.register(ItemLoreHook())

        /* Cosmetics */
        MinecraftForge.EVENT_BUS.register(ColorText())
        LOGGER.info("SkyTweaks Mod Initialized")
    }

    @Mod.EventHandler
    fun postinit(event: FMLPostInitializationEvent) {
        BlendingCommandHandler.registerCommand(SavePickupLog())
        BlendingCommandHandler.registerCommand(SkyTweaksHUDCommand())
        BlendingCommandHandler.registerCommand(SkyTweaksCommand())
        BlendingCommandHandler.registerCommand(RepartyCommand())
        BlendingCommandHandler.registerCommand(WaypointsCommand())
        ClientRegistry.registerKeyBinding(NOTIFY_CRUSH)
        configHandler!!.saveConfig()

        Thread { SecretUtils.updateBazaarCache() }.start()

    }

    companion object {
        const val MODID: String = "skytweaks-mod"
        const val VERSION = "v1.0.6.2"
        val NOTIFY_CRUSH = KeyBinding("Notify Crush Storm", Keyboard.KEY_Z, "SkyTweaks")
        const val IS_UNSTABLE = false
        @JvmField
        val LOGGER: Logger = LogManager.getLogger("SkyTweaks Logger")!!


        var configHandler: SkyTweaksBlend? = null

        @Mod.Instance(MODID)
        var INSTANCE: SkyTweaks? = null
    }
}
