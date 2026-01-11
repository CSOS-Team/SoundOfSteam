package com.finchy.pipeorgans.ponder;

import com.finchy.pipeorgans.content.pipes.generic.*;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.*;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;

import java.util.Optional;

public class PonderPipe<TS extends Enum<TS> & ExtensionShapes.IExtensionShape & StringRepresentable> {
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
    protected PipeSize currentSize;
    protected int currentHeight;
    protected int maxHeight;
    protected GenericPipeBlock block;
    protected GenericExtensionBlock<TS> extensionBlock;
    protected Transition defaultTransition;

    public PonderPipe(CreateSceneBuilder scene, SceneBuildingUtil util, BlockPos pos, int initialHeight, PipeSize initialSize, GenericPipeBlock block, GenericExtensionBlock<TS> extensionBlock, Transition defaultTransition, int maxHeight) {
        this.scene = scene;
        this.util = util;
        this.pos = pos;
        this.currentHeight = initialHeight;
        this.maxHeight = Math.min(maxHeight, 12);
        this.currentSize = initialSize;
        this.block = block;
        this.extensionBlock = extensionBlock;
        this.defaultTransition = defaultTransition;
    }

    protected static final int DEFAULT_DURATION = PonderTimings.BUILD_STEP;

    public PonderPipe(CreateSceneBuilder scene, SceneBuildingUtil util, BlockPos pos, int initialHeight, PipeSize initialSize, GenericPipeBlock block, GenericExtensionBlock<TS> extensionBlock, Transition defaultTransition) {
        this(scene, util, pos, initialHeight, initialSize, block, extensionBlock, defaultTransition, getMaxHeight(block));
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
        return util.select().fromTo(pos, pos.above(maxHeight));
    }

    public void showPipe(Direction dir) {
        scene.world().showSection(getMaximumPipeSelection(), dir);
        scene.idle(PonderTimings.BUILD_STEP);
    }

    public void changePipeSize(PipeSize newSize, boolean particles, Transition transition) {
        this.currentSize = newSize;
        scene.world().modifyBlock(pos, bs -> bs.setValue(GenericPipeBlock.SIZE, newSize), particles);
        tryBothTransitionParts(transition);
    }

    public void changePipeSize(PipeSize newSize, boolean particles) {
        changePipeSize(newSize, particles, defaultTransition);
    }

