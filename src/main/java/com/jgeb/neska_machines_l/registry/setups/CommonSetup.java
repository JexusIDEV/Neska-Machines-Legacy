package com.jgeb.neska_machines_l.registry.setups;

import com.jgeb.neska_machines_l.registry.NetworkReg;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static com.jgeb.neska_machines_l.Core.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CommonSetup {

    @SubscribeEvent
    public static void initCommonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            NetworkReg.initNetwork();
        });
    }
}
