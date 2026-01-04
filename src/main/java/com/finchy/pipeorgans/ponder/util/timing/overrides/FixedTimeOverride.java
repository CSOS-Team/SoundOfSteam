package com.finchy.pipeorgans.ponder.util.timing.overrides;

public record FixedTimeOverride(int fixed) implements TimingOverride {
    @Override
    public int getMinimum(int originalMinimum) {
        return fixed;
    }

    @Override
    public int getFixed(int originalFixed) {
        return fixed;
    }

    @Override
    public int getAdded(int originalHighest) {
        return 0;
    }
}
