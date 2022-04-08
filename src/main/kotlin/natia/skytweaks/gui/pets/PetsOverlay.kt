package natia.skytweaks.gui.pets

import natia.skytweaks.SecretUtils
import mixin.natia.skytweaks.SkyTweaksConfig
import natia.skytweaks.gui.base.SkyblockButton
import natia.skytweaks.gui.base.SkyblockGui
import natia.skytweaks.utils.ItemUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.inventory.ContainerChest
import net.minecraft.item.Item
import net.minecraft.util.StringUtils
import org.lwjgl.input.Mouse

import java.awt.*
import java.util.ArrayList
import java.util.regex.Pattern

class PetsOverlay : SkyblockGui() {

    private val mc = Minecraft.getMinecraft()

    private var autoPet = 0
    private var hidePets = 0
    private var pickupPets = 0

    private var updatePets = 0
    private var currentsr = ScaledResolution(mc)
    private val alreadyShownFavoritePets = ArrayList<String>()

    var shownFavorite = false

    private var addedPets = false

    override fun render(container: ContainerChest) {
        val sr = ScaledResolution(mc)

        if (sr.scaleFactor != currentsr.scaleFactor) {
            refreshPetPage()
            currentsr = sr
        }

        drawPetsBox(container)
        if (SkyTweaksConfig.customPetsMenuFavorite)
            drawFavoritePets(container)

        Gui.drawRect(0, 0, sr.scaledWidth, sr.scaledHeight, Color(255, 255, 255, 103).rgb)
        super.render(container)

    }

    private fun drawFavoritePets(container: ContainerChest) {
        val sr = ScaledResolution(mc)

        val width = SkyTweaksConfig.favoritedPets.split("@").size * 45 + 10
        val height = 40

        val uiX = (sr.scaledWidth - width) / 2

        val uiY = 30
        /* selects */
        val textureWidth = 45
        val textureHeight = 45
        val itemX = -20
        Gui.drawRect(uiX, uiY, uiX + width, uiY + height, Color(255, 174, 0, 157).rgb)
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)

