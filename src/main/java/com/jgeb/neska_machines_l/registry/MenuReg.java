package com.jgeb.neska_machines_l.registry;

import com.jgeb.neska_machines_l.Core;
import com.jgeb.neska_machines_l.common.block.menu.ChemicalReactorMenu;
import com.jgeb.neska_machines_l.common.block.menu.CombustionGeneratorMenu;
import com.jgeb.neska_machines_l.common.block.menu.CompressorMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuReg {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Core.MODID);

    public static final RegistryObject<MenuType<CombustionGeneratorMenu>> COMBUSTION_GENERATOR_MENU = MENUS.register("combustion_generator_menu", () -> IForgeMenuType.create(CombustionGeneratorMenu::new));
    public static final RegistryObject<MenuType<ChemicalReactorMenu>> CHEMICAL_REACTOR_MENU = MENUS.register("chemical_reactor_menu", () -> IForgeMenuType.create(ChemicalReactorMenu::new));
    public static final RegistryObject<MenuType<CompressorMenu>> COMPRESSOR_MENU = MENUS.register("compressor_menu", () -> IForgeMenuType.create(CompressorMenu::new));

}
