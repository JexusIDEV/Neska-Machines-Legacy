package com.jgeb.neska_machines_l.common.screens.api;

import com.jgeb.neska_machines_l.util.math.NeskaMath;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public interface IScreenUtilities {

    default void bindTexture(ResourceLocation gui) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, gui);
    }

    default int getProgressBarValue(int value, int maxValue, int pixelSize) {
        return maxValue != 0 && value != 0 ? value * pixelSize / maxValue : 0;
    }

    default boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return NeskaMath.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }

}
