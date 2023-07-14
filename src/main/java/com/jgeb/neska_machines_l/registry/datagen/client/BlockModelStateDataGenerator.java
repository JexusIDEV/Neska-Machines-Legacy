package com.jgeb.neska_machines_l.registry.datagen.client;

import com.jgeb.neska_machines_l.Core;
import com.jgeb.neska_machines_l.registry.BlockReg;
import com.jgeb.neska_machines_l.util.datas.NeskaBooleansProperties;
import com.jgeb.neska_machines_l.util.lang.NeskaLangTranslate;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.function.Function;

public class BlockModelStateDataGenerator extends BlockStateProvider {

    public BlockModelStateDataGenerator(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Core.MODID, exFileHelper);
    }

    private void orientedBlock(Block block, Function<BlockState, ModelFile> modelFunc) {
        getVariantBuilder(block)
                .forAllStates(state -> {
                    Direction dir = state.getValue(BlockStateProperties.FACING);
                    return ConfiguredModel.builder()
                            .modelFile(modelFunc.apply(state))
                            .rotationX(dir.getAxis() == Direction.Axis.Y ?  dir.getAxisDirection().getStep() * -90 : 0)
                            .rotationY(dir.getAxis() != Direction.Axis.Y ? ((dir.get2DDataValue() + 2) % 4) * 90 : 0)
                            .build();
                });
    }

    private void createBlockMachine(Block block, ArrayList<ResourceLocation> state1Textures, ArrayList<ResourceLocation> state2Textures) {
        BlockModelBuilder originalBlock = models().cube(NeskaLangTranslate.getKeyPathRegistry(block) + "_off", state1Textures.get(0), state1Textures.get(1), state1Textures.get(2), state1Textures.get(3), state1Textures.get(4), state1Textures.get(5));
        BlockModelBuilder statedBlock = models().cube(NeskaLangTranslate.getKeyPathRegistry(block) + "_on", state2Textures.get(0), state2Textures.get(1), state2Textures.get(2), state2Textures.get(3), state2Textures.get(4), state2Textures.get(5));
        orientedBlock(block, states -> {
            if(states.getValue(NeskaBooleansProperties.WORKING)) {
                return statedBlock;
            } else {
                return originalBlock;
            }
        });
    }

    @Override
    protected void registerStatesAndModels() {

    }
}
