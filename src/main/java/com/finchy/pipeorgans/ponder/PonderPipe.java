package com.finchy.pipeorgans.ponder;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.pipes.generic.*;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class PonderPipe<TS extends Enum<TS> & ExtensionShapes.IExtensionShape<TS> & StringRepresentable> {
    public record Transition(Optional<Integer> duration, int bufferTime, int demonstratePitchDuration) {

        public Transition(int duration, int bufferTime, int demonstratePitchDuration) {
            this(Optional.of(duration), bufferTime, demonstratePitchDuration);
        }

        public Transition(int bufferTime, int demonstratePitchDuration) {
            this(Optional.empty(), bufferTime, demonstratePitchDuration);
        }

        public boolean hasDemonstratePitch() {
            return demonstratePitchDuration > 0;
        }

        public boolean hasBufferTime() {
            return bufferTime > 0;
        }

        public int durationOrDefault(int defaultDuration) {
            return duration.orElse(defaultDuration);
        }

        public static final Transition NONE = new Transition(Optional.empty(), 0, 0);
    }

    protected CreateSceneBuilder scene;
    protected SceneBuildingUtil util;
    protected BlockPos pos;
    protected BlockState state; // not used in rendering, just for tracking changes to the pipe
    protected int currentHeight;
    protected GenericPipeBlock pipeBlock;
    protected GenericExtensionBlock<TS> extensionBlock;
    protected Transition defaultTransition;

    protected static final int DEFAULT_DURATION = PonderTimings.BUILD_STEP;

    public PonderPipe(CreateSceneBuilder scene, SceneBuildingUtil util,
                      BlockPos pos, BlockState state,
                      int initialHeight,
                      GenericPipeBlock block, GenericExtensionBlock<TS> extensionBlock,
                      Transition defaultTransition) {
        this.scene = scene;
        this.util = util;
        this.pos = pos;
        this.state = state;
        this.currentHeight = initialHeight;
        this.pipeBlock = block;
        this.extensionBlock = extensionBlock;
        this.defaultTransition = defaultTransition;
    }

    public PonderPipe(CreateSceneBuilder scene, SceneBuildingUtil util,
                      BlockPos pos,
                      PipeSize initialSize, Direction facing, boolean wall, boolean powered,
                      int initialHeight,
                      GenericPipeBlock block, GenericExtensionBlock<TS> extensionBlock,
                      Transition defaultTransition) {
        this(scene, util,
                pos, block.defaultBlockState().setValue(GenericPipeBlock.SIZE, initialSize)
                        .setValue(GenericPipeBlock.FACING, facing)
                        .setValue(GenericPipeBlock.WALL, wall)
                        .setValue(GenericPipeBlock.POWERED, powered), initialHeight, block, extensionBlock, defaultTransition);
    }

    protected int getMainDuration(Transition t) {
        return t.durationOrDefault(DEFAULT_DURATION);
    }

    protected void tryDemonstratePitch(Transition t) {
        if (t.hasDemonstratePitch()) {
            scene.idle(PonderTimings.CONTEXT_INFO_BUFFER);
            showPitchOverlay(t.demonstratePitchDuration);
        }
    }

    protected void tryBufferTime(Transition t) {
        if (t.hasBufferTime())
            scene.idle(t.bufferTime);
    }

    protected void tryBothTransitionParts(Transition t) {
        tryDemonstratePitch(t);
        tryBufferTime(t);
    }

    public Selection selectPipe() {
        return util.select().fromTo(pos, pos.above(getExtensionBlockHeight()));
    }

    public Selection getMaximumPipeSelection() {
        return util.select().fromTo(pos, pos.above(getMaxHeight(pipeBlock)));
    }

    public void showPipe(Direction dir) {
        scene.world().showSection(getMaximumPipeSelection(), dir);
        scene.idle(PonderTimings.BUILD_STEP);
    }

    public void cyclePipeSize(Transition transition, boolean reverse) {
        PipeSize newSize = PipeSize.values()[(getSize().ordinal() + (reverse ? -1 : 1) + PipeSize.values().length) % PipeSize.values().length];
        setSize(newSize);

        scene.world().modifyBlock(pos, bs -> bs.setValue(GenericPipeBlock.SIZE, newSize), false);

        tryBothTransitionParts(transition);
        for (int i=1; i<=getExtensionBlockHeight(); i++) {
            scene.world().cycleBlockProperty(pos.above(i), GenericPipeBlock.SIZE);
        }
    }

    public void cyclePipeSize(boolean reverse) {
        cyclePipeSize(defaultTransition, reverse);
    }

    public void cyclePipeSize(Transition transition) {
        cyclePipeSize(transition, false);
    }

    public void cyclePipeSize() {
        cyclePipeSize(false);
    }

    public void incrementPipeHeight(Transition transition) {
        if (currentHeight >= 12)
            return;
        Direction pipeToExtensions = pipeBlock.getExtensionDirection(state);

        BlockPos extensionPos = pos.relative(pipeToExtensions, getExtensionBlockHeight());

        if (currentHeight % pipeBlock.extensionsPerBlock() == 0) { // if a new block must be placed
            if (currentHeight > 0) // if there are any other extensions
                scene.world().modifyBlock(extensionPos, bs -> bs.setValue(extensionBlock.SHAPE,
                        bs.getValue(extensionBlock.SHAPE).getConnected() // set the old extension's shape to connected
                ), false);

            BlockState toPlace = extensionBlock.defaultBlockState().setValue(GenericExtensionBlock.SIZE, getSize());
            if (extensionBlock.isDirectional())
                toPlace = toPlace.setValue(GenericExtensionBlock.FACING, getFacing());
            scene.world().setBlock(extensionPos.relative(pipeToExtensions), toPlace, false);


        } else { // if another extension can be added without placing a new block
            PipeOrgans.LOGGER.debug("EXTENDING EXISTING");
            scene.world().modifyBlock(extensionPos, bs -> {
                BlockState toPlace = bs.cycle(extensionBlock.SHAPE);  // cycle to the next shape
                if (extensionBlock.isDirectional()) // only set direction if the extension is directional
                    toPlace = toPlace.setValue(GenericExtensionBlock.FACING, getFacing()); // (would cause a crash otherwise)
                return toPlace;
            }, false);

        }

        scene.world().modifyBlockEntity(pos, pipeBlock.getBlockEntityClass(), GenericPipeBlockEntity::updatePitch); // update the block entity
        currentHeight++;
        tryBothTransitionParts(transition);
    }

    public void incrementPipeHeight() {
        incrementPipeHeight(defaultTransition);
    }

    protected static int getMaxHeight(GenericPipeBlock block) {
        return Math.min(12 / block.extensionsPerBlock(), 12);
    }

    protected static <T extends Enum<T>> T getPreviousEnumValue(T[] values, T current) {
        int index = current.ordinal() - 1;
        if (index < 0) {
            index = values.length - 1;
        }
        return values[index];
    }

    public int getExtensionBlockHeight() {
        return (int) Math.ceil(getExtensionRealHeight());
    }

    public double getExtensionRealHeight() {
        return ((float)currentHeight/ pipeBlock.extensionsPerBlock());
    }

    public PipeSize getSize() {
        return state.getValue(GenericPipeBlock.SIZE);
    }

    public void setSize(PipeSize size) {
        state.setValue(GenericPipeBlock.SIZE, size);
    }

    public Direction getFacing() {
        return state.getValue(GenericPipeBlock.FACING);
    }

    public void setFacing(Direction facing) {
        state.setValue(GenericPipeBlock.FACING, facing);
    }

    public boolean getPowered() {
        return state.getValue(GenericPipeBlock.POWERED);
    }

    public void setPowered(boolean powered) {
        state.setValue(GenericPipeBlock.POWERED, powered);
    }

    public boolean getWall() {
        return state.getValue(GenericPipeBlock.WALL);
    }

    public void setWall(boolean wall) {
        state.setValue(GenericPipeBlock.WALL, wall);
    }

    protected final static String[] NOTE_NAMES = {"F#", "F", "E", "D#", "D", "C#", "C", "B", "A#", "A", "G#", "G", "F#"};
    public String getNote() {
        int octave = 6 - getSize().ordinal() - currentHeight / 7;
        return NOTE_NAMES[currentHeight] + octave;
    }

    public void showPitchOverlay(int duration) {
        scene.overlay().showText(duration)
                .text(getNote())
                .colored(PonderPalette.GREEN)
                .pointAt(pos.getCenter())
                .placeNearTarget();
    }
}
