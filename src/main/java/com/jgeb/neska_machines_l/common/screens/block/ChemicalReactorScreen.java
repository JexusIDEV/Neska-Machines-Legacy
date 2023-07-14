package com.jgeb.neska_machines_l.common.screens.block;

import com.jgeb.neska_machines_l.Core;
import com.jgeb.neska_machines_l.common.block.menu.ChemicalReactorMenu;
import com.jgeb.neska_machines_l.common.screens.api.IScreenUtilities;
import com.jgeb.neska_machines_l.common.screens.api.NeskaScreen;
import com.jgeb.neska_machines_l.common.screens.api.RenderableEnergyBar;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ChemicalReactorScreen extends NeskaScreen<ChemicalReactorMenu> implements IScreenUtilities {

    private static final ResourceLocation GUI = new ResourceLocation(Core.MODID, "textures/gui/container/chemical_reactor.png");

    public ChemicalReactorScreen(ChemicalReactorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        this.energyBar = new RenderableEnergyBar(this.leftPos + 11, this.topPos + 13, menu.getBE().getEnergyManager(), 10, 32, 0xffff0000, 0xff000000, true);
    }

    @Override
    public void renderTooltip(PoseStack pPoseStack, Component pText, int pMouseX, int pMouseY) {
        super.renderTooltip(pPoseStack, pText, pMouseX, pMouseY);
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        super.renderLabels(pPoseStack, pMouseX, pMouseY);
        renderEnergyAreaTooltips(pPoseStack, this.energyBar, pMouseX, pMouseY, this.leftPos, this.topPos, 11, 13, 10, 32);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        bindTexture(GUI);
        this.blit(pPoseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        this.energyBar.draw(pPoseStack);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pPoseStack, pMouseX, pMouseY);
    }
}
