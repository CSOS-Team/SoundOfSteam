package com.finchy.pipeorgans.content.pipes.nasard;

import com.finchy.pipeorgans.ClientConfig;
import com.finchy.pipeorgans.content.pipes.generic.GenericWhistleProperties;
import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlockEntity;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class NasardBlockEntity extends GenericPipeBlockEntity {
    public NasardBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState, AllBlockEntities.NASARD_BLOCK_ENTITY);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        String[] pitches = CreateLang.translateDirect("generic.notes")
                .getString()
                .split(";");
        int displayPitch = ClientConfig.displayMutationSoundingPitch? pitch+5 : pitch;
        CreateLang.translate("generic.pitch", pitches[displayPitch%12 % pitches.length]).forGoggles(tooltip);
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    protected NasardSoundInstance soundInstance;

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void tickAudio(GenericWhistleProperties.WhistleSize size, boolean powered) {
        if (!powered) {
            if (soundInstance != null) {
                soundInstance.fadeOut();
                soundInstance = null;
            }
            return;
        }

        float f = (float) Math.pow(2, -pitch / 12.0);
        boolean particle = level.getGameTime() % 8 == 0;
        Vec3 eyePosition = Minecraft.getInstance().cameraEntity.getEyePosition();
        float maxVolume = (float) Mth.clamp((64 - eyePosition.distanceTo(Vec3.atCenterOf(worldPosition))) / 64, 0, 1);

        if (soundInstance == null || soundInstance.isStopped() || soundInstance.getOctave() != size) {
            Minecraft.getInstance()
                    .getSoundManager()
                    .play(soundInstance = new NasardSoundInstance(size, worldPosition));

            AllSoundEvents.WHISTLE_CHIFF.playAt(level, worldPosition, maxVolume * .1f, f, false);

            particle = true;
        }

        soundInstance.keepAlive();
        soundInstance.setPitch(f);

        if (!particle)
            return;

        createSteamJet(size);
    }
}
