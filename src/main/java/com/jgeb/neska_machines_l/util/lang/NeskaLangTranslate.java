package com.jgeb.neska_machines_l.util.lang;

import com.jgeb.neska_machines_l.Core;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ForgeRegistries;

public class NeskaLangTranslate {

    //getKeysPaths
    public static String getKeyPathRegistry(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block).getPath();
    }

    public static String getKeyPathRegistry(Item item) {
        return ForgeRegistries.ITEMS.getKey(item).getPath();
    }

    public static String getKeyPathRegistry(BlockEntityType<?> blockEntityType) {
        return ForgeRegistries.BLOCK_ENTITY_TYPES.getKey(blockEntityType).getPath();
    }

    public static String getKeyPathRegistry(MenuType<?> menuType) {
        return ForgeRegistries.MENU_TYPES.getKey(menuType).getPath();
    }

    //getKeys
    public static ResourceLocation getKeyResourceLocation(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    public static ResourceLocation getKeyResourceLocation(Item item) {
        return ForgeRegistries.ITEMS.getKey(item);
    }

    public static ResourceLocation getKeyResourceLocation(BlockEntityType<?> blockEntityType) {
        return ForgeRegistries.BLOCK_ENTITY_TYPES.getKey(blockEntityType);
    }

    public static ResourceLocation getKeyResourceLocation(MenuType<?> menuType) {
        return ForgeRegistries.MENU_TYPES.getKey(menuType);
    }

    //metodos de creación de titulos o translatables
    public static Component createNamedTooltip(String name) {
        return Component.translatable("tooltip."+ Core.MODID+"."+name);
    }
    public static Component createNamedTooltip(Block block) {
        return Component.translatable("tooltip."+ Core.MODID+"."+getKeyPathRegistry(block));
    }
    public static Component createNamedTooltip(Item item) {
        return Component.translatable("tooltip."+ Core.MODID+"."+getKeyPathRegistry(item));
    }

    public static Component createContainerTittle(String name) {
        return Component.translatable("container"+Core.MODID+"."+name);
    }
    public static Component createContainerTittle(Block block) {
        return Component.translatable("container"+Core.MODID+"."+getKeyPathRegistry(block));
    }
    public static Component createContainerTittle(Item item) {
        return Component.translatable("container"+Core.MODID+"."+getKeyPathRegistry(item));
    }
}