    public void cyclePipeSize(Transition transition, boolean reverse) {
        PipeSize newSize = PipeSize.values()[(currentSize.ordinal() + (reverse ? -1 : 1) + PipeSize.values().length) % PipeSize.values().length];
        this.currentSize = newSize;
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

    public void changePipeHeight(int newHeight, boolean particles, Transition transition) {
        for (int i = currentHeight; i < newHeight; i++) {
            incrementPipeHeight(Transition.NONE);
        }
        for (int i = currentHeight; i > newHeight; i--) {
            decrementPipeHeight(particles, Transition.NONE);
        }
        tryBothTransitionParts(transition);
    }

    public void changePipeHeight(int newHeight, boolean particles) {
        changePipeHeight(newHeight, particles, defaultTransition);
    }

    public void incrementPipeHeight(Transition transition) {
        if (currentHeight >= 12)
            return;
        BlockPos extensionPos = pos.above(getExtensionBlockHeight());
        if (block instanceof SinglePipeBlock) {
            if (currentHeight > 0)
                scene.world().modifyBlock(extensionPos, bs -> bs.setValue(SingleExtensionBlock.SHAPE, ExtensionShapes.Single.SINGLE_CONNECTED), false);
            scene.world().setBlock(extensionPos.above(), extensionBlock.defaultBlockState().setValue(SingleExtensionBlock.SHAPE, ExtensionShapes.Single.SINGLE).setValue(GenericPipeBlock.SIZE, currentSize), false);
        } else if (block instanceof DoublePipeBlock) {
            if (currentHeight % 2 == 0) { // cross-block change required
                if (currentHeight > 0)
                    scene.world().modifyBlock(extensionPos, bs -> bs.setValue(DoubleExtensionBlock.SHAPE, ExtensionShapes.Double.DOUBLE_CONNECTED), false);
                scene.world().setBlock(extensionPos.above(), extensionBlock.defaultBlockState().setValue(DoubleExtensionBlock.SHAPE, ExtensionShapes.Double.SINGLE).setValue(GenericPipeBlock.SIZE, currentSize), false);
            } else {
                scene.world().modifyBlock(extensionPos, bs -> bs.cycle(DoubleExtensionBlock.SHAPE), false);
            }
        } else if (block instanceof QuadruplePipeBlock) {
            if (currentHeight % 4 == 0) { // cross-block change required
                if (currentHeight > 0)
                    scene.world().modifyBlock(extensionPos, bs -> bs.setValue(QuadrupleExtensionBlock.SHAPE, ExtensionShapes.Quadruple.QUAD_CONNECTED), false);
                scene.world().setBlock(extensionPos.above(), extensionBlock.defaultBlockState().setValue(QuadrupleExtensionBlock.SHAPE, ExtensionShapes.Quadruple.SINGLE).setValue(GenericPipeBlock.SIZE, currentSize), false);
            } else {
                scene.world().modifyBlock(extensionPos, bs -> bs.cycle(QuadrupleExtensionBlock.SHAPE), false);
            }
        }
        scene.world().modifyBlockEntity(pos, block.getBlockEntityClass(), GenericPipeBlockEntity::updatePitch);
        currentHeight += 1;
        tryBothTransitionParts(transition);
    }

    public void incrementPipeHeight() {
        incrementPipeHeight(defaultTransition);
    }

    public void decrementPipeHeight(boolean particles, Transition transition) {
        if (currentHeight <= 0)
            return;
        BlockPos extensionPos = pos.above(getExtensionBlockHeight());
        if (block instanceof SinglePipeBlock) {
            scene.world().destroyBlock(extensionPos);
            if (currentHeight > 1)
                scene.world().modifyBlock(extensionPos.below(), bs -> bs.setValue(SingleExtensionBlock.SHAPE, ExtensionShapes.Single.SINGLE), false);
        } else if (block instanceof DoublePipeBlock) {
            if (currentHeight % 2 == 1) { // cross-block change required
                scene.world().destroyBlock(extensionPos);
                if (currentHeight > 1)
                    scene.world().modifyBlock(extensionPos.below(), bs -> bs.setValue(DoubleExtensionBlock.SHAPE, ExtensionShapes.Double.DOUBLE), false);
            } else {
                scene.world().modifyBlock(extensionPos, bs -> bs.setValue(DoubleExtensionBlock.SHAPE, getPreviousEnumValue(ExtensionShapes.Double.values(), bs.getValue(DoubleExtensionBlock.SHAPE))), particles);
            }
        } else if (block instanceof QuadruplePipeBlock) {
            if (currentHeight % 4 == 1) { // cross-block change required
                scene.world().destroyBlock(extensionPos);
                if (currentHeight > 1)
                    scene.world().modifyBlock(extensionPos.below(), bs -> bs.setValue(QuadrupleExtensionBlock.SHAPE, ExtensionShapes.Quadruple.QUAD), false);
            } else {
                scene.world().modifyBlock(extensionPos, bs -> bs.setValue(QuadrupleExtensionBlock.SHAPE, getPreviousEnumValue(ExtensionShapes.Quadruple.values(), bs.getValue(QuadrupleExtensionBlock.SHAPE))), particles);
            }
        }
        scene.world().modifyBlockEntity(pos, block.getBlockEntityClass(), GenericPipeBlockEntity::updatePitch);
        currentHeight -= 1;
        tryBothTransitionParts(transition);
    }

    public void decrementPipeHeight(boolean particles) {
        decrementPipeHeight(particles, defaultTransition);
    }

    public void destroyTopPipeExtension(Transition transition) {
        if (currentHeight <= 0)
            return;
        BlockPos extensionPos = pos.above(getExtensionBlockHeight());
        scene.world().destroyBlock(extensionPos);
        currentHeight -= 1;
        if (block instanceof SinglePipeBlock) {
            if (currentHeight > 1)
                scene.world().modifyBlock(extensionPos.below(), bs -> bs.setValue(SingleExtensionBlock.SHAPE, ExtensionShapes.Single.SINGLE), false);
        } else if (block instanceof DoublePipeBlock) {
            if (currentHeight > 2)
                scene.world().modifyBlock(extensionPos.below(), bs -> bs.setValue(DoubleExtensionBlock.SHAPE, ExtensionShapes.Double.DOUBLE), false);
            currentHeight = (currentHeight / 2) * 2; // round down to nearest even number
        } else if (block instanceof QuadruplePipeBlock) {
            if (currentHeight > 4)
                scene.world().modifyBlock(extensionPos.below(), bs -> bs.setValue(QuadrupleExtensionBlock.SHAPE, ExtensionShapes.Quadruple.QUAD), false);
            currentHeight = (currentHeight / 4) * 4; // round down to nearest multiple of 4
        }
        tryBothTransitionParts(transition);
    }

    public void destroyTopPipeExtension() {
        destroyTopPipeExtension(defaultTransition);
    }

    protected static int getMaxHeight(GenericPipeBlock block) {
        if (block instanceof SinglePipeBlock) {
            return 12;
        } else if (block instanceof DoublePipeBlock) {
            return 6;
        } else if (block instanceof QuadruplePipeBlock) {
            return 3;
        }
        return 12;
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
        return ((float)currentHeight/block.EPB);
    }

    public double getExtensionCenterOffset() {
        if (block instanceof SinglePipeBlock) {
            return .5f;
        } else if (block instanceof DoublePipeBlock) {
            return .25f;
        } else if (block instanceof QuadruplePipeBlock) {
            return .125f;
        }
        return .5f;
    }

    protected final static String[] NOTE_NAMES = {"F#", "F", "E", "D#", "D", "C#", "C", "B", "A#", "A", "G#", "G", "F#"};
    public String getNote() {
        int octave = 6 - currentSize.ordinal() - currentHeight / 7;
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
