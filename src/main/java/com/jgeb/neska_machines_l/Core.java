package com.jgeb.neska_machines_l;

import com.jgeb.neska_machines_l.registry.*;
import com.jgeb.neska_machines_l.registry.setups.CommonSetup;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Core.MODID)
public class Core {
    public static final String MODID = "neska_machines_l";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Core() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);

        BlockReg.BLOCKS.register(modEventBus);
        ItemReg.ITEMS.register(modEventBus);
        BlockEntityReg.BLOCK_ENTITIES.register(modEventBus);
        MenuReg.MENUS.register(modEventBus);
        FluidReg.FLUIDS.register(modEventBus);
        FluidReg.FLUID_TYPES.register(modEventBus);
        RecipeReg.RECIPE_SERIALS.register(modEventBus);
        RecipeReg.RECIPE_TYPES.register(modEventBus);
        modEventBus.addListener(ScreenReg::registerScreens);

        //modEventBus.addListener(ClientSetup::initClientSetup);
        modEventBus.addListener(CommonSetup::initCommonSetup);
        //modEventBus.addListener(ServerSetup::initServerSetup);*/
    }
}
