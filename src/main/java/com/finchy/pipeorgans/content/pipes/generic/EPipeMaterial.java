package com.finchy.pipeorgans.content.pipes.generic;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public class EPipeMaterial {

    public enum PipeMaterial implements StringRepresentable {
        WOOD("wood"), METAL("metal");

        private final String name;
        PipeMaterial(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }
}
