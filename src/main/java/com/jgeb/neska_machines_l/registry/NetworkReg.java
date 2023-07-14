package com.jgeb.neska_machines_l.registry;

import com.jgeb.neska_machines_l.Core;
import com.jgeb.neska_machines_l.registry.packets.common.SCChemicalReactorPacket;
import com.jgeb.neska_machines_l.registry.packets.common.SCCombustionGeneratorPacket;
import com.jgeb.neska_machines_l.registry.packets.common.SCCompressorPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkReg {

    public static SimpleChannel networkChannel;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void initNetwork() {
        SimpleChannel network = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Core.MODID, "neska_network"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();
        network.messageBuilder(SCCombustionGeneratorPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(SCCombustionGeneratorPacket::encode)
                .decoder(SCCombustionGeneratorPacket::new)
                .consumerMainThread(SCCombustionGeneratorPacket::handle)
                .add();
        network.messageBuilder(SCChemicalReactorPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(SCChemicalReactorPacket::encode)
                .decoder(SCChemicalReactorPacket::new)
                .consumerMainThread(SCChemicalReactorPacket::handle)
                .add();
        network.messageBuilder(SCCompressorPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(SCCompressorPacket::encode)
                .decoder(SCCompressorPacket::new)
                .consumerMainThread(SCCompressorPacket::handle)
                .add();

        networkChannel = network;
        Core.LOGGER.info("Registered {} packets for mod '{}'", packetId, Core.MODID);
    }

    public static <MSG> void sendToServer(MSG message) {
        networkChannel.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        networkChannel.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        networkChannel.send(PacketDistributor.ALL.noArg(), message);
    }
}
