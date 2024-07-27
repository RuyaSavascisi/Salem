package com.buuz135.salem.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

public class BlockUtil {

    public static BlockPos getRandomSurfaceNearby(Level level, BlockPos pos, int rad){
        BlockPos randomDistance = new BlockPos(pos.getX() + level.random.nextInt(rad * 2) - rad, pos.getY(), pos.getZ() + level.random.nextInt(rad * 2) - rad);
        randomDistance = randomDistance.atY(level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, randomDistance.getX(), randomDistance.getZ()));
        return randomDistance;
    }

}
