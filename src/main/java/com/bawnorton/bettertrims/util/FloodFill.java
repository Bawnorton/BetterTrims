package com.bawnorton.bettertrims.util;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;

public class FloodFill {
    public static void solid(Vec3d start, double maxDistance, Set<Vec3d> result, Predicate<Vec3d> isWall) {
        Queue<Vec3d> queue = new LinkedList<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            Vec3d position = queue.poll();
            double distance = start.distanceTo(position);

            if (distance > maxDistance || isWall.test(position) || result.contains(position)) {
                continue;
            }

            result.add(position);

            for (Direction dir : Direction.values()) {
                Vec3d newPos = position.offset(dir, 1);
                if (!result.contains(newPos)) {
                    queue.add(newPos);
                }
            }
        }
    }

    public static void hollow(Vec3d start, double maxDistance, Set<Vec3d> result, Set<Pair<Vec3d, Direction>> walls, Set<Vec3d> edges, Predicate<Vec3d> isWall) {
        Set<Vec3d> processed = new HashSet<>();
        Queue<Vec3d> queue = new LinkedList<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            Vec3d position = queue.poll();
            double distance = start.distanceTo(position);

            if (distance > maxDistance || isWall.test(position) || processed.contains(position)) {
                continue;
            }

            processed.add(position);

            boolean hitWall = false;
            for (Direction dir : Direction.values()) {
                Vec3d newPos = position.offset(dir, 1);
                if (isWall.test(newPos)) {
                    walls.add(Pair.of(newPos, dir));
                    hitWall = true;
                } else if (!processed.contains(newPos) && distance + 1 <= maxDistance) {
                    queue.add(newPos);
                }
            }

            if(distance + 1 > maxDistance) {
                edges.add(position);
                result.add(position);
            } else if(hitWall) {
                result.add(position);
            }
        }
    }
}
