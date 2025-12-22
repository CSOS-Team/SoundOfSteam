package com.finchy.pipeorgans.infrastructure.pipePitchScrollValue;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.util.PipePitch;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBoard;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsFormatter;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Consumer;

public class PipePitchScrollValueBehaviour extends ScrollValueBehaviour {
    // TODO: Adjust to F#-1 to F#8 range, and use octave groups (F#-1 to F0, F#0 to F1, etc) in the value settings board (prevents the frequency map from complaining and fits better with how most pipes are arranged)
    public PipePitchScrollValueBehaviour(SmartBlockEntity be, ValueBoxTransform slot) {
        super(Component.translatable("pipeorgans.pitch.select"), be, slot);
        between(0, PipePitch.HIGHEST.getPitchIndex());
        setValue(PipePitch.DEFAULT.getPitchIndex());
        withFormatter(n -> defaultFormatValue());
        // This is done like this to be overridable without subclassing
    }

    @Override
    public ValueSettingsBoard createBoard(Player player, BlockHitResult hitResult) {
        return new ValueSettingsBoard(
                label,
                PipePitch.PitchClass.values().length,
                1,
                PipePitch.OctaveGroup.getAllFullComponents(),
                new ValueSettingsFormatter(this::formatValueSetting)
        );
    }

    @Override
    public void setValueSettings(Player player, ValueSettings valueSetting, boolean ctrlDown) {
        PipePitch pitch = fromValueSettings(valueSetting);
        setValue(pitch.getPitchIndex());
    }

    protected MutableComponent formatValueSetting(ValueSettings setting) {
        return Component.literal(fromValueSettings(setting).getName());
    }

    protected PipePitch getPipePitchValue() {
        return PipePitch.fromPitchIndex(getValue());
    }

    protected static PipePitch fromValueSettings(ValueSettings setting) {
        int octaveGroupIndex = setting.row();
        int offset = setting.value() % 13;
        PipeOrgans.LOGGER.debug("Creating PipePitch from settings: octaveGroupIndex={}, offset={} (row={}, value={})", octaveGroupIndex, offset, setting.row(), setting.value());
        return PipePitch.fromOctaveGroupAndOffset(octaveGroupIndex + offset / 12, offset % 12);
    }

    public PipePitchScrollValueBehaviour withPipePitchCallback(Consumer<PipePitch> callback) {
        withCallback(n -> callback.accept(PipePitch.fromPitchIndex(n)));
        return this;
    }

    protected String defaultFormatValue() {
        return getPipePitchValue().getName();
    }

    public void setValue(PipePitch pitch) {
        setValue(pitch.getPitchIndex());
    }
}
