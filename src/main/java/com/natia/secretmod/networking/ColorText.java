package com.natia.secretmod.networking;

import com.natia.secretmod.config.SkyTweaksConfig;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import static com.natia.secretmod.utils.WebUtils.fetch;

public class ColorText {
    private HashMap<String, String> cache = new HashMap<>();
    private List<String> supporters = new ArrayList<>();
    public ColorText() {
        /* caching & supporters! */
        fetch("https://secretmod-hypixel.herokuapp.com/userswithcolor", res -> {
            System.out.println("List of usernames who bought color cosmetic:");
            for (String supporter: res.asJson().get("user").getAsString().split(Pattern.quote("|"))) {
                if (supporter.equals("")) continue;
                System.out.println("- " + supporter);
                supporters.add(supporter);
                if (!cache.containsKey(supporter)) {
                    fetch("https://secretmod-hypixel.herokuapp.com/usercolor?name=" + supporter, res1 -> {
                        if (res1 != null) {
                            cache.put(supporter, res1.asString());
                        }
                    });
                }
            }
        });
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void changeText(ClientChatReceivedEvent event) {
        if (!SkyTweaksConfig.colorCosmetic) return;
        if (event.type == 0) {
            IChatComponent sentComponent = event.message;

            if (!event.message.getUnformattedText().contains("-----------------")) {
                String formattedText = sentComponent.getFormattedText();

                for (String name : supporters) {
                    if (event.message.getUnformattedText().contains(name)) {
                        sentComponent = getColor(formattedText, name);
                        formattedText = sentComponent.getFormattedText();
                    }
                }

                event.message = sentComponent;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void modifyItemTooltip(ItemTooltipEvent event) {
        if (!SkyTweaksConfig.colorCosmetic) return;
        for (String tip : event.toolTip) {
            String formattedTip = tip;
            for (String name : supporters) {
                if (StringUtils.stripControlCodes(tip).contains(name)) {
                    formattedTip = getColor(formattedTip, name).getFormattedText();
                }
            }
            event.toolTip.set(event.toolTip.indexOf(tip), formattedTip);

        }
    }

    private ChatComponentText getColor(String formattedText, String name) {
        switch (cache.get(name)) {
            case "gold":
                return new ChatComponentText(formattedText.replace(name, EnumChatFormatting.GOLD + "" + name + EnumChatFormatting.RESET));

            case "black":
                return new ChatComponentText(formattedText.replace(name,EnumChatFormatting.BLACK + "" + name + EnumChatFormatting.RESET));

            case "blue":
                return new ChatComponentText(formattedText.replace(name,EnumChatFormatting.BLUE + "" + name + EnumChatFormatting.RESET));

            case "purple":
                return new ChatComponentText(formattedText.replace(name,EnumChatFormatting.DARK_PURPLE + "" + name + EnumChatFormatting.RESET));

            case "red":
                return new ChatComponentText(formattedText.replace(name,EnumChatFormatting.RED + "" + name + EnumChatFormatting.RESET));
        }

        return new ChatComponentText(formattedText);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void renderTags(PlayerEvent.NameFormat event) {
        if (!SkyTweaksConfig.colorCosmetic) return;

        if (supporters == null) return;

        String formattedText = event.username;
        for (String name : supporters) {
            if (event.username.contains(name)) {
                event.displayname = getColor(formattedText, name).getFormattedText();
                break;
            }
        }
    }
}
