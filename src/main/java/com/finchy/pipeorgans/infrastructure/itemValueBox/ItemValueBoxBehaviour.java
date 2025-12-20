package com.finchy.pipeorgans.infrastructure.itemValueBox;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Set;

public class ItemValueBoxBehaviour extends BlockEntityBehaviour {
    public static final BehaviourType<ItemValueBoxBehaviour> TYPE = new BehaviourType<>();

    public interface InteractionHandler {
        InteractionResult onInteract(ItemStack held, Player player);
    }

    public interface Retriever {
        ItemStack getItemStack();
    }

    public record ItemValueBoxGroup(
            Set<ValueBoxTransform> transforms,
            InteractionHandler interactionHandler,
            Retriever retriever,
            Component label,
            List<MutableComponent> tooltip
    ) {
        public boolean testHit(LevelAccessor level, BlockPos pos, BlockState state, Vec3 localHit) {
            for (ValueBoxTransform transform : transforms) {
                if (transform.testHit(level, pos, state, localHit))
                    return true;
            }
            return false;
        }
    }

    protected List<ItemValueBoxGroup> boxGroups;

    public ItemValueBoxBehaviour(SmartBlockEntity be, List<ItemValueBoxGroup> boxGroups) {
        super(be);
        this.boxGroups = boxGroups;
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    public int testHit(Vec3 hit) {
        Vec3 localHit = hit.subtract(Vec3.atLowerCornerOf(blockEntity.getBlockPos()));
        for (int i = 0; i < boxGroups.size(); i++) {
            if (boxGroups.get(i).testHit(getWorld(), getPos(), blockEntity.getBlockState(), localHit)) {
                return i;
            }
        }
        return -1;
    }

    public InteractionResult onInteract(int boxGroup, ItemStack held, Player player) {
        if (boxGroup < 0 || boxGroup >= boxGroups.size())
            return InteractionResult.PASS;
        return boxGroups.get(boxGroup).interactionHandler.onInteract(held, player);
    }

    public ItemStack getItemStack(int boxGroup) {
        if (boxGroup < 0 || boxGroup >= boxGroups.size())
            return ItemStack.EMPTY;
        return boxGroups.get(boxGroup).retriever.getItemStack();
    }

    public List<ItemValueBoxGroup> getBoxGroups() {
        return boxGroups;
    }
}
