package mixin.natia.skytweaks;

import cc.blendingMC.vicious.BlendingConfig;
import cc.blendingMC.vicious.HudElement;
import cc.blendingMC.vicious.SerializeField;
import cc.blendingMC.vicious.SerializeType;
import natia.skytweaks.config.HudPreviews;

import java.awt.*;

public class SkyTweaksConfig implements BlendingConfig {

    /* General - --- */
    @SerializeField(
            name = "Hypixel API Key",
            description = "Your Hypixel API Key. Required for some API-based modules.",
            category = "General",
            subCategory = "---",
            type = SerializeType.INPUT_FIELD,
            UAYOR = false
    )
    public static String apiKey = "";

    @SerializeField(
            name = "Colored Name Cosmetic",
            description = "Allows overriding text messages with colors if a player has bought the color cosmetic.",
            category = "General",
            subCategory = "---",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean colorCosmetic = true;

    @SerializeField(
            name = "Stop Log4J Exploit (Log4Shell)",
            description = "Stops logging chat messages in Minecraft. This is the MAIN reason why Log4J works.",
            category = "General",
            subCategory = "---",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean stopLog4J = true;

    @SerializeField(
            name = "Cache Bazaar",
            description = "Allow looping every 15-20s through the bazaar. Will (maybe) reduce ping/lag.",
            category = "General",
            subCategory = "---",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean bazaarCaching = true;

    /* General - Quality of Life */

    @SerializeField(
            name = "Alert Rare Drops",
            description = "Alert all rare drops that do not already have a notification.",
            category = "General",
            subCategory = "Quality of Life",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean alertRareDrops = true;

    @SerializeField(
            name = "Lobby Day Notifier",
            description = "Alerts the current day of the lobby you are in everytime you swap.",
            category = "General",
            subCategory = "Quality of Life",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean serverDayNotifier = true;

    @SerializeField(
            name = "Skyblock Timers",
            description = "Shows Timers for Skyblock events. Required for some modules to work.",
            category = "General",
            subCategory = "Quality of Life",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean allowTimers = false;

    @SerializeField(
            name = "Trade GUI Helper",
            description = "A helper that shows bad trades and also possible scams.",
            category = "General",
            subCategory = "Quality of Life",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean tradeGui = false;

    @SerializeField(
            name = "Don't Render Players in Hub",
            description = "Stops rendering players in Hub. The only exception is NPCS.",
            category = "General",
            subCategory = "Quality of Life",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean dontRenderPlayersInHub = false;

    @SerializeField(
            name = "Minion Analyzer",
            description = "Analyzes minion contents and calculates the amount of coins in it.",
            category = "General",
            subCategory = "Quality of Life",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean minionAnalyzer = false;

    @SerializeField(
            name = "Damage Meter",
            description = "Gets damage dealt per second. (NOT VERY GOOD IF API OFF)",
            category = "General",
            subCategory = "Quality of Life",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean damageMeter = false;

    @SerializeField(
            name = "Assume First Strike ALWAYS",
            description = "Always assumes that every time you hit that it's the first.",
            category = "General",
            subCategory = "Quality of Life",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean assumeFirstStrike = true;

    @SerializeField(
            name = "ALT+C Executor",
            description = "Allows you to quickly run sb commands similar to ALT+TAB. This is UAYOR because this is technically a chat macro.",
            category = "General",
            subCategory = "Quality of Life",
            type = SerializeType.TOGGLE,
            UAYOR = true
    )
    public static boolean allowALTC = false;

    @SerializeField(
            name = "Custom Pets Menu",
            description = "Enables the Custom Pet Menu. This is UAYOR because the mod is (technically) clicking for you.",
            category = "General",
            subCategory = "Pets Menu (UAYOR)",
            type = SerializeType.TOGGLE,
            UAYOR = true
    )
    public static boolean customPetsMenu = false;

    @SerializeField(
            name = "Favorite Pets",
            description = "Enables the ability to favorite pets in the menu.",
            category = "General",
            subCategory = "Pets Menu (UAYOR)",
            type = SerializeType.TOGGLE,
            UAYOR = true
    )
    public static boolean customPetsMenuFavorite = false;

    @SerializeField(
            name = "Show Pet Lore",
            description = "Enables the ability to show hovered pet lore.",
            category = "General",
            subCategory = "Pets Menu (UAYOR)",
            type = SerializeType.TOGGLE,
            UAYOR = true
    )
    public static boolean customPetsMenuShowPetLore = false;

    /* Fishing */

    /* Fishing - Worm Fishing */
    @SerializeField(
            name = "Worm Fishing Counter",
            description = "Counts Worms Caught & Worm Membrane.",
            category = "Fishing",
            subCategory = "Worm Fishing",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean wormCounter = false;

    @SerializeField(
            name = "Worm Fishing Timer",
            description = "Helps you with Worm Fishing by displaying timers. Requires Timers Hud enabled.",
            category = "Fishing",
            subCategory = "Worm Fishing",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean wormTimer = true;

    @SerializeField(
            name = "Worm Fishing Reminder",
            description = "Reminder text when worm fishing timer is completed.",
            category = "Fishing",
            subCategory = "Worm Fishing",
            type = SerializeType.INPUT_FIELD,
            UAYOR = false
    )
    public static String wormFishText = "Kill Worms Timer!";

    /* Dungeons */

    @SerializeField(
            name = "Spirit/Bonzo Timers",
            description = "Displays a timer which shows how long the cooldown for the spirit/bonzo mask is.",
            category = "Dungeons",
            subCategory = "Dungeons",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean maskTimers = true;

    @SerializeField(
            name = "Copy Fails/Deaths",
            description = "Automatically copies dungeon fails AND dungeon deaths.",
            category = "Dungeons",
            subCategory = "Dungeons",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean copyFails = true;

    @SerializeField(
            name = "Point Bats",
            description = "Adds a compass that points to bats.",
            category = "Dungeons",
            subCategory = "Dungeons",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean pointBats = false;

    @SerializeField(
            name = "Point Bats Color",
            description = "Color of the compass that points to bats.",
            category = "Dungeons",
            subCategory = "Dungeons",
            type = SerializeType.COLOR_WHEEL,
            UAYOR = false
    )
    public static int pointBatsColor = new Color(50, 50, 50).getRGB();

    /*@SerializeField(
            name = "Highlight Inactive Terminals",
            description = "Highlights all skipped/uncompleted terminals in F7. UAYOR because you can see blocks through walls. (NOT TESTED)",
            category = "Dungeons",
            subCategory = "Dungeons",
            type = SerializeType.TOGGLE,
            UAYOR = true
    )
    public static boolean terminalHighlight = false;

    @SerializeField(
            name = "Highlight Terminals Color - Inactive",
            description = "Color of Inactive/Inactivated Terminals.",
            category = "Dungeons",
            subCategory = "Dungeons",
            type = SerializeType.COLOR_WHEEL,
            UAYOR = false
    )
    public static int terminalHighlightColorINACTIVE = Color.RED.getRGB();

    @SerializeField(
            name = "Highlight Terminals Color - Active",
            description = "Color of Activated Terminals.",
            category = "Dungeons",
            subCategory = "Dungeons",
            type = SerializeType.COLOR_WHEEL,
            UAYOR = false
    )
    public static int terminalHighlightColorACTIVE = Color.GREEN.getRGB();
    */

    @SerializeField(
            name = "Bat Died",
            description = "Notifies the client when a bat was killed in the dungeon.",
            category = "Dungeons",
            subCategory = "Dungeons",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean batdead = false;

    @SerializeField(
            name = "Reparty Command",
            description = "Automatically reparties all party members. (/rp) This is UAYOR because this is technically a chat macro.",
            category = "Dungeons",
            subCategory = "Dungeons",
            type = SerializeType.TOGGLE,
            UAYOR = true
    )
    public static boolean rpCommand = false;



    /* Slayers */

    /* Slayers - General */
    @SerializeField(
            name = "RNGesus Meter",
            description = "Always displays the RNGesus Meter on your screen.",
            category = "Slayers",
            subCategory = "General",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean rngMeter = false;

    @SerializeField(
            name = "Slayer Time Notification",
            description = "Shows the slayer time after the completion of a quest.",
            category = "Slayers",
            subCategory = "General",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean slayerTimeNotify = false;

    @SerializeField(
            name = "Copy RNGesus",
            description = "Copies every RNGesus drop that you have received.",
            category = "Slayers",
            subCategory = "General",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean copyRNG = false;

    /* Slayers - Voidgloom Seraph */
    @SerializeField(
            name = "Voidgloom Seraph Assist",
            description = "Assists you in the fight with the Voidgloom Seraph.",
            category = "Slayers",
            subCategory = "Voidgloom Seraph",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean seraphHelper = false;

    @SerializeField(
            name = "Force Voidgloom Seraph",
            description = "Forces the Voidgloom Seraph module to be enabled.",
            category = "Slayers",
            subCategory = "Voidgloom Seraph",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean forceSeraph = false;

    @SerializeField(
            name = "Color Voidgloom Seraph",
            description = "Assists you in the fight by coloring it when it is in a certain phase.",
            category = "Slayers",
            subCategory = "Voidgloom Seraph",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean seraphColorize = false;

    @SerializeField(
            name = "Skull Highlighter",
            description = "Highlights the Nukekubi Fixations.",
            category = "Slayers",
            subCategory = "Voidgloom Seraph",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean skullHighlights = false;

    @SerializeField(
            name = "Skull Highlight Color",
            description = "Highlight color of the Nukekubi Fixations.",
            category = "Slayers",
            subCategory = "Voidgloom Seraph",
            type = SerializeType.COLOR_WHEEL,
            UAYOR = false
    )
    public static int skullHighlightColor = new Color(164, 2, 17).getRed();

    /* beacons */
    @SerializeField(
            name = "Yang Glyph Notifier",
            description = "Notifies and highlights the exploding beacon.",
            category = "Slayers",
            subCategory = "Voidgloom Seraph",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean yangGlyphHighlights = false;

    @SerializeField(
            name = "Yang Glyph Highlight Color",
            description = "Highlight color of the YANG GLYPH.",
            category = "Slayers",
            subCategory = "Voidgloom Seraph",
            type = SerializeType.COLOR_WHEEL,
            UAYOR = false
    )
    public static int yangGlyphHighlightColor = new Color(0, 101, 164).getRGB();

    @SerializeField(
            name = "Yang Glyph Compass Color",
            description = "Highlight color of the YANG GLYPH compass.",
            category = "Slayers",
            subCategory = "Voidgloom Seraph",
            type = SerializeType.COLOR_WHEEL,
            UAYOR = false
    )
    public static int yangGlyphCompassColor = new Color(6, 222, 68).getRGB();

    @SerializeField(
            name = "Yang Glyph Notify Type",
            description = "Type of notification for yang glyph.",
            category = "Slayers",
            subCategory = "Voidgloom Seraph",
            type = SerializeType.SLIDER_TEXT,
            sliderChoices = {"Both", "Highlight Only", "Notification Only"},
            UAYOR = false
    )
    public static String yangGlyphHighlightType = "Both";


    /* Special */

    /* Special - Mythos */
    @SerializeField(
            name = "Diana Waypoints",
            description = "Displays waypoints for burrows in the Diana event.",
            category = "Special",
            subCategory = "Mythological Rituals",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean mythosWaypoints = false;

    @SerializeField(
            name = "Optimize Diana Waypoints",
            description = "Detects if the Griffin burrow chain is uneven and if so only display optimized Griffin burrows.",
            category = "Special",
            subCategory = "Mythological Rituals",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean mythosWaypointOptimize = false;

    @SerializeField(
            name = "Bazaar Overlay",
            description = "An overlay for the Bazaar menu that shows undercut sell orders and other valuable info.",
            category = "Special",
            subCategory = "Bazaar",
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean bazaarOverlay = false;



    /* Persistent Variables */
    @SerializeField(
            name = "Favorite Pets/S",
            description = "favorite pets",
            hidden = true,
            category = "Persistent Variables",
            type = SerializeType.INPUT_FIELD,
            subCategory = "---",
            UAYOR = false
    )
    public static String favoritedPets = "";

    @SerializeField(
            name = "Worm Fishing - Worms Caught",
            description = "All worms caught.",
            hidden = true,
            category = "Persistent Variables",
            subCategory = "---",
            type = SerializeType.INTEGER_SLIDER,
            UAYOR = false
    )
    public static int wormsCaught = 0;

    @SerializeField(
            name = "Worm Fishing - Worms Collected",
            description = "All worms collected.",
            hidden = true,
            category = "Persistent Variables",
            subCategory = "---",
            type = SerializeType.INTEGER_SLIDER,
            UAYOR = false
    )
    public static int wormsCollected = 0;

    /* Bazaar Overlay */
    @SerializeField(
            name = "Bazaar Overlay HUD",
            description = "Bazaar Overlay",
            category = "Overlay",
            subCategory = "General",
            type = SerializeType.HUD,
            UAYOR = false,
            hidden = true
    )
    public static HudElement bazaarOverlayHUD = new HudElement(190, 100, 190, 100, null);

    @SerializeField(
            name = "Bazaar Leaderboard HUD",
            description = "Bazaar Overlay",
            category = "Overlay",
            subCategory = "General",
            type = SerializeType.HUD,
            UAYOR = false,
            hidden = true
    )
    public static HudElement bazaarLeaderboardHUD = new HudElement(20, 20, 190, 100, null);

    @SerializeField(
            name = "Trade Log HUD",
            description = "Trade HUD",
            category = "HUD",
            type = SerializeType.HUD,
            subCategory = "",
            UAYOR = false,
            hidden = true
    )
    public static HudElement tradeLogHUD = new HudElement(0, 0, 190, 100, new HudPreviews.VoidgloomAssist());

    /* HUD stuff at end */

    @SerializeField(
            name = "Voidgloom Assist",
            description = "Voidgloom HUD",
            category = "HUD",
            type = SerializeType.HUD,
            subCategory = "",
            requiredElementToggled = "seraphHelper",
            UAYOR = false
    )
    public static HudElement voidgloomHUD = new HudElement(0, 0, 190, 100, new HudPreviews.VoidgloomAssist());

    @SerializeField(
            name = "Worms Counter",
            description = "Worm Counter",
            category = "HUD",
            type = SerializeType.HUD,
            subCategory = "",
            requiredElementToggled = "wormCounter",
            UAYOR = false
    )
    public static HudElement wormCounterHUD = new HudElement(30, 30, 100, 50, new HudPreviews.WormCounter());

    @SerializeField(
            name = "Skyblock Timers Hud",
            description = "Skyblock Timers HUD",
            category = "HUD",
            type = SerializeType.HUD,
            subCategory = "",
            requiredElementToggled = "allowTimers",
            UAYOR = false
    )
    public static HudElement timersHUD = new HudElement(50, 50, 100, 40, new HudPreviews.SkyblockTimers());

    @SerializeField(
            name = "RNG Meter HUD",
            description = "rngesus meter",
            category = "HUD",
            type = SerializeType.HUD,
            subCategory = "",
            requiredElementToggled = "rngMeter",
            UAYOR = false
    )
    public static HudElement rngesusmeter = new HudElement(50, 30, 190, 30, new HudPreviews.RNGesusMeter());

    @SerializeField(
            name = "Damage Meter HUD",
            description = "damage per second",
            category = "HUD",
            type = SerializeType.HUD,
            subCategory = "",
            requiredElementToggled = "damageMeter",
            UAYOR = false
    )
    public static HudElement damageMeterHud = new HudElement(50, 30, 190, 50, new HudPreviews.DamageMeter());
}