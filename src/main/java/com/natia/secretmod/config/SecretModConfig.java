package com.natia.secretmod.config;

import com.natia.secretmod.vicious.AddConfig;
import com.natia.secretmod.vicious.ConfigType;
import com.natia.secretmod.vicious.HudElement;
import com.natia.secretmod.vicious.ViciousConfig;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;

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

    @AddConfig(
            name = "Cache Bazaar",
            description = "Allow looping every 15-20s through the bazaar. Will (maybe) reduce ping/lag.",
            category = "General",
            subCategory = "---",
            type = ConfigType.TOGGLE,
            UAYOR = false
    )
    public static boolean bazaarCaching = true;

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
            name = "Minion Analyzer",
            description = "Analyzes minion contents and calculates the amount of coins in it.",
            category = "General",
            subCategory = "Quality of Life",
            type = ConfigType.TOGGLE,
            UAYOR = false
    )
    public static boolean minionAnalyzer = false;



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
            name = "Spirit/Bonzo Timers",
            description = "Displays a timer which shows how long the cooldown for the spirit/bonzo mask is.",
            category = "Dungeons",
            subCategory = "Dungeons",
            type = ConfigType.TOGGLE,
            UAYOR = false
    )
    public static boolean maskTimers = true;

    @AddConfig(
            name = "Copy Fails/Deaths",
            description = "Automatically copies dungeon fails AND dungeon deaths.",
            category = "Dungeons",
            subCategory = "Dungeons",
            type = ConfigType.TOGGLE,
            UAYOR = false
    )
    public static boolean copyFails = true;

    @AddConfig(
            name = "Highlight Inactive Terminals",
            description = "Highlights all skipped/uncompleted terminals in F7. UAYOR because you can see blocks through walls. (NOT TESTED)",
            category = "Dungeons",
            subCategory = "Dungeons",
            type = ConfigType.TOGGLE,
            UAYOR = true
    )
    public static boolean terminalHighlight = true;

    @AddConfig(
            name = "Highlight Terminals Color - Inactive",
            description = "Color of Inactive/Inactivated Terminals.",
            category = "Dungeons",
            subCategory = "Dungeons",
            type = ConfigType.COLOR_WHEEL,
            UAYOR = false
    )
    public static int terminalHighlightColorINACTIVE = Color.RED.getRGB();

    @AddConfig(
            name = "Highlight Terminals Color - Active",
            description = "Color of Activated Terminals.",
            category = "Dungeons",
            subCategory = "Dungeons",
            type = ConfigType.COLOR_WHEEL,
            UAYOR = false
    )
    public static int terminalHighlightColorACTIVE = Color.GREEN.getRGB();


    @AddConfig(
            name = "Reparty Command",
            description = "Automatically reparties all party members. (/rp) This is UAYOR because this is technically a chat macro.",
            category = "Dungeons",
            subCategory = "Dungeons",
            type = ConfigType.TOGGLE,
            UAYOR = true
    )
    public static boolean rpCommand = false;



    /* Slayers */

    /* Slayers - Voidgloom Seraph */
    @AddConfig(
            name = "Voidgloom Seraph Assist",
            description = "Assists you in the fight with the Voidgloom Seraph.",
            category = "Slayers",
            subCategory = "Voidgloom Seraph",
            type = ConfigType.TOGGLE,
            UAYOR = false
    )
    public static boolean seraphHelper = false;

    @AddConfig(
            name = "Color Voidgloom Seraph",
            description = "Assists you in the fight by coloring it when it is in a certain phase.",
            category = "Slayers",
            subCategory = "Voidgloom Seraph",
            type = ConfigType.TOGGLE,
            UAYOR = false
    )
    public static boolean seraphColorize = false;

    @AddConfig(
            name = "Yang Glyph Highlighter",
            description = "Highlights the YANG GLYPH.",
            category = "Slayers",
            subCategory = "Voidgloom Seraph",
            type = ConfigType.TOGGLE,
            UAYOR = false
    )
    public static boolean yangGlyphHighlights = false;

    @AddConfig(
            name = "Yang Glyph Highlight Color",
            description = "Highlight color of the YANG GLYPH.",
            category = "Slayers",
            subCategory = "Voidgloom Seraph",
            type = ConfigType.COLOR_WHEEL,
            UAYOR = false
    )
    public static int yangGlyphHighlightColor = new Color(164, 2, 17).getRed();

    /* beacons */
    @AddConfig(
            name = "Exploding Beacon Notifier",
            description = "Notifies and highlights the exploding beacon.",
            category = "Slayers",
            subCategory = "Voidgloom Seraph",
            type = ConfigType.TOGGLE,
            UAYOR = false
    )
    public static boolean beaconHighlights = false;

    @AddConfig(
            name = "Exploding Beacon Highlight Color",
            description = "Highlight color of the EXPLODING BEACON.",
            category = "Slayers",
            subCategory = "Voidgloom Seraph",
            type = ConfigType.COLOR_WHEEL,
            UAYOR = false
    )
    public static int beaconHighlightColor = new Color(0, 101, 164).getRed();

    @AddConfig(
            name = "Exploding Beacon Notify Type",
            description = "Type of notification for exploding beacon.",
            category = "Slayers",
            subCategory = "Voidgloom Seraph",
            type = ConfigType.SLIDER_TEXT,
            sliderChoices = {"Both", "Highlight Only", "Notification Only"},
            UAYOR = false
    )
    public static String beaconHighlightType = "Both";














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
            name = "Voidgloom Assist",
            description = "Voidgloom HUD",
            category = "HUD",
            type = ConfigType.HUD,
            subCategory = "",
            requiredElementToggled = "seraphHelper",
            UAYOR = false
    )
    public static HudElement voidgloomHUD = new HudElement(0, 0, 190, 100);

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
