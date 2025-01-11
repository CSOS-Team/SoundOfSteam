package com.finchy.pipeorgans.block;

import com.finchy.pipeorgans.blockentity.GedecktBlockEntity;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllShapes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.materials.ExperienceBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GedecktBlock extends Block implements EntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WALL = BooleanProperty.create("wall");
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<WhistleSize> SIZE = EnumProperty.create("size", WhistleSize.class);


    // custom hitbox
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        VoxelShape whistle = AllShapes.GEDECKT_MEDIUM; // get base whistle shape (temporarily medium)
        return AllShapes.add(whistle,
                !pState.getValue(WALL) ?
                        AllShapes.BASE_FLOOR : AllShapes.getBase(pState.getValue(FACING)));
                        // if block is not on wall, add BASE_FLOOR, else add correct wall base for direction
    }

    // declare block and default blockstate
    public GedecktBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false)
                .setValue(WALL, false)
                .setValue(SIZE, WhistleSize.MEDIUM));
    }

    // create GEDECKT_BLOCK_ENTITY at block coords upon block placement
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return AllBlockEntities.GEDECKT_BLOCK_ENTITY.get().create(pos, state);
    }

    // define blockstate params
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, WALL, POWERED, SIZE);
    }

    // on right-click
    @Override
    public @NotNull InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide() && pHand == InteractionHand.MAIN_HAND) {
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (be instanceof GedecktBlockEntity blockEntity) {
                pPlayer.sendSystemMessage(Component.literal("BlockEntity has been used"));
                if (pLevel.getBlockState(pPos.above()).getBlock() instanceof ExperienceBlock) {
                    pPlayer.sendSystemMessage(Component.literal("Block above is experience block"));
                }
                return InteractionResult.sidedSuccess(pLevel.isClientSide());
            }
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    // check if placed on fluid tank
    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return FluidTankBlock.isTank(pLevel.getBlockState(pPos.relative(getAttachedDirection(pState))));
    }

    public static Direction getAttachedDirection(BlockState state) {
        return state.getValue(WALL) ? state.getValue(FACING) : Direction.DOWN;
    }

    // set blockstates when placing block
    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Direction face = context.getClickedFace();
        boolean wall = true;
        if (face.getAxis() == Direction.Axis.Y) { // if placed on floor
            face = context.getHorizontalDirection() // face = the opposite of the player's direction
                    .getOpposite();
            wall = false; // not on wall
        }

        BlockState state = super.getStateForPlacement(context)
                .setValue(FACING, face.getOpposite()) // set facing to the opposite of Direction face
                                                      // (this results in orientation being the same as player's, so
                                                      // model is rotated in blockstate json)
                .setValue(POWERED, level.hasNeighborSignal(clickedPos)) // true if power source adjacent, else false
                .setValue(WALL, wall);
        if (!canSurvive(state, level, clickedPos)) // if placed on fluid tank
            return null;
        return state;
    }

    // if neighbour updates
    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
                                boolean isMoving) {
        if (worldIn.isClientSide) // only on serverside
            return;
        boolean previouslyPowered = state.getValue(POWERED);
        if (previouslyPowered != worldIn.hasNeighborSignal(pos))
            worldIn.setBlock(pos, state.cycle(POWERED), 2); // if redstone signal has changed, toggle powered
    }

    // on block placed
    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        FluidTankBlock.updateBoilerState(pState, pLevel, pPos.relative(getAttachedDirection(pState)));
    }

    // on block broken
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
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
