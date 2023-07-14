package com.jgeb.neska_machines_l.common.screens.api;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.List;
import java.util.function.IntSupplier;

/*
 *  BluSunrize
 *  Copyright (c) 2021
 *
 *  This code is licensed under "Blu's License of Common Sense"
 *  Details can be found in the license file in the root folder of this project
 */
public class RenderableEnergyBar extends InformationArea{

    protected final IEnergyStorage energy;
    protected final int colorA;
    protected final int colorB;
    protected final boolean isVerticalOrHorizontal;

    public RenderableEnergyBar(int xMin, int yMin, EnergyStorage energy, int width, int height, int colorA, int colorB, boolean isVerticalOrHorizontal)  {
        super(new Rect2i(xMin, yMin, width, height));
        this.energy = energy;
        this.colorA = colorA;
        this.colorB = colorB;
        this.isVerticalOrHorizontal = isVerticalOrHorizontal;
    }

    public List<Component> getTooltips() {
        return List.of(Component.literal(energy.getEnergyStored()+"/"+energy.getMaxEnergyStored()+" §4FE"));
    }

    @Override
    public void draw(PoseStack transform) {
        final int height = area.getHeight();
        final int width = area.getWidth();
        if(this.isVerticalOrHorizontal) {
            int stored = (int)(height*(energy.getEnergyStored()/(float)energy.getMaxEnergyStored()));
            fillGradient(transform, area.getX(), area.getY()+(height-stored), area.getX() + area.getWidth(), area.getY() + area.getHeight(), colorA, colorB);
        } else {
            int stored = (int)(width*(energy.getEnergyStored()/(float)energy.getMaxEnergyStored()));
            fillGradient(transform, area.getX() + (width-stored), area.getY(), area.getX() + area.getWidth() + area.getWidth(), area.getY(), colorA, colorB);
        }
    }
}
