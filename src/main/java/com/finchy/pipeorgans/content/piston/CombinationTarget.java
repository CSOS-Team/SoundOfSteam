package com.finchy.pipeorgans.content.piston;

public interface CombinationTarget {

    // Number of pressable entries
    int combinationSize();

    boolean isEntryPressed(int index);

    // Server-side: set an entry's pressed state
    void setEntryPressed(int index, boolean pressed);

    // Server-side: set all entries on/off
    void setAllEntriesPressed(boolean pressed);

    // Non destructive tutti override
    void setTuttiOverride(boolean active);
}
