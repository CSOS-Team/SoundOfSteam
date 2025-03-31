package com.finchy.pipeorgans.block.midi;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class StopMasterBlock extends Block implements IBE<StopMasterBlockEntity>, IWrenchable {

    public StopMasterBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Class<StopMasterBlockEntity> getBlockEntityClass() {
        return StopMasterBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends StopMasterBlockEntity> getBlockEntityType() {
        return null;
    }
}
