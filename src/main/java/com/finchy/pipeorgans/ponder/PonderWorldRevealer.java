package com.finchy.pipeorgans.ponder;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.outliner.Outline;
import net.createmod.catnip.outliner.Outliner;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

public class PonderWorldRevealer {
    protected CreateSceneBuilder scene;
    protected SceneBuildingUtil util;
    protected int defaultIdleTime;

    public record Section(Selection selection, Direction direction, Optional<Integer> idleTime) implements Selection {
        public Section(Selection selection, Direction direction) {
            this(selection, direction, Optional.empty());
        }

        @Override
        public @NotNull Selection add(@NotNull Selection other) {
            return selection.add(other);
        }

        @Override
        public Selection substract(@NotNull Selection other) {
            return selection.substract(other);
        }

        @Override
        public Selection copy() {
            return selection.copy();
        }

        @Override
        public Vec3 getCenter() {
            return selection.getCenter();
        }

        @Override
        public Outline.OutlineParams makeOutline(Outliner outliner, Object slot) {
            return selection.makeOutline(outliner, slot);
        }

        /**
         * Returns an iterator over elements of type {@code T}.
         *
         * @return an Iterator.
         */
        @Override
        public @NotNull Iterator<BlockPos> iterator() {
            return selection.iterator();
        }

        /**
         * Evaluates this predicate on the given argument.
         *
         * @param blockPos the input argument
         * @return {@code true} if the input argument matches the predicate,
         * otherwise {@code false}
         */
        @Override
        public boolean test(BlockPos blockPos) {
            return selection.test(blockPos);
        }
    }

    public PonderWorldRevealer(CreateSceneBuilder scene, SceneBuildingUtil util, int defaultIdleTime) {
        this.scene = scene;
        this.util = util;
        this.defaultIdleTime = defaultIdleTime;
    }

    public void revealSections(Section... sections) {
        revealSections(List.of(sections));
    }

    public void revealSections(Iterable<Section> sections) {
        for (Section section : sections) {
            revealSection(section);
        }
    }

    public void revealSection(Section section) {
        scene.world().showSection(section.selection(), section.direction());
        scene.idle(section.idleTime().orElse(defaultIdleTime));
    }

    public Section createSection(BoxVolume boxVolume, Direction direction, @Nullable Integer idleTime) {
        return new Section(boxVolume.asSelection(util), direction, Optional.ofNullable(idleTime));
    }

    public Section createSection(BoxVolume boxVolume, Direction direction) {
        return new Section(boxVolume.asSelection(util), direction);
    }

    public List<Section> constructSectionsFromVolumes(Direction defaulDirection, @Nullable Integer defaultIdleTime, BiFunction<BoxVolume, Integer, Direction> directionOverrider, BiFunction<BoxVolume, Integer, Integer> idleTimeOverrider, Iterable<BoxVolume> volumes) {
        List<Section> sections = new java.util.ArrayList<>();
        for (BoxVolume boxVolume : volumes) {
            int i = sections.size();
            Direction direction = defaulDirection;
            if (directionOverrider != null) {
                direction = directionOverrider.apply(boxVolume, i);
                direction = direction == null ? defaulDirection : direction;
            }
            Integer idleTime = defaultIdleTime;
            if (idleTimeOverrider != null) {
                idleTime = idleTimeOverrider.apply(boxVolume, i);
                idleTime = idleTime == null ? defaultIdleTime : idleTime;
            }
            sections.add(createSection(boxVolume, direction, idleTime));
        }
        return sections;
    }

    public List<Section> constructSectionsFromVolumes(Direction defaulDirection, BiFunction<BoxVolume, Integer, Direction> directionOverrider, BoxVolume... boxVolumes) {
        return constructSectionsFromVolumes(defaulDirection, null, directionOverrider, null, List.of(boxVolumes));
    }

    public List<Section> constructSectionsFromPositions(Direction defaulDirection, @Nullable Integer defaultIdleTime, BiFunction<BoxVolume, Integer, Direction> directionOverrider, BiFunction<BoxVolume, Integer, Integer> idleTimeOverrider, BlockPos... singlePositions) {
        return constructSectionsFromVolumes(defaulDirection, defaultIdleTime, directionOverrider, idleTimeOverrider, () -> Arrays.stream(singlePositions).map(BoxVolume::point).iterator());
    }

    public List<Section> constructSectionsFromPositions(Direction defaulDirection, BiFunction<BoxVolume, Integer, Direction> directionOverrider, BlockPos... singlePositions) {
        return constructSectionsFromPositions(defaulDirection, null, directionOverrider, null, singlePositions);
    }

    public List<Section> constructSectionsFromSelections(Direction defaulDirection, @Nullable Integer defaultIdleTime, BiFunction<Selection, Integer, Direction> directionOverrider, BiFunction<Selection, Integer, Integer> idleTimeOverrider, Iterable<Selection> selections) {
        List<Section> sections = new java.util.ArrayList<>();
        for (Selection sel : selections) {
            int i = sections.size();
            Direction direction = defaulDirection;
            if (directionOverrider != null) {
                direction = directionOverrider.apply(sel, i);
                direction = direction == null ? defaulDirection : direction;
            }
            Integer idleTime = defaultIdleTime;
            if (idleTimeOverrider != null) {
                idleTime = idleTimeOverrider.apply(sel, i);
                idleTime = idleTime == null ? defaultIdleTime : idleTime;
            }
            sections.add(new Section(sel, direction, Optional.ofNullable(idleTime)));
        }
        return sections;
    }

    public List<Section> constructSectionsFromSelections(Direction defaulDirection, @Nullable Integer defaultIdleTime, BiFunction<Selection, Integer, Direction> directionOverrider, BiFunction<Selection, Integer, Integer> idleTimeOverrider, Selection... selections) {
        return constructSectionsFromSelections(defaulDirection, defaultIdleTime, directionOverrider, idleTimeOverrider, List.of(selections));
    }

    public List<Section> constructSectionsFromSelections(Direction defaulDirection, BiFunction<Selection, Integer, Direction> directionOverrider, Selection... selections) {
        return constructSectionsFromSelections(defaulDirection, null, directionOverrider, null, selections);
    }

    public Section constructSection(BlockPos pos, Direction direction, @Nullable Integer idleTime) {
        return createSection(BoxVolume.point(pos), direction, idleTime);
    }

    public Section constructSection(BlockPos pos, Direction direction) {
        return createSection(BoxVolume.point(pos), direction);
    }


}
