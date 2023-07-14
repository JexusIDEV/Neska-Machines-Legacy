package com.jgeb.neska_machines_l.registry;

import com.jgeb.neska_machines_l.Core;
import com.jgeb.neska_machines_l.common.block.be.ChemicalReactorBE;
import com.jgeb.neska_machines_l.common.block.be.CombustionGeneratorBE;
import com.jgeb.neska_machines_l.common.block.be.CompressorBE;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityReg {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Core.MODID);

    public static final RegistryObject<BlockEntityType<CombustionGeneratorBE>> COMBUSTION_GENERATOR_BE = BLOCK_ENTITIES.register("combustion_generator_be", () -> BlockEntityType.Builder.of(CombustionGeneratorBE::new, BlockReg.COMBUSTION_GENERATOR.get()).build(null));
    public static final RegistryObject<BlockEntityType<ChemicalReactorBE>> CHEMICAL_REACTOR_BE = BLOCK_ENTITIES.register("chemical_reactor_be", () -> BlockEntityType.Builder.of(ChemicalReactorBE::new, BlockReg.CHEMICAL_REACTOR.get()).build(null));
    public static final RegistryObject<BlockEntityType<CompressorBE>> COMPRESSOR_BE = BLOCK_ENTITIES.register("compressor_be", () -> BlockEntityType.Builder.of(CompressorBE::new, BlockReg.COMPRESSOR.get()).build(null));
}
