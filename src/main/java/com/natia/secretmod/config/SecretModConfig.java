package com.natia.secretmod.config;

import com.natia.secretmod.vicious.AddConfig;
import com.natia.secretmod.vicious.ConfigType;
import com.natia.secretmod.vicious.HudElement;
import com.natia.secretmod.vicious.ViciousConfig;
import net.minecraft.util.EnumChatFormatting;

public class SecretModConfig implements ViciousConfig {

    /* General - --- */
    @AddConfig(
            name = "Hypixel API Key",
            description = "Your Hypixel API Key. Required for some API-based modules.",
            category = "General",
            subCategory = "---",
            type = ConfigType.INPUT_FIELD,
            UAYOR = false
    )
    public static String apiKey = "";

    @AddConfig(
            name = "Colored Name Cosmetic",
            description = "Allows overriding text messages with colors if a player has bought the color cosmetic.",
            category = "General",
            subCategory = "---",
            type = ConfigType.TOGGLE,
            UAYOR = false
    )
    public static boolean colorCosmetic = true;
    /* General - Quality of Life */
    @AddConfig(
            name = "Don't Render Players in Hub",
            description = "Stops rendering players in Hub. The only exception is NPCS.",
            category = "General",
            subCategory = "Quality of Life",
            type = ConfigType.TOGGLE,
            UAYOR = false
    )
    public static boolean dontRenderPlayersInHub = false;

    @AddConfig(
            name = "Item Pickup Logs",
            description = "Creates a text file where it shows item drops/pickups throughout your session.",
            category = "General",
            subCategory = "Quality of Life",
            type = ConfigType.TOGGLE,
            UAYOR = false
    )
    public static boolean itemPickupLogs = false;

    @AddConfig(
            name = "New Bank HUD",
            description = "Enables the new bank gui. This is UAYOR because it clicks gui elements for you.",
            category = "General",
            subCategory = "Quality of Life",
            type = ConfigType.TOGGLE,
            UAYOR = true
    )
    public static boolean bankGui = false;

    @AddConfig(
            name = "Minion Analyzer",
            description = "Analyzes minion contents and calculates the amount of coins in the minion.",
            category = "General",
            subCategory = "Quality of Life",
            type = ConfigType.TOGGLE,
            UAYOR = false
    )
    public static boolean minionAnalyzer = false;

    /* General - Discord */

    @AddConfig(
            name = "Discord Webhook",
            description = "The Discord Webhook used to send messages in a Discord server.",
            category = "General",
            subCategory = "Discord",
            type = ConfigType.INPUT_FIELD,
            UAYOR = false
    )
    public static String discordWebhookURL = "";

    @AddConfig(
            name = "Announce Dungeon Drops",
            description = "Notifies in a Discord Server about dungeon drops.",
            category = "General",
            subCategory = "Discord",
            type = ConfigType.TOGGLE,
            UAYOR = false
    )
    public static boolean announceDungeonDrops = false;

    /* Dungeons */

    @AddConfig(
            name = "Readied Players",
            description = "Adds a Ready Players HUD which shows the users in your party which are ready.",
            category = "Dungeons",
            subCategory = "Dungeons",
            type = ConfigType.TOGGLE,
            UAYOR = false
    )
    public static boolean readyPlayers = false;

    @AddConfig(
            name = "Floor to join",
            description = "Notifies to join this floor.",
            category = "Dungeons",
            subCategory = "Dungeons",
            type = ConfigType.SLIDER_TEXT,
            sliderChoices = {"F1", "F2", "F3", "F4", "F5", "F6", "F7"},
            UAYOR = false
    )
    public static String autojoindungeonFloor = "F1";

    @AddConfig(
            name = "Join dungeon once ready",
            description = "Once all players are ready, start the dungeon automatically.",
            category = "Dungeons",
            subCategory = "Dungeons",
            type = ConfigType.TOGGLE,
            UAYOR = true
    )
    public static boolean autojoindungeon = false;

    @AddConfig(
            name = "Spirit/Bonzo Timers",
            description = "Displays a timer which shows how long the cooldown for the spirit/bonzo mask is.",
            category = "Dungeons",
            subCategory = "Dungeons",
            type = ConfigType.TOGGLE,
            UAYOR = false
    )
    public static boolean maskTimers = true;

    @AddConfig(
            name = "Reparty Command",
            description = "Automatically reparties all party members. (/rp) This is UAYOR because this is technically a chat macro.",
            category = "Dungeons",
            subCategory = "Dungeons",
            type = ConfigType.TOGGLE,
            UAYOR = true
    )
    public static boolean rpCommand = false;

    /* HUD stuff at end */

    @AddConfig(
            name = "Spirit/Bonzo Mask",
            description = "Bonzo Mask HUD",
            category = "HUD",
            type = ConfigType.HUD,
            subCategory = "",
            requiredElementToggled = "maskTimers",
            UAYOR = false
    )
    public static HudElement spiritBonzoTimerHUD = new HudElement(0, 0, 130, 50);

    @AddConfig(
            name = "Readied Players HUD",
            description = "simple readied players hud",
            category = "HUD",
            type = ConfigType.HUD,
            subCategory = "",
            requiredElementToggled = "readyPlayers",
            UAYOR = false
    )
    public static HudElement readyHUD = new HudElement(0, 0, 190, 100);
}
