package com.finchy.pipeorgans.ponder.util.timing.overrides;

public record TimingTuple(int... times) {
    public int total() {
        int sum = 0;
        for (int time : times) {
            sum += time;
        }
        return sum;
    }

    public TimingTuple add(TimingTuple other) {
        int maxLength = Math.max(this.times.length, other.times.length);
        int[] newTimes = new int[maxLength];

        for (int i = 0; i < maxLength; i++) {
            newTimes[i] = this.get(i) + other.get(i);
        }

        return new TimingTuple(newTimes);
    }

    public TimingTuple addAll(Iterable<TimingTuple> others) {
        TimingTuple result = this;
        for (TimingTuple other : others) {
            result = result.add(other);
        }
        return result;
    }

    public int get(int index) {
        if (index < times.length) {
            return times[index];
        } else {
            return 0;
        }
    }
}
