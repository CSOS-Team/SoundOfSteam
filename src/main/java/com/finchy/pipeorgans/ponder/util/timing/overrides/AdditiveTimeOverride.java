package com.finchy.pipeorgans.ponder.util.timing.overrides;

public record AdditiveTimeOverride(int extraTime) implements TimingOverride {
    @Override
    public int getMinimum(int originalMinimum) {
        return originalMinimum + extraTime;
    }

    @Override
    public int getFixed(int originalFixed) {
        return originalFixed + extraTime;
    }
}
