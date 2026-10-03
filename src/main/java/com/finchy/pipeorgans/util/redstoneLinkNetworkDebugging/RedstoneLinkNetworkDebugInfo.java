package com.finchy.pipeorgans.util.redstoneLinkNetworkDebugging;

import com.simibubi.create.Create;
import com.simibubi.create.content.redstone.link.IRedstoneLinkable;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler;
import net.createmod.catnip.data.Couple;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RedstoneLinkNetworkDebugInfo extends ConcurrentHashMap<Couple<ItemStack>, Set<RedstoneLinkNetworkDebugInfo.RedstoneLinkableDebugInfo>> {
    public record RedstoneLinkableDebugInfo(
            boolean isReceiver,
            BlockPos pos,
            String blockName,
            int objectHashCode, // to help identify the specific instance and debug possible edge cases where multiple IRedstoneLinkable instances exist for the same block position
            Couple<ItemStack> currentFrequency
    )
    {
        public static RedstoneLinkableDebugInfo fromLinkable(IRedstoneLinkable linkable, LevelAccessor level) {
            BlockEntity be = level.getBlockEntity(linkable.getLocation());



            return new RedstoneLinkableDebugInfo(
                    linkable.isListening(),
                    linkable.getLocation(),
                    be != null ? be.getBlockState().getBlock().getName().getString() : "UNKNOWN",
                    linkable.hashCode(),
                    linkable.getNetworkKey().map(RedstoneLinkNetworkHandler.Frequency::getStack)
            );
        }

        public static RedstoneLinkableDebugInfo fromBuffer(FriendlyByteBuf buffer) {
            return new RedstoneLinkableDebugInfo(
                    buffer.readBoolean(),
                    buffer.readBlockPos(),
                    buffer.readUtf(),
                    buffer.readInt(),
                    Couple.create(buffer::readItem)
            );
        }

        public void writeToBuffer(FriendlyByteBuf buffer) {
            buffer.writeBoolean(isReceiver);
            buffer.writeBlockPos(pos);
            buffer.writeUtf(blockName);
            buffer.writeInt(objectHashCode);
            buffer.writeItem(currentFrequency.getFirst());
            buffer.writeItem(currentFrequency.getSecond());
        }
    }

    public static RedstoneLinkNetworkDebugInfo fromBuffer(FriendlyByteBuf buffer) {
        RedstoneLinkNetworkDebugInfo info = new RedstoneLinkNetworkDebugInfo();
        int mapSize = buffer.readInt();
        for (int i = 0; i < mapSize; i++) {
            Couple<ItemStack> key = Couple.create(buffer::readItem);
            int setSize = buffer.readInt();
            Set<RedstoneLinkableDebugInfo> valueSet = ConcurrentHashMap.newKeySet();
            for (int j = 0; j < setSize; j++) {
                valueSet.add(RedstoneLinkableDebugInfo.fromBuffer(buffer));
            }
            info.put(key, valueSet);
        }
        return info;
    }

    public static RedstoneLinkNetworkDebugInfo forLevel(LevelAccessor level) {
        RedstoneLinkNetworkDebugInfo info = new RedstoneLinkNetworkDebugInfo();
        RedstoneLinkNetworkHandler handler = Create.REDSTONE_LINK_NETWORK_HANDLER;
        var networks = handler.networksIn(level);
        for (var network : networks.entrySet()) {
            Couple<ItemStack> frequency = network.getKey().map(f -> {
                ItemStack stack = f.getStack().copy();
                stack.setCount(1);
                return stack;
            });
            Set<RedstoneLinkableDebugInfo> linkablesInfo = ConcurrentHashMap.newKeySet();
            for (IRedstoneLinkable linkable : network.getValue()) {
                linkablesInfo.add(RedstoneLinkableDebugInfo.fromLinkable(linkable, level));
            }
            info.put(frequency, linkablesInfo);
        }
        return info;
    }

    public String toText() {
        StringBuilder sb = new StringBuilder();
        sb.append("Redstone Link Networks:\n");
        for (var entry : this.entrySet()) {
            Couple<ItemStack> frequency = entry.getKey();
            Set<RedstoneLinkableDebugInfo> linkables = entry.getValue();
            sb.append("\tFrequency: [")
                    .append(frequency.getFirst().getHoverName().getString())
                    .append("] , [")
                    .append(frequency.getSecond().getHoverName().getString())
                    .append("]\n");
            for (RedstoneLinkableDebugInfo linkable : linkables) {
                String pos = "(" + linkable.pos.getX() + ", " + linkable.pos.getY() + ", " + linkable.pos.getZ() + ")";
                String hexHash = Integer.toHexString(linkable.objectHashCode);
                hexHash = hexHash.length() < 8 ? "00000000".substring(hexHash.length()) + hexHash : hexHash;

                sb.append("\t\t")
                        .append(linkable.blockName)
                        .append(" at ")
                        .append(pos)
                        .append(" (")
                        .append(linkable.isReceiver ? "Receiver" : "Transmitter")
                        .append(") [")
                        .append(hexHash)
                        .append("]\n")
                        .append("\t\t\tCurrent Frequency: ")
                        .append("[")
                        .append(linkable.currentFrequency.getFirst().getHoverName().getString())
                        .append("] , [")
                        .append(linkable.currentFrequency.getSecond().getHoverName().getString())
                        .append("]\n");
            }
        }
        return sb.toString();
    }

    public void writeToBuffer(FriendlyByteBuf buffer) {
        buffer.writeInt(this.size());
        for (var entry : this.entrySet()) {
            Couple<ItemStack> frequency = entry.getKey();
            Set<RedstoneLinkableDebugInfo> linkables = entry.getValue();
            buffer.writeItem(frequency.getFirst());
            buffer.writeItem(frequency.getSecond());
            buffer.writeInt(linkables.size());
            for (RedstoneLinkableDebugInfo linkable : linkables) {
                linkable.writeToBuffer(buffer);
            }
        }
    }
}
