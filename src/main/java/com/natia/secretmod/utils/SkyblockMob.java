package com.natia.secretmod.utils;

import com.natia.secretmod.features.slayers.VoidGloom;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.StringUtils;

public class SkyblockMob {

    private String health;
    private Entity entity;
    public SkyblockMob(String mobName, Entity entity) {
        this.entity = entity;
        for (EntityArmorStand e : Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityArmorStand.class, entity.getEntityBoundingBox().expand(2, 5, 2))) {
            if (StringUtils.stripControlCodes(e.getName()).contains(mobName)) {
                health = e.getName();
                break;
            }
        }
    }

    public Entity getEntity() {
        return entity;
    }

    public String getHealth() {
        return health;
    }
}
