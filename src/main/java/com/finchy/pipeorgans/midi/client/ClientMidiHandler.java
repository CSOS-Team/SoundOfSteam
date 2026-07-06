package com.finchy.pipeorgans.midi.client;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.network.AllPackets;
import com.finchy.pipeorgans.network.packet.kbr.KBRMidiMessagePacket;
import com.finchy.pipeorgans.network.packet.kbr.KBRStopUsingPacket;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import org.lwjgl.glfw.GLFW;

import javax.sound.midi.MidiMessage;

public class ClientMidiHandler {
    public static final MidiInputDeviceManager inputDeviceManager = new MidiInputDeviceManager();
    public static BlockPos KBRPos;
    
    public static void activateInKBR(BlockPos KBRAt) {
        KBRPos = KBRAt;
        PipeOrgans.LOGGER.debug("***activateInKBR()***");
    }
    
    public static void deactivateInKBR() {
        if (inKBR()) {
            onReset();
        }
        PipeOrgans.LOGGER.debug("***deactivateInKBR()***");
    }
    
    public static boolean inKBR() {
        return KBRPos != null;
    }
    
    protected static void onReset() {
        if (inKBR()) {
            AllPackets.getChannel().sendToServer(new KBRStopUsingPacket(KBRPos));
        }
        KBRPos = null;
    }
    
    public static void tick() {
        if (!inKBR())
            return;
        
        Minecraft mc = Minecraft.getInstance();
        
        if (AllBlocks.KEYBOARD_RELAY.get()
                .getBlockEntityOptional(mc.level, KBRPos)
                .map(be -> !be.isUsedBy(mc.player))
                .orElse(true)) {
            deactivateInKBR();
            return;
        }
        
        if (mc.screen != null) {
            onReset();
        }

        if (InputConstants.isKeyDown(mc.getWindow()
                .getWindow(), GLFW.GLFW_KEY_ESCAPE)) {
            onReset();
        }
    }
    
    public static void handleMessage(MidiMessage mm) {
        if (inKBR())
            AllPackets.getChannel().sendToServer(new KBRMidiMessagePacket(mm, KBRPos));
    }
}
