package com.jgeb.neska_machines_l.registry.datagen.client;

import com.jgeb.neska_machines_l.Core;
import com.jgeb.neska_machines_l.registry.BlockReg;
import com.jgeb.neska_machines_l.util.lang.NeskaLangTranslate;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelDataGenerator extends ItemModelProvider {

    public ItemModelDataGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Core.MODID, existingFileHelper);
    }

    protected void simpleBlockItem(Item item) {
        getBuilder(NeskaLangTranslate.getKeyResourceLocation(item).toString())
                .parent(getExistingFile(modLoc("block/" + NeskaLangTranslate.getKeyPathRegistry(item))));
    }

    protected void oneLayerItem(Item item, ResourceLocation texture) {
        ResourceLocation itemTexture = new ResourceLocation(texture.getNamespace(), "item/" + texture.getPath());
        if (existingFileHelper.exists(itemTexture, PackType.CLIENT_RESOURCES, ".png", "textures")) {
            getBuilder(NeskaLangTranslate.getKeyPathRegistry(item)).parent(getExistingFile(mcLoc("item/generated")))
                    .texture("layer0", itemTexture);
        } else {
            System.out.println(
                    "Texture for " + NeskaLangTranslate.getKeyResourceLocation(item).toString() + " not present at " + itemTexture.toString());
        }
    }

    protected void oneLayerItem(Item item) {
        oneLayerItem(item, NeskaLangTranslate.getKeyResourceLocation(item));
    }

    @Override
    protected void registerModels() {
        simpleBlockItem(BlockReg.COMBUSTION_GENERATOR.get().asItem());
        simpleBlockItem(BlockReg.CHEMICAL_REACTOR.get().asItem());
    }
}
