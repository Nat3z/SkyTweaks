package com.natia.secretmod;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.natia.secretmod.config.SecretModConfig;
import com.natia.secretmod.core.TickedEvent;
import com.natia.secretmod.utils.FileUtils;
import com.natia.secretmod.utils.ItemDiff;
import com.natia.secretmod.utils.Location;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
        fetch("https://api.hypixel.net/skyblock/profiles?uuid=" + UUID + "&key=" + key, res -> {
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
                if (diff != 0) {
                    List<String> scoreboardlines = SecretUtils.getScoreboardLines();
                    boolean no = false;
                    for (String sc : scoreboardlines) {
                        String sCleaned = SecretUtils.cleanSB(sc);
                        if (Location.getAsLocation(sCleaned) != Location.NONE) {
                            inventoryDifference.add(new ItemDiff(key, diff, Location.getAsLocation(sCleaned)));
                            no = true;
                            break;
                        }
                    }
                    if (!no) {
                        inventoryDifference.add(new ItemDiff(key, diff, Location.NONE));
                    }
                }
            });

            // Add changes to already logged changes of the same item, so it will increase/decrease the amount
            // instead of displaying the same item twice
            if (itemPickupLog != null)
            for (ItemDiff diff : inventoryDifference) {
                Collection<ItemDiff> itemDiffs = itemPickupLog.get(diff.getDisplayName());
                if (itemDiffs.size() <= 0) {
                    itemPickupLog.put(diff.getDisplayName(), diff);
                } else {
                    boolean added = false;
                    for (ItemDiff loopDiff : itemDiffs) {
                        if ((diff.getAmount() < 0 && loopDiff.getAmount() < 0) ||
                                (diff.getAmount() > 0 && loopDiff.getAmount() > 0)) {
                            loopDiff.add(diff.getAmount());
                            added = true;
                        }
                    }
                    if (!added) itemPickupLog.put(diff.getDisplayName(), diff);
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

    public static File getGeneralFolder() {
        File folder = new File(Minecraft.getMinecraft().mcDataDir + "\\Skyblock Secret Mod\\");
        if (!folder.exists()) folder.mkdir();
        return folder;
    }
}
