package com.jgeb.neska_machines_l.registry.datagen;

import com.jgeb.neska_machines_l.Core;
import com.jgeb.neska_machines_l.registry.datagen.client.BlockModelStateDataGenerator;
import com.jgeb.neska_machines_l.registry.datagen.client.ItemModelDataGenerator;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Core.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NeskaDataGen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent dataEvent) {
        DataGenerator dataGenerator = dataEvent.getGenerator();
        ExistingFileHelper helper = dataEvent.getExistingFileHelper();
        //Datas
        dataGenerator.addProvider(dataEvent.includeClient(), new ItemModelDataGenerator(dataGenerator, helper));
        dataGenerator.addProvider(dataEvent.includeClient(), new BlockModelStateDataGenerator(dataGenerator, helper));
    }
}
