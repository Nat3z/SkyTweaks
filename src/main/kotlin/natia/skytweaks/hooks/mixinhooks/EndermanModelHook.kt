package natia.skytweaks.hooks.mixinhooks

import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.features.slayers.VoidGloom
import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.StringUtils
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

object EndermanModelHook {

    private val empty_enderman = ResourceLocation("secretmod", "entity/enderman-empty.png")
    private val hit_state_enderman = ResourceLocation("secretmod", "entity/enderman-hit.png")
    private val damage_state_enderman = ResourceLocation("secretmod", "entity/enderman-damage.png")


    /*public static void changeEndermanColor() {
        if (!SecretModConfig.seraphHelper) return;
        if (VoidGloom.slayerHealth.isEmpty()) return;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        String unformattedSlayer = StringUtils.stripControlCodes(VoidGloom.slayerHealth);

        if (unformattedSlayer.contains("Hits")) {
            Color color = new Color(SecretModConfig.seraphColorHIT);
            SecretUtils.bindColor(color);
        } else {
            Color color = new Color(SecretModConfig.seraphColorDAMAGE);
            SecretUtils.bindColor(color);
        }

    }*/

    fun overrideTexture(entity: Entity, cir: CallbackInfoReturnable<ResourceLocation>) {
        if (!SkyTweaksConfig.seraphHelper) return
        if (!SkyTweaksConfig.seraphColorize) return
        if (VoidGloom.slayerHealth.isEmpty()) return
        val entityName = StringUtils.stripControlCodes(entity.name)

        if (entityName != "Enderman") return
        /* check if it's a voidgloom thru world using nearby armor stands */
        for (e in Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityArmorStand::class.java, entity.entityBoundingBox.expand(3.0, 5.0, 3.0))) {
            if (StringUtils.stripControlCodes(e.name).contains("Voidgloom Seraph")) {
                val unformattedSlayer = StringUtils.stripControlCodes(VoidGloom.slayerHealth)
                if (unformattedSlayer.contains("Hits")) {
                    cir.setReturnValue(hit_state_enderman)
                } else {
                    cir.setReturnValue(damage_state_enderman)
                }
                break
            }
        }
    }
    /*public static void post() {
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }*/

}
