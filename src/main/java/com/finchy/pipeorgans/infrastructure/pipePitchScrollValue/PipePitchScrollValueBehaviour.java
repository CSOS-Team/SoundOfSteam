package com.finchy.pipeorgans.infrastructure.pipePitchScrollValue;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.noteLink.NoteLinkBlockEntity;
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

import java.util.Optional;
import java.util.function.Consumer;

public class PipePitchScrollValueBehaviour extends ScrollValueBehaviour {
    // TODO: Adjust to F#-1 to F#8 range, and use octave groups (F#-1 to F0, F#0 to F1, etc) in the value settings board (prevents the frequency map from complaining and fits better with how most pipes are arranged)
    public PipePitchScrollValueBehaviour(SmartBlockEntity be, ValueBoxTransform slot, Component label) {
        super(label, be, slot);
        between(0, PipePitch.HIGHEST.getPitchIndex());
        setValue(PipePitch.DEFAULT.getPitchIndex());
        withFormatter(n -> defaultFormatValue());
        // This is done like this to be overridable without subclassing
    }

    protected int silentUpdates = 0;

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
        if (blockEntity instanceof NoteLinkBlockEntity) ((NoteLinkBlockEntity) blockEntity).updateHeldClipboard(player);
        playFeedbackSound(this);
    }

    @Override
    public ValueSettings getValueSettings() {
        PipePitch pitch = getPipePitchValue();
        return new ValueSettings(
                Optional.ofNullable(pitch.getOctaveGroup()).map(PipePitch.OctaveGroup::ordinal).orElse(0),
                pitch.pitchClass().getIndexOffset()
        );
    }

    protected MutableComponent formatValueSetting(ValueSettings setting) {
        return Component.literal(fromValueSettings(setting).getName());
    }

    protected PipePitch getPipePitchValue() {
        return PipePitch.fromPitchIndex(getValue());
    }

    protected static PipePitch fromValueSettings(ValueSettings setting) {
        int octaveGroupIndex = setting.row();
        int wrappingOffset = setting.value();
        return PipePitch.fromOctaveGroupAndOffset(octaveGroupIndex + wrappingOffset / PipePitch.PitchClass.values().length, wrappingOffset % PipePitch.PitchClass.values().length);
    }

    public PipePitchScrollValueBehaviour withPipePitchCallback(Consumer<PipePitch> callback) {
        withCallback(n -> callback.accept(PipePitch.fromPitchIndex(n)));
        return this;
    }

    @Override
    public ScrollValueBehaviour withCallback(Consumer<Integer> valueCallback) {
        return super.withCallback(v -> {
            if (silentUpdates > 0) {
                silentUpdates--;
                return;
            }
            valueCallback.accept(v);
        });
    }

    protected String defaultFormatValue() {
        return getPipePitchValue().getName();
    }

    public void setValue(PipePitch pitch) {
        setValue(pitch.getPitchIndex());
    }

    public void setValueSilent(PipePitch pitch) {
        silentUpdates++;
        setValue(pitch.getPitchIndex());
    }
}
