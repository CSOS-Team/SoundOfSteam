package com.finchy.pipeorgans.ponder.util.timing;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TimingMap {
    public interface Calculator extends BiFunction<Integer, Function<Integer, Integer>, Integer> {
        /**
         * Applies the calculation function.
         *
         * @param slot          the slot the calculation is for
         * @param valueAccessor a function to access existing values by channel. Null is returned for unset values.
         * @return the calculated value for the given slot
         */
        @Override
        Integer apply(Integer slot, Function<Integer, Integer> valueAccessor);
    }

    protected Map<Integer, Integer[]> channelMap;
    protected int channelWidth;

    public TimingMap(int channelWidth) {
        this.channelWidth = channelWidth;
        this.channelMap = new HashMap<>();
    }

    public Integer[] getChannel(Integer channel) {
        return channelMap.computeIfAbsent(channel, k -> new Integer[channelWidth]);
    }

    public void set(Integer channel, int slot, int value) {
        if (slot < 0 || slot >= channelWidth)
            throw new IndexOutOfBoundsException("Slot " + slot + " is out of bounds for channel width " + channelWidth);
        getChannel(channel)[slot] = value;
    }

    public int get(Integer channel, int slot) {
        if (slot < 0 || slot >= channelWidth)
            throw new IndexOutOfBoundsException("Slot " + slot + " is out of bounds for channel width " + channelWidth);
        return getChannel(channel)[slot];
    }

    public int getSlotMax(int slot, Integer... channels) {
        int max = 0;
        if (channels == null || channels.length == 0) {
            channels = channelMap.keySet().toArray(new Integer[0]);
        }
        for (Integer channel : channels) {
            Integer[] times = getChannel(channel);
            if (slot < times.length && times[slot] != null) {
                max = Math.max(max, times[slot]);
            }
        }
        return max;
    }

    protected Function<Integer, Integer> slotConstrictedGetter(int slot) {
        return channel -> get(channel, slot);
    }

    public void calculateChannel(Integer channel, Calculator calculator) {
        Integer[] times = getChannel(channel);
        for (int i = 0; i < times.length; i++) {
            times[i] = calculator.apply(i, slotConstrictedGetter(i));
        }
    }

    public int getChannelTotal(Integer channel) {
        Integer[] times = getChannel(channel);
        int total = 0;
        for (Integer time : times) {
            if (time != null) {
                total += time;
            }
        }
        return total;
    }

    public void addMap(TimingMap other, int shiftSlots) {
        Set<Integer> channels = Stream.concat(channelMap.keySet().stream(), other.channelMap.keySet().stream()).collect(Collectors.toSet());
        int ownSlotShift = Math.abs(Math.min(0, shiftSlots));
        int otherSlotShift = Math.max(0, shiftSlots);
        int maxChannelWidth = Math.max(this.channelWidth + ownSlotShift, other.channelWidth + otherSlotShift);
        this.channelWidth = maxChannelWidth;
        for (Integer channel : channels) {
            Integer[] otherTimes = other.getChannel(channel);
            Integer[] thisTimes = this.getChannel(channel);
            Integer[] newTimes = new Integer[maxChannelWidth];
            for (int i = 0; i < maxChannelWidth; i++) {
                newTimes[i] =
                        (i + ownSlotShift > thisTimes.length ? 0 : thisTimes[i + ownSlotShift]) +
                        (i + otherSlotShift > otherTimes.length ? 0 : otherTimes[i + otherSlotShift]);
            }
            this.channelMap.put(channel, newTimes);
        }
    }
}
