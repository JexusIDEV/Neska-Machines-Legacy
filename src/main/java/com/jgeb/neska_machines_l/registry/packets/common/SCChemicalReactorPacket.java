package com.jgeb.neska_machines_l.registry.packets.common;

import com.jgeb.neska_machines_l.common.block.be.ChemicalReactorBE;
import com.jgeb.neska_machines_l.common.block.be.CombustionGeneratorBE;
import com.jgeb.neska_machines_l.common.block.menu.ChemicalReactorMenu;
import com.jgeb.neska_machines_l.common.block.menu.CombustionGeneratorMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SCChemicalReactorPacket {
    private final int energy;
    private final BlockPos pos;

    public BlockEntity TBE;

    public SCChemicalReactorPacket(int energy, BlockPos pos) {
        this.energy = energy;
        this.pos = pos;
    }

    public SCChemicalReactorPacket(FriendlyByteBuf buffer) {
        this.energy = buffer.readInt();
        this.pos = buffer.readBlockPos();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(energy);
        buffer.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctxSupply) {
        NetworkEvent.Context ctx = ctxSupply.get();
        ctx.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof ChemicalReactorBE machine) {
                machine.getEnergyManager().setEnergy(energy);
                machine.setChanged();

                if(Minecraft.getInstance().player.containerMenu instanceof ChemicalReactorMenu machineMenu && machineMenu.getBE().getBlockPos().equals(pos)) {
                    machine.getEnergyManager().setEnergy(energy);
                    machine.setChanged();
                }
            }
        });
        return ctx.getPacketHandled();
    }
}
