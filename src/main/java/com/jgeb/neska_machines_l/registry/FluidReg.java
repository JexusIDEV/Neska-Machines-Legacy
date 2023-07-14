package com.jgeb.neska_machines_l.registry;

import com.jgeb.neska_machines_l.Core;
import com.jgeb.neska_machines_l.util.registry.FluidFactoryBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidReg {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Core.MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Core.MODID);

    public static final FluidFactoryBuilder EXAMPLE_FLUID = new FluidFactoryBuilder(
            "example_fluid",
            FluidType.Properties.create().canSwim(true).canDrown(true).canPushEntity(true).supportsBoating(true),
            () -> FluidFactoryBuilder.createExtension(
                    new FluidFactoryBuilder.ClientExtensions(
                            Core.MODID,
                            "example_fluid"
                    ).tint(0xFF44AA)
                            .fogColor(1.0f, 0.2f, 0.5f)
            ),
            BlockBehaviour.Properties.copy(Blocks.WATER),
            new Item.Properties()
                    .tab(TabReg.MAIN_TAB)
                    .stacksTo(1)
    );
}
