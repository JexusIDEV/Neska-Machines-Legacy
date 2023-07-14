package com.jgeb.neska_machines_l.registry;

import com.jgeb.neska_machines_l.Core;
import com.jgeb.neska_machines_l.common.recipes.CompressorRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeReg {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Core.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Core.MODID);

    public static final RegistryObject<RecipeSerializer<CompressorRecipe>> COMPRESSOR_SERIAL = RECIPE_SERIALS.register("compressing", () -> CompressorRecipe.Serial.INSTANCE);
    public static final RegistryObject<RecipeType<CompressorRecipe>> COMPRESSOR_TYPE = RECIPE_TYPES.register("compressing", () -> CompressorRecipe.Type.INSTANCE);
}
