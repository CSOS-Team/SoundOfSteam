package com.finchy.pipeorgans.midi.server;

import com.finchy.pipeorgans.ServerConfig;
import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.midi.MusicRollItem;
import com.finchy.pipeorgans.content.midi.rollpuncher.RollPuncherBlockEntity;
import com.finchy.pipeorgans.init.AllBlocks;
import com.simibubi.create.foundation.utility.FilesHelper;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class ServerMidiLoader {

    public static final String serverMidiPath = "midi_files/uploaded";
    private Map<String, MidiUploadEntry> activeUploads;
    private final ObjectArrayList<String> deadEntries = ObjectArrayList.of();

    public class MidiUploadEntry {
        public Level world;
        public BlockPos tablePos;
        public OutputStream stream;
        public long bytesUploaded;
        public long totalBytes;
        public int idleTime;

        public MidiUploadEntry(OutputStream stream, long totalBytes, Level world, BlockPos tablePos) {
            this.stream = stream;
            this.totalBytes = totalBytes;
            this.tablePos = tablePos;
            this.world = world;
            this.bytesUploaded = 0;
            this.idleTime = 0;
        }
    }

    public ServerMidiLoader() {
        activeUploads = new HashMap<>();
    }

    public void tick() {
        // detect timed-out uploads
        int timeout = ServerConfig.midiIdleTimeout;
        for (String upload : activeUploads.keySet()) {
            MidiUploadEntry entry = activeUploads.get(upload);
            if (entry.idleTime++ > timeout) {
                PipeOrgans.LOGGER.warn(".mid file upload timed out: {}", upload);
                deadEntries.add(upload);
            }
        }

        // remove timed-out uploads
        for (String toRemove : deadEntries) {
            cancelUpload(toRemove);
        }
        deadEntries.clear();
    }

    public void shutdown() {
        // close open streams
        new HashSet<>(activeUploads.keySet()).forEach(this::cancelUpload);
    }

    public void handleNewUpload(ServerPlayer player, String midi, long size, BlockPos pos) {
        String playerPathString = serverMidiPath + "/" + player.getGameProfile().getName();
        String playerMidiId = player.getGameProfile().getName() + "/" + midi;
        Path playerPath = Paths.get(playerPathString);
        FilesHelper.createFolderIfMissing(playerPath);

        if (!midi.endsWith(".mid")) {
            PipeOrgans.LOGGER.warn("Attempted .mid file upload with non-supported format: {}", playerMidiId);
            return;
        }

        Path playerMidisPath = Paths.get(serverMidiPath, player.getGameProfile().getName()).toAbsolutePath();
        Path uploadPath = playerMidisPath.resolve(midi).normalize();
        if (!uploadPath.startsWith(playerMidisPath)) {
            PipeOrgans.LOGGER.warn("Attempted .mid file upload with directory escape: {}", playerMidiId);
            return;
        }

        // skip if too big
        if (!validateMidiSizeOnServer(player, size))
            return;

        // skip existing uploads
        if (activeUploads.containsKey(playerMidiId))
            return;

        try {
            // confirm player is at roll puncher
            RollPuncherBlockEntity puncher = getPuncher(player.getCommandSenderWorld(), pos);
            if (puncher == null)
                return;

            // delete midi file with same name
            Files.deleteIfExists(uploadPath);

            long count;
            try (Stream<Path> list = Files.list(playerPath)) {
                count = list.count();
            }
            if (count >= ServerConfig.maxMidiFiles) {
                Stream<Path> list2 = Files.list(playerPath);
                Optional<Path> lastFilePath = list2.filter(f -> !Files.isDirectory(f))
                        .min(Comparator.comparingLong(f -> f.toFile().lastModified()));
                list2.close();
                if (lastFilePath.isPresent())
                    Files.deleteIfExists(lastFilePath.get());
            }

            // open stream
            OutputStream writer = Files.newOutputStream(uploadPath);
            activeUploads.put(playerMidiId, new MidiUploadEntry(writer, size, player.level(), pos));

            // notify block entity
            puncher.startUpload(midi);

        } catch (IOException e) {
            PipeOrgans.LOGGER.error("Exception thrown when starting upload: {}", playerMidiId);
            e.printStackTrace();
        }
    }

    public void handleWriteRequest(ServerPlayer player, String midi, byte[] data) {
        String playerMidiId = player.getGameProfile().getName() + "/" + midi;

        if (activeUploads.containsKey(playerMidiId)) {
            MidiUploadEntry entry = activeUploads.get(playerMidiId);
            entry.bytesUploaded += data.length;

            // size validations
            if (data.length > ServerConfig.maxMidiPacketSize) {
                PipeOrgans.LOGGER.warn("Oversized Midi upload packet received: {}", playerMidiId);
                cancelUpload(playerMidiId);
                return;
            }

            if (entry.bytesUploaded > entry.totalBytes) {
                PipeOrgans.LOGGER.warn("Received more data than expected: {}", playerMidiId);
                cancelUpload(playerMidiId);
                return;
            }

            try {
                entry.stream.write(data);
                entry.idleTime = 0;

                RollPuncherBlockEntity puncher = getPuncher(entry.world, entry.tablePos);
                if (puncher == null)
                    return;
                puncher.uploadingProgress = (float) ((double) entry.bytesUploaded / entry.totalBytes);
                puncher.sendUpdate = true;

            } catch (IOException e) {
                PipeOrgans.LOGGER.error("Exception thrown when uploading .mid file: {}", playerMidiId);
                e.printStackTrace();
                cancelUpload(playerMidiId);
            }
        }
    }

    private void cancelUpload(String playerMidiId) {
        if (!activeUploads.containsKey(playerMidiId))
            return;

        MidiUploadEntry entry = activeUploads.remove(playerMidiId);
        try {
            entry.stream.close();
            Files.deleteIfExists(Paths.get(serverMidiPath, playerMidiId));
            PipeOrgans.LOGGER.warn("Cancelled .mid file upload: {}", playerMidiId);
        } catch (IOException e) {
            PipeOrgans.LOGGER.error("Exception thrown when cancelling upload: {}", playerMidiId);
            e.printStackTrace();
        }

        BlockPos pos = entry.tablePos;
        if (pos == null)
            return;

        RollPuncherBlockEntity puncher = getPuncher(entry.world, pos);
        if (puncher != null)
            puncher.finishUpload();
    }

    public RollPuncherBlockEntity getPuncher(Level world, BlockPos pos) {
		BlockEntity be = world.getBlockEntity(pos);
		if (!(be instanceof RollPuncherBlockEntity puncher))
			return null;
		return puncher;
	}

    public void handleFinishedUpload(ServerPlayer player, String midi) {
        String playerMidiId = player.getGameProfile().getName() + "/" + midi;

        if (activeUploads.containsKey(playerMidiId)) {
            try {
                activeUploads.get(playerMidiId).stream.close();
                MidiUploadEntry removed = activeUploads.remove(playerMidiId);
                Level world = removed.world;
                BlockPos pos = removed.tablePos;

                PipeOrgans.LOGGER.info("New .mid file uploaded: {}", playerMidiId);
                if (pos == null)
                    return;

                BlockState blockState = world.getBlockState(pos);
                if (AllBlocks.ROLL_PUNCHER.get() != blockState.getBlock())
					return;

				RollPuncherBlockEntity puncher = getPuncher(world, pos);
				if (puncher == null)
					return;
				puncher.finishUpload();
				puncher.inventory.setStackInSlot(1, MusicRollItem.create(midi, player.getGameProfile()
					.getName()));

            } catch (IOException e) {
                PipeOrgans.LOGGER.error("Exception thrown when finishing upload: {}", playerMidiId);
                e.printStackTrace();
            }
        }
    }

    private boolean validateMidiSizeOnServer(ServerPlayer player, long size) {
        long maxFileSize = ServerConfig.midiFileSizeLimit;
        if (size > maxFileSize * 1024L) {
            player.sendSystemMessage(Component.literal("Midi upload too large") // make translatable later
                    .append(Component.literal(" (" + size/1024 + " KB).")));
            player.sendSystemMessage(Component.literal("Maximum size is")
                    .append(Component.literal(" " + maxFileSize + " KB")));
            return false;
        }
        return true;
    }

}
