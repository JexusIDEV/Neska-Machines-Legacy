package com.jgeb.neska_machines_l.common.screens.api;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.Rect2i;

public abstract class InformationArea extends GuiComponent {
    protected final Rect2i area;

    protected InformationArea(Rect2i area) {
        this.area = area;
    }

    public abstract void draw(PoseStack transform);
}
