package com.finchy.pipeorgans.ponder;

import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;

public class BoxVolume {
    protected Vec3i start;
    protected Vec3i end;

    public BoxVolume(Vec3i start, Vec3i end) {
        this.start = start;
        this.end = end;
    }

    public BoxVolume(int x1, int y1, int z1, int x2, int y2, int z2) {
        this.start = new Vec3i(x1, y1, z1);
        this.end = new Vec3i(x2, y2, z2);
    }

    public static BoxVolume point(int x, int y, int z) {
        return point(new Vec3i(x, y, z));
    }

    public static BoxVolume point(Vec3i point) {
        return new BoxVolume(point, point);
    }

    public static BoxVolume point(BlockPos pos) {
        return point(new Vec3i(pos.getX(), pos.getY(), pos.getZ()));
    }

    public static BoxVolume column(int x, int z, int y1, int y2) {
        return new BoxVolume(new Vec3i(x, y1, z), new Vec3i(x, y2, z));
    }

    public static BoxVolume column(Vec3i base, int height) {
        return new BoxVolume(new Vec3i(base.getX(), base.getY(), base.getZ()), new Vec3i(base.getX(), base.getY() + height - 1, base.getZ()));
    }

    public static BoxVolume singleAxis(Vec3i start, int length, Direction dir) {
        Vec3i end = start.offset(dir.getStepX() * (length - 1), dir.getStepY() * (length - 1), dir.getStepZ() * (length - 1));
        return new BoxVolume(start, end);
    }

    public static BoxVolume singleAxis(int x, int y, int z, int length, Direction dir) {
        Vec3i start = new Vec3i(x, y, z);
        Vec3i end = start.offset(dir.getStepX() * (length - 1), dir.getStepY() * (length - 1), dir.getStepZ() * (length - 1));
        return new BoxVolume(start, end);
    }

    public Selection asSelection(SceneBuildingUtil util) {
        return util.select().fromTo(start.getX(), start.getY(), start.getZ(), end.getX(), end.getY(), end.getZ());
    }
}
