package com.natia.secretmod;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.natia.secretmod.config.SkyTweaksConfig;
import com.natia.secretmod.core.ItemPickupEvent;
import com.natia.secretmod.utils.*;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.natia.secretmod.utils.WebUtils.fetch;

public class SecretUtils {

    private static List<ItemStack> previousInventory;
    private static final int SKYBLOCK_MENU_SLOT = 8;
    private static Multimap<String, ItemDiff> itemPickupLog = ArrayListMultimap.create();
    public static JsonObject bazaarCached = new JsonObject();
    public static TileEntitySign CURRENT_SIGN;

    public static void sendMessage(String message) {
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.AQUA + "[SkyTweaks] " + EnumChatFormatting.YELLOW + message));
    }

    public static boolean isValid() {
        if (Minecraft.getMinecraft().getCurrentServerData().serverIP != null && Minecraft.getMinecraft().getCurrentServerData().serverIP.contains("hypixel.net")) {
            if (Minecraft.getMinecraft().thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1) == null) return false;
            String sbTitle = EnumChatFormatting.getTextWithoutFormattingCodes(Minecraft.getMinecraft().thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1).getDisplayName());
            if (sbTitle.startsWith("SKYBLOCK")) {
                return true;
            }
        }
        return false;
    }

    public static void updateBazaarCache() {
        fetch("https://api.hypixel.net/skyblock/bazaar", res -> {
            if (res.asJson().get("success").getAsBoolean()) {
                bazaarCached = res.asJson();
            }
        });
    }

    /**
     * Made By BiscuitDevelopment for SkyblockAddons
     * @author BiscuitDevelopment
     *
     * @param item
     * @return
     */
    private static final Pattern PURSE_REGEX = Pattern.compile("(?:Purse|Piggy): (?<coins>[0-9.]*)(?: .*)?");
    public static double getCoins() {
        for (String line : getScoreboardLines()) {
            String stripped = keepScoreboardCharacters(StringUtils.stripControlCodes(line));
            Matcher matcher = PURSE_REGEX.matcher(stripped);
            if (matcher.matches()) {
                try {
                    return Double.parseDouble(matcher.group("coins"));
                } catch(NumberFormatException ignored) {
                    return 0;
                }
            }
        }
        return 0;
    }
    /**
     * Made By BiscuitDevelopment for SkyblockAddons
     * @author BiscuitDevelopment
     *
     * @param item
     * @return
     */
    private static final Pattern SCOREBOARD_CHARACTERS = Pattern.compile("[^a-z A-Z:0-9/'.]");
    public static String keepScoreboardCharacters(String text) {
        return SCOREBOARD_CHARACTERS.matcher(text).replaceAll("");
    }

    public static String getLatestProfile(String UUID, String key) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        AtomicReference<String> returnValue = new AtomicReference<>("");
        // Get profiles
        System.out.println("Fetching profiles...");
        System.out.println(UUID);
        fetch("https://api.hypixel.net/skyblock/profiles?key=" + key + "&uuid=" + UUID, res -> {
            JsonObject profilesResponse = res.asJson();
            if (!profilesResponse.get("success").getAsBoolean()) {
                String reason = profilesResponse.get("cause").getAsString();
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Failed with reason: " + reason));
            } else {
                if (profilesResponse.get("profiles").isJsonNull()) {
                    player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "This player doesn't appear to have played SkyBlock."));
                } else {
                    // Loop through profiles to find latest
                    System.out.println("Looping through profiles...");
                    String latestProfile = "";
                    long latestSave = 0;
                    JsonArray profilesArray = profilesResponse.get("profiles").getAsJsonArray();

                    for (JsonElement profile : profilesArray) {
                        JsonObject profileJSON = profile.getAsJsonObject();
                        long profileLastSave = 1;
                        if (profileJSON.get("members").getAsJsonObject().get(UUID).getAsJsonObject().has("last_save")) {
                            profileLastSave = profileJSON.get("members").getAsJsonObject().get(UUID).getAsJsonObject().get("last_save").getAsLong();
                        }

                        if (profileLastSave > latestSave) {
                            latestProfile = profileJSON.get("profile_id").getAsString();
                            latestSave = profileLastSave;
                        }
                        returnValue.set(latestProfile);
                    }
                }
            }
        });
        return returnValue.get();
    }

    public static Multimap<String, ItemDiff> getItemPickupLog() {
        return itemPickupLog;
    }

    public static void setItemPickupLog(Multimap<String, ItemDiff> itemPickupLog) {
        SecretUtils.itemPickupLog = itemPickupLog;
    }

    public static List<String> getScoreboardLines() {
        List<String> lines = new ArrayList<>();
        Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
        if (scoreboard == null)
            return lines;

        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
        if (objective == null)
            return lines;

        Collection<Score> scores = scoreboard.getSortedScores(objective);

        List<Score> list = scores.stream()
                .filter(input -> input != null && input.getPlayerName() != null && !input.getPlayerName()
                        .startsWith("#"))
                .collect(Collectors.toList());

        if (list.size() > 15)
            scores = Lists.newArrayList(Iterables.skip(list, scores.size() - 15));
        else
            scores = list;

        for (Score score : scores) {
            ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
            lines.add(ScorePlayerTeam.formatPlayerName(team, score.getPlayerName()));
        }

        return lines;
    }

    public static String cleanSB(String scoreboard) {
        char[] nvString = StringUtils.stripControlCodes(scoreboard).toCharArray();
        StringBuilder cleaned = new StringBuilder();

        for (char c : nvString) {
            if ((int) c > 20 && (int) c < 127)
                cleaned.append(c);
        }

        return cleaned.toString();
    }

    /**
    * Created by BiscuitDevelopement
     * https://github.com/BiscuitDevelopment/SkyblockAddons
     * MIT License
     */
    public static boolean isNPC(Entity entity) {
        if (!(entity instanceof EntityOtherPlayerMP)) {
            return false;
        }

        EntityLivingBase entityLivingBase = (EntityLivingBase) entity;

        return entity.getUniqueID().version() == 2 && entityLivingBase.getHealth() == 20.0F && !entityLivingBase.isPlayerSleeping();
    }

    public static Location isInDungeons() {
        try {
            if (isValid()) {
                List<String> scoreboard = getScoreboardLines();

                for (String s : scoreboard) {
                    String sCleaned = cleanSB(s);
                    if (sCleaned.contains("Dungeon Cleared"))
                        return Location.THE_CATACOMBS;
                }
            }

        } catch (Exception e) { e.printStackTrace(); }
        return Location.NONE;
    }

    public static void playLoudSound(String sound, float pitch) {
        Minecraft.getMinecraft().thePlayer.playSound(sound, 1, pitch);
    }

    /**
     * Made by BiscuitDevelopemnt's SkyblockAddons
     * MIT License
     * @param currentInventory
     */
    public static void getInventoryDiff(ItemStack[] currentInventory) {
        List<ItemStack> currentInv = Arrays.asList(currentInventory);
        ItemStack[] pickuplist = new ItemStack[36];
        List<ItemStack> newInventory = copyInventory(currentInventory);
        Map<String, Integer> previousInventoryMap = new HashMap<>();
        Map<String, Integer> newInventoryMap = new HashMap<>();

        if (previousInventory != null) {

            for(int i = 0; i < newInventory.size(); i++) {
                if (i == SKYBLOCK_MENU_SLOT) { // Skip the SkyBlock Menu slot all together (which includes the Quiver Arrow now)
                    continue;
                }

                ItemStack previousItem = previousInventory.get(i);
                ItemStack newItem = newInventory.get(i);

                if(previousItem != null) {
                    int amount = previousInventoryMap.getOrDefault(previousItem.getDisplayName(), 0) + previousItem.stackSize;
                    previousInventoryMap.put(previousItem.getDisplayName(), amount);
                }

                if(newItem != null) {
                    if (newItem.getDisplayName().contains(" "+ EnumChatFormatting.DARK_GRAY +"x")) {
                        String newName = newItem.getDisplayName().substring(0, newItem.getDisplayName().lastIndexOf(" "));
                        newItem.setStackDisplayName(newName); // This is a workaround for merchants, it adds x64 or whatever to the end of the name.
                    }
                    int amount = newInventoryMap.getOrDefault(newItem.getDisplayName(), 0) + newItem.stackSize;
                    newInventoryMap.put(newItem.getDisplayName(), amount);
                }
            }

            List<ItemDiff> inventoryDifference = new LinkedList<>();
            Set<String> keySet = new HashSet<>(previousInventoryMap.keySet());
            keySet.addAll(newInventoryMap.keySet());

            keySet.forEach(key -> {
                int previousAmount = previousInventoryMap.getOrDefault(key, 0);
                int newAmount = newInventoryMap.getOrDefault(key, 0);
                int diff = newAmount - previousAmount;
                if (diff > 0) {
                    inventoryDifference.add(new ItemDiff(key, diff, Location.getCurrentLocation()));
                }
            });

            // Add changes to already logged changes of the same item, so it will increase/decrease the amount
            // instead of displaying the same item twice
            if (itemPickupLog != null)
            for (ItemDiff diff : inventoryDifference) {
                Collection<ItemDiff> itemDiffs = itemPickupLog.get(diff.getDisplayName());
                if (itemDiffs.size() <= 0) {
                    itemPickupLog.put(diff.getDisplayName(), diff);
                    MinecraftForge.EVENT_BUS.post(new ItemPickupEvent(diff));
                } else {
                    boolean added = false;
                    for (ItemDiff loopDiff : itemDiffs) {
                        if ((diff.getAmount() < 0 && loopDiff.getAmount() < 0) ||
                                (diff.getAmount() > 0 && loopDiff.getAmount() > 0)) {
                            loopDiff.add(diff.getAmount());
                            MinecraftForge.EVENT_BUS.post(new ItemPickupEvent(loopDiff));
                            added = true;
                        }
                    }
                    if (!added) {
                        itemPickupLog.put(diff.getDisplayName(), diff);
                        MinecraftForge.EVENT_BUS.post(new ItemPickupEvent(diff));
                    }
                }
            }

        }

        previousInventory = newInventory;
    }

    /**
     * Made by Biscuit from SkyblockAddons
     * MIT License
     * @param inventory
     * @return
     */
    public static List<ItemStack> copyInventory(ItemStack[] inventory) {
        List<ItemStack> copy = new ArrayList<>(inventory.length);
        for (ItemStack item : inventory) {
            if (item != null) {
                copy.add(ItemStack.copyItemStack(item));
            } else {
                copy.add(null);
            }
        }
        return copy;
    }

    public static void setPreviousInventory(List<ItemStack> previousInventory) {
        SecretUtils.previousInventory = previousInventory;
    }

    /**
     * @author NatiaDev
     * Gets all blocks in a certain block radius. (e.g. -100 5 10, -200 5 50)
     * @return blocklist
     */
    public static Map<Block, BlockPos> getBlocksInBox(World world, BlockPos pos1, BlockPos pos2) {
        Map<Block, BlockPos> blocks = new HashMap<>();

        for (BlockPos blockPos : BlockPos.getAllInBox(pos1, pos2)) {
            blocks.put(world.getBlockState(blockPos).getBlock(), blockPos);
        }

        return blocks;
    }

    /**
     * Generates an Empty Runnable. Useful for SkyblockTimers
     * @author NatiaDev
     */
    public static Runnable generateEmptyRunnable() {
        return () -> {

        };
    }

    /**
     * @author NatiaDev
     * Simulates a title and sends it to client.
     * @param message
     * @param color
     */
    public static void sendTitleCentered(String message, int color) {

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        int height = sr.getScaledHeight();
        int width = sr.getScaledWidth();
        drawCenteredString(message, (int) (width / 2) / 3, (int) (height *0.450) / 3, color, 3);
    }

    public static boolean withinRange(BlockPos range, BlockPos pos1, BlockPos pos2) {
            return (
                    pos1.getX() >= pos2.getX() && pos1.getX() <= range.getX() &&
                    pos1.getZ() >= pos2.getZ() && pos1.getZ() <= range.getZ()
            );
    }

    /**
     * Allows you to bind a color to entities, blocks, whatever you want!
     * @author NatiaDev
     * @param color
     */
    public static void bindColor(Color color) {
        GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
    }

    /**
     * Draws a centered string on the screen based on text length
     * @author NatiaDev ext. Minecraft
     * @param text
     * @param x
     * @param y
     * @param color
     * @param scale
     */
    public static void drawCenteredString(String text, int x, int y, int color, double scale) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1);
        Minecraft.getMinecraft().fontRendererObj.drawString(text,
                (int) (x) - Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) / 2,
                (int) (y), color, true);
        GlStateManager.popMatrix();
    }

    public static File getGeneralFolder() {
        File folder = new File(Minecraft.getMinecraft().mcDataDir + "\\Skyblock Secret Mod\\");
        if (!folder.exists()) folder.mkdir();
        return folder;
    }

    public static double calcArbitraryDamage(double strength, double critDamage, double additiveMultipliers, double weaponDamage) {
        return (5 + weaponDamage) * (1 + strength/100) * (1 + critDamage / 100) * (1 + additiveMultipliers / 100);
    }

    private static List<Double> cachedStats = new ArrayList<>();
    private static Map<String, Double> enchantsAdditivesEnderman = new HashMap<>();
    private static Map<String, Double> enchantAdditives = new HashMap<>();

    public static void addEnchantAdditives() {
        if (enchantsAdditivesEnderman.isEmpty()) {
            /* if someone can make this more optimized, please do :D */
            enchantsAdditivesEnderman.putIfAbsent("Ender Slayer I", 0.12);
            enchantsAdditivesEnderman.putIfAbsent("Ender Slayer II", 0.24);
            enchantsAdditivesEnderman.putIfAbsent("Ender Slayer III", 0.36);
            enchantsAdditivesEnderman.putIfAbsent("Ender Slayer IV", 0.48);
            enchantsAdditivesEnderman.putIfAbsent("Ender Slayer V", 0.60);
            enchantsAdditivesEnderman.putIfAbsent("Ender Slayer VI", 0.72);
            enchantsAdditivesEnderman.putIfAbsent("Ender Slayer VII", 0.84);

            enchantAdditives.putIfAbsent("First Strike I", 0.25);
            enchantAdditives.putIfAbsent("First Strike II", 0.5);
            enchantAdditives.putIfAbsent("First Strike III", 0.75);
            enchantAdditives.putIfAbsent("First Strike IV", 1d);
            enchantAdditives.putIfAbsent("First Strike V", 1.25);

            enchantAdditives.putIfAbsent("Sharpness I", 0.05);
            enchantAdditives.putIfAbsent("Sharpness II", 0.1);
            enchantAdditives.putIfAbsent("Sharpness III", 0.15);
            enchantAdditives.putIfAbsent("Sharpness IV", 0.2);
            enchantAdditives.putIfAbsent("Sharpness V", 0.25);
            enchantAdditives.putIfAbsent("Sharpness VI", 0.3);
            enchantAdditives.putIfAbsent("Sharpness VII", 0.35);
        }
    }

    public static double calcCurrentDamage(String name, Entity entityDamaged) {
        Minecraft mc = Minecraft.getMinecraft();
        if (cachedStats.isEmpty()) {
            AtomicReference<String> latest = new AtomicReference<>("");
            fetch("https://sky.shiiyu.moe/api/v2/profile/" + name, res -> {
                if (res == null) return;
                JsonObject jsonObject = res.asJson().get("profiles").getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                for (Map.Entry<String,JsonElement> entry : entrySet) {
                    if (entry.getValue().getAsJsonObject().get("current").getAsBoolean()) {
                        latest.set(entry.getKey());
                        break;
                    }
                }
            });
            if (latest.get().equals("")) return -404;

            fetch("https://sky.shiiyu.moe/api/v2/profile/" + name, res -> {
                JsonObject profileData = res.asJson().getAsJsonObject("profiles").getAsJsonObject(latest.get()).get("data").getAsJsonObject();

                double critDamage = profileData.getAsJsonObject("stats").get("crit_damage").getAsDouble();
                double strength = profileData.getAsJsonObject("stats").get("strength").getAsDouble();
                double additive = profileData.getAsJsonObject("stats").get("damage_increase").getAsDouble();

                cachedStats.add(strength);
                cachedStats.add(critDamage);
                cachedStats.add(additive);
            });
        }
        double itemdamage = 0;
        double itemcritDamage = 0;
        double itemStrength = 0;
        final double[] itemAdditive = {0};

        for (String line : ItemUtils.getLore(mc.thePlayer.getHeldItem())) {
            line = StringUtils.stripControlCodes(line);

            if (line.startsWith("Damage:")) {
                try {
                    itemdamage = Double.parseDouble(line.split(" ")[1].replace("+", ""));
                } catch (NumberFormatException ex) {
                    itemdamage = 0;
                }
            } else if (line.startsWith("Strength")) {
                try {
                    itemStrength = Double.parseDouble(line.split(" ")[1].replace("+", ""));
                } catch (NumberFormatException ex) {
                    itemStrength = 0;
                }
            } else if (line.startsWith("Crit Damage")) {
                try {
                    itemcritDamage = Double.parseDouble(line.split(" ")[2].replace("+", "").replace("%", ""));
                } catch (NumberFormatException ex) {
                    itemcritDamage = 0;
                }
            }

            else if (line.contains(",")) {

                String[] enchants = line.split(",");
                for (String enchant : enchants) {
                    if (enchant.startsWith(" ")) enchant = enchant.replaceFirst(" ", "");
                    final String enchantFinal = StringUtils.stripControlCodes(enchant);

                    if (entityDamaged instanceof EntityEnderman)
                        enchantsAdditivesEnderman.forEach((theEnchant, additive) -> {
                            if (enchantFinal.startsWith(theEnchant)) {
                                itemAdditive[0] += additive;
                            }
                        });


                    enchantAdditives.forEach((theEnchant, additive) -> {
                        if (theEnchant.contains("First Strike") && SkyTweaksConfig.assumeFirstStrike) {
                            if (enchantFinal.startsWith(theEnchant)) {
                                itemAdditive[0] += additive;
                            }
                        } else if (!theEnchant.contains("First Strike"))
                            if (enchantFinal.startsWith(theEnchant)) {
                                itemAdditive[0] += additive;
                            }
                    });
                }
            }
        }

        if (StringUtils.stripControlCodes(mc.thePlayer.getHeldItem().getDisplayName()).contains("Fabled")) {
            itemAdditive[0] += 1;
        }
        return calcArbitraryDamage(cachedStats.get(0) + itemStrength, cachedStats.get(1) + itemcritDamage, cachedStats.get(2) + itemAdditive[0], itemdamage);
    }

}
