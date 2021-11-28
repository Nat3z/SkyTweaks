package com.natia.secretmod.features.slayers;

import com.google.common.base.Stopwatch;
import com.natia.secretmod.SecretUtils;
import com.natia.secretmod.config.SkyTweaksConfig;
import com.natia.secretmod.utils.AsyncAwait;
import com.natia.secretmod.utils.Location;
import com.natia.secretmod.utils.RenderUtils;
import com.natia.secretmod.vicious.HudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.util.Constants;

import javax.vecmath.Vector3f;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class VoidGloom {

    Minecraft mc = Minecraft.getMinecraft();

    private BlockPos range = new BlockPos(15, 0, 15);

    private static VoidGloom INSTANCE = new VoidGloom();
    public static VoidGloom getInstance() {
        return INSTANCE;
    }

    private boolean sendTitle = false;
    private String title = "";

    public boolean beaconDown = false;
    public boolean holdingBeacon = false;

    public static String slayerHealth = "";
    public boolean doingVoidgloom = false;

    int yangGlyphsSpawned = 0;

    public void blockRender(DrawBlockHighlightEvent event) {
        BlockPos playerPos = new BlockPos(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ);
        World world = Minecraft.getMinecraft().theWorld;
        if (world == null) return;
        if (!SkyTweaksConfig.seraphHelper) return;

        /* is in void sepulture */
        AtomicBoolean foundBeacon = new AtomicBoolean(false);
        if (Location.getCurrentLocation() == Location.VOID_SEPULTURE || Location.getCurrentLocation() == Location.THE_END) {
            /* check voidglooms */
            for (Entity e : world.getEntitiesWithinAABB(Entity.class, mc.thePlayer.getEntityBoundingBox().expand(15, 10, 15))) {
                String name = StringUtils.stripControlCodes(e.getName());
                if (name.contains("Voidgloom Seraph")) {
                    /* formatted slayer health */
                    slayerHealth = e.getName();
                    doingVoidgloom = true;
                }
                /* within reasonable distance of player */
                if (e instanceof EntityEnderman) {
                    EntityEnderman entity = (EntityEnderman) e;
                    if (SecretUtils.withinRange(range, e.getPosition(), playerPos) && entity.getHeldBlockState().getBlock() != null && entity.getHeldBlockState().getBlock().equals(Blocks.beacon)) {
                        holdingBeacon = true;
                    }
                }

            }

            if (this.doingVoidgloom) {
                /*
                * Taken From HyAddons under GNU General Public License v3.0
                * author: jxxe
                */
                Collection<Entity> entities = mc.theWorld.getLoadedEntityList();
                yangGlyphsSpawned = 0;
                for (Entity e : entities) {
                    if (e != null && e instanceof EntityArmorStand) {
                        EntityArmorStand entity = (EntityArmorStand) e;
                        if (entity.getEquipmentInSlot(4) != null) {
                            ItemStack item = entity.getEquipmentInSlot(4);
                            if (item.getItem() == Items.skull) {
                                NBTTagCompound nbt = item.getTagCompound();
                                if (nbt != null && nbt.hasKey("SkullOwner")) {
                                    String texture = nbt.getCompoundTag("SkullOwner").getCompoundTag("Properties").getTagList("textures", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(0).getString("Value");
                                    texture = new String(Base64.getDecoder().decode(texture));
                                    if (texture.contains("eb07594e2df273921a77c101d0bfdfa1115abed5b9b2029eb496ceba9bdbb4b3")) {
                                        if (SecretUtils.withinRange(new BlockPos(30, 0, 30), e.getPosition(), playerPos)) {
                                            yangGlyphsSpawned++;
                                            Vector3f vec = new Vector3f(entity.getPosition().getX(), entity.getPosition().getY() + 1f, entity.getPosition().getZ());
                                            RenderUtils.highlightBlock(vec, 0.5f, event.partialTicks, new Color(SkyTweaksConfig.yangGlyphHighlightColor));
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
            /*
             * everything under here is by me (NatiaDev)
             */

            SecretUtils.getBlocksInBox(world,
                    new BlockPos(playerPos.getX() + 15, playerPos.getY() + 10, playerPos.getZ() + 15),
                    new BlockPos(playerPos.getX() - 15, playerPos.getY() - 10, playerPos.getZ() - 15))

            .forEach((block, pos) -> {
                if (block.equals(Blocks.beacon)) {
                    if (SkyTweaksConfig.beaconHighlights) {
                        beaconDown = true;
                        foundBeacon.set(true);
                        holdingBeacon = false;

                        if (!sendTitle && (SkyTweaksConfig.beaconHighlightType.equals("Both") || SkyTweaksConfig.beaconHighlightType.equals("Notification Only"))) {
                            title = "Exploding Beacon!";
                            sendTitle = true;
                        }
                        /* checks beacon highlight type */
                        if ((SkyTweaksConfig.beaconHighlightType.equals("Both") || SkyTweaksConfig.beaconHighlightType.equals("Highlight Only"))) {
                            Vector3f vec = new Vector3f(pos.getX(), pos.getY(), pos.getZ());
                            RenderUtils.showBeam(vec, new Color(SkyTweaksConfig.beaconHighlightColor), event.partialTicks);
                            RenderUtils.highlightBlock(vec, 0.5f, event.partialTicks, new Color(SkyTweaksConfig.beaconHighlightColor));
                        }
                    }
                }
            });

            if (!foundBeacon.get() && beaconDown)
                beaconDown = false;
        }
    }

    private Stopwatch titleWatch = Stopwatch.createUnstarted();

    public void render() {
        if (mc.currentScreen != null) return;
        if (!SkyTweaksConfig.seraphHelper) return;
        if (!slayerHealth.equals("")) {
            if (StringUtils.stripControlCodes(slayerHealth).endsWith(" 0❤")) {
                slayerHealth = "";
                return;
            }
            HudElement hudElement = SkyTweaksConfig.voidgloomHUD;
            /* boss health */
            mc.fontRendererObj.drawString(EnumChatFormatting.BOLD + "BOSS: ", hudElement.x, hudElement.y, Color.white.getRGB(), true);
            mc.fontRendererObj.drawString(slayerHealth, hudElement.x + 30, hudElement.y, Color.white.getRGB(), true);
            /* beacon down */
            mc.fontRendererObj.drawString(EnumChatFormatting.DARK_AQUA + "" + EnumChatFormatting.BOLD +  "BEACON DOWN: ", hudElement.x, hudElement.y + 10, Color.white.getRGB(), true);
            mc.fontRendererObj.drawString((beaconDown ? EnumChatFormatting.GREEN + "YES" : EnumChatFormatting.RED + "NO"), hudElement.x + 90, hudElement.y + 10, Color.white.getRGB(), true);
            /* holding beacon */
            mc.fontRendererObj.drawString(EnumChatFormatting.DARK_AQUA + "" + EnumChatFormatting.BOLD +  "BEACON HELD: ", hudElement.x, hudElement.y + 20, Color.white.getRGB(), true);
            mc.fontRendererObj.drawString((holdingBeacon ? EnumChatFormatting.GREEN + "YES" : EnumChatFormatting.RED + "NO"), hudElement.x + 90, hudElement.y + 20, Color.white.getRGB(), true);
            /* yang glyphs spawned */
            mc.fontRendererObj.drawString(EnumChatFormatting.DARK_PURPLE + "" + EnumChatFormatting.BOLD +  "YANG GLYPHS: ", hudElement.x, hudElement.y + 30, Color.white.getRGB(), true);
            mc.fontRendererObj.drawString("" + EnumChatFormatting.LIGHT_PURPLE + yangGlyphsSpawned, hudElement.x + 90, hudElement.y + 30, Color.white.getRGB(), true);
            /* is in hit phase */
            String unformattedSlayer = StringUtils.stripControlCodes(slayerHealth);
            mc.fontRendererObj.drawString( EnumChatFormatting.RED + "" + EnumChatFormatting.BOLD + "HIT PHASE: ", hudElement.x, hudElement.y + 40, Color.white.getRGB(), true);
            mc.fontRendererObj.drawString((unformattedSlayer.contains("Hits") ? EnumChatFormatting.GREEN + "YES" : EnumChatFormatting.RED + "NO"), hudElement.x + 90, hudElement.y + 40, Color.white.getRGB(), true);
        }

        if (sendTitle && !titleWatch.isRunning()) {
            titleWatch.start();
        }

        if (titleWatch.isRunning() && titleWatch.elapsed(TimeUnit.SECONDS) <= 2) {
            SecretUtils.sendTitleCentered(title, Color.red.getRGB());
        } else if (sendTitle) {
            sendTitle = false;
            titleWatch.reset();
            title = "";
        }
    }

    private Stopwatch checkIfVoidgloomNearby = Stopwatch.createUnstarted();

    public void tick() {
        if (mc.theWorld == null) return;

        if (!SkyTweaksConfig.seraphHelper) return;

        if (Location.getCurrentLocation() == Location.VOID_SEPULTURE || Location.getCurrentLocation() == Location.THE_END) {
            AsyncAwait.until(() -> {
                BlockPos playerPos = new BlockPos(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ);

                List<Entity> entities = mc.theWorld.getLoadedEntityList();
                boolean foundVoidgloom = false;
                for (Entity entity : entities) {
                    if (SecretUtils.withinRange(range, entity.getPosition(), playerPos)) {
                        String name = StringUtils.stripControlCodes(entity.getName());
                        if (name.contains("Voidgloom Seraph")) {
                            foundVoidgloom = true;
                            break;
                        }

                    }
                }

                /* did not find nearby voidgloom. reset vgloom util variables. */
                if (!foundVoidgloom) {
                    sendTitle = false;
                    slayerHealth = "";
                    yangGlyphsSpawned = 0;
                    doingVoidgloom = false;
                    holdingBeacon = false;
                    beaconDown = false;
                }
            }, 6, checkIfVoidgloomNearby);
        }
    }

    public void worldLoad() {
        if (titleWatch.isRunning()) titleWatch.reset();
        title = "";
        slayerHealth = "";
        if (checkIfVoidgloomNearby.isRunning()) checkIfVoidgloomNearby.stop();
        yangGlyphsSpawned = 0;

        sendTitle = false;
        doingVoidgloom = false;
        holdingBeacon = false;
        beaconDown = false;
    }


}
