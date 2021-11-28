package com.natia.secretmod.gui.pets;

import com.natia.secretmod.SecretUtils;
import com.natia.secretmod.config.SkyTweaksConfig;
import com.natia.secretmod.gui.base.SkyblockButton;
import com.natia.secretmod.gui.base.SkyblockGui;
import com.natia.secretmod.utils.ItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class PetsOverlay extends SkyblockGui {

    private Minecraft mc = Minecraft.getMinecraft();

    private int autoPet = 0;
    private int hidePets = 0;
    private int pickupPets = 0;

    private int updatePets = 0;
    private ScaledResolution currentsr = new ScaledResolution(mc);
    private List<String> alreadyShownFavoritePets = new ArrayList<>();

    public PetsOverlay() {

    }

    @Override
    public void render(ContainerChest container) {
        ScaledResolution sr = new ScaledResolution(mc);

        if (sr.getScaleFactor() != currentsr.getScaleFactor()) {
            refreshPetPage();
            currentsr = sr;
        }

        drawPetsBox(container);
        if (SkyTweaksConfig.customPetsMenuFavorite)
            drawFavoritePets(container);

        Gui.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(255, 255, 255, 103).getRGB());
        super.render(container);

    }

    public boolean shownFavorite = false;

    private void drawFavoritePets(ContainerChest container) {
        ScaledResolution sr = new ScaledResolution(mc);

        int width = ((SkyTweaksConfig.favoritedPets.split("@").length) * 45) + 10;
        int height = 40;

        int uiX = (sr.getScaledWidth() - width) / 2;

        int uiY = 30;
        /* selects */
        int textureWidth = 45;
        int textureHeight = 45;
        int itemX = -20;
        Gui.drawRect(uiX, uiY, uiX + width, uiY + height, new Color(255, 174, 0, 157).getRGB());
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        if (!shownFavorite) {
            int petX = -30;
            int petY = 0;
            for (int i = 10; i < 44; i++) {
                ItemStack pet = container.getLowerChestInventory().getStackInSlot(i);
                if (pet == null) continue;
                if (pet.getItem().equals(Item.getItemFromBlock(Blocks.stained_glass_pane))) continue;
                if (!SkyTweaksConfig.favoritedPets.contains(StringUtils.stripControlCodes(pet.getDisplayName()).split("]")[1].replaceFirst(" ", ""))) continue;
                if (alreadyShownFavoritePets.contains(StringUtils.stripControlCodes(pet.getDisplayName()))) continue;

                alreadyShownFavoritePets.add(StringUtils.stripControlCodes(pet.getDisplayName()));
                petX += 50;

                int level = Integer.parseInt(StringUtils.stripControlCodes(pet.getDisplayName()).split(Pattern.quote("]"), 2)
                        [0].replace("[Lvl ", ""));
                buttonList.add(
                        new Pet(pet, true, level, i, i, (int) (petX + uiX), (int) (uiY + petY) + 5)
                );
            }
        }
    }

    private boolean addedPets = false;
    private void drawPetsBox(ContainerChest container) {
        ScaledResolution sr = new ScaledResolution(mc);

        int width = 380;
        int height = 300;

        int uiX = ((sr.getScaledWidth() - width) / 2) - 30;
        int uiY = sr.getScaledHeight() - height;
        /* selects */
        int petX = -60;
        int petY = 0;
        if (!addedPets) {
            addedPets = true;
            if (container.getLowerChestInventory().getSizeInventory() > 36) {
                for (int i = 10; i < 44; i++) {
                    ItemStack pet = container.getLowerChestInventory().getStackInSlot(i);
                    if (pet == null) continue;
                    if (pet.getItem().equals(Item.getItemFromBlock(Blocks.stained_glass_pane))) continue;
                    petX += 60;

                    /* prevent overflow */
                    if (petX + uiX > uiX + width) {
                        petY += 50;
                        petX = 0;
                    }
                    int level = Integer.parseInt(StringUtils.stripControlCodes(pet.getDisplayName()).split(Pattern.quote("]"), 2)
                            [0].replace("[Lvl ", ""));
                    buttonList.add(
                            new Pet(pet, false, level, i, i, (int) (petX + uiX) + 10, (int) (uiY + petY) + 20)
                    );
                }
            }
            if (buttonList.isEmpty()) {
                buttonList.add(new SkyblockButton(
                        0, 10, sr.getScaledHeight() - 30, 150, 20, "Refresh Pets Menu"
                ));
                return;
            }

            /* other button cycles */
            updatePets = buttonList.get(buttonList.size() - 1).id;
            autoPet = buttonList.get(buttonList.size() - 1).id + 1;
            pickupPets = buttonList.get(buttonList.size() - 1).id + 2;
            hidePets = buttonList.get(buttonList.size() - 1).id + 3;

            buttonList.add(new SkyblockButton(
                    updatePets, 10, sr.getScaledHeight() - 30, 150, 20, "Refresh Pets Menu"
            ));

            if (container.getLowerChestInventory().getStackInSlot(46) == null || container.getLowerChestInventory().getSizeInventory() < 46) {
                refreshPetPage();
                return;
            }
            /* just to make things easier :) */
            {
                int dynamicX = 10;
                if (container.getLowerChestInventory().getStackInSlot(46).getItem().equals(Items.skull)) {
                    buttonList.add(new ItemButton(
                            container.getLowerChestInventory().getStackInSlot(46), 46,
                            autoPet, dynamicX, 40,
                            ItemUtils.getLore(container.getLowerChestInventory().getStackInSlot(46))
                    ));
                    dynamicX += 30;
                }

                if (container.getLowerChestInventory().getStackInSlot(50) != null) {
                    buttonList.add(new ItemButton(
                            container.getLowerChestInventory().getStackInSlot(50), 50,
                            pickupPets, dynamicX, 40,
                            ItemUtils.getLore(container.getLowerChestInventory().getStackInSlot(50))
                    ));
                    dynamicX += 30;
                }

                if (container.getLowerChestInventory().getStackInSlot(51) != null) {
                    buttonList.add(new ItemButton(
                            container.getLowerChestInventory().getStackInSlot(51), 51,
                            hidePets, dynamicX, 40,
                            ItemUtils.getLore(container.getLowerChestInventory().getStackInSlot(51))
                    ));
                }
            }
        }
    }

    public void addPetToFavorites(String petName) {
        if (SkyTweaksConfig.favoritedPets.split("@").length > 7) {
            SecretUtils.sendMessage("You have reached the maximum amount of favorite pets.");
            return;
        }
        if (!SkyTweaksConfig.favoritedPets.contains(StringUtils.stripControlCodes(petName).split("]")[1].replaceFirst(" ", "")))
            SkyTweaksConfig.favoritedPets += "@" + StringUtils.stripControlCodes(petName).split("]")[1].replaceFirst(" ", "");
    }

    public void removePetFromFavorites(String petName) {
        SkyTweaksConfig.favoritedPets = SkyTweaksConfig.favoritedPets.replace("@" + StringUtils.stripControlCodes(petName).split("]")[1].replaceFirst(" ", ""), "");
    }

    public void refreshPetPage() {
        shownFavorite = false;
        addedPets = false;
        buttonList.clear();
        alreadyShownFavoritePets.clear();
    }

    @Override
    public void onGuiClosed() {

    }

    @Override
    public void actionPreformed(SkyblockButton button) {
        super.actionPreformed(button);

        if (button instanceof Pet && Mouse.getEventButton() == 1) {
            if (!SkyTweaksConfig.customPetsMenuFavorite) return;
            Pet pet = (Pet) button;
            if (pet.isFavorite())
                removePetFromFavorites(pet.getPetItem().getDisplayName());
            else
                addPetToFavorites(pet.getPetItem().getDisplayName());
            refreshPetPage();
            SecretUtils.playLoudSound("random.orb", 0.5f);
        } else {
            if (button instanceof Pet) {
                Pet pet = (Pet) button;
                clickSlotOnContainer(pet.slotNumber);
            }
            else if (button instanceof ItemButton) {
                ItemButton itemButton = (ItemButton) button;
                clickSlotOnContainer(itemButton.slotNumber);
            }

            else if (button.id == updatePets) {
                refreshPetPage();
                SecretUtils.playLoudSound("random.orb", 0.5f);
            }
        }
    }
}
