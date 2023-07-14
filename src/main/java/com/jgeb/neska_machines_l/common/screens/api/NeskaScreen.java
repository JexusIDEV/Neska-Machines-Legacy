package com.jgeb.neska_machines_l.common.screens.api;

import com.jgeb.neska_machines_l.Core;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.energy.EnergyStorage;

import java.util.Optional;

public abstract class NeskaScreen <T extends AbstractContainerMenu> extends AbstractContainerScreen<T> implements IScreenUtilities {

    public RenderableEnergyBar energyBar;
    public static ResourceLocation guiSlotsComponents = new ResourceLocation(Core.MODID, "textures/gui/component/slot_components.png");
    public static ResourceLocation guiTabComponents = new ResourceLocation(Core.MODID, "textures/gui/component/tab_components.png");

    public NeskaScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    public void renderEnergyAreaTooltips(PoseStack pPoseStack, RenderableEnergyBar energyStorage, int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, offsetX, offsetY, width, height)) {
            renderTooltip(pPoseStack, energyStorage.getTooltips(),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }
}
