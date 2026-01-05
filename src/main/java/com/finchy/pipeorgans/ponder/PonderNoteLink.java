package com.finchy.pipeorgans.ponder.ponderWrappers;

import com.finchy.pipeorgans.content.noteLink.NoteLinkBlock;
import com.finchy.pipeorgans.content.noteLink.NoteLinkBlockEntity;
import com.finchy.pipeorgans.ponder.util.smartText.SmartText;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.outliner.Outline;
import net.createmod.catnip.outliner.Outliner;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.level.PonderLevel;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class PonderNoteLink implements Selection {
    protected CreateSceneBuilder scene;
    protected SceneBuildingUtil util;
    protected BlockPos pos;

    public PonderNoteLink(CreateSceneBuilder scene, SceneBuildingUtil util, BlockPos pos) {
        this.scene = scene;
        this.util = util;
        this.pos = pos;
    }

    public Direction getFacing() {
        return scene.getScene().getWorld().getBlockState(pos).getValue(NoteLinkBlock.FACING);
    }

    public Vec3 visualCenter() {
        Direction facingOpp = getFacing().getOpposite();
        float backoff = (facingOpp.getAxis().isHorizontal() ? 1.5f : 1) / 16f;
        return pos.getCenter()
                .add(facingOpp.getStepX() * (.5f - backoff), facingOpp.getStepY() * (.5f - backoff), facingOpp.getStepZ() * (.5f - backoff));
    }

    public Vec3 keySlotPosition() {
        PonderLevel level = scene.getScene().getWorld();
        return pos.getCenter().subtract(.5f, .5f, .5f).add(NoteLinkBlockEntity.KEY_SLOT_TRANSFORM.getLocalOffset(level, pos, level.getBlockState(pos)));
    }

    public Vec3 pitchSlotPosition() {
        PonderLevel level = scene.getScene().getWorld();
        return pos.getCenter().subtract(.5f, .5f, .5f).add(NoteLinkBlockEntity.PITCH_SLOT_TRANSFORM.getLocalOffset(level, pos, level.getBlockState(pos)));
    }

    public void showKeySlot(PonderPalette color, int duration) {
        Direction facing = getFacing();
        scene.overlay().showFilterSlotInput(keySlotPosition(), facing, duration);
    }

    public void showPitchSlot(PonderPalette color, int duration) {
        Direction facing = getFacing();
        Vec3 slotPos = pitchSlotPosition();
        scene.overlay().chaseBoundingBoxOutline(color, this, new AABB(slotPos, slotPos).inflate(3f / 16f, 3f / 16f, 0), duration);
    }

    public SmartText textOnCenter(String text, boolean keyed, boolean nearTarget) {
        return SmartText.plainPointing(text, visualCenter(), keyed, nearTarget);
    }

    public SmartText textOnKeySlot(String text, boolean keyed, boolean nearTarget) {
        return SmartText.plainPointing(text, keySlotPosition(), keyed, nearTarget);
    }

    public SmartText textOnPitchSlot(String text, boolean keyed, boolean nearTarget) {
        return SmartText.plainPointing(text, pitchSlotPosition(), keyed, nearTarget);
    }

    public SmartText textOnPitchSlot(String text, boolean keyed, boolean nearTarget, PonderPalette color) {
        return SmartText.coloredPointing(text, pitchSlotPosition(), color, keyed, nearTarget);
    }

    @Override
    public Selection add(Selection other) {
        return util.select().position(pos).add(other);
    }

    @Override
    public Selection substract(Selection other) {
        return util.select().position(pos).substract(other);
    }

    @Override
    public Selection copy() {
        return util.select().position(pos);
    }

    @Override
    public Vec3 getCenter() {
        return pos.getCenter();
    }

    @Override
    public Outline.OutlineParams makeOutline(Outliner outliner, Object slot) {
        return util.select().position(pos).makeOutline(outliner, slot);
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public @NotNull Iterator<BlockPos> iterator() {
        return util.select().position(pos).iterator();
    }

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param blockPos the input argument
     * @return {@code true} if the input argument matches the predicate,
     * otherwise {@code false}
     */
    @Override
    public boolean test(BlockPos blockPos) {
        return util.select().position(pos).test(blockPos);
    }
}