        if (!shownFavorite) {
            var petX = -30
            val petY = 0
            for (i in 10..43) {
                val pet = container.lowerChestInventory.getStackInSlot(i) ?: continue
                if (pet.item == Item.getItemFromBlock(Blocks.stained_glass_pane)) continue
                if (!SkyTweaksConfig.favoritedPets.contains(StringUtils.stripControlCodes(pet.displayName).split("]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].replaceFirst(" ".toRegex(), ""))) continue
                if (alreadyShownFavoritePets.contains(StringUtils.stripControlCodes(pet.displayName))) continue

                alreadyShownFavoritePets.add(StringUtils.stripControlCodes(pet.displayName))
                petX += 50

                val level = Integer.parseInt(StringUtils.stripControlCodes(pet.displayName).split(Pattern.quote("]").toRegex(), 2).toTypedArray()[0].replace("[Lvl ", ""))
                buttonList.add(
                        Pet(pet, true, level, i, i, (petX + uiX).toInt(), uiY + petY + 5)
                )
            }
        }
    }

    private fun drawPetsBox(container: ContainerChest) {
        val sr = ScaledResolution(mc)

        val width = 380
        val height = 300

        val uiX = (sr.scaledWidth - width) / 2 - 30
        val uiY = sr.scaledHeight - height
        /* selects */
        var petX = -60
        var petY = 0
        if (!addedPets) {
            addedPets = true
            if (container.lowerChestInventory.sizeInventory > 36) {
                for (i in 10..43) {
                    val pet = container.lowerChestInventory.getStackInSlot(i) ?: continue
                    if (pet.item == Item.getItemFromBlock(Blocks.stained_glass_pane)) continue
                    petX += 60

                    /* prevent overflow */
                    if (petX + uiX > uiX + width) {
                        petY += 50
                        petX = 0
                    }
                    val level = Integer.parseInt(StringUtils.stripControlCodes(pet.displayName).split(Pattern.quote("]").toRegex(), 2).toTypedArray()[0].replace("[Lvl ", ""))
                    buttonList.add(
                            Pet(pet, false, level, i, i, petX + uiX + 10, uiY + petY + 20)
                    )
                }
            }
            if (buttonList.isEmpty()) {
                buttonList.add(SkyblockButton(
                        0, 10, sr.scaledHeight - 30, 150, 20, "Refresh Pets Menu"
                ))
                return
            }

            /* other button cycles */
            updatePets = buttonList[buttonList.size - 1].id
            autoPet = buttonList[buttonList.size - 1].id + 1
            pickupPets = buttonList[buttonList.size - 1].id + 2
            hidePets = buttonList[buttonList.size - 1].id + 3

            buttonList.add(SkyblockButton(
                    updatePets, 10, sr.scaledHeight - 30, 150, 20, "Refresh Pets Menu"
            ))

            if (container.lowerChestInventory.getStackInSlot(46) == null || container.lowerChestInventory.sizeInventory < 46) {
                refreshPetPage()
                return
            }
            /* just to make things easier :) */
            run {
                var dynamicX = 10
                if (container.lowerChestInventory.getStackInSlot(46).item == Items.skull) {
                    buttonList.add(ItemButton(
                            container.lowerChestInventory.getStackInSlot(46), 46,
                            autoPet, dynamicX, 40,
                            ItemUtils.getLore(container.lowerChestInventory.getStackInSlot(46))
                    ))
                    dynamicX += 30
                }

                if (container.lowerChestInventory.getStackInSlot(50) != null) {
                    buttonList.add(ItemButton(
                            container.lowerChestInventory.getStackInSlot(50), 50,
                            pickupPets, dynamicX, 40,
                            ItemUtils.getLore(container.lowerChestInventory.getStackInSlot(50))
                    ))
                    dynamicX += 30
                }

                if (container.lowerChestInventory.getStackInSlot(51) != null) {
                    buttonList.add(ItemButton(
                            container.lowerChestInventory.getStackInSlot(51), 51,
                            hidePets, dynamicX, 40,
                            ItemUtils.getLore(container.lowerChestInventory.getStackInSlot(51))
                    ))
                    dynamicX += 30
                }

                if (container.lowerChestInventory.getStackInSlot(45) != null && StringUtils.stripControlCodes(container.lowerChestInventory.getStackInSlot(45).displayName)
                                .contains("Last")) {
                    buttonList.add(ItemButton(
                            container.lowerChestInventory.getStackInSlot(45), 45,
                            hidePets, dynamicX, 40,
                            ItemUtils.getLore(container.lowerChestInventory.getStackInSlot(45))
                    ))
                    dynamicX += 30
                }

                if (container.lowerChestInventory.getStackInSlot(53) != null && StringUtils.stripControlCodes(container.lowerChestInventory.getStackInSlot(45).displayName)
                                .contains("Next")) {
                    buttonList.add(ItemButton(
                            container.lowerChestInventory.getStackInSlot(53), 53,
                            hidePets, dynamicX, 40,
                            ItemUtils.getLore(container.lowerChestInventory.getStackInSlot(53))
                    ))
                }
            }
        }
    }

    fun addPetToFavorites(petName: String) {
        if (SkyTweaksConfig.favoritedPets.split("@").size > 7) {
            SecretUtils.sendMessage("You have reached the maximum amount of favorite pets.")
            return
        }
        if (!SkyTweaksConfig.favoritedPets.contains(StringUtils.stripControlCodes(petName).split("]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].replaceFirst(" ".toRegex(), "")))
            SkyTweaksConfig.favoritedPets += "@" + StringUtils.stripControlCodes(petName).split("]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].replaceFirst(" ".toRegex(), "")
    }

    fun removePetFromFavorites(petName: String) {
        SkyTweaksConfig.favoritedPets = SkyTweaksConfig.favoritedPets.replace("@" + StringUtils.stripControlCodes(petName).split("]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].replaceFirst(" ".toRegex(), ""), "")
    }

    fun refreshPetPage() {
        shownFavorite = false
        addedPets = false
        buttonList.clear()
        alreadyShownFavoritePets.clear()
    }

    override fun onGuiClosed() {

    }

    override fun actionPreformed(button: SkyblockButton) {
        super.actionPreformed(button)

        if (button is Pet && Mouse.getEventButton() == 1) {
            if (!SkyTweaksConfig.customPetsMenuFavorite) return
            if (button.isFavorite)
                removePetFromFavorites(button.petItem.displayName)
            else
                addPetToFavorites(button.petItem.displayName)
            refreshPetPage()
            SecretUtils.playLoudSound("random.orb", 0.5f)
        } else {
            if (button is Pet) {
                clickSlotOnContainer(button.slotNumber)
            } else if (button is ItemButton) {
                clickSlotOnContainer(button.slotNumber)
                refreshPetPage()
            } else if (button.id == updatePets) {
                refreshPetPage()
                SecretUtils.playLoudSound("random.orb", 0.5f)
            }
        }
    }
}
