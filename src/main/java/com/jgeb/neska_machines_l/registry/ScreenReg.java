package com.jgeb.neska_machines_l.registry;

import com.jgeb.neska_machines_l.common.block.menu.CombustionGeneratorMenu;
import com.jgeb.neska_machines_l.common.screens.block.ChemicalReactorScreen;
import com.jgeb.neska_machines_l.common.screens.block.CombustionGeneratorScreen;
import com.jgeb.neska_machines_l.common.screens.block.CompressorScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.jgeb.neska_machines_l.Core.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ScreenReg {

    @SubscribeEvent
    public static void registerScreens(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
           //Registra las pantallas haha
            MenuScreens.register(MenuReg.CHEMICAL_REACTOR_MENU.get(), ChemicalReactorScreen::new);
            MenuScreens.register(MenuReg.COMBUSTION_GENERATOR_MENU.get(), CombustionGeneratorScreen::new);
            MenuScreens.register(MenuReg.COMPRESSOR_MENU.get(), CompressorScreen::new);

        });
    }
}
