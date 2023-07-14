package com.jgeb.neska_machines_l.registry;

import com.jgeb.neska_machines_l.Core;
import com.jgeb.neska_machines_l.common.block.ChemicalReactorBlock;
import com.jgeb.neska_machines_l.common.block.CombustionGeneratorBlock;
import com.jgeb.neska_machines_l.common.block.CompressorBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockReg {

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> supplier, Item.Properties properties) {
        RegistryObject<T> block = BLOCKS.register(name, supplier);
        ItemReg.ITEMS.register(name, () -> new BlockItem(block.get(), properties));
        return block;
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> supplier) {
        return  registerBlock(name, supplier, new Item.Properties());
    }
    private static <T extends Block> RegistryObject<T> registerBlockTab(String name, Supplier<T> supplier, CreativeModeTab tab) {
        return  registerBlock(name, supplier, new Item.Properties().tab(tab));
    }

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Core.MODID);

    public static final RegistryObject<CombustionGeneratorBlock> COMBUSTION_GENERATOR = registerBlockTab("combustion_generator", () -> new CombustionGeneratorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)), TabReg.MAIN_TAB);
    public static final RegistryObject<ChemicalReactorBlock> CHEMICAL_REACTOR = registerBlockTab("chemical_reactor", () -> new ChemicalReactorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)), TabReg.MAIN_TAB);
    public static final RegistryObject<CompressorBlock> COMPRESSOR = registerBlockTab("compressor", () -> new CompressorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)), TabReg.MAIN_TAB);

}
