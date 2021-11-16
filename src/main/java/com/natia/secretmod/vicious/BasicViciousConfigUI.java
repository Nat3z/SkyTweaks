package com.natia.secretmod.vicious;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;

public class BasicViciousConfigUI extends GuiScreen {
    List<ConfigItem> configItems;
    ViciousMod arcticModule;

    public String[] sections = {"General", "Dungeons"};

    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

    private String currentCategory;

    List<Category> categories = new ArrayList<>();
    List<Object> cfgButtons = new ArrayList<>();
    private ResourceLocation Menu = new ResourceLocation("secretmod", "menu_box.png");

    public BasicViciousConfigUI(ViciousMod vModule, List<ConfigItem> configItems, String currentCategory, String[] sections) {
        this.currentCategory = currentCategory;
        this.sections = sections;
        this.configItems = configItems;
        arcticModule = vModule;
        try {
            this.COLOR_WHEEL_IMAGE = TextureUtil.readBufferedImage(Minecraft.getMinecraft().getResourceManager().getResource(COLOR_WHEEL).getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ResourceLocation toggle_off = new ResourceLocation("vicious", "off-toggle.png");
    ResourceLocation toggle_on = new ResourceLocation("vicious", "on-toggle.png");
    ResourceLocation color_switch = new ResourceLocation("vicious", "color-switch.png");

    int maxSectionOffset = 0;

    @Override
    public void initGui() {
        super.initGui();

        double heightlen = 0.2;
        int modnum = -1;

        Map<String, List<Object>> categoryList = new LinkedHashMap<>();
        for (ConfigItem items : configItems) {
            modnum += 1;
            if (!items.getCategory().equals(currentCategory)) continue;
            heightlen += 0.11;

            if (items.getType() == ConfigType.TOGGLE) {
                Button button;
                if ((boolean)items.getValue()) {
                    button = new Button(modnum, (int) (width / 2 * 1.6), (int) (height * heightlen), 30, 10, toggle_on, null);
                } else {
                    // off
                    button = new Button(modnum, (int) (width / 2 * 1.6), (int) (height * heightlen), 30, 10, toggle_off, null);
                }

                if (!categoryList.containsKey(items.getSubCategory())) {
                    button.yPosition += 30; heightlen += 0.08;
                }

                categoryList.putIfAbsent(items.getSubCategory(), new ArrayList<>());
                categoryList.get(items.getSubCategory()).add(button);
            }

            if (items.getType() == ConfigType.INPUT_FIELD) {
                GuiTextField textField = new GuiTextField(modnum, fontRendererObj, (int) (width / 2 * 1.6), (int) (height * heightlen), 80, 15);
                textField.setText((String) items.getValue());
                if (!categoryList.containsKey(items.getSubCategory())) {
                    textField.yPosition += 30; heightlen += 0.08;
                }
                categoryList.putIfAbsent(items.getSubCategory(), new ArrayList<>());
                categoryList.get(items.getSubCategory()).add(textField);
            }

            if (items.getType() == ConfigType.COLOR_WHEEL) {
                Button button = new Button(modnum, (int) (width / 2 * 1.6), (int) (height * heightlen), 30, 10, color_switch, null);
                if (!categoryList.containsKey(items.getSubCategory())) {
                    button.yPosition += 30; heightlen += 0.08;
                } else {

                }
                categoryList.putIfAbsent(items.getSubCategory(), new ArrayList<>());
                categoryList.get(items.getSubCategory()).add(button);
            }

            if (items.getType() == ConfigType.SLIDER_TEXT) {
                List<String> array = Arrays.asList(items.getSliderOptions());
                TextSlider optionSlider = new TextSlider(modnum, (int) (width / 2 * 1.6), (int) (height * heightlen),
                        80, 20, "", "", 1, array.size(), array.indexOf(items.getValue()) + 1, false, true, null, items.getSliderOptions());
                optionSlider.displayString = (String) items.getValue();
                if (!categoryList.containsKey(items.getSubCategory())) {
                    optionSlider.yPosition += 30; heightlen += 0.08;
                }
                categoryList.putIfAbsent(items.getSubCategory(), new ArrayList<>());
                categoryList.get(items.getSubCategory()).add(optionSlider);
            }
        }

        final double[] categoryOffset = {height * 0.31};
        categoryList.forEach((subCategory, configs) -> {
            /* update to offset on category */
            if (configs.get(0) instanceof GuiButton) {
                GuiButton button = (GuiButton) configs.get(0);
                categoryOffset[0] = button.yPosition;
            } else if (configs.get(0) instanceof GuiTextField) {
                GuiTextField button = (GuiTextField) configs.get(0);
                categoryOffset[0] = button.yPosition;
            }

            for (Object obj : configs) {
                if (obj instanceof GuiButton)
                    this.buttonList.add((GuiButton) obj);
                cfgButtons.add(obj);
            }
            categories.add(new Category(subCategory, configs, categoryOffset[0]));
        });

        /* categories */
        int mo = 101;
        int xLine = -60;
        for (String section : sections) {
            mo++;
            xLine += 80;
            MoveableButtons moveableButtons = new MoveableButtons(mo, xLine, 20, 60, 20, section, false);
            buttonList.add(moveableButtons);
            maxSectionOffset = mo;
        }

    }
    public void drawString(String text, int x, int y, int color, int scale) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);
        mc.fontRendererObj.drawString(text, x, y, color, true);
        GlStateManager.popMatrix();
    }
    float scrollmount = 1.0f;
    private int width = new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth();

    private int imageX = width / 2 - 20;
    private int imageY = 80;

    private int currentColor = 0;
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        try {
            // background
            drawDefaultBackground();
            if (showColorWheel) {
                int pickerWidth = COLOR_WHEEL_IMAGE.getWidth();
                int pickerHeight = COLOR_WHEEL_IMAGE.getHeight();

                buttonList.forEach(guiButton -> {
                    guiButton.visible = false;
                });
                mc.getTextureManager().bindTexture(COLOR_WHEEL);
                Gui.drawModalRectWithCustomSizedTexture(imageX, imageY, 0, 0, pickerWidth, pickerHeight, pickerWidth, pickerHeight);
                drawCenteredString("Color Picker", (width / 2 + 3) / 2, (int) (height * 0.1) / 2, new Color(28, 209, 70).getRGB(), 2);
                int currColorx = imageY + 15;
                int currColorY = imageX / 2;
                Gui.drawRect(currColorx, currColorY, currColorx + 20, currColorY + 20, currentColor);

                super.drawScreen(mouseX, mouseY, partialTicks);
                return;
            }

            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
            int width = scaledResolution.getScaledWidth();
            int height = scaledResolution.getScaledHeight();

            int xSize = Math.min(width-100/scaledResolution.getScaleFactor(), 500);
            int ySize = Math.min(height-100/scaledResolution.getScaleFactor(), 500);

            int x2 = (scaledResolution.getScaledWidth() - xSize)/2;
            int y2 = (scaledResolution.getScaledWidth() - ySize)/2;
            GlStateManager.enableBlend();

            Minecraft.getMinecraft().getTextureManager().bindTexture(Menu);
            Gui.drawModalRectWithCustomSizedTexture(x2 / 2, (int) (height * 0.20), 0, 0, (int) (width / 2) + 250,  (int) (height / 1.3), 356, 267);

            super.drawScreen(mouseX, mouseY, partialTicks);


            /* Keep this at the end */
            int id = -1;

            for (Category category : categories) {
                if (category.dynamicOffset > height * 0.33 && category.dynamicOffset < (height - 50)) {
                    drawCenteredString(category.getName(), x2 / 2 + 130, category.dynamicOffset / 2 - 15, new Color(28, 209, 70).getRGB(), 2);
                }
                if (category.getConfigs().get(0) instanceof GuiButton)
                    id = ((GuiButton) category.getConfigs().get(0)).id - 1;
                else if (category.getConfigs().get(0) instanceof GuiTextField)
                    id = ((GuiTextField) category.getConfigs().get(0)).getId() - 1;
                for (Object obj : category.getConfigs()) {
                    id++;
                    if (obj instanceof GuiButton) {
                        GuiButton button = (GuiButton) obj;
                        if (button.visible) {
                            if (configItems.get(id).getType() == ConfigType.HUD) continue;

                            if (configItems.get(id).getType() == ConfigType.COLOR_WHEEL) {
                                this.drawString(configItems.get(id).getName(), x2 / 2, button.yPosition / 2, new Color((int) configItems.get(id).getValue()).getRGB(), 2);
                                this.drawString(configItems.get(id).getDescription(), x2 + 10, button.yPosition + 20, configItems.get(id).shouldUseAtOwnRisk() ? new Color(255, 90, 41).getRGB() : Color.white.getRGB(), 1);
                            } else {
                                this.drawString(configItems.get(id).getName(), x2 / 2, button.yPosition / 2, configItems.get(id).shouldUseAtOwnRisk() ? Color.red.getRGB() : new Color(218, 159, 0).getRGB(), 2);
                                this.drawString(configItems.get(id).getDescription(), x2 + 10, button.yPosition + 20, configItems.get(id).shouldUseAtOwnRisk() ? new Color(255, 90, 41).getRGB() : Color.white.getRGB(), 1);
                            }
                        }
                    } else if (obj instanceof GuiTextField) {
                        ((GuiTextField) obj).drawTextBox();
                        GuiTextField button = (GuiTextField) obj;
                        if (button.getVisible()) {
                            this.drawString(configItems.get(id).getName(), x2 / 2, button.yPosition / 2, configItems.get(id).shouldUseAtOwnRisk() ? Color.red.getRGB() : new Color(218, 159, 0).getRGB(), 2);
                            this.drawString(configItems.get(id).getDescription(), x2 + 10, button.yPosition + 20, configItems.get(id).shouldUseAtOwnRisk() ? Color.red.getRGB() : Color.white.getRGB(), 1);
                        }
                    }
                }
            }

            // Scroll Wheel Amount
            int scrollwheelscrolled = Mouse.getDWheel();
            if (scrollwheelscrolled == 0) return;

            /* Down */
            /*if (cfgButtons.get(cfgButtons.size() - 1) instanceof GuiButton) {
                GuiButton button = (GuiButton) cfgButtons.get(cfgButtons.size() - 1);
                if (scrollwheelscrolled * -1 <= -1 && scrollmount >= button.yPosition) return;
            } else if (cfgButtons.get(cfgButtons.size() - 1) instanceof GuiTextField) {
                GuiTextField button = (GuiTextField) cfgButtons.get(cfgButtons.size() - 1);
                if (scrollwheelscrolled * -1 <= -1 && scrollmount >= button.yPosition) return;
            }

            if (cfgButtons.get(0) instanceof GuiTextField) {
                GuiTextField firstButton = (GuiTextField) cfgButtons.get(0);
                if (scrollwheelscrolled * -1 >= 1 && scrollmount >= firstButton.yPosition) return;
            } else if (cfgButtons.get(0) instanceof GuiButton) {
                GuiButton firstButton = (GuiButton) cfgButtons.get(0);
                if (scrollwheelscrolled * -1 >= 1 && scrollmount >= firstButton.yPosition) return;
            }*/

            scrollmount += scrollwheelscrolled;

            buttonList.forEach(guiButton -> {
                /* is not within range of sections */
                if (guiButton.id < maxSectionOffset && guiButton.id <= 101) {
                    guiButton.yPosition += scrollwheelscrolled / 10;
                    guiButton.visible = true;
                    if (guiButton.yPosition < height * 0.2 || guiButton.yPosition > (height - 50)) guiButton.visible = false;
                }
            });

            categories.forEach(category -> {
                category.dynamicOffset += scrollwheelscrolled / 10;
            });

            cfgButtons.forEach(guiTextField -> {
                if (guiTextField instanceof GuiTextField) {
                    ((GuiTextField) guiTextField).yPosition += scrollwheelscrolled / 10;
                    ((GuiTextField) guiTextField).setVisible(true);
                    if (((GuiTextField) guiTextField).yPosition < height * 0.2 || ((GuiTextField) guiTextField).yPosition > (height - 50)) ((GuiTextField) guiTextField).setVisible(false);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            Minecraft.getMinecraft().thePlayer.closeScreen();
        }
    }

    public static void drawCenteredString(String text, int x, int y, int color, double scale) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1);
        Minecraft.getMinecraft().fontRendererObj.drawString(text,
                (int) (x) - Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) / 2,
                (int) (y), color, true);
        GlStateManager.popMatrix();
    }

