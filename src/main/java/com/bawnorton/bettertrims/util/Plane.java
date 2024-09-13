package com.bawnorton.bettertrims.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import org.joml.Vector3d;
import java.util.Random;

public class Plane {
    private static final Random rand = new Random();

    private final Vector3d point1;
    private final Vector3d point2;
    private final Vector3d point3;
    private final Vector3d point4;

    public Plane(Vector3d point1, Vector3d point2, Vector3d normal) {
        this.point1 = point1;
        this.point2 = point2;

        Vector3d v = new Vector3d(point1).sub(point2);
        Vector3d n = new Vector3d(normal);
        Vector3d u = new Vector3d(v).cross(n).normalize();
        double length = v.length();
        Vector3d m = new Vector3d(point1).add(point2).div(2);

        this.point3 = new Vector3d(m).add(new Vector3d(u).mul(length / 2));
        this.point4 = new Vector3d(m).sub(new Vector3d(u).mul(length / 2));
    }

    public static Plane fromSideOfBox(Box box, Direction direction) {
        return switch (direction) {
            case UP -> new Plane(new Vector3d(box.minX, box.maxY, box.minZ), new Vector3d(box.maxX, box.maxY, box.maxZ), new Vector3d(0, 1, 0));
            case DOWN -> new Plane(new Vector3d(box.minX, box.minY, box.minZ), new Vector3d(box.maxX, box.minY, box.maxZ), new Vector3d(0, -1, 0));
            case EAST -> new Plane(new Vector3d(box.maxX, box.minY, box.minZ), new Vector3d(box.maxX, box.maxY, box.maxZ), new Vector3d(1, 0, 0));
            case WEST -> new Plane(new Vector3d(box.minX, box.minY, box.minZ), new Vector3d(box.minX, box.maxY, box.maxZ), new Vector3d(-1, 0, 0));
            case NORTH -> new Plane(new Vector3d(box.minX, box.minY, box.minZ), new Vector3d(box.maxX, box.maxY, box.minZ), new Vector3d(0, 0, -1));
            case SOUTH -> new Plane(new Vector3d(box.minX, box.minY, box.maxZ), new Vector3d(box.maxX, box.maxY, box.maxZ), new Vector3d(0, 0, 1));
        };
    }

    public static Plane fromBlockPos(BlockPos pos, Direction direction) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        return fromSideOfBox(new Box(x, y, z, x + 1, y + 1, z + 1), direction);
    }

    public Vector3d getRandPointOnPlane() {
        Vector3d v1 = new Vector3d(point3).sub(point1);
        Vector3d v2 = new Vector3d(point4).sub(point2);

        double r1 = rand.nextDouble();
        double r2 = rand.nextDouble();

        double x = point1.x + r1 * v1.x + r2 * v2.x;
        double y = point1.y + r1 * v1.y + r2 * v2.y;
        double z = point1.z + r1 * v1.z + r2 * v2.z;
        return new Vector3d(x, y, z);
    }

    @Override
    public String toString() {
        return "Plane[%s -> %s]".formatted(point1, point2);
    }
}
