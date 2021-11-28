package com.natia.secretmod.config;

import com.natia.secretmod.vicious.AddConfig;
import com.natia.secretmod.vicious.ConfigType;
import com.natia.secretmod.vicious.HudElement;
import com.natia.secretmod.vicious.ViciousConfig;

import java.awt.*;

public class SkyTweaksConfig implements ViciousConfig {

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
            name = "Skyblock Timers",
            description = "Shows Timers for Skyblock events. Required for some modules to work.",
            category = "General",
            subCategory = "Quality of Life",
            type = ConfigType.TOGGLE,
            UAYOR = false
    )
    public static boolean allowTimers = false;

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

    @AddConfig(
            name = "Damage Meter",
            description = "Gets damage dealt per second. (NOT VERY GOOD IF API OFF)",
            category = "General",
            subCategory = "Quality of Life",
            type = ConfigType.TOGGLE,
            UAYOR = false
    )
    public static boolean damageMeter = false;

    @AddConfig(
            name = "Assume First Strike ALWAYS",
            description = "Always assumes that every time you hit that it's the first.",
            category = "General",
            subCategory = "Quality of Life",
            type = ConfigType.TOGGLE,
            UAYOR = false
    )
    public static boolean assumeFirstStrike = true;

    @AddConfig(
            name = "ALT+C Executor",
            description = "Allows you to quickly run sb commands similar to ALT+TAB. This is UAYOR because this is technically a chat macro.",
            category = "General",
            subCategory = "Quality of Life",
            type = ConfigType.TOGGLE,
            UAYOR = true
    )
    public static boolean allowALTC = false;

    @AddConfig(
            name = "Custom Pets Menu",
            description = "Enables the Custom Pet Menu. This is UAYOR because the mod is (technically) clicking for you.",
            category = "General",
            subCategory = "Pets Menu (UAYOR)",
            type = ConfigType.TOGGLE,
            UAYOR = true
    )
    public static boolean customPetsMenu = false;

    @AddConfig(
            name = "Favorite Pets",
            description = "Enables the ability to favorite pets in the menu.",
            category = "General",
            subCategory = "Pets Menu (UAYOR)",
            type = ConfigType.TOGGLE,
            UAYOR = true
    )
    public static boolean customPetsMenuFavorite = false;

    @AddConfig(
            name = "Show Pet Lore",
            description = "Enables the ability to show hovered pet lore.",
            category = "General",
            subCategory = "Pets Menu (UAYOR)",
            type = ConfigType.TOGGLE,
            UAYOR = true
    )
    public static boolean customPetsMenuShowPetLore = false;

    /* Fishing */

    /* Fishing - Worm Fishing */
    @AddConfig(
            name = "Worm Fishing Counter",
            description = "Counts Worms Caught & Worm Membrane.",
            category = "Fishing",
            subCategory = "Worm Fishing",
            type = ConfigType.TOGGLE,
            UAYOR = false
    )
    public static boolean wormCounter = true;

    @AddConfig(
            name = "Worm Fishing Timer",
            description = "Helps you with Worm Fishing by displaying timers. Requires Timers Hud enabled.",
            category = "Fishing",
            subCategory = "Worm Fishing",
            type = ConfigType.TOGGLE,
            UAYOR = false
    )
    public static boolean wormTimer = true;

    @AddConfig(
            name = "Worm Fishing Reminder",
            description = "Reminder text when worm fishing timer is completed.",
            category = "Fishing",
            subCategory = "Worm Fishing",
            type = ConfigType.INPUT_FIELD,
            UAYOR = false
    )
    public static String wormFishText = "Kill Worms Timer!";

    /* Dungeons */

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

    /* Slayers - General */
    @AddConfig(
            name = "RNGesus Meter",
            description = "Always displays the RNGesus Meter on your screen.",
            category = "Slayers",
            subCategory = "General",
            type = ConfigType.TOGGLE,
            UAYOR = false
    )
    public static boolean rngMeter = false;

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










    /* Persistent Variables */
    @AddConfig(
            name = "Favorite Pets/S",
            description = "favorite pets",
            hidden = true,
            category = "Persistent Variables",
            type = ConfigType.INPUT_FIELD,
            subCategory = "---",
            UAYOR = false
    )
    public static String favoritedPets = "";

    @AddConfig(
            name = "Worm Fishing - Worms Caught",
            description = "All worms caught.",
            hidden = true,
            category = "Persistent Variables",
            subCategory = "---",
            type = ConfigType.INTEGER_SLIDER,
            UAYOR = false
    )
    public static int wormsCaught = 0;

    @AddConfig(
            name = "Worm Fishing - Worms Collected",
            description = "All worms collected.",
            hidden = true,
            category = "Persistent Variables",
            subCategory = "---",
            type = ConfigType.INTEGER_SLIDER,
            UAYOR = false
    )
    public static int wormsCollected = 0;


    /* HUD stuff at end */

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
            name = "Worms Counter",
            description = "Worm Counter",
            category = "HUD",
            type = ConfigType.HUD,
            subCategory = "",
            requiredElementToggled = "wormCounter",
            UAYOR = false
    )
    public static HudElement wormCounterHUD = new HudElement(30, 30, 100, 50);

    @AddConfig(
            name = "Skyblock Timers Hud",
            description = "Skyblock Timers HUD",
            category = "HUD",
            type = ConfigType.HUD,
            subCategory = "",
            requiredElementToggled = "allowTimers",
            UAYOR = false
    )
    public static HudElement timersHUD = new HudElement(50, 50, 100, 40);

    @AddConfig(
            name = "RNG Meter HUD",
            description = "rngesus meter",
            category = "HUD",
            type = ConfigType.HUD,
            subCategory = "",
            requiredElementToggled = "rngMeter",
            UAYOR = false
    )
    public static HudElement rngesusmeter = new HudElement(50, 30, 190, 30);

    @AddConfig(
            name = "Damage Meter HUD",
            description = "damage per second",
            category = "HUD",
            type = ConfigType.HUD,
            subCategory = "",
            requiredElementToggled = "damageMeter",
            UAYOR = false
    )
    public static HudElement damageMeterHud = new HudElement(50, 30, 190, 50);
}
