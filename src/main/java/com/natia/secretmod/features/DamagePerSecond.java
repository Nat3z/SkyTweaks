package com.natia.secretmod.features;

import com.google.common.base.Stopwatch;
import com.natia.secretmod.SecretUtils;
import com.natia.secretmod.config.SkyTweaksConfig;
import com.natia.secretmod.vicious.HudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DamagePerSecond {
    Minecraft mc = Minecraft.getMinecraft();
    List<Double> damagesDealt = new ArrayList<>();

    @SubscribeEvent
    public void onDamageEvent(LivingAttackEvent event) {
        if (!SkyTweaksConfig.damageMeter) return;
        /* damaged by player */
        if (event.source == null) return;
        if (event.source.getEntity() == null) return;

        if (event.source.getEntity().isEntityEqual(mc.thePlayer)) {
            new Thread(() -> {
                double approxDamageDealt = Math.round(SecretUtils.calcCurrentDamage(mc.thePlayer.getName(), event.entity));
                damagesDealt.add(approxDamageDealt);
            }).start();
        }
    }

    Stopwatch damageDetecter = Stopwatch.createUnstarted();

    public void render() {
        if (!SkyTweaksConfig.damageMeter) return;
        HudElement element = SkyTweaksConfig.damageMeterHud;
        final double[] averageDamageDealt = {0};
        damagesDealt.forEach(damagesDealt -> {
            averageDamageDealt[0] += damagesDealt;
        });
        /* prevent dividing by 0 */
        if (averageDamageDealt[0] == 0d) return;
        /* Reset damage every 3s */
        if (!damageDetecter.isRunning()) damageDetecter.start();
        if (damageDetecter.elapsed(TimeUnit.SECONDS) >= 3) {
            damageDetecter.reset();
            damagesDealt.clear();
        } else {
            averageDamageDealt[0] = averageDamageDealt[0] / damagesDealt.size();
            mc.fontRendererObj.drawString(EnumChatFormatting.RED + "Damage per Second:" + EnumChatFormatting.DARK_RED + " ~" + averageDamageDealt[0], element.x, element.y, Color.WHITE.getRGB(), true);
        }
    }

    private static DamagePerSecond INSTANCE = new DamagePerSecond();
    public static DamagePerSecond getInstance() {
        return INSTANCE;
    }
}
