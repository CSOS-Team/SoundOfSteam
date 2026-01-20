package com.finchy.pipeorgans.content.midi.keyboardRelay;

import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings("NullableProblems")
public class KeyboardRelayBlock extends Block implements IBE<KeyboardRelayBlockEntity>, IWrenchable {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty TRANSMITTING = BooleanProperty.create("transmitting");
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public KeyboardRelayBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(TRANSMITTING, false)
                .setValue(ACTIVE, false)
        );
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.KBR.get(pState.getValue(FACING));
    }

    // define blockstate params
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, TRANSMITTING, ACTIVE);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction facing = pContext.getHorizontalDirection().getOpposite();
        return Objects.requireNonNull(super.getStateForPlacement(pContext))
                .setValue(FACING, facing);
    }

    @Override
    public Class<KeyboardRelayBlockEntity> getBlockEntityClass() {
        return KeyboardRelayBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends KeyboardRelayBlockEntity> getBlockEntityType() {
        return AllBlockEntities.KEYBOARD_RELAY_BLOCK_ENTITY.get();
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) { // serverside only
            return InteractionResult.SUCCESS;
        }

        if (pHand.equals(InteractionHand.OFF_HAND)) {
            return InteractionResult.SUCCESS;
        }
        if (pPlayer.isShiftKeyDown()) {
            withBlockEntityDo(pLevel, pPos, be -> NetworkHooks.openScreen((ServerPlayer) pPlayer, be, be::sendToMenu));
            return InteractionResult.SUCCESS;

        }  else if (KeyboardRelayBlockEntity.playerInRange(pPlayer, pLevel, pPos)) {
            if (!KeyboardRelayBlockEntity.playerIsUsing(pPlayer)) { // if player is not currently using a keyboard relay
                withBlockEntityDo(pLevel, pPos, be -> be.tryStartUsing(pPlayer)); // start using relay

            } else { // if player IS using a keyboard relay
                withBlockEntityDo(pLevel, pPos, be -> be.tryStopUsing(pPlayer)); // stop using relay

            }

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;

    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pState.is(pNewState.getBlock())) {
            if (!pLevel.isClientSide) {
                withBlockEntityDo(pLevel, pPos, KeyboardRelayBlockEntity::onBlockRemoved);
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }
}
