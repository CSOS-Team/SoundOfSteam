package com.finchy.pipeorgans.ponder.util.timing.overrides;

public interface TimingOverride {
    int getMinimum(int originalMinimum);
    int getFixed(int originalFixed);
    int getAdded(int originalHighest);

    static int getHighest(TimingOverride override, int original) {
        return Math.max(override.getMinimum(original), override.getFixed(original));
    }

    TimingOverride NONE = new TimingOverride() {
        @Override
        public int getMinimum(int originalMinimum) {
            return originalMinimum;
        }

        @Override
        public int getFixed(int originalFixed) {
            return originalFixed;
        }

        @Override
        public int getAdded(int originalHighest) {
            return 0;
        }
    };
}