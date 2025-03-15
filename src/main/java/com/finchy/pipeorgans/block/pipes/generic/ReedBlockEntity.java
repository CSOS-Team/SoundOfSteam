package com.finchy.pipeorgans.block.pipes.generic;

import com.finchy.pipeorgans.block.Generic;
import com.simibubi.create.content.kinetics.steamEngine.SteamJetParticleData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ReedBlockEntity extends GenericPipeBlockEntity {

    public ReedBlockEntity(BlockPos pos, BlockState blockState, BlockEntityType<? extends GenericPipeBlockEntity> blockEntity) {
        super(pos, blockState, blockEntity);
    }

    @Override
    public void createSteamJet(Generic.WhistleSize size) {
        float yOffset = steamJetOffset;
        double yPos = ((double) pitch/pipeBlock.extensionsPerBlock) +1 + yOffset;
        Vec3 v = new Vec3(0, yPos, 0).add(Vec3.atBottomCenterOf(worldPosition));
        Vec3 m = new Vec3(0, 1, 0);
        level.addParticle(new SteamJetParticleData(1), v.x, v.y, v.z, m.x, m.y, m.z);
    }
}
