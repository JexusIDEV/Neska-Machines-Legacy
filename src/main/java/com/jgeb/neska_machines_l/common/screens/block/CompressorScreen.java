package com.jgeb.neska_machines_l.common.screens.block;

import com.jgeb.neska_machines_l.Core;
import com.jgeb.neska_machines_l.common.block.menu.ChemicalReactorMenu;
import com.jgeb.neska_machines_l.common.block.menu.CompressorMenu;
import com.jgeb.neska_machines_l.common.screens.api.IScreenUtilities;
import com.jgeb.neska_machines_l.common.screens.api.NeskaScreen;
import com.jgeb.neska_machines_l.common.screens.api.RenderableEnergyBar;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CompressorScreen extends NeskaScreen<CompressorMenu> implements IScreenUtilities {

    private static final ResourceLocation GUI = new ResourceLocation(Core.MODID, "textures/gui/container/compressor.png");

    public CompressorScreen(CompressorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 176;
        this.imageHeight = 172;
        this.inventoryLabelY = 79;
    }

    @Override
    protected void init() {
        super.init();
        this.energyBar = new RenderableEnergyBar(this.leftPos + 8, this.topPos + 70, menu.getBE().getEnergyManager(), 68, 6, 0xffff0000, 0xff000000, false);
    }

    @Override
    public void renderTooltip(PoseStack pPoseStack, Component pText, int pMouseX, int pMouseY) {
        super.renderTooltip(pPoseStack, pText, pMouseX, pMouseY);
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        super.renderLabels(pPoseStack, pMouseX, pMouseY);
        renderEnergyAreaTooltips(pPoseStack, this.energyBar, pMouseX, pMouseY, this.leftPos, this.topPos, 8, 70, 68, 6);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        bindTexture(GUI);
        this.blit(pPoseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        this.energyBar.draw(pPoseStack);
        if(this.menu.getBE().getContainerData().get(0) > 0) {
            this.blit(pPoseStack, this.leftPos+67, this.topPos+32, 177, 0, 42, 16);

        }
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pPoseStack, pMouseX, pMouseY);
    }
}
