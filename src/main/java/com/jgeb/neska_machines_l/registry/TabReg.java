package com.jgeb.neska_machines_l.registry;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class TabReg extends CreativeModeTab {

    public TabReg(int pId, String pLangId) {
        super(pId, pLangId);
    }

    public static TabReg MAIN_TAB = new TabReg(TABS.length, "main") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Blocks.ACACIA_LOG);
        }
    };

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(Blocks.AIR);
    }
}
