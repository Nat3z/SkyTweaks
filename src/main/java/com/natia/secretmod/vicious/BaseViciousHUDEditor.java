package com.natia.secretmod.vicious;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class BaseViciousHUDEditor extends GuiScreen {
    Map<MoveableButtons, ConfigItem> moveableButtonsMap = new HashMap<>();
    boolean moving = false;
    int move = -1;
    ViciousMod module;
    public BaseViciousHUDEditor(ViciousMod viciousMod) {
        module = viciousMod;
    }
    @Override
    public void initGui() {
        super.initGui();
        GlStateManager.enableBlend();

        AtomicInteger id = new AtomicInteger(1);

        module.getConfigItems().forEach(configItem -> {
                if (configItem.getType() == ConfigType.HUD) {
                    boolean continues = false;
                    if (configItem.getField().getAnnotation(AddConfig.class).requiresElementToggled()) {
                        if (!configItem.getField().getAnnotation(AddConfig.class).requiredElementToggled().equals("")) {
                            try {
                                continues = (boolean) configItem.getConfig().getClass().getDeclaredField(configItem.getField().getAnnotation(AddConfig.class).requiredElementToggled()).get(configItem.getConfig());
                            } catch (Exception ex) {
                            }
                        }
                    } else {
                        continues = true;
                    }
                    if (continues) {
                        HudElement element = (HudElement) configItem.getValue();
                        id.getAndIncrement();
                        MoveableButtons buttons = new MoveableButtons(id.get(), element.x, element.y, element.width, element.height, configItem.getName(), moving);
                        buttonList.add(buttons);
                        moveableButtonsMap.put(buttons, configItem);
                    }
                }
            });
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        mouseMove(mouseX, mouseY);
        drawRect(0, 0, this.width, this.height, new Color(17, 17, 17, 176).getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    double pressTime = 0;
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        moveableButtonsMap.forEach((moveableButtons, configItem) -> {
            if (moveableButtons.id == button.id) {
                moving = true;
                move = button.id;

                for (Field variable : configItem.getConfig().getClass().getDeclaredFields()) {
                    if (variable.isAnnotationPresent(AddConfig.class)) {
                        AddConfig config = variable.getAnnotation(AddConfig.class);
                        if (config.name().equals(moveableButtons.displayString)) {
                            this.variable = variable;
                        }
                    }
                }
                if (pressTime == 0) {
                    pressTime = System.currentTimeMillis();
                } else {
                    pressTime = 0;
                }
            }
        });
        super.actionPerformed(button);
    }
    private int lastMouseX = -1;
    private int lastMouseY = -1;
    private void mouseMove(int mouseX, int mouseY) {
        moveableButtonsMap.forEach((moveableButtons, configItem) -> {

            if (moveableButtons.id == move) {
                int xMove = mouseX - lastMouseX;
                int yMove = mouseY - lastMouseY;

                moveableButtons.xPosition += xMove;
                moveableButtons.yPosition += yMove;
                HudElement element = (HudElement) configItem.getValue();
                try {
                    variable.set(configItem.getConfig(), new HudElement((int)moveableButtons.xPosition, (int)moveableButtons.yPosition, element.width, element.height));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        lastMouseX = mouseX;
        lastMouseY = mouseY;
    }
    Field variable = null;
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        moveableButtonsMap.forEach((moveableButtons, configItem) -> {
            if (moveableButtons.id == move) {
                move = -1;
                module.saveConfig();
                variable = null;
            }
        });
    }
}
