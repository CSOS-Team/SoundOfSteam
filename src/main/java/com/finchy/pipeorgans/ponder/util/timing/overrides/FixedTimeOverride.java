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
}
