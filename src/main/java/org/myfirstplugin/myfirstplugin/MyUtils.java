package org.myfirstplugin.myfirstplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

import java.util.Map;

public class MyUtils {
    public static String joinMap(Map<String, Integer> map) {
        StringBuilder result = new StringBuilder("{");

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            result.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append(", ");
        }

        if (!map.isEmpty()) {
            result.delete(result.length() - 2, result.length());
        }

        result.append("}");

        return result.toString();
    }

    static public boolean hasAirUp(Location location, int dy) {
        for (int i = 0; i < Math.abs(dy); i++) {
            Location ubicacionActual = location.clone().add(0, i * (dy < 0 ? -1 : 1), 0);
            BlockData blockData = ubicacionActual.getBlock().getBlockData();

            if (blockData.getMaterial() != Material.AIR && blockData.isOccluding()) {
                return false;
            }
        }

        return true;
    }

    static public String vectorIntoString(Vector vector) {
        return String.format("%.2f %.2f %.2f", vector.getX(), vector.getY(), vector.getZ());
    }
}
