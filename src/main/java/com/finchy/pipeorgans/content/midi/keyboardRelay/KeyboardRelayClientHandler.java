package com.finchy.pipeorgans.content.midi.keyboardRelay;

import com.simibubi.create.foundation.utility.ControlsUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Vector;

@OnlyIn(Dist.CLIENT)
public class KeyboardRelayClientHandler {

    public enum Mode {
        IDLE,
        ACTIVE
    }

    public static Mode MODE = Mode.IDLE;
    private static BlockPos activePos;

    public static void activate(BlockPos pos) {
        MODE = Mode.ACTIVE;
        activePos = pos;
    }

    public static void deactivate() {
        MODE = Mode.IDLE;
        activePos = null;

        ControlsUtil.getControls()
                .forEach(kb -> kb.setDown(ControlsUtil.isActuallyPressed(kb)));
    }

    public static void tick() {
        if (MODE != Mode.ACTIVE)
            return;

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null || mc.level == null) {
            deactivate();
            return;
        }

        if (!mc.level.isLoaded(activePos)) {
            deactivate();
            return;
        }

        BlockState state = mc.level.getBlockState(activePos);
        if (!(state.getBlock() instanceof KeyboardRelayBlock)
                || !state.getValue(KeyboardRelayBlock.ACTIVE)) {
            deactivate();
            return;
        }

        //Break your legs
        ControlsUtil.getControls().forEach(kb -> kb.setDown(false));

        //(break em, break em)
        player.setDeltaMovement(0, player.getDeltaMovement().y, 0);
        player.setSprinting(false);
    }
}