    int hoverid = -1;
    final ResourceLocation COLOR_WHEEL = new ResourceLocation("vicious", "colorwheel.png");
    BufferedImage COLOR_WHEEL_IMAGE = null;

    boolean showColorWheel = false;

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        /* For Sections */
        int mo = 101;
        for (String section : sections) {
            mo++;
            if (button.id == mo) {
                mc.displayGuiScreen(new BasicViciousConfigUI(arcticModule, configItems, section, sections));
                break;
            }
        }

        if (button instanceof Button && ((Button) button).textureResource == color_switch) {
            hoverid = button.id;
            currentColor = (int) configItems.get(button.id).getValue();
            showColorWheel = true;
        }
        else if (button instanceof Button) {
            Button button1 = (Button) button;

            if (button1.textureResource == toggle_off) {
                new Thread(() -> {
                    for (Field variable : arcticModule.getConfig().getClass().getDeclaredFields()) {
                        if (variable.isAnnotationPresent(AddConfig.class)) {
                            AddConfig config = variable.getAnnotation(AddConfig.class);
                            if (config.name().equals(configItems.get(button1.id).getName())) {
                                try {
                                    variable.set(arcticModule.getConfig(), true);
                                    button1.textureResource = toggle_on;
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }
                    }
                }).start();
            } else if (button1.textureResource == toggle_on) {
                new Thread(() -> {
                    for (Field variable : arcticModule.getConfig().getClass().getDeclaredFields()) {
                        if (variable.isAnnotationPresent(AddConfig.class)) {
                            AddConfig config = variable.getAnnotation(AddConfig.class);
                            if (config.name().equals(configItems.get(button1.id).getName())) {
                                try {
                                    variable.set(arcticModule.getConfig(), false);
                                    button1.textureResource = toggle_off;
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }
                    }
                }).start();

            }
        }

        super.actionPerformed(button);
    }



    @Override
    public void onGuiClosed() {
        new Thread(() -> {
            cfgButtons.forEach(o -> {
                if (o instanceof TextSlider) {
                    TextSlider slider = (TextSlider) o;
                    ConfigItem item = configItems.get(slider.id);
                    try {
                        item.getField().set(item.getConfig(), slider.displayString);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else if (o instanceof GuiTextField) {
                    GuiTextField field = (GuiTextField) o;
                    ConfigItem item = configItems.get(field.getId());
                    try {
                        item.getField().set(item.getConfig(), field.getText());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
            arcticModule.saveConfig();
        }).start();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (Object obj : cfgButtons) {
            if (obj instanceof GuiTextField) {
                ((GuiTextField) obj).mouseClicked(mouseX, mouseY, mouseButton);
            }
            else if (showColorWheel) {
                int xPixel = mouseX - imageX;
                int yPixel = mouseY - imageY;
                // If the mouse is over the color picker.
                if (xPixel > 0 && xPixel < COLOR_WHEEL_IMAGE.getWidth() &&
                        yPixel > 0 && yPixel < COLOR_WHEEL_IMAGE.getHeight()) {
                    buttonList.forEach(guiButton -> {
                        guiButton.visible = true;
                    });
                    // Get the color of the clicked pixel.
                    Color selectedColor = new Color(COLOR_WHEEL_IMAGE.getRGB(xPixel, yPixel), true);
                    int id = -1;
                    for (Field variable : arcticModule.getConfig().getClass().getDeclaredFields()) {
                        id++;
                        if (variable.isAnnotationPresent(AddConfig.class)) {
                            if (id == hoverid) {
                                try {
                                    variable.set(arcticModule.getConfig(), selectedColor.getRGB());
                                    ConfigItem configItem = configItems.get(hoverid);
                                    configItems.set(hoverid, new ConfigItem(variable, configItem.getName(), configItem.getDescription(), configItem.getCategory(), configItem.getSubCategory(), configItem.getType(), configItem.shouldUseAtOwnRisk(), configItem.getSliderOptions(), configItem.getConfig()));
                                    hoverid = -1;
                                    showColorWheel = false;

                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        for (Object obj : cfgButtons) {
            if (obj instanceof GuiTextField) {
                if (((GuiTextField) obj).isFocused()) {
                    ((GuiTextField) obj).textboxKeyTyped(typedChar, keyCode);
                }
                break;
            }
        }

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void updateScreen() {
        for (Object obj : cfgButtons) {
            if (obj instanceof GuiTextField) {
                if (((GuiTextField) obj).isFocused())
                    ((GuiTextField) obj).updateCursorCounter();
            }
        }

        super.updateScreen();
    }

}