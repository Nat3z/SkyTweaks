package natia.skytweaks.networking

import natia.skytweaks.SkyTweaks
import mixin.natia.skytweaks.SkyTweaksConfig
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.IChatComponent
import net.minecraft.util.StringUtils
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderLivingEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

import java.util.ArrayList
import java.util.HashMap
import java.util.regex.Pattern

import natia.skytweaks.utils.WebUtils.fetch

class ColorText {
    private val cache = HashMap<String, String>()
    private val supporters = ArrayList<String>()

    init {
        /* caching & supporters! */
        fetch("https://skytweaks-web.vercel.app/userswithcolor") { res ->
            SkyTweaks.LOGGER.info("List of usernames who bought color cosmetic:")
            for (supporter in res.asJson().get("user").asString.split("|")) {
                if (supporter == "") continue
                SkyTweaks.LOGGER.info("- $supporter")
                supporters.add(supporter)
                if (!cache.containsKey(supporter)) {
                    fetch("https://skytweaks-web.vercel.app/usercolor?name=$supporter") { res1 ->
                        cache[supporter] = res1.asString()
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onLivingRender(event: RenderLivingEvent.Specials.Pre<EntityLivingBase>) {
        if (SkyTweaksConfig.colorCosmetic) return

        val entity = event.entity
        if (entity.hasCustomName()) {
            for (name in supporters) {
                if (entity.customNameTag.contains(name)) {
                    entity.customNameTag = getColor(entity.customNameTag, name).formattedText
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun changeText(event: ClientChatReceivedEvent) {
        if (!SkyTweaksConfig.colorCosmetic) return
        if (event.type.toInt() == 0) {
            var sentComponent = event.message

            if (!event.message.unformattedText.contains("-----------------")) {
                for (name in supporters) {
                    if (event.message.unformattedText.contains(name)) {
                        sentComponent = replaceChat(sentComponent, name)
                    }
                }
                event.message = sentComponent
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun modifyItemTooltip(event: ItemTooltipEvent) {
        if (!SkyTweaksConfig.colorCosmetic) return
        for (tip in event.toolTip) {
            var formattedTip = tip
            for (name in supporters) {
                if (StringUtils.stripControlCodes(tip).contains(name)) {
                    formattedTip = getColor(formattedTip, name).formattedText
                }
            }
            event.toolTip[event.toolTip.indexOf(tip)] = formattedTip

        }
    }

    private fun replaceChat(component: IChatComponent, user: String): IChatComponent {
        val newComponent: IChatComponent
        val text = component as ChatComponentText

        newComponent = getColor(text.unformattedTextForChat, user)
        newComponent.setChatStyle(text.chatStyle.createShallowCopy())

        for (sibling in text.siblings) {
            newComponent.appendSibling(replaceChat(sibling, user))
        }

        return newComponent
    }

    private fun getColor(formattedText: String, name: String): ChatComponentText {
        when (cache[name]) {
            "gold" -> return ChatComponentText(formattedText.replace(name, EnumChatFormatting.GOLD.toString() + "" + name + EnumChatFormatting.RESET))

            "black" -> return ChatComponentText(formattedText.replace(name, EnumChatFormatting.BLACK.toString() + "" + name + EnumChatFormatting.RESET))

            "blue" -> return ChatComponentText(formattedText.replace(name, EnumChatFormatting.BLUE.toString() + "" + name + EnumChatFormatting.RESET))

            "purple" -> return ChatComponentText(formattedText.replace(name, EnumChatFormatting.DARK_PURPLE.toString() + "" + name + EnumChatFormatting.RESET))

            "red" -> return ChatComponentText(formattedText.replace(name, EnumChatFormatting.RED.toString() + "" + name + EnumChatFormatting.RESET))
        }

        return ChatComponentText(formattedText)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun renderTags(event: PlayerEvent.NameFormat) {
        if (!SkyTweaksConfig.colorCosmetic) return

        if (supporters == null) return

        val formattedText = event.username
        for (name in supporters) {
            if (event.username.contains(name)) {
                event.displayname = getColor(formattedText, name).formattedText
                break
            }
        }
    }
}
