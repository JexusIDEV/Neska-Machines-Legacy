package com.jgeb.neska_machines_l.common.screens.block;

import com.jgeb.neska_machines_l.Core;
import com.jgeb.neska_machines_l.common.block.menu.ChemicalReactorMenu;
import com.jgeb.neska_machines_l.common.block.menu.CombustionGeneratorMenu;
import com.jgeb.neska_machines_l.common.screens.api.IScreenUtilities;
import com.jgeb.neska_machines_l.common.screens.api.NeskaScreen;
import com.jgeb.neska_machines_l.common.screens.api.RenderableEnergyBar;
import com.jgeb.neska_machines_l.util.datas.NeskaBooleansProperties;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.Optional;

public class CombustionGeneratorScreen extends NeskaScreen<CombustionGeneratorMenu> implements IScreenUtilities {

    private static final ResourceLocation texGui = new ResourceLocation(Core.MODID, "textures/gui/container/combustion_generator.png");

    public CombustionGeneratorScreen(CombustionGeneratorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 176;
        this.imageHeight = 180;
        this.inventoryLabelY = 86;
    }

    @Override
    public void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        super.renderLabels(pPoseStack, pMouseX, pMouseY);
        renderEnergyAreaTooltips(pPoseStack, this.energyBar, pMouseX, pMouseY, this.leftPos, this.topPos, 53, 19, 34, 40);
    }

    @Override
    protected void init() {
        super.init();
        this.energyBar = new RenderableEnergyBar(this.leftPos + 53, this.topPos + 19, menu.getBE().getEnergyManager(), 34, 40,0xffff0000, 0xff000000, true);
    }

    @Override
    public void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        bindTexture(texGui);
        this.blit(pPoseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        int burningProgress = getProgressBarValue(menu.getDataContainer(0), menu.getDataContainer(1), 14);
        if(menu.getBE().getContainerData().get(0) > 0) {
            this.blit(pPoseStack, this.leftPos + 81, this.topPos + 69 + 12 - burningProgress,  this.imageWidth + 1, 12 - burningProgress, 14, burningProgress + 1);
        }
        this.energyBar.draw(pPoseStack);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pPoseStack, pMouseX, pMouseY);
    }
}
