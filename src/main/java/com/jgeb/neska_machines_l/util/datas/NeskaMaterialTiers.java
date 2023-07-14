package com.jgeb.neska_machines_l.util.datas;

import com.google.common.base.Supplier;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public enum NeskaMaterialTiers implements Tier {

    CRIPTIAN(1 ,1, 1, 1, 1, () -> null),
    CRISTAYLIUM(1 ,1, 1, 1, 1, () -> null),
    DARK_MATTER(1 ,1, 1, 1, 1, () -> null);

    private final int use;
    private final float speed;
    private final float atkBonus;
    private final int level;
    private final int enchantLevel;
    private final LazyLoadedValue<Ingredient> ingredient;

    NeskaMaterialTiers(int use, float speed, float atkBonus, int level, int enchantLevel, Supplier<Ingredient> ingredient) {
        this.use = use;
        this.speed = speed;
        this. atkBonus = atkBonus;
        this.level = level;
        this.enchantLevel = enchantLevel;
        this.ingredient = new LazyLoadedValue<>(ingredient);
    }

    @Override
    public int getUses() {
        return this.use;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.atkBonus;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantLevel;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.ingredient.get();
    }
}
