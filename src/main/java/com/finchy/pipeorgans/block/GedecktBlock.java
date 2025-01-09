package com.finchy.pipeorgans.block;

import com.finchy.pipeorgans.blockentity.GedecktBlockEntity;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GedecktBlock extends Block implements EntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WALL = BooleanProperty.create("wall");
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<WhistleSize> SIZE = EnumProperty.create("size", WhistleSize.class);

    public GedecktBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false)
                .setValue(WALL, false)
                .setValue(SIZE, WhistleSize.MEDIUM));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return AllBlockEntities.GEDECKT_BLOCK_ENTITY.get().create(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, WALL, POWERED, SIZE);
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof GedecktBlockEntity blockEntity) {
                player.sendSystemMessage(Component.literal("BlockEntity has been used"));
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return FluidTankBlock.isTank(pLevel.getBlockState(pPos.relative(getAttachedDirection(pState))));
    }

    public static Direction getAttachedDirection(BlockState state) {
        return state.getValue(WALL) ? state.getValue(FACING) : Direction.DOWN;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Direction face = context.getClickedFace();
        boolean wall = true;
        if (face.getAxis() == Direction.Axis.Y) {
            face = context.getHorizontalDirection()
                    .getOpposite();
            wall = false;
        }

        BlockState state = super.getStateForPlacement(context)
                .setValue(FACING, face.getOpposite())
                .setValue(POWERED, level.hasNeighborSignal(clickedPos))
                .setValue(WALL, wall);
        if (!canSurvive(state, level, clickedPos))
            return null;
        return state;
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
                                boolean isMoving) {
        if (worldIn.isClientSide)
            return;
        boolean previouslyPowered = state.getValue(POWERED);
        if (previouslyPowered != worldIn.hasNeighborSignal(pos))
            worldIn.setBlock(pos, state.cycle(POWERED), 2);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        FluidTankBlock.updateBoilerState(pState, pLevel, pPos.relative(getAttachedDirection(pState)));
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        IBE.onRemove(pState, pLevel, pPos, pNewState);
        FluidTankBlock.updateBoilerState(pState, pLevel, pPos.relative(getAttachedDirection(pState)));
    }

    public enum WhistleSize implements StringRepresentable {
        SMALL("small"), MEDIUM("medium"), LARGE("large");

        private final String name;

        WhistleSize(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }
}
