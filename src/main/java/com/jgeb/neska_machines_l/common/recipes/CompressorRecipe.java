package com.jgeb.neska_machines_l.common.recipes;

import com.google.gson.JsonObject;
import com.jgeb.neska_machines_l.Core;
import com.jgeb.neska_machines_l.registry.RecipeReg;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public record CompressorRecipe(ResourceLocation modid,
                               Ingredient inputIngredient,
                               ItemStack output, int energyPerTask, int count,
                               int maxProgress) implements Recipe<SimpleContainer> {

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if (pLevel.isClientSide) {
            return false;
        }

        return this.inputIngredient.test(pContainer.getItem(0)) && (pContainer.getItem(2).isEmpty() || pContainer.canPlaceItem(2, this.output) && pContainer.getItem(2).getCount() < pContainer.getItem(2).getMaxStackSize());
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer) {
        pContainer.removeItem(0, this.getCount());
        return getResultItem();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }


    @Override
    public ItemStack getResultItem() {
        return this.output.copy();
    }

    public Ingredient getInputIngredient() {
        return this.inputIngredient;
    }

    public int getEnergy() {
        return this.energyPerTask;
    }

    public int getMaxProgress() {
        return this.maxProgress;
    }

    public int getCount() {
        return this.count;
    }

    @Override
    public ResourceLocation getId() {
        return this.modid;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeReg.COMPRESSOR_SERIAL.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeReg.COMPRESSOR_TYPE.get();
    }

    public static class Type implements RecipeType<CompressorRecipe> {
        public static final Type INSTANCE = new Type();

        private Type() {
        }

        public static final ResourceLocation ID = new ResourceLocation(Core.MODID, "compressing");

        @Override
        public String toString() {
            return ID.toString();
        }
    }

    public static class Serial implements RecipeSerializer<CompressorRecipe> {
        public static final Serial INSTANCE = new Serial();

        private Serial() {
        }

        public static final ResourceLocation ID = new ResourceLocation(Core.MODID, "compressing");

        @Override
        public CompressorRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "result"));
            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "input"));
            int energy = GsonHelper.getAsInt(pSerializedRecipe, "energyTick");
            int count = GsonHelper.getAsInt(pSerializedRecipe, "count");
            int progress = GsonHelper.getAsInt(pSerializedRecipe, "maxProgress");
            return new CompressorRecipe(pRecipeId, input, output, energy, count, progress);
        }

        @Override
        public @Nullable
        CompressorRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            Ingredient input = Ingredient.fromNetwork(pBuffer);
            int energy = pBuffer.readInt();
            int count = pBuffer.readInt();
            int progress = pBuffer.readInt();
            ItemStack output = pBuffer.readItem();
            return new CompressorRecipe(pRecipeId, input, output, energy, count, progress);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, CompressorRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.getIngredients().size());
            pBuffer.writeInt(pRecipe.getEnergy());
            pBuffer.getInt(pRecipe.getCount());
            pBuffer.getInt(pRecipe.getMaxProgress());
            pBuffer.writeItemStack(pRecipe.getResultItem(), false);
            pRecipe.getInputIngredient().toNetwork(pBuffer);
        }
    }
}
